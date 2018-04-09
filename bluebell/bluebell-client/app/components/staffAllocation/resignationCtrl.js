bbApp.controller('resignationCtrl', function ($scope,$modalInstance,params,appService,$http) {
    $scope.close = function () {
        $modalInstance.dismiss('cancel');
    };
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
    $scope.resign = function () {
        var startDate = appService.convertDate($scope.startDate);
        var endDate = appService.convertDate($scope.endDate);
        $scope.maxstartDate=endDate;
        $scope.minEndDate=startDate;
        if ((startDate <= endDate) &&$scope.noticePeriod!=undefined&&$scope.noticePeriod!=" ") {
            $scope.completeReportData = [];
            $scope.showExportLink = false;
            $http.post(host + "/ttn/employee/resignations", {
                resignDate: startDate,
                lastWorkDate: endDate,
                noticePeriod: $scope.noticePeriod,
                employeeCode: params.empCode
            })
                .success(function (response) {
                    params.scope.resigned();
                    $modalInstance.dismiss('cancel');

                })

        }
    };
    $scope.maxstartDate="2017-01-01"
    $scope.minEndDate="2017-01-01"
});