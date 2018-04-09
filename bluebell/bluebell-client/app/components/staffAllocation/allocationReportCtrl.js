bbApp.controller('allocationReportCtrl', function ($scope, $location, $http, appCache, $timeout, $q, ngDialog, appService) {
    $scope.showTable = true;
    $scope.showExportLink = false;
    $scope.enableCalenderReq = function (id, date) {

        $('#' + id).datetimepicker({
            format: 'YYYY-MM-DD'
        });
        $('#' + id).on("dp.change", function () {
            if (date == 'startDate') {
                $scope.startDate = new Date($("#" + id + ">input").val() + " 00:00:00");
            } else {
                $scope.endDate = new Date($("#" + id + ">input").val() + " 00:00:00");
            }
        });
    };
    $scope.report = function () {
        var startDate = appService.convertDate($scope.startDate);
        var endDate = appService.convertDate($scope.endDate);

        $scope.completeReportData = [];
        $scope.showExportLink = false;
        $http.get(host + "/project/allocationReport?startDate=" + startDate + "&endDate=" + endDate)
            .success(function (response) {
                $scope.headerDate = [];
                for (var d = new Date(startDate); d <= new Date(endDate); d.setDate(d.getDate() + 1)) {
                    $scope.headerDate.push(new Date(d));
                }
                $scope.reportData = response;
                $scope.showExportLink = true;
            });
        $scope.email = "Email";
        $scope.empCode = "Employee Code";
        $scope.name = "Name";
        $scope.designation = "Designation";
        $scope.competency = "Competency";
        $scope.project = "Project";
        $scope.region = "Region";
        $scope.billableType="Billing Type";
        $scope.showTable = false
        $scope.client = "Client";
    };

    $scope.exportToExcel = function (tableId) {
        $("#" + tableId).tableExport({type: 'excel', escape: false});
    }
});