bbApp.controller('userRoleMappingCtrl', function($scope, $http, $route, ngDialog, $rootScope, appService) {

    $scope.authorityList = [];
	$scope.assignRoles = function () {
        ngDialog.openConfirm({
            template: 'app/components/userRole/assignRoles.html',
            className: 'ngdialog-theme-default ngdialog-search noscroll',
            preCloseCallback: function() {
                $route.reload();
                return true;
            }
        });
    };

    $scope.authorityAjaxSuccess = function (response) {
        $scope.authorityList = response;
        $rootScope.authorities = response;
    };

    appService.getData(host + "/ttn/employee/authority", "GET", "", $scope.authorityAjaxSuccess, undefined, false, true, true);
    
    $scope.label = {};
    $scope.label.userName = "User Name";
    $scope.label.email  = "Email";
    $scope.label.userRole = "Role";
    $scope.label.userRegion = "Region";

});
