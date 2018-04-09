/**
 * Created by Ashutosh Meher on 21/10/16.
 */

bbApp.controller('assignRolesCtrl', function ($scope, $http, appService, $route, ngDialog, $rootScope) {

    $scope.showDialogSpinner = true;
    $scope.sRegion = [];
    $scope.sRole = [];
    $scope.authorities = angular.copy($rootScope.authorities);

    $scope.isEmployeeSelected = null;
    $scope.selectedEmployee = null;

    $scope.setEmployeeSelected = function (isEmployeeSelected, emailId) {
        $scope.isEmployeeSelected = isEmployeeSelected;
        $scope.selectedEmployee = emailId;

        $scope.sRegion = [];
        $scope.sRole = [];

        angular.forEach($scope.authorities, function (authority) {
            if (authority.email == emailId) {
                if (authority.regions != undefined)
                    $scope.sRegion = authority.regions;
                if (authority.roles != undefined)
                    $scope.sRole = authority.roles;
            }
        });
    };

    $scope.empListAjaxSuccess = function (data) {
        $scope.empList = data;
        $scope.showDialogSpinner = false;
    };
    $scope.rolestAjaxSuccess = function (data) {
        $scope.roles = data;
    };
    $scope.regionAjaxSuccess = function (data) {
        $scope.regions = data;
    };

    appService.getData(host + "/ttn/employee?requireAll=true", "GET", "", $scope.empListAjaxSuccess, undefined, false, true, true);
    appService.getData(host + "/employee-roles", "GET", "", $scope.rolestAjaxSuccess, undefined, true, false, false);
    appService.getData(host + "/allregions", "GET", "", $scope.regionAjaxSuccess, undefined, true, false, false);

    $scope.isRegionChecked = function (region) {
        var items = $scope.sRegion;
        for (var i = 0; i < items.length; i++) {
            if (region == items[i])
                return true;
        }
        return false;
    };
    $scope.isRoleChecked = function (role) {
        var items = $scope.sRole;
        for (var i = 0; i < items.length; i++) {
            if (role == items[i])
                return true;
        }
        return false;
    };

    $scope.assignRoleAjaxCallback = function (response) {
        $scope.showDialogSpinner = false;
    };

    $scope.requestAssignRole = function () {
        $scope.showDialogSpinner = true;

        var data = {
            email: $scope.selectedEmployee,
            regions: $scope.sRegion,
            roles: $scope.sRole
        };

        appService.getData(host + "/ttn/employee/authority", "PUT", data, $scope.assignRoleAjaxCallback, $scope.assignRoleAjaxCallback, false, false, true);
    };
});