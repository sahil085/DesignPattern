bbApp.controller('staffingNeedsCtrl', function ($scope, $http, $location, appCache, appService, ngDialog, $rootScope, $route, $filter, $modal, $timeout) {

    if (appCache.get("project") == undefined) {
        $location.path("/");
    }
    else {
        var project = appCache.get("project");
        $scope.projectId = project.projectId;
        $scope.projectName = project.projectName;
        $scope.projectStartDate = new Date(project.startDate);
        $scope.projectEndDate = new Date(project.endDate);
        $scope.projectStartDate.setHours(0, 0, 0, 0);
        $scope.projectEndDate.setHours(0, 0, 0, 0);
        $scope.gotoDL = project.fromDL;
    }

    $scope.todayDate = new Date();
    $scope.employeeStartDate = new Date();
    $scope.nominationPopup = [];
    $scope.row = [];
    $scope.addNew = [];
    $scope.erow = [];
    $scope.reallocateRow = [];
    $scope.reallocateERow = [];
    $scope.deallocatedStaffing = [];
    $scope.allocatedList = [];
    $scope.unAllocatedList = [];
    $scope.needTabFlag = 1;

    $scope.allocatedActive = "active";
    $scope.deAllocationActive = "";
    $scope.openNeedActive = "";
    $scope.currAllocationList = [];
    $scope.currAllocationPage = 1;
    $scope.currUnAllocList = [];
    $scope.currUnAllocPage = 1;
    $scope.itemPerPage = 10;
    $scope.maxSize = 5;
    $scope.popup = [];
    // $scope.isBillable=false;
    if ($rootScope.loggedUser != undefined) {

        if ($rootScope.loggedUser.currentRole == 'REGION HEAD') {
            $scope.isRegionHead = true;
        }
    }

    $scope.editReqActive = false;
    $scope.osrRow = [];
    $scope.currEditIndex = -1;

    $scope.addNewStaffReq = function (pos) {
        $scope.newStaffReq = {};
        $scope.newStaffReq.billableType = "BILLABLE";
        console.log("sfsf"+ $scope.newStaffReq.billableType)
        $scope.addNew[pos] = true;
    };

    $scope.editRequest = function (index) {
        $scope.editReqActive = true;

        var arr_index = (($scope.currUnAllocPage - 1) * $scope.itemPerPage) + index;

        $scope.addNew[index] = true;
        $scope.staff_bkp = $scope.unAllocatedList.splice(arr_index, 1)[0];
        $scope.newStaffReq = angular.copy($scope.staff_bkp);
        $scope.newStaffReq.startDate = new Date($scope.newStaffReq.startDate);
        $scope.newStaffReq.startDate.setHours(0, 0, 0, 0);
        $scope.newStaffReq.endDate = new Date($scope.newStaffReq.endDate);
        $scope.newStaffReq.endDate.setHours(0, 0, 0, 0);
        if ($scope.newStaffReq.billableType === "BILLABLE") {
            $scope.newStaffReq.billableType = "BILLABLE";
        } else if($scope.newStaffReq.billableType === "NON_BILLABLE"){
            $scope.newStaffReq.billableType = "NON_BILLABLE";
        } else if($scope.newStaffReq.billableType === "SHADOW"){
            $scope.newStaffReq.billableType = "SHADOW";
        }
        $scope.currEditIndex = arr_index;
    };

    $scope.addEditStaff = function (pos) {
        $scope.formSubmitted = true;
        if ($scope.openStaffReq.$valid) {
            if ($scope.editReqActive) {

                var arr_index = (($scope.currUnAllocPage - 1) * $scope.itemPerPage) + pos;

                $scope.addNew[pos] = true;
                var data = angular.copy($scope.newStaffReq);

                data.startDate = appService.convertDate(data.startDate);
                $scope.employeeStartDate = data.startDate;
                data.endDate = appService.convertDate(data.endDate);
                data.state = "Open";
                data['isAddNew'] = false;
                $http.put(host + "/project/" + $scope.projectId + '/staffing-request', data)
                    .success(function (response) {
                        if ($scope.newStaffReq.billableType === "BILLABLE") {
                            $scope.newStaffReq.billableType = "BILLABLE";
                        } else if($scope.newStaffReq.billableType === "NON_BILLABLE"){
                            $scope.newStaffReq.billableType = "NON_BILLABLE";
                        } else if($scope.newStaffReq.billableType === "SHADOW"){
                            $scope.newStaffReq.billableType = "SHADOW";
                        }
                        $scope.unAllocatedList.splice(arr_index, 0, $scope.newStaffReq);
                        $scope.currEditIndex = -1;
                        $scope.editReqActive = false;
                        $scope.addNew[pos] = false;
                    })
                    .error(function (response) {
                        $scope.row[index] = true;
                        ngDialog.open({
                            template: '<div class="error-dialog"><h4 class="title">Error</h4><p>'
                            + response[0].messages[0].message + '</p></div>', plain: true
                        });
                    })
                    .finally(function () {
                        $scope.osrRow[pos] = false;
                    });

            }
            else {
                var data = angular.copy($scope.newStaffReq);

                data.startDate = appService.convertDate(data.startDate);
                data.endDate = appService.convertDate(data.endDate);
                $scope.osrRow[0] = true;
                $scope.addNew[pos] = false;
                data['isAddNew'] = true;
                $http.put(host + "/project/" + $scope.projectId + '/staffing-request', data)
                    .success(function (response) {
                        $scope.newStaffReq = response;
                        $scope.newStaffReq.nominatedStaffings = [];
                        $scope.unAllocatedList.splice(0, 0, $scope.newStaffReq);
                        $scope.formSubmitted = false;
                        $scope.newStaffReq = {};
                    })
                    .error(function (response) {
                        $scope.addNew[pos] = true;
                        ngDialog.open({
                            template: '<div class="error-dialog"><h4 class="title">Error</h4><p>'
                            + response[0].messages[0].message + '</p></div>', plain: true
                        });
                    })
                    .finally(function () {
                        $scope.osrRow[0] = false;
                    });
            }
        }
    };

    $scope.cancelAddEditReq = function (pos) {
        $scope.addNew[pos] = false;
        $scope.formSubmitted = false;

        if ($scope.currEditIndex > -1) {
            $scope.unAllocatedList.splice($scope.currEditIndex, 0, $scope.staff_bkp);
            $scope.currEditIndex = -1;
            $scope.editReqActive = false;
        }
    };

    $scope.copyRow = function (index) {
        var arr_index = (($scope.currUnAllocPage - 1) * $scope.itemPerPage) + index;

        $scope.osrRow[index + 1] = true;
        var data = angular.copy($scope.unAllocatedList[arr_index]);
        data.id = '';
        data['isAddNew'] = true;

        $http.put(host + "/project/" + $scope.projectId + '/staffing-request', data)
            .success(function (response) {
                var staffObj = angular.copy($scope.unAllocatedList[arr_index]);
                staffObj.id = response.id;
                staffObj.state = response.state;
                $scope.unAllocatedList.splice(arr_index, 0, staffObj);
            })
            .error(function (response) {
                ngDialog.open({
                    template: '<div class="error-dialog"><h4 class="title">Error</h4><p>'
                    + response[0].messages[0].message + '</p></div>', plain: true
                });
            })
            .finally(function () {
                $scope.osrRow[index + 1] = false;
            });
    };

    $scope.deleteRequest = function (index) {
        var arr_index = (($scope.currUnAllocPage - 1) * $scope.itemPerPage) + index;

        var data = angular.copy($scope.unAllocatedList[arr_index]);
        var staffBak = $scope.unAllocatedList.splice(arr_index, 1)[0];

        $scope.osrRow[index] = true;

        $http.delete(host + "/project/" + $scope.projectId + '/staffing-request/' + data.id, data)
            .success(function (response) {
                ;
            })
            .error(function (response) {
                $scope.unAllocatedList.splice(arr_index, 0, staffBak);
                ngDialog.open({
                    template: '<div class="error-dialog"><h4 class="title">Error</h4><p>'
                    + response[0].messages[0].message + '</p></div>', plain: true
                });
            })
            .finally(function () {
                $scope.osrRow[index] = false;
            });
    };


    $scope.$watch('currAllocationPage + itemPerPage + allocatedList', function () {
        $scope.updateAllocationPage();
    });
    $scope.$watch('currUnAllocPage + itemPerPage + unAllocatedList', function () {
        $scope.updateUnAllocationPage();
    });
    $scope.$watch('staffingDetailsSearchText', function () {
        $scope.competencyChanged(true);
        var filtered = [];
        angular.forEach($scope.allocatedList, function (item) {
            if (item.allocatedStaffing.employeeName.toLowerCase().indexOf($scope.staffingDetailsSearchText.toLowerCase()) >= 0) {
                filtered.push(item);
            }
        });
        $scope.allocatedList = filtered;
        $scope.updateAllocationPage();

    });
    $scope.updateAllocationPage = function () {
        var begin = (($scope.currAllocationPage - 1) * $scope.itemPerPage)
            , end = begin + $scope.itemPerPage;

        if ($scope.allocatedList != undefined) {
            $scope.currAllocationList = $scope.allocatedList.slice(begin, end);
        }
    };
    $scope.updateUnAllocationPage = function () {
        var begin = (($scope.currUnAllocPage - 1) * $scope.itemPerPage)
            , end = begin + $scope.itemPerPage;

        if ($scope.unAllocatedList != undefined) {
            $scope.currUnAllocList = $scope.unAllocatedList.slice(begin, end);
        }
    };
    $scope.projectAjaxSuccess = function (response) {
        $scope.projectStaffinglist = response;

        angular.forEach($scope.projectStaffinglist, function (item) {
            if (item.state == 'Closed') {
                $scope.allocatedList.push(item);
            }
            else {
                $scope.unAllocatedList.push(item);
            }
        });
        $scope.origAllocatedList = $scope.allocatedList;
        $scope.origUnAllocatedList = $scope.unAllocatedList;
        $scope.resigned();
    };

    $scope.competencyAjaxSuccess = function (response) {
        $scope.competencies = response;
    };
    $scope.titleAjaxSuccess = function (response) {
        $scope.titleList = response;
    };
    $scope.deallocatedStaffingAjaxSuccess = function (response) {
        $scope.deallocatedStaffing = response;
    };

    function getDeallocatedEmp() {
        appService.getData(host + "/project/deallocatedEmployees/" + $scope.projectId, "GET", "", $scope.deallocatedStaffingAjaxSuccess, undefined, true, false, false);
    }

    if (appCache.get("project") != undefined) {
        getDeallocatedEmp();
        appService.getData(host + "/competencies", "GET", "", $scope.competencyAjaxSuccess, undefined, true, false, false);
        appService.getData(host + "/ttn/employee/title", "GET", "", $scope.titleAjaxSuccess, undefined, true, false, false);

        appService.getData(host + "/project/" + $scope.projectId + "/staffing-request", "GET", "", $scope.projectAjaxSuccess, undefined, false, true, true);
    }

    $scope.projectAssign = function (id, competency, nominationList) {
        $rootScope.staffId = id;
        $rootScope.competency = competency;
        $rootScope.projectId = $scope.projectId;
        $rootScope.nominationList = nominationList;
        ngDialog.openConfirm({
            template: 'app/components/staffAllocation/staffAllocation.html',
            className: 'ngdialog-theme-default ngdialog-search',
            preCloseCallback: function () {
                $route.reload();
                return true;
            }
        });
    };

    $scope.ajaxSuccess = function (response) {
        $route.reload();
    };
    $scope.closeThisDialog = function () {
        $route.reload();
        $scope.closePopUp($scope.closePopUpIndex);
    };
    $scope.closePopUp = function (closePopUp) {
        closePopUp = false
    };
    $scope.allocate = function (staffId, empCode, employee) {
        $scope.empEmailAddress = empCode;
        $scope.currStaffId = staffId;
        $scope.projectAssign = true;
        $scope.emp = employee;
        var templateUrl = 'app/components/staffAllocation/employeeAllocation.html';
        var controller = 'employeeAllocation';
        $scope.route = $route.reload();
        appService.showModal($modal, controller, templateUrl, {scope: $scope}, 'resignation-details');
        $scope.route = $route.reload();
    };
    $scope.allocateNominee = function (staffID, empCode, employee, closePopUp) {
        $scope.emp = [];
        $scope.emp['code'] = employee.employeeCode;
        $scope.emp['name'] = employee.employeeName;
        $scope.emp['emailAddress'] = employee.email;
        $scope.closePopUpIndex = closePopUp
        $scope.allocate(staffID, employee.email, $scope.emp)

    }
    $scope.rejectNomination = function (staffId, empCode) {
        appService.getData(host + '/project/' + $scope.projectId + '/staffing-request/' + staffId + '/reject/' + empCode,
            "PUT", "", $scope.ajaxSuccess, undefined, false, true, true);
    };

    $scope.deAllocate = function (empCode, staffId, emp, startDate) {
        $scope.emp = [];
        $scope.currStaffId = staffId;
        $scope.employeeStartDate = startDate;
        $scope.projectAssign = true;
        $scope.emp['code'] = emp.employeeCode;
        $scope.emp['name'] = emp.employeeName;
        $scope.emp['emailAddress'] = empCode;
        $scope.deallocate = true;
        $scope.route = $route;
        var templateUrl = 'app/components/staffAllocation/employeeAllocation.html';
        var controller = 'employeeAllocation';
        appService.showModal($modal, controller, templateUrl, {scope: $scope}, '');

    };

    $scope.initObjForEdit = function (index) {
        var arr_index = (($scope.currAllocationPage - 1) * $scope.itemPerPage) + index;
        $scope.staff = angular.copy($scope.allocatedList.slice(arr_index, arr_index + 1)[0]);
        $scope.staff.startDate = new Date($scope.staff.startDate);
        $scope.staff.startDate.setHours(0, 0, 0, 0);
        $scope.staff.endDate = new Date($scope.staff.endDate);
        $scope.staff.endDate.setHours(0, 0, 0, 0);
        $scope.erow[index] = true;
        $scope.empAllUpdateActive = true;
    };

    $scope.cancelEdit = function (index) {
        $scope.erow[index] = false;
        $scope.empAllUpdateActive = false;
    };
    $scope.doneEdit = function (index, staff) {
        $scope.submitted = true;
        if ($scope.staffReq.$valid) {
            $scope.erow[index] = false;
            $scope.row[index] = true;

            var arr_index = (($scope.currAllocationPage - 1 ) * $scope.itemPerPage) + index;

            var data = angular.copy($scope.staff);

            data.startDate = appService.convertDate(data.startDate);
            data.endDate = appService.convertDate(data.endDate);
            data.billable = staff.billable;
            data.billableType = staff.billableType;
            data['isAddNew'] = false;
            $http.put(host + "/project/" + $scope.projectId + '/staffing-request', data)
                .success(function (response) {
                    $route.reload();
                    $scope.submitted = false;
                    $scope.allocatedList.splice(arr_index, 1);
                    $scope.allocatedList.splice(arr_index, 0, $scope.staff);
                    $scope.empAllUpdateActive = false;
                    $scope.updateAllocationPage();
                })
                .error(function (response) {
                    $scope.row[index] = false;
                    $scope.erow[index] = true;
                    ngDialog.open({
                        template: '<div class="error-dialog"><h4 class="title">Error</h4><p>'
                        + response[0].messages[0].message + '</p></div>', plain: true
                    });
                })
                .finally(function () {
                    $scope.row[index] = false;
                });
        }
    };

    $scope.enableCalender = function (id, date) {
        $('#' + id).datetimepicker({
            format: 'YYYY-MM-DD'
        });

        $('#' + id).on("dp.change", function () {
            if (date == 'startdate') {
                $scope.staff.startDate = new Date($("#" + id + ">input").val() + " 00:00:00");
                $scope.staffReq.startdate.$setViewValue($("#" + id + ">input").val());
            }
            else {
                $scope.staff.endDate = new Date($("#" + id + ">input").val() + " 00:00:00");
                $scope.staffReq.enddate.$setViewValue($("#" + id + ">input").val());
            }
        });
    };

    $scope.enableCalenderReq = function (id, date) {

        $('#' + id).datetimepicker({
            format: 'YYYY-MM-DD'
        });

        $('#' + id).on("dp.change", function () {
            if (date == 'startdate') {
                $scope.newStaffReq.startDate = new Date($("#" + id + ">input").val() + " 00:00:00");
                $scope.openStaffReq.startdate.$setViewValue($("#" + id + ">input").val());
            }
            else {
                $scope.newStaffReq.endDate = new Date($("#" + id + ">input").val() + " 00:00:00");
                $scope.openStaffReq.enddate.$setViewValue($("#" + id + ">input").val());
            }
        });
    };

    $scope.enableCalenderDeallocatedEmployee = function (id, date) {
        $('#' + id).datetimepicker({
            format: 'YYYY-MM-DD',
            minDate: $scope.empDeallocatedEndDate,
            maxDate: $scope.projectEndDate,
        });

        $scope.empDeallocatedEndDate = $scope.todayDate;

        $('#' + id).on("dp.change", function () {
            $scope.empDeallocatedEndDate = new Date($("#" + id + ">input").val() + " 00:00:00");
        });
    };
    $scope.goBack = function () {
        if ($scope.gotoDL) {
            $location.path($rootScope.callBackURL);
        }
        else {
            $location.path("/listProjects");
        }
    };
    $scope.competencyFilter = function (input, key, value) {
        if (key == "" || typeof key == undefined || typeof value == undefined) {
            return input;
        }
        output = [];
        angular.forEach(input, function (data) {
            if (data['competency'] != undefined && value.indexOf(data['competency']) != -1) {
                output.push(data);
            }
        });
        return output;
    };
    $scope.competencyChanged = function (allocationFlag) {
        if (allocationFlag) {
            if ($scope.competencyAlloction == undefined || $scope.competencyAlloction == "") {
                $scope.allocatedList = $scope.origAllocatedList;
            }

            else {

                $scope.allocatedList = $scope.competencyFilter($scope.origAllocatedList, "competency.name", [$scope.competencyAlloction]);
            }
            $scope.updateAllocationPage();
        } else {

            if ($scope.competencyDeAlloction == undefined || $scope.competencyDeAlloction == "") {
                $scope.unAllocatedList = $scope.origUnAllocatedList;
            }

            else {

                $scope.unAllocatedList = $scope.competencyFilter($scope.origUnAllocatedList, "competency.name", [$scope.competencyDeAlloction]);
            }
            $scope.updateUnAllocationPage();
        }


    };
    $scope.sortedList = function (property, allocationFlag) {
        if (allocationFlag)
            $scope.origAllocatedList = appService.sortedList(property, $scope, $filter, $scope.origAllocatedList);
        else
            $scope.origUnAllocatedList = appService.sortedList(property, $scope, $filter, $scope.origUnAllocatedList);
        $scope.competencyChanged(allocationFlag)
    };

    $scope.sortedDeallocatedList = function (property) {
        $scope.deallocatedStaffing = appService.sortedList(property, $scope, $filter, $scope.deallocatedStaffing);
    };
    $scope.competencyAjaxSuccess = function (data) {
        $scope.competencyList = data;
    };
    $scope.resigned = function (EmployeeCode) {
        $http.get(host + "/ttn/employee/resignations")
            .success(function (response) {
                angular.forEach(response, function (value, key) {
                    for (var i = 0; i < $scope.origAllocatedList.length; i++) {
                        if (value.employeeCode === $scope.origAllocatedList[i].allocatedStaffing.employeeCode) {
                            $scope.origAllocatedList[i].resign = true;
                            $scope.origAllocatedList[i].initiationDate = value.initiationDate;
                            $scope.origAllocatedList[i].lastWorkingDate = value.lastWorkingDate;
                            $scope.origAllocatedList[i].noticePeriod = value.noticePeriod;
                            break;
                        }

                    }

                });
                $scope.competencyChanged(true);

            });


    };
    $scope.resigntionDetails = function (employee) {
        $scope.emp = employee;
        var templateUrl = 'app/components/staffAllocation/resignationDetails.html';
        var controller = 'resignationDetailCtrl';
        appService.showModal($modal, controller, templateUrl, {scope: $scope});
    };
    $scope.changeMentovisor = function (employee) {
        $scope.emp = [];
        $scope.emp['name'] = employee.employeeName;
        $scope.emp['code'] = employee.employeeCode;

        var templateUrl = 'app/components/staffAllocation/changeMentovisor.html';
        var controller = 'changeMentovisorCtrl';
        appService.showModal($modal, controller, templateUrl, {scope: $scope, route: $route});


    };

    $scope.showAllocationOrDeallocationProjectStaffing = function (needTabFlag) {
        $scope.needTabFlag = needTabFlag;
        if ($scope.needTabFlag === 1) {
            $scope.allocatedActive = "active";
            $scope.deAllocationActive = "";
            $scope.openNeedActive = "";
        } else if ($scope.needTabFlag === 2) {
            $scope.openNeedActive = "active";
            $scope.deAllocationActive = "";
            $scope.allocatedActive = "";
        }
        else if ($scope.needTabFlag === 3) {
            $scope.openNeedActive = "";
            $scope.deAllocationActive = "active";
            $scope.allocatedActive = "";
        }
    };
    $scope.reallocateAjaxSuccess = function (response) {
        $route.reload();
        $scope.disable = false;
        $scope.reallocateRow[$scope.index] = false;
        $scope.loading = true;
        $scope.deallocatedStaffing.splice($scope.index, 1);
        getDeallocatedEmp();
    };
    $scope.reallocateStaffing = function (empDeallocated, index) {
        $scope.disable = true;
        $scope.empDeallocatedStartDate = new Date(empDeallocated.startDate);
        $scope.empDeallocatedEndDate = new Date(empDeallocated.endDate);
        $scope.reallocateRow[index] = true;

    };
    $scope.reallocate = function (empDeallocated, index) {
        $scope.index = index;
        empDeallocated.endDate = appService.convertDate($scope.empDeallocatedEndDate);
        appService.getData(host + "/project/reallocate", "PUT", empDeallocated, $scope.reallocateAjaxSuccess, undefined, false, false, true);

    };
    $scope.cancelReallocation = function (index) {
        $scope.reallocateRow[index] = false;
        $scope.disable = false;
        $scope.loading = true;
    };
    if (appCache.get('message') != undefined) {
        $scope.showAlertMessage = true;
        $scope.message = appCache.get('message');
        appCache.remove('message');
        fadeOut();
    }

    function fadeOut() {
        $timeout(function () {
            $scope.showAlertMessage = false;
        }, 3000);
    }

    if (appCache.get("project") != undefined) {
        appService.getData(host + "/competencies", "GET", "", $scope.competencyAjaxSuccess, undefined, true, false, false);
    }

    $scope.label = {};
    $scope.label.title = "title";
    $scope.label.competency = "competency";
    $scope.label.projectId = "projectId";
    $scope.label.startdate = "start date";
    $scope.label.endDate = "end Date";
    $scope.label.billingType = "billing Type";
    $scope.label.details = "details";
    $scope.label.status = "status";
    $scope.label.action = "action";
    $scope.label.allocation = "%";
    $scope.label.page_title = "Staffing Details: ";
    $scope.label.open_needs = "Open Needs";
    $scope.label.employee_name = "Employee Name";
    $scope.label.region = "Region";
    $scope.label.add = "Add";
    $scope.label.edit = "Edit";
    $scope.label.cancel = "Cancel";
    $scope.label.selectTitle = "-Select Title-";
    $scope.label.billable = "Billable";
    $scope.label.deallocate = "DeAllocate";
    $scope.label.mentovisor = "Mentovisor";
    $scope.label.nonBillable = "Non-Billable";
    $scope.label.shadow = "Shadow";
    $scope.label.allocationTab = "Current Team";
    $scope.label.deAllocationTab = "Recent Deallocations";
    $scope.label.openNeedTab = "Open Needs";
    $scope.label.employee_code = "Code";
    $scope.label.selectCompetency = "-Select Competency-";
    $scope.errorMessage = {};
    $scope.errorMessage.allocation_invalid = "Invalid Number";
    $scope.errorMessage.allocation_invalid_minmax = "Allocation should be between 0 and 100";
    $scope.errorMessage.date_invalid = "Invalid Date Format";
    $scope.errorMessage.startdate_invalid_min = "Start Date should not be less than Project Start Date";
    $scope.errorMessage.startdate_invalid_max = "Start Date should not be greater than Project End Date";
    $scope.errorMessage.enddate_invalid_min = "End Date should not be less than Start Date";
    $scope.errorMessage.enddate_invalid_max = "End Date should not be greater than Project End Date";

});