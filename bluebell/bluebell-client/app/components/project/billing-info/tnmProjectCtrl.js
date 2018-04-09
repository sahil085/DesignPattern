bbApp.controller('tnmProjectCtrl', function($scope, $location, appCache, ngDialog, appService,$rootScope) {

	if(appCache.get('project-form') == undefined && appCache.get('projectId') == undefined){
		$location.path("/project");
	}

	$scope.mode = "CREATE";
	$scope.projectStartDate = appCache.get('project-start-date');

    $scope.currencyAjaxSuccess = function (response) {
        $scope.currencyList = response;
    };
    $scope.invoiceCycleAjaxSuccess = function (response) {
        $scope.invoiceCycle = response;
    };
    $scope.titleAjaxSuccess = function (response) {
        $scope.titleList = response;
    };

    appService.getData(host + "/currency", "GET", "", $scope.currencyAjaxSuccess, undefined, true, false, false);
    appService.getData(host + "/invoice-cycle", "GET", "", $scope.invoiceCycleAjaxSuccess, undefined, true, false, false);
    appService.getData(host + "/ttn/employee/title", "GET", "", $scope.titleAjaxSuccess, undefined, true, false, false);
	

    $scope.billingAjaxSuccess = function (response) {
        $scope.formData = response;
        $scope.formData.invoiceInfo.invoiceStartDate = new Date($scope.formData.invoiceInfo.invoiceStartDate);
    };

	if(appCache.get('projectId') != undefined){

		$scope.mode = "UPDATE";

        appService.getData(host + "/project/billable/" + appCache.get('projectId') + '/billing-info',
            "GET", "", $scope.billingAjaxSuccess, undefined, false, true, true);

	}
	else if(appCache.get('tnm-billing-form') != undefined){
		$scope.formData = appCache.get('tnm-billing-form');
	}
	else{

		$scope.formData = {};
		$scope.formData.invoiceInfo = {};
		$scope.formData.billingRates = [];
		$scope.billingrate = {};
		$scope.billingrate.rate = {};
		$scope.billingrate.title="";

		$scope.billingrate.rate.currency="";
		$scope.billingrate.rate.amount="";
		$scope.formData.billingRates.push($scope.billingrate);
	}


	$scope.addRate = function(){
		$scope.billingrate = {};
		$scope.billingrate.title="";

		$scope.billingrate.rate = {};
		$scope.billingrate.rate.currency="";
		$scope.billingrate.rate.amount="";
		$scope.formData.billingRates.push($scope.billingrate);
	};

	$scope.billingUpdateAjaxSuccess = function (response) {
        /*ngDialog.open({ template: '<div class="success-dialog"><h4 class="title">Success</h4><p>'
        + 'Billing Details Updated Successfully' +'</p></div>', plain:true });*/
        appCache.put('message', "Project updated successfully!");
        $scope.navigate()
    };
    $scope.navigate = function () {
        appService.getData(host + "/project/" + appCache.get('projectId'), "GET", "", $scope.getProjectAjaxSuccess, undefined, false, true, true);
    };
    $scope.getProjectAjaxSuccess = function (response) {
        var project = response;
        $rootScope.callBackURL='/listProjects';
        project.startDate = new Date(project.startDate);
        project.endDate = new Date(project.endDate);
        project.fromDL = true;
        appCache.put("project", project);
        $location.path("/staffing-needs");
    };
	$scope.update = function(){

		var data = angular.copy($scope.formData);
		data.invoiceInfo.invoiceStartDate = appService.convertDate(data.invoiceInfo.invoiceStartDate);

        appService.getData(host + '/project/billable/' + appCache.get('projectId') +'/billing-info',
            "PUT", data, $scope.billingUpdateAjaxSuccess, undefined, false, true, true);

	};


	$scope.continue = function(){

		if($scope.mode == 'CREATE'){
			appCache.put('tnm-billing-form',$scope.formData);
		}

		$location.path("/project/staff-request");

	};


	$scope.removeRate = function(index){
		$scope.formData.billingRates.splice(index, 1);
	};

	$scope.goBack = function(){
		$location.path("/project");

		if($scope.mode == 'CREATE'){
			appCache.put('tnm-billing-form',$scope.formData);
		}
	};

	$(function() {

		$('#datePicker').datetimepicker({
        	format: 'YYYY-MM-DD'
        });

		$("#datePicker").on("dp.change", function() {
			$scope.formData.invoiceInfo.invoiceStartDate = new Date($("#datePicker>input").val()+" 00:00:00");
			$scope.tnm.date.$setViewValue($("#datePicker>input").val());
		});

	});

	$scope.label = {};
	$scope.label.invoicingStartDate = "INVOICE DATE";
	$scope.label.frequency = "FREQUENCY";
	$scope.label.billingrate = "BILLING RATES";
	$scope.label.addResourceRate = "Add Resource Rate";
	$scope.label.selectFrequency = "-Select Frequency-";
	$scope.label.selectTitle = "-Select Title-";
	$scope.label.selectCurrency = "-Select Currency-";
	$scope.label.remove = "Remove";

	$scope.errorMessage = {};
	$scope.errorMessage.invoicingDate_required = "Invoice Date is required";
	$scope.errorMessage.invoicingDate_invalid = "Invalid Date Format";
	$scope.errorMessage.invoicingDate_invalid_min = "Invoice Date should not be less than Project Start Date";
	$scope.errorMessage.frequency_required = "Select Frequency";
	$scope.errorMessage.title_required = "Select Title";
	$scope.errorMessage.currency_required = "Select Currency";
	$scope.errorMessage.rate_required = "Rate is required";
	$scope.errorMessage.rate_invalid = "Invalid Rate";

});