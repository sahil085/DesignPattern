bbApp.controller('clientCtrl', function($scope, $http, $location, appCache, ngDialog, appService) {

	$scope.mode = "CREATE";

    $scope.clientAjaxSuccess = function (response) {
        $scope.formData = response;
    };

	if(appCache.get('clientId') != undefined){

		$scope.clientId = appCache.get('clientId');
		appCache.remove('clientId');
		$scope.mode = "UPDATE";

        appService.getData(host + "/client/" + $scope.clientId, "GET", "", $scope.clientAjaxSuccess, undefined, false, true, true);

	}

    $scope.createClientAjaxSuccess = function (response) {
        appCache.put('message', "Client created successfully!");
        $location.path("/listClients");
    };

	$scope.create = function(){
		var data = $scope.formData;
        appService.getData(host + "/client", "POST", data, $scope.createClientAjaxSuccess, undefined, false, true, true);
	};

    $scope.updateClientAjaxSuccess = function (response) {
        appCache.put('message', "Client updated successfully!");
        $location.path("/listClients");
    };

	$scope.update = function(){
		var data = $scope.formData;
        appService.getData(host + "/client/" +  $scope.clientId, "PUT", data, $scope.updateClientAjaxSuccess, undefined, false, true, true);
	};

	$scope.label = {};
	$scope.label.clientName = "Name";
	$scope.label.address1 = "Address Line1";
	$scope.label.address2 = "Address Line2";
	$scope.label.city = "City";
	$scope.label.state = "State";
	$scope.label.country = "Country";
	$scope.label.zipcode = "zipcode";
	$scope.label.createClient = "Create Client";
	$scope.label.updateClient = "Update Client";

	$scope.errorMessage = {};
	$scope.errorMessage.client_name1_required = "Client Name is required";
	$scope.errorMessage.add_required = "Address is required";
	$scope.errorMessage.city_required = "City is required";
	$scope.errorMessage.state_required = "State is required";
	$scope.errorMessage.country_required = "Please Select a country";
	$scope.errorMessage.zipCode_required = "Zip Code is required";
	$scope.errorMessage.invalid_zip = "Invalid Zip Code"

});