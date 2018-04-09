bbApp.controller('topNavCtrl', function($http, $scope, $location, appCache, $rootScope, $route, ngDialog) {

    $rootScope.sideMenuActive = false;

	$scope.$watch('$root.loggedUser', function() {
  		$scope.userInfo = $rootScope.loggedUser;

        if(typeof $rootScope.loggedUser != 'undefined' ){
            $scope.role = $rootScope.loggedUser.currentRole;
            $scope.regions = $rootScope.loggedUser.regions;

            if($rootScope.loggedUser.roles.length > 1){
                $scope.addnRoles = [];
                angular.forEach($rootScope.loggedUser.roles, function (role) {
                    if(role != $scope.role)
                        $scope.addnRoles.push(role);
                })

            }
        }
  	}, true);

    $scope.changeRole = function (role) {
        $rootScope.loggedUser.currentRole = role;
        $scope.role = role;
        $location.path("/");
        $route.reload();
        $scope.showUserDtls = !$scope.showUserDtls;
    };

	$scope.createProject = function(){
		appCache.removeAll();
		$location.path("/project");
	};

	$scope.signOut = function(){
		$scope.showUserDtls = false;
		$http.get(host + "/endSession")
            .success(function(response){
                $location.path("/login");
            })
            .error(function (response) {
                alert("Something Went Wrong! Please try later.");
            });
	};

	$scope.assignRole = function(){
        $scope.showUserDtls = false;
		$location.path("/assign-roles");
	};

	$scope.toggleSideMenu = function(){
	    $rootScope.sideMenuActive = !$rootScope.sideMenuActive;
    };

    $scope.syncProjects = function () {
        $http.get(host+"/ttn/employee/pushAllUsersProjectInfo")
            .then(
                function(response){
                    // success callback
                    console.log(response);
                    if (response.status == 200) {
                        ngDialog.open({
                            template: "<div class='success-dialog'><h4 class='title'>Success</h4><p>"
                            + "The user's current projects information has been successfully synced with NewersWorld Application" + "</p></div>", plain: true
                        });
                    }
                },
                function(response){
                    // failure call back
                    console.log(response);
                    if (response.status == 500) {
                        ngDialog.open({
                            template: '<div class="error-dialog"><h4 class="title">Error</h4><p>'
                            + 'Error syncing user projects information from NewersWorld Application' + '</p></div>', plain: true
                        });
                    }
                }
            );
    };

    $scope.label = {};
	$scope.label.createProject = "Create Project";
	$scope.label.createClient = "Create Client";
	$scope.label.login = "Login";

});