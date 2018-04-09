bbApp.controller('projectCtrl', function ($scope, $location, appCache, ngDialog, appService, $rootScope, $http) {

    $scope.currDate = new Date();
    $scope.currDate.setHours(0, 0, 0, 0);
    $scope.mode = "CREATE";

    $scope.buAjaxSuccess = function (data) {
        $scope.bu = data;
    };
    $scope.clientAjaxSuccess = function (data) {
        $scope.clientList = data;
    };
    $scope.regionAjaxSuccess = function (data) {
        $scope.regionList = data;
    };

    appService.getData(host + "/business-units", "GET", "", $scope.buAjaxSuccess, undefined, true, false, false);
    appService.getData(host + "/client", "GET", "", $scope.clientAjaxSuccess, undefined, false, false, false);
    appService.getData(host + "/allregions", "GET", "", $scope.regionAjaxSuccess, undefined, true, false, false);


    $scope.projectAjaxSuccess = function (response) {
        $scope.formData = response;
        $scope.formData.startDate = new Date($scope.formData.startDate);
        $scope.formData.endDate = new Date($scope.formData.endDate);
        $scope.formData.client.clientId = $scope.formData.client.clientId + "";
        if ($scope.formData.projectType == "Billable") {
            $scope.isBillable = true;
        }
    };

    if (appCache.get('projectId') != undefined) {
        var projectId = appCache.get('projectId');
        $scope.mode = "UPDATE";

        appService.getData(host + "/project/" + projectId, "GET", "", $scope.projectAjaxSuccess, undefined, false, true, true);

    }
    else if (appCache.get('project-form') == undefined) {
        $scope.isBillable = true;
        $scope.formData = {};
        $scope.formData.projectType = "Billable";
        $scope.formData.billingType = "FixedPrice";
        $scope.formData._Type = "";
    }
    else {
        $scope.formData = appCache.get('project-form');
        if ($scope.formData.projectType == "Billable") {
            $scope.isBillable = true;
        }
    }

    $scope.checkProjectAjaxSuccess = function (data) {
        if (!(data == "" || data == null || data == undefined)) {
            $scope.project.projectName.$invalid = true;
            $scope.projectNameExists = true;
        }
        else {
            $scope.projectNameExists = false;
        }
    };

    $scope.checkProjectName = function (pName) {
        appService.getData(host + "/project/checkName/" + pName, "GET", "", $scope.checkProjectAjaxSuccess, undefined, false, false, true);
    };

    $scope.projectUpdateAjaxSuccess = function (response) {
        if (!$scope.isBillable) {
            appCache.put('message', "Project updated successfully!");
            $scope.navigate();
        }
        else {
            ngDialog.open({
                template: '<div class="success-dialog"><h4 class="title">Success</h4><p>'
                + 'Project Updated Successfully' + '</p></div>', plain: true
            });
        }
    };
    $scope.navigate = function () {
        appService.getData(host + "/project/" + appCache.get('projectId'), "GET", "", $scope.getProjectAjaxSuccess, undefined, false, true, true);
    };
    $scope.getProjectAjaxSuccess = function (response) {
        var project = response;
        $rootScope.callBackURL = '/listProjects';
        project.startDate = new Date(project.startDate);
        project.endDate = new Date(project.endDate);
        project.fromDL = true;
        appCache.put("project", project);
        $location.path("/staffing-needs");
    };
    $scope.update = function () {

        var data = angular.copy($scope.formData);

        data.startDate = appService.convertDate(data.startDate);
        if (!(data.endDate == undefined || isNaN(data.endDate.getDate()))) {
            data.endDate = appService.convertDate(data.endDate);
        }
        $http.get(host + "/regionData?regionName=" + escape(data.regionvalue.regionName)).success(function (response) {
            data.regionvalue = response;
            appService.getData(host + "/project/" + appCache.get('projectId'), "PUT", data, $scope.projectUpdateAjaxSuccess, undefined, false, true, true);
        });

    };
    $scope.next = function () {

        appCache.put('project-type', $scope.formData.projectType);
        appCache.put('billing-type', $scope.formData.billingType);
        appCache.put('project-start-date', $scope.formData.startDate);
        appCache.put('project-end-date', $scope.formData.endDate);

        if ($scope.formData.projectType == 'NonBillable') {
            $location.path("/project/staff-request");
        }
        else {
            if ($scope.formData.billingType == 'FixedPrice') {
                $location.path("/project/billing-info-fixed");
            }
            else {
                $location.path("/project/billing-info-tnm");
            }
        }
    };

    $scope.continue = function () {
        $http.get(host + "/regionData?regionName=" + $scope.formData.regionvalue.regionName).success(function (response) {
            $scope.formData.regionvalue = response;
            $scope.formData.region = response.regionName;
            appCache.put('project-form', $scope.formData);
            appCache.put('project-type', $scope.formData.projectType);
            appCache.put('billing-type', $scope.formData.billingType);
            appCache.put('project-start-date', $scope.formData.startDate);
            appCache.put('project-end-date', $scope.formData.endDate);

            if ($scope.formData.projectType == 'NonBillable') {
                $location.path("/project/staff-request");
            }
            else {
                if ($scope.formData.billingType == 'FixedPrice') {
                    $location.path("/project/billing-info-fixed");
                }
                else {
                    $location.path("/project/billing-info-tnm");
                }
            }
        });
    };

    $scope.showBillingType = function (show) {
        $scope.isBillable = show;
        if (!show) {
            $scope.formData.billingType = "";
        }
        else {
            if ($scope.formData.billingType == "" || $scope.formData.billingType == undefined) {
                $scope.formData.billingType = "FixedPrice";
            }
        }
    };

    $(function () {

        $('#startDatePicker').datetimepicker({
            format: 'YYYY-MM-DD'
        });


        $('#endDatePicker').datetimepicker({
            format: 'YYYY-MM-DD'
        });

        $("#startDatePicker").on("dp.change", function () {
            $scope.formData.startDate = new Date($("#startDatePicker>input").val() + " 00:00:00");
            $scope.project.startDate.$setViewValue($("#startDatePicker>input").val());
        });

        $("#endDatePicker").on("dp.change", function () {
            $scope.formData.endDate = new Date($("#endDatePicker>input").val() + " 00:00:00");
            $scope.project.endDate.$setViewValue($("#endDatePicker>input").val());
        });

    });

    $scope.label = {};
    $scope.label.billableProject = "BILLABLE PROJECT";
    $scope.label.nonBillableProject = "NON-BILLABLE PROJECT";
    $scope.label.fixedPriceProject = "FIXED-PRICE PROJECT";
    $scope.label.timeAndMoneyProject = "TIME & MONEY PROJECT";
    $scope.label.projectName = "PROJECT NAME";
    $scope.label.clientName = "CLIENT NAME";
    $scope.label.businessUnit = "BUSINESS UNIT";
    $scope.label.region = "REGION";
    $scope.label.startDate = "START DATE";
    $scope.label.endDate = "END DATE";
    $scope.label.addClient = "Add Client";
    $scope.label.pleaseSelect = "-Please Select-";

    $scope.errorMessage = {};
    $scope.errorMessage.project_name_required = "Project Name is required";
    $scope.errorMessage.client_name_required = "Please Select Client";
    $scope.errorMessage.bu_required = "Please Select Business Unit";
    $scope.errorMessage.region_required = "Please Select Region";
    $scope.errorMessage.startDate_required = "Start Date is required";
    $scope.errorMessage.startDate_invalid = "Invalid Start Date";
    $scope.errorMessage.startDate_invalid_min = "Start Date should not be less than Current Date";
    $scope.errorMessage.endDate_invalid = "Invalid End Date";
    $scope.errorMessage.endDate_invalid_min = "End Date should not be less than Start Date";
    $scope.errorMessage.project_name_invalid = "Project Already Exists";

});