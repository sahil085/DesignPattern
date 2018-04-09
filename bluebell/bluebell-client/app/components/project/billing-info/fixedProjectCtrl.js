bbApp.controller('fixedProjectCtrl', function($scope, $location, appCache, ngDialog, appService,$rootScope) {

	$scope.mode = "CREATE";

	if(appCache.get('project-form') == undefined && appCache.get('projectId') == undefined){
		$location.path("/project");
	}

	$scope.projectStartDate = appCache.get('project-start-date');

    $scope.currencyAjaxSuccess = function (response) {
        $scope.currencyList = response;
    };

    appService.getData(host + "/currency", "GET", "", $scope.currencyAjaxSuccess, undefined, true, false, false);


    $scope.billingAjaxSuccess = function (response) {
        $scope.formData = response;
        angular.forEach($scope.formData.billingMilestones, function(mileStone){
            mileStone.tentativeDate = new Date(mileStone.tentativeDate);
        });
    };

	if(appCache.get('projectId') != undefined){
		var projectId = appCache.get('projectId');
		$scope.mode = "UPDATE";

        appService.getData(host + '/project/billable/' + appCache.get('projectId') + '/billing-info',
            "GET", "", $scope.billingAjaxSuccess, undefined, false, true, true);

	}
	else if(appCache.get('fp-billing-form') != undefined){
		$scope.formData = appCache.get('fp-billing-form');
	}
	else{

		$scope.formData = {};
		$scope.formData.billingMilestones = [];
		$scope.mileStone = {};

		$scope.mileStone.name="";
		$scope.mileStone.amount="";
		$scope.mileStone.tentativeDate="";
		$scope.formData.billingMilestones.push($scope.mileStone);
	}

	$scope.addMilestone = function(){
		$scope.mileStone = {};
		$scope.mileStone.name="";
		$scope.mileStone.amount="";
		$scope.mileStone.tentativeDate="";
		$scope.formData.billingMilestones.push($scope.mileStone);
	};

	$scope.goBack = function(){
		$location.path("/project");

		if($scope.mode=='CREATE'){
			appCache.put('fp-billing-form',$scope.formData);
		}
	};

	$scope.goNext = function(){
		$location.path("/project/staff-request");
	};

	$scope.reconcile = function(){
		if($scope.formData.billingMilestones.length > 0){
			var projValue = $scope.formData.projectValue.amount;
			var totalAmt = 0;

			angular.forEach($scope.formData.billingMilestones, function(thisMileStone){
				totalAmt += thisMileStone.amount;
			});

			if(totalAmt != projValue){
				ngDialog.open({ template: '<div class="error-dialog"><h4 class="title">Error</h4><p>'
					+$scope.errorMessage.reconcileError+'</p></div>', plain:true });
			}
			else{
				if($scope.mode == 'UPDATE'){
					updateBilling();
				}
				else{
					next();
				}
			}
		}
		else{
			if($scope.mode == 'UPDATE'){
					updateBilling();
				}
				else{
					next();
				}
		}

	};

    $scope.billingUpdateAjaxSuccess = function (response) {
        /*ngDialog.open({ template: '<div class="success-dialog"><h4 class="title">Success</h4><p>'
        + 'Billing Details Updated Successfully' +'</p></div>', plain:true });*/
		appCache.put('message', "Project updated successfully!");
        $scope.navigate();
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

	function updateBilling(){

		var data = angular.copy($scope.formData);

		angular.forEach(data.billingMilestones, function(mileStone){
			mileStone.tentativeDate = appService.convertDate(mileStone.tentativeDate);
		});

        appService.getData(host + '/project/billable/' + appCache.get('projectId') +'/billing-info',
            "PUT", data, $scope.billingUpdateAjaxSuccess, undefined, false, true, true);

	}

	function next(){		
		$location.path("/project/staff-request");
		appCache.put('fp-billing-form',$scope.formData);
	}	

	$scope.loadScript = function(i){
		console.log("loaded");
		$('#datePicker1').datetimepicker({
        	format: 'DD/MM/YYYY'
        });

	};

	$scope.removeMilestone = function(index){
		$scope.formData.billingMilestones.splice(index, 1);

	};

	$scope.enableCalender = function(index, name){
		$('#datePicker'+(index+1)).datetimepicker({
        	format: 'YYYY-MM-DD'
        });

        $("#datePicker"+(index+1)).on("dp.change", function() {
			$scope.formData.billingMilestones[index].tentativeDate = new Date($("#datePicker"+(index+1) + ">input").val()+" 00:00:00");
			name.$setViewValue($("#datePicker"+(index+1) + ">input").val());
		});
	};

	$scope.label = {};
	$scope.label.projectValue = "PROJECT VALUE";
	$scope.label.selectCurrency = "-Select Currency-";
	$scope.label.billingMilestone = "BILLING MILESTONE";
	$scope.label.remove = "Remove";
	$scope.label.addMilestone = "Add Billing Milestone";

	$scope.errorMessage = {};
	$scope.errorMessage.selectCurrency = "Select Currency";
	$scope.errorMessage.amount_required = "Amount is Required";
	$scope.errorMessage.amount_invalid = "Invalid Amount";
	$scope.errorMessage.name_required = "Name is Required";
	$scope.errorMessage.date_required = "Date is Required";
	$scope.errorMessage.date_invalid = "Invalid Date Format";
	$scope.errorMessage.date_invalid_min = "Date Should not be less than Project Start Date";
	$scope.errorMessage.reconcileError = "Project Value should be equal to the sum of Billing Milestone Amounts";

});