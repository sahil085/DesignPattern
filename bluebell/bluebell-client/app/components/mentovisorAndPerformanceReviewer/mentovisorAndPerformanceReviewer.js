bbApp.controller('mentovisorAndPerformanceReviewer', function ($scope, appService, $http, arrayByKeyValuesFilter, $modal, $route, $filter) {
    $scope.$emit('toggleSpinner', true);
    $scope.route = [];
    $scope.employeeandMentovisorListOrig = [];
    $scope.employeeandMentovisorList = [];
    $scope.label = {};
    $scope.label.employeeName = "Name";
    $scope.label.employeeCode = "Code";
    $scope.label.employeeEmail = "Email";
    $scope.label.employeeCompetency = "Competency";
    $scope.label.employeeRegion = "Region";
    $scope.label.mentovisorEmail = "Mentovisor";
    $scope.label.performanceReviewer = "Performance Reviewer";
    $scope.label.onPageLoad = "Please Wait Page Is Loading";
    $scope.regionChanged = function () {
        if ($scope.region == undefined || $scope.region == "")
            $scope.employeeandMentovisorList = angular.copy($scope.employeeandMentovisorListOrig);
        else
            $scope.employeeandMentovisorList = arrayByKeyValuesFilter($scope.employeeandMentovisorListOrig, "menteeRegion", [$scope.region]);
        if ($scope.competency != undefined && $scope.competency != "")
            $scope.employeeandMentovisorList = $scope.competencyFilter($scope.employeeandMentovisorList, "menteeCompetency.name", [$scope.competency]);
    };
    $scope.regionAjaxSuccess = function (data) {
        $scope.regionList = data;
    };
    $scope.competencyAjaxSuccess = function (data) {
        $scope.competencyList = data;
    };
    $scope.competencyFilter = function (input, key, value) {
        if (key == "" || typeof key == undefined || typeof value == undefined) {
            return input;
        }
        output = [];
        angular.forEach(input, function (data) {
            if (data.menteeCompetency.name != undefined && value.indexOf(data.menteeCompetency.name) != -1) {
                output.push(data);
            }
        });
        return output;
    };
    $scope.competencyChanged = function () {
        if ($scope.competency == undefined || $scope.competency == "")
            $scope.employeeandMentovisorList = angular.copy($scope.employeeandMentovisorListOrig);
        else {
            $scope.employeeandMentovisorList = $scope.competencyFilter($scope.employeeandMentovisorListOrig, "menteeCompetency.name", [$scope.competency]);
        }
        if ($scope.region != undefined && $scope.region != "")
            $scope.employeeandMentovisorList = arrayByKeyValuesFilter($scope.employeeandMentovisorList, "menteeRegion", [$scope.region]);
    };
    $scope.changeMentovisor = function (employeeName, employeeCode) {
        $scope.emp = [];
        $scope.emp['name'] = employeeName;
        $scope.emp['code'] = employeeCode;
        var templateUrl = 'app/components/staffAllocation/changeMentovisor.html';
        var controller = 'changeMentovisorCtrl';
        appService.showModal($modal, controller, templateUrl, {scope: $scope, route: $scope.route});


    };
    $scope.sortedList = function (property) {
        $scope.employeeandMentovisorListOrig = appService.sortedList(property, $scope, $filter, $scope.employeeandMentovisorListOrig);
        $scope.competencyChanged();
    };
    $scope.route.reload = function () {
        $http.get(host + "/ttn/employee/getAllEmployeesMentovisor").success(function (response) {
            $scope.employeeandMentovisorList = response;
            $scope.employeeandMentovisorListOrig = $scope.employeeandMentovisorList;
            $scope.sortedList();
            $scope.competencyChanged();
            $scope.regionChanged();
            $scope.label.onPageLoad = "No Employee Found";
            $scope.$emit('toggleSpinner', false);
        });
        appService.getData(host + "/allregions", "GET", "", $scope.regionAjaxSuccess, undefined, true, false, false);
        appService.getData(host + "/competencies", "GET", "", $scope.competencyAjaxSuccess, undefined, true, false, false);


    };
    $scope.route.reload();
});