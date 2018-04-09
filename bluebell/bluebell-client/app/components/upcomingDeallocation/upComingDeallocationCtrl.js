bbApp.controller('upcomingDeallocationCtrl', function ($scope, $http, $location, arrayByKeyValuesFilter, appService, $rootScope, appCache, $filter, $modal) {

    $scope.$watch('$root.loggedUser', function () {
        if (typeof $rootScope.loggedUser != 'undefined') {

            $scope.role = $rootScope.loggedUser.currentRole;

            if ($rootScope.loggedUser.currentRole == 'REGION HEAD') {
                $scope.region = $rootScope.loggedUser.regions[0];
            }
            else {
                $scope.region = '';
                $scope.competency = '';
            }

            $scope.fireAjax();
        }
    });
    $scope.currList = [];
    $scope.currPage = 1;
    $scope.itemPerPage = 20;
    $scope.maxSize = 20;
    $scope.duration = "7";
    $scope.dayRange = [{key: "7", value: 'Next 7 days'}, {key: "15", value: 'Next 15 days'}, {
        key: "30",
        value: 'Next 30 days'
    }];

    $scope.currBenchList = [];
    $scope.currBenchPage = 1;

    $scope.$watchGroup(['currPage', 'itemPerPage', 'upcomingDeallocationsList'], function () {
        var begin = (($scope.currPage - 1) * $scope.itemPerPage)
            , end = begin + $scope.itemPerPage;

        if ($scope.upcomingDeallocationsList != undefined) {
            $scope.currList = $scope.upcomingDeallocationsList.slice(begin, end);
        }
    }, true);

    $scope.$watchGroup(['currBenchPage', 'itemPerPage', 'benchList'], function () {
        var begin = (($scope.currBenchPage - 1) * $scope.itemPerPage)
            , end = begin + $scope.itemPerPage;

        if ($scope.benchList != undefined) {
            $scope.currBenchList = $scope.benchList.slice(begin, end);
        }
    }, true);

    $scope.regionChanged = function () {
        if ($scope.region == undefined || $scope.region == "")
            $scope.upcomingDeallocationsList = angular.copy($scope.upcomingDeallocationsListOrig);
        else
            $scope.upcomingDeallocationsList = arrayByKeyValuesFilter($scope.upcomingDeallocationsListOrig, "region", [$scope.region]);
    };

    $scope.rangeChanged = function () {
        $scope.fireAjax();
    };

    $scope.upcomingDeallocationCompetencyFilterChanged = function () {
        $scope.fireAjax();
    }

    $scope.competencyFilter = function (input, key, value) {
        if (key == "" || typeof key == undefined || typeof value == undefined) {
            return input;
        }
        output = [];
        angular.forEach(input, function (data) {
            if (data['competency'] != undefined && value.indexOf(data['competency']['name']) != -1) {
                output.push(data);
            }
        });
        return output;
    };
    $scope.competencyChanged = function () {
        if ($scope.competency == undefined || $scope.competency == "")
            $scope.benchList = $scope.benchListOrig;
        else
            $scope.benchList = $scope.competencyFilter($scope.benchListOrig, "competency.name", [$scope.competency]);

    };
    $scope.sortedList = function (property, upComingDellocationFlag) {
        if (upComingDellocationFlag)
            $scope.upcomingDeallocationsList = appService.sortedList(property, $scope, $filter, $scope.upcomingDeallocationsList);
        else {
            $scope.benchListOrig = appService.sortedList(property, $scope, $filter, $scope.benchListOrig);
            $scope.competencyChanged();
        }

    };


    $scope.competenciesAjaxSuccess = function (data) {
        $scope.competenciesList = data;
    };
    appService.getData(host + "/competencies", "GET", "", $scope.competenciesAjaxSuccess, undefined, true, false, false);

    $scope.regionAjaxSuccess = function (data) {
        $scope.regionList = data;
    };
    appService.getData(host + "/allregions", "GET", "", $scope.regionAjaxSuccess, undefined, true, false, false);

    $scope.upcomingDeallocationsSuccess = function (response) {
        $scope.upcomingDeallocationsListOrig = response;
        $scope.regionChanged();
        $scope.resigned(false);
    };

    $scope.benchDataSuccess = function (response) {
        $scope.benchListOrig = response;
        $scope.resigned(true);
        $scope.competencyChanged();


    };

    $scope.fireAjax = function () {
        appService.getData(host + "/project/upcompingDeallocationsList", "POST", {
            days: parseInt($scope.duration, 10),
            regions: $scope.regions,
            competency: $scope.upcomingDeallocationCompetency
        }, $scope.upcomingDeallocationsSuccess, undefined, false, true, true);
        appService.getData(host + "/ttn/employee/bench", "GET", "", $scope.benchDataSuccess, undefined, false, false, true);
    };

    $scope.projectAjaxSuccess = function (response) {
        var project = response;
        project.startDate = new Date(project.startDate);
        project.endDate = new Date(project.endDate);
        project.fromDL = true;
        appCache.put("project", project);
        $location.path("/staffing-needs");
    };

    $scope.navigate = function (pid) {
        appService.getData(host + "/project/" + pid, "GET", "", $scope.projectAjaxSuccess, undefined, false, true, true);
    };
    $scope.resigned = function (onBenchFlag) {
        $http.get(host + "/ttn/employee/resignations")
            .success(function (response) {
                var listToIncludeResignEmp = onBenchFlag == true ? $scope.benchListOrig : $scope.upcomingDeallocationsListOrig;
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

                });
                if (onBenchFlag)
                    $scope.benchListOrig = listToIncludeResignEmp;
                else {

                    $scope.upcomingDeallocationsListOrig = listToIncludeResignEmp;
                    $scope.upcomingDeallocationsList = $scope.upcomingDeallocationsListOrig;
                    $scope.upcomingDeallocationsListbckup = undefined;
                }
                $scope.resignationFilter(false);
                $scope.resignationFilter(true);

            });
        $scope.competencyChanged();
    };
    $scope.resignationFilter = function (benchFlag) {
        if (benchFlag) {
            if ($scope.benchListbckup == undefined)
                $scope.benchListbckup = $scope.benchListOrig;
            $scope.benchListOrig = $scope.benchListbckup;
            if (!$scope.benchResignFilter || $scope.benchResignFilter == undefined)
                $scope.benchListOrig = includeResignation($scope.benchListOrig);
            $scope.benchList = $scope.benchListOrig;
            $scope.competencyChanged();
        }
        else {
            if ($scope.upcomingDeallocationsListbckup == undefined)
                $scope.upcomingDeallocationsListbckup = $scope.upcomingDeallocationsListOrig;
            $scope.upcomingDeallocationsListOrig = $scope.upcomingDeallocationsListbckup;
            if (!$scope.upcmgDellresignFilter || $scope.upcmgDellresignFilter == undefined)
                $scope.upcomingDeallocationsListOrig = includeResignation($scope.upcomingDeallocationsListOrig);
            $scope.upcomingDeallocationsList = $scope.upcomingDeallocationsListOrig
        }


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
    $scope.label = {};
    $scope.label.upcomingDeallocations = "Upcoming Deallocations";
    $scope.label.projectName = "Project Name";
    $scope.label.title = "title";
    $scope.label.employeename = "Employee Name";
    $scope.label.deallocationDate = "Deallocation Date";
    $scope.label.billable = "BILLABLE";
    $scope.label.region = "Region";
    $scope.label.benchLabel = "People on Bench";
    $scope.label.empCode = "Employee Code";
    $scope.label.competency = "Competency";
    $scope.label.email = "Email";


});