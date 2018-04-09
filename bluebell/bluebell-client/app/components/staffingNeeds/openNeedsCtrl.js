    bbApp.controller('openNeedsCtrl', function ($scope, $http, $location, appCache, appService, ngDialog, $rootScope, arrayByKeyValuesFilter, $filter, $modal) {

    $scope.$watch('$root.loggedUser', function () {
        if (typeof $rootScope.loggedUser !== 'undefined') {
            $scope.role = $rootScope.loggedUser.currentRole;
            if ($rootScope.loggedUser.currentRole === 'REGION HEAD') {
                $scope.region = $rootScope.loggedUser.regions[0];
            }
        }
    });

    $scope.nominationPopup = [];
    $scope.currAllocationList = [];
    $scope.currAllocationPage = 1;
    $scope.currUnAllocList = [];
    $scope.currUnAllocPage = 1;
    $scope.itemPerPage = 10;
    $scope.maxSize = 5;

    $scope.$watch('currUnAllocPage + itemPerPage + needList', function () {
        openListChange();
    });
    function openListChange() {
        var begin = (($scope.currUnAllocPage - 1) * $scope.itemPerPage)
            , end = begin + $scope.itemPerPage;

        if ($scope.needList != undefined) {
            $scope.currUnAllocList = $scope.needList.slice(begin, end);

        }
    }

    $scope.regionChanged = function () {
        if ($scope.region == undefined || $scope.region == "")
            $scope.needList = angular.copy($scope.needListOrig);
        else
            $scope.needList = arrayByKeyValuesFilter($scope.needListOrig, "region", [$scope.region]);
        if ($scope.competency != undefined && $scope.competency != "")
            $scope.needList = $scope.competencyFilter($scope.needList, "competency.name", [$scope.competency]);
    };

    $scope.regionAjaxSuccess = function (data) {
        $scope.regionList = data;
    };
    appService.getData(host + "/allregions", "GET", "", $scope.regionAjaxSuccess, undefined, true, false, false);


    $scope.openNeedsAjaxSuccess = function (response) {
        var respObj = response;
        $scope.needList = [];

        angular.forEach(respObj, function (projectList, regionName) {

            angular.forEach(projectList, function (compList, projectName) {

                angular.forEach(compList, function (needList, competencyName) {

                    angular.forEach(needList, function (need) {
                        if (need.active) {
                            var project = projectName.split(":");
                            need.region = regionName;
                            need.projectName = project[0];
                            need.projectId = project[1];
                            $scope.needList.push(need);
                        }
                    });
                });
            });
        });
        if ($scope.needListOrig == undefined) {
            $scope.needListOrig = angular.copy($scope.needList);
        }

        $scope.regionChanged();
    };
    $scope.navigate = function (pid) {
        appService.getData(host + "/project/" + pid, "GET", "", $scope.projectAjaxSuccess, undefined, false, true, true);
    };
    $scope.projectAjaxSuccess = function (response) {
        var project = response;
        $rootScope.callBackURL = '/openNeeds';
        project.startDate = new Date(project.startDate);
        project.endDate = new Date(project.endDate);
        project.fromDL = true;
        appCache.put("project", project);
        $location.path("/staffing-needs");
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
            $scope.needList = angular.copy($scope.needListOrig);
        else {
            $scope.needList = $scope.competencyFilter($scope.needListOrig, "competency.name", [$scope.competency]);
        }
        if ($scope.region != undefined && $scope.region != "")
            $scope.needList = arrayByKeyValuesFilter($scope.needList, "region", [$scope.region]);
        openListChange();
    };
    $scope.sortedList = function (property) {
        $scope.needListOrig = appService.sortedList(property, $scope, $filter, $scope.needListOrig)
        $scope.competencyChanged();
    };
    $scope.competencyAjaxSuccess = function (data) {
        $scope.competencyList = data;
    };
    appService.getData(host + "/regionWiseOpenNeeds", "GET", "", $scope.openNeedsAjaxSuccess, undefined, false, true, true);
    appService.getData(host + "/competencies", "GET", "", $scope.competencyAjaxSuccess, undefined, true, false, false);

    $scope.label = {};
    $scope.label.open_needs = "Open Needs";
    $scope.label.projectName = "Project Name";
    $scope.label.title = "title";
    $scope.label.competency = "competency";
    $scope.label.projectId = "projectId";
    $scope.label.startdate = "start date";
    $scope.label.endDate = "end Date";
    $scope.label.billingType = "billing Type";
    $scope.label.details = "details";
    $scope.label.status = "status";
    $scope.label.allocation = "allocation";
    $scope.label.action = "action";
    $scope.label.allocate = "ALLOCATE";
    $scope.label.nominate = "NOMINATE";
    $scope.label.reject = "REJECT";
    $scope.label.billable = "Billable";
    $scope.label.nonBillable = "Non-Billable";
    $scope.label.shadow = "Shadow";

    $scope.isNominated = function (nominatedStaffing) {
        var respObj = nominatedStaffing;
        var isNominated = false;
        angular.forEach(respObj, function (employee) {
            if (employee.email === $rootScope.empEmailAddress) {
                isNominated = true;
            }
        });
        return isNominated;
    };

    $scope.allocate = function (projectId, currStaffId) {
        $scope.projectId = projectId;
        $scope.currStaffId = currStaffId;
        var templateUrl = 'app/components/staffAllocation/employeeAllocation.html';
        var controller = 'employeeAllocation';
        appService.showModal($modal, controller, templateUrl, {scope: $scope}, 'resignation-details');

    };

    $scope.nominate = function (projectId, currStaffId, nominationList) {
        $scope.showDialogSpinner = true;
        $http.put(host + '/project/' + projectId + '/staffing-request/' + currStaffId + '/nominate/' + $scope.empEmailAddress)
            .success(function (response) {
                var obj = {};
                obj.email = $rootScope.empEmailAddress;
                obj.employeeName = $rootScope.empName;
                nominationList.push(obj);
                // updateNeedList();
            })
            .error(function (response) {
                ngDialog.open({
                    template: '<div class="error-dialog"><h4 class="title">Error</h4><p>'
                    + response[0].messages[0].message + '</p></div>', plain: true
                });
            })
            .finally(function () {
                $scope.showDialogSpinner = false;
            });
    };

    $scope.rejectNomination = function (index, projectId, currStaffId, nominationList) {
        $scope.showDialogSpinner = true;
        var nindex = 0;
        $http.put(host + '/project/' + projectId + '/staffing-request/' + currStaffId + '/reject/' + $scope.empEmailAddress)
            .success(function (response) {
                angular.forEach(nominationList, function (nemp) {
                    if (nemp.email == $rootScope.empEmailAddress) {
                        nominationList.splice(nindex, 1);
                    }
                    nindex++;
                });
                // updateNeedList();
            })
            .error(function (response) {
                ngDialog.open({
                    template: '<div class="error-dialog"><h4 class="title">Error</h4><p>'
                    + response[0].messages[0].message + '</p></div>', plain: true
                });
            })
            .finally(function () {
                $scope.showDialogSpinner = false;
            });
    };

});