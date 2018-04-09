bbApp.controller('staffReqCtrl', function($scope, $http, $location, appService, appCache, ngDialog,$rootScope) {

	if(appCache.get('project-form') == undefined && appCache.get('projectId') == undefined){
		$location.path("/project");
	}

	$scope.mode = "CREATE";

	$scope.projectStartDate = appCache.get('project-start-date');
	$scope.projectEndDate = appCache.get('project-end-date');
	$scope.row = [];
	$scope.lrow = [];
	$scope.currEditIndex = -1;
    $scope.staffList = [];


    $scope.staffingAjaxSuccess = function (response) {
        $scope.staffList = [];
        angular.forEach(response, function(currStaff){
            if(currStaff.active){
                currStaff.startDate = new Date(currStaff.startDate);
				currStaff.startDate.setHours(0,0,0,0);
                currStaff.endDate = new Date(currStaff.endDate);
                currStaff.endDate.setHours(0,0,0,0);
                if(currStaff.billable){
                    currStaff.billable = 'true';
                }else{
                    currStaff.billable = 'false';
                }
                $scope.staffList.push(currStaff);
            }
        });
    };
    
	if(appCache.get('projectId') != undefined){
		$scope.mode = "UPDATE";

        appService.getData(host + "/project/" + appCache.get('projectId') + '/staffing-request',
            "GET", "", $scope.staffingAjaxSuccess, undefined, false, true, true);


	}
	else if(appCache.get('staff-req-state') == undefined){
		$scope.staffList = [];
		$scope.staff={};
		$scope.staff.billable = "true";
		$scope.addNew = false;
		$scope.submitted = false;

		if(appCache.get('project-type')=="NonBillable"){
			$scope.staff.billable = "false";
		}
	}
	else{
		var cacheObj = appCache.get('staff-req-state');
		$scope.staffList = cacheObj.staffList;
		$scope.staff = cacheObj.staff;
		$scope.addNew = cacheObj.addNew;
		$scope.submitted = cacheObj.submitted;

		if(appService.isEmpty($scope.staff)){
			$scope.staff.billable = "true";
		}

		if(appCache.get('project-type')=="NonBillable"){
			$scope.staff.billable = "false";
		}
	}

	if(appCache.get('project-type')=="NonBillable"){
		$scope.disableBillable =  true;

		angular.forEach($scope.staffList, function(staff){
			staff.billable = "false";
		});

	}
	else{
		$scope.disableBillable =  false;
	}

    $scope.competencyAjaxSuccess = function (response) {
        $scope.competencies = response;
    };
    $scope.titleAjaxSuccess = function (response) {
        $scope.titleList = response;
    };

    appService.getData(host + "/competencies", "GET", "", $scope.competencyAjaxSuccess, undefined, true, false, false);
    appService.getData(host + "/ttn/employee/title", "GET", "", $scope.titleAjaxSuccess, undefined, true, false, false);


	$scope.done = function(){
		$location.path("/listProjects");
	};

	$scope.goBack = function(){
		if(appCache.get('project-type')=="NonBillable"){
			$location.path("/project");
		}
		else{
			if(appCache.get('billing-type')=="FixedPrice"){
				$location.path("/project/billing-info-fixed");
			}else{
				$location.path("/project/billing-info-tnm");
			}
		}

		if($scope.mode == 'CREATE'){
			var obj = {};
			obj.staffList = $scope.staffList;
			obj.staff = $scope.staff;
			obj.addNew = $scope.addNew;
			obj.submitted = $scope.submitted;
			appCache.put('staff-req-state',obj);
		}
	};

	$scope.addStaff = function(){
		$scope.submitted = true;
		
		if($scope.staffReq.$valid){
			$scope.addNew = false;
			
			if($scope.mode == 'UPDATE'){
				$scope.showLoader = true;
				var data = angular.copy($scope.staff);

				data.startDate = appService.convertDate(data.startDate);
				data.endDate = appService.convertDate(data.endDate);

				$http.put(host + "/project/" + appCache.get('projectId') + '/staffing-request', data)
				.success(function(response){
					$scope.staff.id = response.id;
                    $scope.staff.state = response.state;
					$scope.staffList.push($scope.staff);
					$scope.submitted = false;
					$scope.staff = {};
				})
				.error(function(response){
					$scope.addNew = true;
					ngDialog.open({ template: '<div class="error-dialog"><h4 class="title">Error</h4><p>'
						+ response[0].messages[0].message +'</p></div>', plain:true });
				})
				.finally(function(){
					$scope.showLoader = false;
				});
			}
			else{
				$scope.staffList.push($scope.staff);
				$scope.submitted = false;
				$scope.staff = {};
			}
		}
	};

	$scope.cancelAdd = function(){
		$scope.staff = {};
		$scope.submitted = false;
		$scope.addNew = false;
		$scope.staffReq.$setPristine();
	};

	$scope.addNewEditableRow = function(){

		if($scope.currEditIndex > -1){
			$scope.row[$scope.currEditIndex] = false;
			$scope.staffList.splice($scope.currEditIndex, 0, $scope.staff_bkp);
			$scope.currEditIndex = -1;
		}

		$scope.addNew = true;
		$scope.staff = {};
		$scope.staff.billable = "true";
		$scope.staffReq.$setPristine();

		if(appCache.get('project-type')=="NonBillable"){
			$scope.staff.billable = "false";
		}
	};

	$scope.copyRow = function(index){	

		if($scope.mode == 'UPDATE'){
			$scope.lrow[index+1] = true;
			var data = angular.copy($scope.staffList[index]);
			data.id = '';

			data.startDate = appService.convertDate(data.startDate);
			data.endDate = appService.convertDate(data.endDate);

			$http.put(host + "/project/" + appCache.get('projectId') + '/staffing-request', data)
			.success(function(response){
				var staffObj = angular.copy($scope.staffList[index]);
				staffObj.id = response.id;
				staffObj.state = response.state;
				$scope.staffList.splice(index, 0, staffObj);
			})
			.error(function(response){
				ngDialog.open({ template: '<div class="error-dialog"><h4 class="title">Error</h4><p>'
						+ response[0].messages[0].message +'</p></div>', plain:true });
			})
			.finally(function(){
				$scope.lrow[index+1] = false;
			});
		}
		else{
			$scope.staffList.splice(index, 0, angular.copy($scope.staffList[index]));
		}
	};

	$scope.deleteRequest = function(index){
		if($scope.mode == 'UPDATE'){

			var data = angular.copy($scope.staffList[index]);
			var staffBak = $scope.staffList.splice(index, 1)[0];

			$scope.lrow[index] = true;

			data.startDate = appService.convertDate(data.startDate);
			data.endDate = appService.convertDate(data.endDate);

			$http.delete(host + "/project/" + appCache.get('projectId') + '/staffing-request/' + data.id, data)
			.success(function(response){
				console.log("deleted");
			})
			.error(function(response){
				$scope.staffList.splice(index, 0, staffBak);
				ngDialog.open({ template: '<div class="error-dialog"><h4 class="title">Error</h4><p>'
					+ response[0].messages[0].message +'</p></div>', plain:true });
			})
			.finally(function(){
				$scope.lrow[index] = false;
			});
		}
		else{
			$scope.staffList.splice(index, 1);
		}
	};

	$scope.editRequest = function(index){
		if($scope.currEditIndex > -1){
			$scope.row[$scope.currEditIndex] = false;
			$scope.staffList.splice($scope.currEditIndex, 0, $scope.staff_bkp);
			
			if(index > $scope.currEditIndex)
				index = index+1;
		}

		$scope.row[index] = true;
		$scope.staff_bkp = $scope.staffList.splice(index, 1)[0];
		$scope.staff = angular.copy($scope.staff_bkp);
		$scope.currEditIndex = index;
	};

	$scope.cancelEdit = function(index){
		$scope.row[index] = false;
		$scope.staffList.splice(index, 0, $scope.staff_bkp);
		$scope.currEditIndex = -1;
	};

	$scope.doneEdit = function(index){
		if($scope.staffReq.$valid){
			$scope.row[index] = false
			$scope.currEditIndex = -1;

			if($scope.mode == 'UPDATE'){
				$scope.lrow[index] = true;
				var data = angular.copy($scope.staff);
				
				data.startDate = appService.convertDate(data.startDate);
				data.endDate = appService.convertDate(data.endDate);
				//data.state = "Open";

				$http.put(host + "/project/" + appCache.get('projectId') + '/staffing-request', data)
				.success(function(response){
					$scope.staffList.splice(index, 0, $scope.staff);
				})
				.error(function(response){
					$scope.currEditIndex = index;
					$scope.row[index] = true;
					ngDialog.open({ template: '<div class="error-dialog"><h4 class="title">Error</h4><p>'
					+ response[0].messages[0].message +'</p></div>', plain:true });
				})
				.finally(function(){
					$scope.lrow[index] = false;
				});
			}
			else{
				$scope.staffList.splice(index, 0, $scope.staff);
			}
		}
	};

	$scope.finish = function(){

		var flag = false;

		angular.forEach($scope.row, function (thisRow) {
			if(thisRow){
				flag = true;
			}
		});

		if($scope.addNew || flag){
			ngDialog.openConfirm({ template: '<div class="confirm-dialog"><h4 class="title">Confirm</h4>'
			+ '<p>You have unsaved data! Do you want to continue?</p>'
			+ '<div class="btns">'
			+ '<button class="btn btn-primary bb-button bb-button-small" data-ng-click="confirm();">Yes</button>'
			+ '<button class="btn btn-primary bb-button bb-button-small" data-ng-click="closeThisDialog();">No</button>'
			+ '</div></div>', plain:true
			}).then(function (success) {
				saveProject();
			});
		}
		else{
			saveProject();
		}
	};

	function saveProject(){
		$scope.$emit('toggleSpinner', true);
		var url = host + '/project';
		
		var project = angular.copy(appCache.get('project-form')); 
		project.staffRequestList = angular.copy($scope.staffList);


		if(project.projectType=='NonBillable'){
			project._Type = "nonBillable";
		}
		else{
			if(project.billingType=="FixedPrice"){
				project._Type = "fixedPrice";
				project.billingInfo = angular.copy(appCache.get('fp-billing-form'));
				project.billingInfo.type="fixedPrice";
			}
			else{
				project._Type = "timeAndMoney";
				project.billingInfo = angular.copy(appCache.get('tnm-billing-form'));
				project.billingInfo.type="timeAndMoney";
			}
		}

		/* Date formats */
		project.startDate = appService.convertDate(project.startDate);
		if(!(project.endDate == undefined || isNaN(project.endDate.getDate()))){
			project.endDate = appService.convertDate(project.endDate);
		}

		if(project.billingType=="FixedPrice"){
			angular.forEach(project.billingInfo.billingMilestones, function(mileStone){
				mileStone.tentativeDate = appService.convertDate(mileStone.tentativeDate);
			});
		}
		else if(project.billingType=="TimeAndMoney"){
			project.billingInfo.invoiceInfo.invoiceStartDate = appService.convertDate(project.billingInfo.invoiceInfo.invoiceStartDate);
		}

		angular.forEach(project.staffRequestList, function(currStaff){
			currStaff.startDate = appService.convertDate(currStaff.startDate);
			currStaff.endDate = appService.convertDate(currStaff.endDate);
		});

		var data = project;
        $http.post(url, data)
        .success(function(response) {
        	appCache.put('message', "Project created successfully!");
            $scope.navigate(project.projectName);
        })
        .error(function(response){
        	ngDialog.open({ template: '<div class="error-dialog"><h4 class="title">Error</h4><p>'
					+ response[0].messages[0].message +'</p></div>', plain:true });
        })
       	.finally(function(){
        	$scope.$emit('toggleSpinner', false);
        });
	}
    $scope.navigate = function (projectName) {
        appService.getData(host + "/project/checkName/" +projectName, "GET", "", $scope.projectAjaxSuccess, undefined, false, true, true);
    };
    $scope.projectAjaxSuccess = function (response) {
        $rootScope.callBackURL="/listProjects";
        var project = response;
        project.startDate = new Date(project.startDate);
        project.endDate = new Date(project.endDate);
        project.fromDL = true;
        appCache.put("project", project);
        $location.path("/staffing-needs");
    };

	$scope.enableCalender = function(id, date){
		$('#'+id).datetimepicker({
        	format: 'YYYY-MM-DD'
        });

        $('#'+id).on("dp.change", function() {
        	if(date=='startdate'){
				$scope.staff.startDate = new Date($("#" + id + ">input").val()+" 00:00:00");
				$scope.staffReq.startdate.$setViewValue($("#" + id + ">input").val());
        	}
			else{
				$scope.staff.endDate = new Date($("#" + id + ">input").val() +" 00:00:00");
				$scope.staffReq.enddate.$setViewValue($("#" + id + ">input").val());
			}
		});
	};

	$scope.label = {};
	$scope.label.addRequest = "Add Request";
	$scope.label.no_staff_request = "No Staff Requested";
	$scope.label.selectTitle = "-Select Title-";
	$scope.label.yes = "Yes";
	$scope.label.no = "No";
	$scope.label.selectCompetency = "-Select Competency-";
	$scope.label.title = "TITLE";
	$scope.label.billable ="BILLABLE";
	$scope.label.competency = "COMPETENCY";
	$scope.label.startdate = "START DATE";
	$scope.label.enddate = "END DATE";
	$scope.label.allocation = "% ALLOCATION";
	$scope.label.addn_details = "ADDL. DETAILS";
	$scope.label.remove = "Remove";
	$scope.label.add = "Add";
	$scope.label.edit = "Edit";
	$scope.label.delete = "Delete";
	$scope.label.cancel = "Cancel";
	$scope.label.copy = "Copy";

	$scope.errorMessage = {};
	$scope.errorMessage.allocation_invalid = "Invalid Number";
	$scope.errorMessage.allocation_invalid_minmax = "Allocation should be between 0 and 100";
	$scope.errorMessage.date_invalid = "Invalid Date Format";
	$scope.errorMessage.startdate_invalid_min = "Start Date should not be less than Project Start Date";
	$scope.errorMessage.startdate_invalid_max = "Start Date should not be greater than Project End Date";
	$scope.errorMessage.enddate_invalid_min = "End Date should not be less than Start Date";
	$scope.errorMessage.enddate_invalid_max = "End Date should not be greater than Project End Date";

});
