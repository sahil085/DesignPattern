bbApp.controller('changeMentovisorCtrl', function ($scope, $modalInstance, params, appService, $http) {
    console.log("Why Im not changing");
    $scope.showSpinner = true;
    $scope.emp = params.scope.emp;
    $scope.emailList = [];
    $scope.performanceReviewerEmail = "";
    $scope.mentovisor = [];
    $scope.region = [];
    $scope.showDropDown = false;
    $scope.label = [];
    $scope.label.update = "Update";
    $scope.close = function () {
        $modalInstance.dismiss('cancel');
    };
    $scope.UpdateMentovisor = function (email) {
        $scope.mentovisorEmail = email;
        $scope.showSpinner = true;
        $http.get(host + "/ttn/employee/region?email=" + email).success(function (response) {
            $scope.region = response.region;
            $scope.showSpinner = false;
        });
    };
    $scope.setEmployeeRegion = function () {
        if ($scope.employeesRegion.hasOwnProperty($scope.mentovisorEmail)) {
            $scope.region = $scope.employeesRegion[$scope.mentovisorEmail].regionName
        }
    };
    $scope.regionAjaxSuccess = function (data) {
        $scope.regionList = data;

    };

    $http.get(host + "/ttn/employee/getmentovisor?empCode=" + $scope.emp.code).success(function (response) {
        $scope.mentovisor = response;
        $scope.mentovisorEmail = response.emailAddress;
        $scope.region = response.menteeRegion ? response.menteeRegion : response.region;
        $scope.performanceReviewerEmail = response.performanceReviewerEmail;
        document.getElementById("email").select();
        $scope.showSpinner = false;
    });

    appService.getData(host + "/allregions", "GET", "", $scope.regionAjaxSuccess, undefined, true, false, false);
    $scope.emailListAjaxSuccess = function (data) {
        $scope.employeesRegion = data;
        angular.forEach(data, function (region, email) {
            $scope.emailList.push(email);

        });
        document.getElementById("email").select();

    };

    appService.getData(host + "/ttn/employee/getAllEmail", "GET", "", $scope.emailListAjaxSuccess, undefined, false, true, true);

    $scope.update = function () {
        if (isValidEmail($scope.mentovisorEmail) || isValidEmail($scope.performanceReviewerEmail)) {
            $scope.mentovisorEmail = $scope.mentovisor.emailAddress;
            $scope.performanceReviewerEmail = $scope.mentovisor.performanceReviewerEmail;
        }
        else {
            $http.put(host + "/ttn/employee/changementovisor", {
                empCode: $scope.emp.code,
                employeeName: $scope.emp.name,
                mentovisorEmail: $scope.mentovisorEmail,
                newRegion: $scope.region,
                performanceReviewerEmail:$scope.performanceReviewerEmail

            }).success(function (response) {
                if (params.route) {
                    params.route.reload();
                }
                $modalInstance.dismiss('cancel');

            })
        }
    };
    function isValidEmail(email) {
        return email === undefined || email === "" || ($scope.emailList.indexOf(email) === -1)
    }


});