bbApp.controller('employeeAllocation', function ($scope, $modalInstance, params, appService, $http, ngDialog) {
    $scope.showSpinner = true;
    $scope.emp = params.scope.emp;
    $scope.emailList = [];
    $scope.mentovisor = [];
    $scope.region = [];
    $scope.showDropDown = false;
    $scope.projectId = params.scope.projectId;
    $scope.currStaffId = params.scope.currStaffId;
    $scope.employeeStartDate = params.scope.employeeStartDate;
    $scope.regionList = [];
    $scope.isDeallocate = params.scope.deallocate;
    $scope.maxDate = formatDate(new Date());
    $scope.lastworkingDateIncorrect = false;
    $scope.lastworkingDateIncorrectStartDate = false;
    $scope.label = [];
    $scope.performanceReviewerEmail = "";
    $scope.label.update = $scope.isDeallocate ? "DEALLOCATE" : "ALLOCATE";
    $scope.close = function () {
        $modalInstance.dismiss('cancel');
    };
    $scope.UpdateMentovisor = function (email) {
        $scope.mentovisorEmail = email;
        $scope.showSpinner = true;
        $http.get(host + "/ttn/employee/region?email=" + email).success(function (response) {
            $scope.region = response.region;
            $scope.showSpinner = false
        });
        $scope.showAndHideDropDown(false)
    };
    $scope.showAndHideDropDown = function (showDropDownFlag) {
        $scope.showDropDown = showDropDownFlag;
    };
    $scope.regionAjaxSuccess = function (data) {
        $scope.regionList = data;
    };

    $http.get(host + "/ttn/employee/getmentovisor?empCode=" + $scope.emp.code).success(function (response) {
        $scope.mentovisor = response;
        $scope.mentovisorEmail = response.emailAddress;
        $scope.performanceReviewerEmail = response.performanceReviewerEmail;
        $scope.region = response.menteeRegion ? response.menteeRegion : response.region;
        $scope.showSpinner = false;
        document.getElementById("email").select();
    });
    $scope.emailListAjaxSuccess = function (data) {
        $scope.employeesRegion = data;
        angular.forEach(data, function (region, email) {
            $scope.emailList.push(email);
        });
    };
    appService.getData(host + "/ttn/employee/getAllEmail", "GET", "", $scope.emailListAjaxSuccess, undefined, false, true, true);
    appService.getData(host + "/allregions", "GET", "", $scope.regionAjaxSuccess, undefined, true, false, false);
    $scope.allocate = function () {
        if (isValidEmail($scope.mentovisorEmail) || isValidEmail($scope.performanceReviewerEmail)) {
            $scope.mentovisorEmail = $scope.mentovisor.emailAddress;
            $scope.performanceReviewerEmail = $scope.mentovisor.performanceReviewerEmail;

        }
        else {
            $http.put(host + '/project/' + $scope.projectId + '/staffing-request/' + $scope.currStaffId + '/allocate/' + $scope.emp.emailAddress)
                .success(function (response) {
                    $scope.ajaxSuccess();
                })
                .error(function (response) {
                    ngDialog.open({
                        template: '<div class="error-dialog"><h4 class="title">Error</h4><p>'
                        + response[0].messages[0].message + '</p></div>', plain: true
                    });
                })

        }
    };
    $scope.ajaxSuccess = function () {
        $http.put(host + "/ttn/employee/changementovisor", {
            empCode: $scope.emp.code,
            employeeName: $scope.emp.name,
            mentovisorEmail: $scope.mentovisorEmail,
            newRegion: $scope.region,
            projectId: $scope.projectId,
            currStaffId: $scope.currStaffId,
            performanceReviewerEmail: $scope.performanceReviewerEmail

        }).success(function (response) {
            $modalInstance.dismiss('cancel');
            if ($scope.isDeallocate)
                params.scope.route.reload();
            else
                params.scope.closeThisDialog();
            $scope.showDialogSpinner = false;
        })
    };

    function formatDate(date) {
        var d = new Date(date),
            month = '' + (d.getMonth() + 1),
            day = '' + d.getDate(),
            year = d.getFullYear();

        if (month.length < 2) month = '0' + month;
        if (day.length < 2) day = '0' + day;

        return [year, month, day].join('-');
    }

    $scope.enableCalenderReq = function (id, date) {

        $('#' + id).datetimepicker({
            format: 'YYYY-MM-DD'
        });
        $('#' + id).on("dp.change", function () {
            $scope.lastWorkingDate = new Date($("#" + id + ">input").val() + " 00:00:00");
        });
    };
    $scope.setEmployeeRegion = function () {
        if ($scope.employeesRegion.hasOwnProperty($scope.mentovisorEmail)) {
            $scope.region = $scope.employeesRegion[$scope.mentovisorEmail].regionName
        }
    };
    $scope.deAllocate = function () {
        $scope.lastworkingDateIncorrect = false;
        var lastWorkingDate = $scope.lastWorkingDate == undefined ? undefined : appService.convertDate($scope.lastWorkingDate);
        $scope.showDialogSpinner = true;
        if (lastWorkingDate > $scope.maxDate || lastWorkingDate == undefined) {
            $scope.lastworkingDateIncorrect = true;
        }
        else if (isValidEmail($scope.mentovisorEmail) || isValidEmail($scope.performanceReviewerEmail)) {
            $scope.mentovisorEmail = $scope.mentovisor.emailAddress;
            $scope.performanceReviewerEmail = $scope.mentovisor.performanceReviewerEmail;
        } else {
            if (lastWorkingDate >= $scope.employeeStartDate) {
                appService.getData(host + '/project/' + $scope.projectId + '/staffing-request/' + $scope.currStaffId + '/de-allocate/' + $scope.emp.emailAddress + "/" + lastWorkingDate,
                    "PUT", "", $scope.ajaxSuccess, undefined, false, true, true);
            }
            else {
                $scope.lastworkingDateIncorrectStartDate = true;
            }
        }
    }

    function isValidEmail(email) {
        return email === undefined || email === "" || ($scope.emailList.indexOf(email) === -1)
    }

});