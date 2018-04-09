bbApp.controller('processStepCtrl', function($scope, $location) {

	$scope.currStep = 1;

	$scope.getClass = function(step){
		if(step==1){
			if($scope.currStep == 1){
				return "active";
			}
			else{
				return "complete";
			}
		}else if(step==2){
			if($scope.currStep == 2){
				return "active";
			}
			else if($scope.currStep == 3){
				return "complete";
			}
			else{
				return "disabled";
			}
		}else if(step==3){
			if($scope.currStep == 3){
				return "active";
			}
			else{
				return "disabled";
			}
		}
	}

	$scope.routeList = ["/project", "/project/billing-info-fixed", "/project/billing-info-tnm", "/project/staff-request"]

	function detectRoute() {
      	if($location.path() == "/project"){
      		$scope.currStep = 1;
      	}
      	else if($location.path()=="/project/billing-info-fixed" || $location.path()=="/project/billing-info-tnm"){
      		$scope.currStep = 2;
      	}
      	else if($location.path() == "/project/staff-request"){
      		$scope.currStep = 3;
      	}
    }
    
    $scope.$on('$routeChangeSuccess', detectRoute);

});