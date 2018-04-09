bbApp.controller('staffAllocationCtrl', function($scope, $http, $location, appCache, appService,$modal,$rootScope, ngDialog, filterFilter) {

    $('[data-toggle="tooltip"]').tooltip();

    $scope.currStaffId = $rootScope.staffId;
    $scope.projectId = $rootScope.projectId;
    $scope.currCompetency = $rootScope.competency;
    $scope.nominationList = $rootScope.nominationList;
    $scope.showTitleFilter = false;
    $scope.showDialogSpinner = true;
    $scope.showExpFilter = false;
    $scope.showCompetencyFilter = false;
    $scope.showAllocationFilter = true;
    $scope.showtimeinTTNFilter = false;
    $scope.empTitle = [];
    $scope.experience = [];
    $scope.crating = [];
    $scope.competencyType = {};
    $scope.competencyType.primary = true;
    $scope.popup = [];
    var ind = 0;

    $scope.empBackUpForSearchFilter = [];
    $scope.refreshBackup = true;
    $scope.searchText = "";

    $scope.isTitleChecked = function(title) {
        var items = $scope.empTitle;
        for (var i=0; i < items.length; i++) {
            if (title == items[i])
                return true;
        }
        return false;
    };

    $scope.setHeight = function () {
        $('#filterWrap').height($('.ngdialog-content').height()-90);
    };

    $scope.$watch('searchText', function () {
        if($scope.employeeListBuffer != undefined ) {
            if($scope.refreshBackup){
                $scope.empBackUpForSearchFilter = angular.copy($scope.employeeListBuffer);
                $scope.refreshBackup = false;
            }

            $scope.employeeListBuffer = filterFilter($scope.empBackUpForSearchFilter, {'name':$scope.searchText});

            ind = 0;
            $scope.currEmpList = $scope.employeeListBuffer.slice(0, 10);
        }
    });

    $scope.billableRangeSlider = {
        minValue: 0,
        maxValue: 0,
        options: {
            floor: 0,
            ceil: 100,
            step: 1,
            getSelectionBarColor: function (value) {
                return "#5b67ab";
            },
            getPointerColor: function (value) {
                return "#5b67ab";
            }
        }
    };

    $scope.$watch('billableRangeSlider', function () {
        if($scope.employeeListBuffer != undefined) {
            $scope.applyFilter();
        }
    }, true);

    $scope.nonBillableRangeSlider = {
        minValue: 0,
        maxValue: 0,
        options: {
            floor: 0,
            ceil: 100,
            step: 1,
            getSelectionBarColor: function (value) {
                return "#5b67ab";
            },
            getPointerColor: function (value) {
                return "#5b67ab";
            }
        }
    };

    $scope.$watch('nonBillableRangeSlider', function () {
        if($scope.employeeListBuffer != undefined) {
            $scope.applyFilter();
        }
    }, true);

    $scope.shadowRangeSlider = {
        minValue: 0,
        maxValue: 0,
        options: {
            floor: 0,
            ceil: 100,
            step: 1,
            getSelectionBarColor: function (value) {
                return "#5b67ab";
            },
            getPointerColor: function (value) {
                return "#5b67ab";
            }
        }
    };

    $scope.$watch('shadowRangeSlider', function () {
        if($scope.employeeListBuffer != undefined) {
            $scope.applyFilter();
        }
    }, true);

    function updateEmpList(){

        var empData = $scope.empList;
        $scope.empList = [];

        angular.forEach(empData, function(emp){
            emp.isNominated = false;

            angular.forEach($scope.nominationList, function(nemp){
                if(nemp.email == emp.emailAddress){
                    emp.isNominated = true;
                }
            });

            $scope.empList.push(emp);

        });

        var empData = $scope.employeeListBuffer;
        $scope.employeeListBuffer = [];

        angular.forEach(empData, function(emp){
            emp.isNominated = false;

            angular.forEach($scope.nominationList, function(nemp){
                if(nemp.email == emp.emailAddress){
                    emp.isNominated = true;
                }
            });

            $scope.employeeListBuffer.push(emp);

        });

        var curLength =   $scope.currEmpList.length;
        $scope.currEmpList = [];
        $scope.currEmpList =  $scope.employeeListBuffer.slice(0, curLength);

   }

   $scope.empListAjaxSuccess = function(data){

      if($scope.nominationList.length == 0)
         $scope.empList = data;
      else{
         var empData = data;
         $scope.empList = [];

         angular.forEach(empData, function(emp){

            angular.forEach($scope.nominationList, function(nemp){
               if(nemp.email == emp.emailAddress){
                  emp.isNominated = true;
               }
            });

            $scope.empList.push(emp);
         });
      }
      $scope.showDialogSpinner = false;
      $scope.employeeListBuffer = angular.copy($scope.empList);
      $scope.currEmpList = $scope.employeeListBuffer.slice(0, 10);
      $scope.refreshBackup = true;
      $scope.searchText = "";
       $scope.resigned();
       $scope.applyFilter();
   };

   $scope.loadMore = function() {
	   if (angular.isUndefined($scope.currEmpList.length)) {
		   ind = 0;
	   } else {
		   ind = $scope.currEmpList.length
	   }
	   var r = 10;
	   if (ind + 10 >= $scope.employeeListBuffer.length) {
		   r = $scope.employeeListBuffer.length - ind;
	   }
	   $scope.currEmpList = $scope.currEmpList.concat($scope.employeeListBuffer.slice(ind, r + ind));
   };

    $scope.competencyAjaxSuccess = function(data){
        $scope.competencyList = data;
    };
    $scope.titleAjaxSuccess = function(data){
        $scope.titleList = data;
    };
    $scope.sysParamExpAjaxSuccess = function (response) {
        $scope.experienceList = response;
    };

    appService.getData(host+"/competencies", "GET", "", $scope.competencyAjaxSuccess, undefined, true, false, false);
    appService.getData(host+"/ttn/employee/title", "GET", "", $scope.titleAjaxSuccess, undefined, true, false, false);
    appService.getData(host+"/ttn/employee?competency="+ $scope.currCompetency, "GET", "", $scope.empListAjaxSuccess, undefined, false, false, true);
    appService.getData(host+"/system-parameters/experience" , "GET", "", $scope.sysParamExpAjaxSuccess, undefined, true, false, true);

    $scope.getEmployeeList = function(){
        appService.getData(host+"/ttn/employee?competency="+ $scope.currCompetency, "GET", "", $scope.empListAjaxSuccess, undefined, false, false, true);
    };


    $scope.allocate = function(employee){
        $scope.emp=employee;
        var templateUrl= 'app/components/staffAllocation/employeeAllocation.html';
        var controller='employeeAllocation';
        appService.showModal($modal,controller,templateUrl,{scope:$scope},'resignation-details');
        // $scope.showDialogSpinner = true;
        //
        // $http.put(host + '/project/' + $scope.projectId + '/staffing-request/' + $scope.currStaffId + '/allocate/' + empCode)
        //     .success(function(response){
        //         $scope.closeThisDialog();
        //     })
        //     .error(function (response) {
        //         ngDialog.open({ template: '<div class="error-dialog"><h4 class="title">Error</h4><p>'
        //         + response[0].messages[0].message +'</p></div>', plain:true });
        //     })
        //     .finally(function(){
        //         $scope.showDialogSpinner = false;
        //     });
    };

    $scope.nominate = function(empCode){

        $scope.showDialogSpinner = true;

        $http.put(host + '/project/' + $scope.projectId + '/staffing-request/' + $scope.currStaffId + '/nominate/' + empCode)
            .success(function(response){
                var obj = {};
                obj.email = empCode;
                $scope.nominationList.push(obj);
                updateEmpList();
            })
            .error(function (response) {
                ngDialog.open({ template: '<div class="error-dialog"><h4 class="title">Error</h4><p>'
                + response[0].messages[0].message +'</p></div>', plain:true });
            })
            .finally(function(){
                $scope.showDialogSpinner = false;
            });
    };

    $scope.rejectNomination = function(index, empCode){

        $scope.showDialogSpinner = true;
        var nindex = 0;
        $http.put(host + '/project/' + $scope.projectId + '/staffing-request/' + $scope.currStaffId + '/reject/' + empCode)
            .success(function(response){
                angular.forEach($scope.nominationList, function (nemp) {
                   if(nemp.email == empCode){
                       $scope.nominationList.splice(nindex, 1);
                   }
                    nindex++;
                });
                updateEmpList();
            })
            .error(function (response) {
                ngDialog.open({ template: '<div class="error-dialog"><h4 class="title">Error</h4><p>'
                + response[0].messages[0].message +'</p></div>', plain:true });
            })
            .finally(function(){
                $scope.showDialogSpinner = false;
            });
    };



    $scope.applyFilter = function () {

        $scope.employeeListBuffer = [];

        if($scope.empTitle == undefined || $scope.empTitle.length == 0){
            $scope.employeeListBuffer = $scope.empList;
        }
        else {
            angular.forEach($scope.empList, function (item) {
                angular.forEach($scope.empTitle, function (title) {
                    title = title.trim();
                    if (item.title == title) {
                        $scope.employeeListBuffer.push(item);
                    }
                });
            });
        }
        if(!$scope.resignFilter || $scope.resignFilter==undefined){
            var filteredList = $scope.employeeListBuffer;
            $scope.employeeListBuffer=[];
            angular.forEach(filteredList, function(item) {
                if(!item.resign)
                    $scope.employeeListBuffer.push(item);

            });


        }
        if($scope.experience != undefined && $scope.experience.length != 0){

            var filteredList = $scope.employeeListBuffer;
            $scope.employeeListBuffer = [];

            angular.forEach(filteredList, function(item) {
                angular.forEach($scope.experience, function (exp) {
                    var thisMin = parseInt(exp.split("-")[0]);
                    var thisMax = parseInt(exp.split("-")[1]);

                    if (item.noOfYearsExp >= thisMin && item.noOfYearsExp <= thisMax) {
                        $scope.employeeListBuffer.push(item);
                    }
                });
            });
        }

        if($scope.crating != undefined && $scope.crating.length != 0){

            var filteredList = $scope.employeeListBuffer;
            $scope.employeeListBuffer = [];

            angular.forEach(filteredList, function(item) {
                angular.forEach($scope.crating, function(thisRating){
                    if(item.competency.rating == thisRating){
                        $scope.employeeListBuffer.push(item);
                    }
                });
            });
        }

        if($scope.billableRangeSlider.minValue > 0 || $scope.billableRangeSlider.maxValue <100 ||
                $scope.nonBillableRangeSlider.minValue > 0 || $scope.nonBillableRangeSlider.maxValue < 100 ||
            $scope.shadowRangeSlider.minValue > 0 || $scope.shadowRangeSlider.maxValue < 100){

            var filteredList = $scope.employeeListBuffer;
            $scope.employeeListBuffer = [];

            angular.forEach(filteredList, function(item) {
                    if((item.additionalInfo.totalBillableAllocation >= $scope.billableRangeSlider.minValue &&
                        item.additionalInfo.totalBillableAllocation <= $scope.billableRangeSlider.maxValue) &&
                        (item.additionalInfo.totalNonBillableAllocation >= $scope.nonBillableRangeSlider.minValue &&
                        item.additionalInfo.totalNonBillableAllocation <= $scope.nonBillableRangeSlider.maxValue &&
                        (item.additionalInfo.totalShadowAllocation >= $scope.shadowRangeSlider.minValue &&
                        item.additionalInfo.totalShadowAllocation <= $scope.shadowRangeSlider.maxValue) )){
                        $scope.employeeListBuffer.push(item);
                    }
                });
        }

        $scope.refreshBackup = true;
        $scope.searchText = "";
        ind = 0;
        $scope.currEmpList = $scope.employeeListBuffer.slice(0, 10);
    };
    $scope.resigned=function () {
        $http.get(host + "/ttn/employee/resignations")
            .success(function (response) {
                $scope.empListBkp= $scope.empList;
                angular.forEach(response,function(value, key) {
                    for(var i = 0; i < $scope.empListBkp.length; i++) {
                        if(value.employeeCode==$scope.empListBkp[i].code){
                            $scope.empListBkp[i].resign=true;
                            $scope.empListBkp[i].initiationDate=value.initiationDate;
                            $scope.empListBkp[i].lastWorkingDate=value.lastWorkingDate;
                            $scope.empListBkp[i].noticePeriod=value.noticePeriod;
                            break;
                        }

                    };

                });
                $scope.empList=$scope.empListBkp;
                $scope.employeeListBuffer=$scope.empList;
                $scope.currEmpList = $scope.employeeListBuffer.slice(0, 10);
                $scope.applyFilter();

            });
    };
    $scope.resigntionDetails=function(employee){
        $scope.emp=employee;
        var templateUrl= 'app/components/staffAllocation/resignationDetails.html';
        var controller='resignationDetailCtrl';
        appService.showModal($modal,controller,templateUrl,{scope:$scope}, "resignation-details");
    };

   $scope.label = {};
   $scope.label.title = "employee title";
   $scope.label.empName = "Employee name";
   $scope.label.competency= "competency";
   $scope.label.reviewerRating = "reviewer rating";
   $scope.label.competencyRating = "competency Ratings";
   $scope.label.priviousProject = "privious Project";
   $scope.label.doj= "Date of Joining";
   $scope.label.experience = "Years of experience";
   $scope.label.action = "action";
   $scope.label.allocate = "ALLOCATE";
   $scope.label.nominate = "NOMINATE";
   $scope.label.reject = "REJECT";

});
