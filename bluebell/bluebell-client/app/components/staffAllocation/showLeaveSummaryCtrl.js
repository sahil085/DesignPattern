/**
 * Created by manpreet on 28/12/17.
 */

bbApp.controller('showLeaveSummaryCtrl', function($scope, $http, appCache, $timeout, ngDialog, filterFilter, params, $modalInstance) {

    $scope.$emit('toggleSpinner', true);
    $scope.lrow_c = [];
    $scope.leaveList = [];
    $scope.currLeaveList = [];
    $scope.currLeavePage = 1;
    $scope.itemPerPage = 10;
    $scope.maxSize = 5;
    $scope.searchText = "";

    $scope.$watch('searchText', function () {
        if($scope.projectList!=undefined) {
            if($scope.projectListBackup == undefined || $scope.projectListBackup.length == 0){
                $scope.projectListBackup = angular.copy($scope.projectList);
            }
            $scope.projectList = filterFilter($scope.projectListBackup, $scope.searchText);
        }
    });


    $scope.$watch('currLeavePage + itemPerPage + leaveList', function() {
        changeCurrLeaveList();
    });
    function changeCurrLeaveList() {
        var begin = (($scope.currLeavePage - 1) * $scope.itemPerPage)
            , end = begin + $scope.itemPerPage;

        $scope.currLeaveList = $scope.leaveList.slice(begin, end);
    }

    if(appCache.get('message') != undefined){
        $scope.showAlertMessage = true;
        $scope.message = appCache.get('message');
        appCache.remove('message');
        fadeOut();
    }

    $scope.clientAjax = $http.get(host + "/ttn/employee/leavesSummary?n=90&username="+params.emailAddress)
        .success(function(response){
            if (response.data.response) {
                $scope.leaveList = response.data.response;
                $scope.currLeaveList = response.data.response.slice(0, $scope.itemPerPage);
            }
        })
        .error(function (response) {
            $scope.$emit('toggleSpinner', false);
            ngDialog.open({ template: '<div class="error-dialog"><h4 class="title">Error</h4><p>'
            + response[0].messages[0].message +'</p></div>', plain:true });
        })
        .finally(function () {
            $scope.$emit('toggleSpinner', false);
        });

    function fadeOut(){
        $timeout(function () { $scope.showAlertMessage = false; }, 3000);
    }

    $scope.close = function () {
        $modalInstance.dismiss('cancel');
    };


    $scope.label = {};

    $scope.label.startDate = "START DATE";
    $scope.label.endDate = "END DATE";
    $scope.label.leaveType = "LEAVE TYPE";
    $scope.label.status = "STATUS";
    $scope.label.no_client = "No Leaves Found";

});