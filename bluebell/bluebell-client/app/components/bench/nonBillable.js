bbApp.controller('nonBillable', function ($scope, $location, $http, appCache, $timeout, $q, ngDialog, arrayByKeyValuesFilter, appService, $rootScope, $route, $filter, $modal) {

    $scope.regionList = [];

    $scope.regionAjaxSuccess = function (data) {
        $scope.regionList = data;
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

    $scope.competencyAjaxSuccess = function (data) {
        $scope.competencyList = data;
    };

    $scope.nonBillableAjaxSuccess = function (response) {
        $scope.nonBillableEmployee = response;
        $scope.nonBillableEmployeeOrigList = angular.copy($scope.nonBillableEmployee);
    };

    $scope.sortedList = function (property) {
        $scope.nonBillableEmployee = appService.sortedList(property, $scope, $filter, $scope.nonBillableEmployee);
    };

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
            $scope.nonBillableEmployee = $scope.nonBillableEmployeeOrigList;
        else {
            $scope.nonBillableEmployee = $scope.competencyFilter($scope.nonBillableEmployeeOrigList, "competency.name", [$scope.competency]);
        }
        if (!($scope.region == undefined || $scope.region == "")) {
            $scope.nonBillableEmployee = arrayByKeyValuesFilter($scope.nonBillableEmployee, "region", [$scope.region])
        }

    };

    $scope.regionChanged = function () {
        if ($scope.region == undefined || $scope.region == "") {
            $scope.nonBillableEmployee = angular.copy($scope.nonBillableEmployeeOrigList);
        }

        else {
            $scope.nonBillableEmployee = arrayByKeyValuesFilter($scope.nonBillableEmployeeOrigList, "region", [$scope.region])
        }
        if ($scope.competency != undefined && $scope.competency != "") {
            $scope.nonBillableEmployee = $scope.competencyFilter($scope.nonBillableEmployee, "competency.name", [$scope.competency]);
        }
    };

    $scope.navigate = function (projectName) {
        appService.getData(host + "/project/checkName/" + projectName, "GET", "", $scope.projectAjaxSuccess, undefined, false, true, true);
    };

    appService.getData(host + "/competencies", "GET", "", $scope.competencyAjaxSuccess, undefined, true, false, false);
    appService.getData(host + "/ttn/employee/NonBillable", "GET", "", $scope.nonBillableAjaxSuccess, undefined, true, false, false);
    appService.getData(host + "/allregions", "GET", "", $scope.regionAjaxSuccess, undefined, true, false, false);

    $scope.label = {};
    $scope.label.nonBillableEmployee = "NON BILLABLE EMPLOYEES";
    $scope.label.region = "REGION";
    $scope.label.employeeName = "NAME";
    $scope.label.empCode = "CODE";
    $scope.label.title = "TITLE";
    $scope.label.competency = "COMPETENCY";
    $scope.label.projects = "PROJECTS";
    $scope.label.email = "EMAIL";

});