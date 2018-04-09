bbApp.controller('benchCtrl', function ($scope, $location, $http, appCache, $timeout, $q, ngDialog, appService, $rootScope, $route, $filter, $modal) {
    $scope.benchList = [];
    $scope.currBenchList = [];
    $scope.currBenchPage = 1;
    $scope.itemPerPage = 20;
    $scope.maxSize = 5;
    $scope.$watch('currBenchPage + itemPerPage + benchList', function () {
        benchListChange();
    });
    function benchListChange() {
        var begin = (($scope.currBenchPage - 1) * $scope.itemPerPage)
            , end = begin + $scope.itemPerPage;

        if ($scope.benchList != undefined) {
            $scope.currBenchList = $scope.benchList.slice(begin, end);
        }

    }

    $scope.$watch('$root.loggedUser', function () {
        if (typeof $rootScope.loggedUser != 'undefined') {

            $scope.role = $rootScope.loggedUser.currentRole;
        }
    });
    $scope.benchAjaxSuccess = function (response) {
        $scope.benchList = [];
        $scope.benchOrigList = [];
        for (var key in response) {
            var empBenchData = [];
            if (response.hasOwnProperty(key)) {
                empBenchData['email'] = key;
                empBenchData['leavesCount'] = response[key].leavesCount;
                empBenchData['employeeName'] = response[key].employeeName;
                empBenchData['title'] = response[key].title;
                empBenchData['competency'] = response[key].competency;
                empBenchData['currentProjects'] = response[key].currentProjects;
                empBenchData['nominatedProjects']=response[key].nominatedProjects;
                console.log(response[key].nominatedProjects)
                empBenchData['availableFrom'] = response[key].availableFrom ? response[key].availableFrom : 99999;
                empBenchData['bench'] = response[key].bench;
                empBenchData['code'] = response[key].code;
            }
            $scope.benchList.push(empBenchData);
        }
        $scope.benchOrigList = $scope.benchList;
        $scope.benchListbckup = $scope.benchOrigList;
        $scope.resigned();
        $scope.sortedList('employeeName');
        $scope.applyDesignationFilter();
        $scope.$emit('toggleSpinner', false);

    };
    $scope.projectAssign = function (emailAddress, name, competency, emp) {
        $scope.empName = name;
        $scope.empEmailAddress = emailAddress;
        $scope.competency = competency;
        $scope.emp = [];
        $scope.emp['code'] = emp.code;
        $scope.emp['emailAddress'] = emailAddress;
        $scope.emp['name'] = name;
        $scope.projectAssign = true;
        ngDialog.openConfirm({
            template: 'app/components/staffingNeeds/openNeeds.html',
            className: 'ngdialog-theme-default ngdialog-search',
            scope: $scope,
            preCloseCallback: function () {
                $route.reload();
                return true;
            }
        });
    };
    $scope.applyDesignationFilter = function () {
        $scope.competencyChanged();
        if ($scope.designation != undefined && $scope.titleList.length != 0) {
            var bkup = $scope.benchList;
            $scope.benchList = [];
            angular.forEach(bkup, function (item) {
                if (item.title.toUpperCase() == $scope.designation.toUpperCase()) {
                    $scope.benchList.push(item);
                }
            });
        }
    };
    $scope.titleAjaxSuccess = function (data) {
        $scope.titleList = data;
    };
    appService.getData(host + "/ttn/employee/title", "GET", "", $scope.titleAjaxSuccess, undefined, true, false, false);
    $scope.benchDateFilter = function () {
        $scope.$emit('toggleSpinner', true);
        var durationInDays = ($scope.onBenchDate == "" || $scope.onBenchDate == undefined) ? 0 : $scope.onBenchDate;
        appService.getData(host + "/ttn/employee/partiallybench?durationInDays=" + durationInDays, "GET", "", $scope.benchAjaxSuccess, undefined, true, false, false)
    };
    $scope.competencyFilter = function (input, key, value) {
        if (key == "" || typeof key == undefined || typeof value == undefined) {
            return input;
        }
        output = [];
        angular.forEach(input, function (data) {
            if (data['competency'] != undefined && value.indexOf(data['competency']) != -1) {
                output.push(data);
            }
        });
        return output;
    };
    $scope.competencyChanged = function () {
        if ($scope.competency == undefined || $scope.competency == "")
            $scope.benchList = $scope.benchOrigList;
        else {
            $scope.benchList = $scope.competencyFilter($scope.benchOrigList, "competency.name", [$scope.competency]);
        }
        benchListChange();
        $scope.currentProperty = "";
        $scope.sortedList('employeeName');
    };
    $scope.sortedList = function (property) {
        $scope.benchList = appService.sortedList(property, $scope, $filter, $scope.benchList);
        benchListChange();
    };

    $scope.showLeaveSummary = function (employee) {
        var templateUrl = 'app/components/staffAllocation/showLeaveSummary.html';
        var controller = 'showLeaveSummaryCtrl';
        if (employee.leavesCount) {
            appService.showModal($modal, controller, templateUrl, {emailAddress: employee.email, scope: $scope}, '', 'lg');
        }
    };

    $scope.resigned = function () {
        $http.get(host + "/ttn/employee/resignations")
            .success(function (response) {
                var listToIncludeResignEmp = $scope.benchOrigList;
                angular.forEach(response, function (value, key) {
                    for (var i = 0; i < listToIncludeResignEmp.length; i++) {
                        if (value.employeeCode == listToIncludeResignEmp[i].code) {
                            listToIncludeResignEmp[i].resign = true;
                            listToIncludeResignEmp[i].initiationDate = value.initiationDate;
                            listToIncludeResignEmp[i].lastWorkingDate = value.lastWorkingDate;
                            listToIncludeResignEmp[i].noticePeriod = value.noticePeriod;
                            break;
                        }

                    }
                    $scope.benchOrigList = listToIncludeResignEmp;

                });
                $scope.resignationFilter();

            });
        $scope.applyDesignationFilter();
    };
    $scope.resignationFilter = function () {
        if ($scope.benchListbckup == undefined)
            $scope.benchListbckup = $scope.benchOrigList;
        $scope.benchOrigList = $scope.benchListbckup;
        if (!$scope.benchResignFilter || $scope.benchResignFilter == undefined)
            $scope.benchOrigList = includeResignation($scope.benchOrigList);
        $scope.applyDesignationFilter();


    };
    function includeResignation(listTOChange) {
        var filteredList = listTOChange;
        listTOChange = [];
        angular.forEach(filteredList, function (item) {
            if (!item.resign)
                listTOChange.push(item);
        });
        return listTOChange;
    }

    $scope.resigntionDetails = function (employee) {
        $scope.emp = employee;
        var templateUrl = 'app/components/staffAllocation/resignationDetails.html';
        var controller = 'resignationDetailCtrl';
        appService.showModal($modal, controller, templateUrl, {scope: $scope});
    };
    $scope.competencyAjaxSuccess = function (data) {
        $scope.competencyList = data;
    };

    $scope.navigate = function (projectName) {
        appService.getData(host + "/project/checkName/" + projectName, "GET", "", $scope.projectAjaxSuccess, undefined, false, true, true);
    };
    $scope.projectAjaxSuccess = function (response) {
        $rootScope.callBackURL = '/bench';
        var project = response;
        project.startDate = new Date(project.startDate);
        project.endDate = new Date(project.endDate);
        project.fromDL = true;
        appCache.put("project", project);
        $location.path("/staffing-needs");
    };
    $scope.allocationAjaxSuccess = function (response) {
        $scope.allocationData = response;
        ngDialog.openConfirm({
            template: 'app/components/staffAllocation/allocationHistory.html',
            className: 'ngdialog-theme-default ngdialog-search',
            scope: $scope
        });

    };

    $scope.viewAllocations = function (email, empName) {
        $scope.empName = empName;
        appService.getData(host + "/ttn/employee/allocation", "POST", {emailId: email}, $scope.allocationAjaxSuccess, undefined, false, true, true);
    };
    appService.getData(host + "/competencies", "GET", "", $scope.competencyAjaxSuccess, undefined, true, false, false);
    $scope.benchDateFilter();
    $scope.benchKey = ['employeeName', 'title', 'competency', 'currentProjects', 'availableFrom', 'bench'];
    $scope.label = {};
    $scope.label.employeeName = "EMPLOYEE NAME";
    $scope.label.title = "TITLE";
    $scope.label.competency = "COMPETENCY";
    $scope.label.currentProject = "CURRENT PROJECT";
    $scope.label.nominatedProjects="NOMINATED PROJECTS"
    $scope.label.availableFrom = "AVAILABLE FROM";
    $scope.label.availablity = "AVAILABILITY %";
    $scope.label.action = "ACTION";
    $scope.label.benchLabel = "Employees Availability"


});