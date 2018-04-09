bbApp.controller('employeeSearchCtrl', function ($scope, $http, $location, appCache, appService, $rootScope, ngDialog, filterFilter, $window, $document, $route, $modal) {

    $('[data-toggle="tooltip"]').tooltip();

    $('#filterWrap').height($(document).height() - 90);

    $scope.showTitleFilter = false;
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
    $scope.revIndex = [];
    var ind = 0;

    $scope.empBackUpForSearchFilter = [];
    $scope.refreshBackup = true;
    $scope.searchText = "";

    $document.on('scroll', function () {
        $("#filterWrap").css({"top": $window.scrollY + "px"});
        if ($window.scrollY + $window.innerHeight + 800 > $(document).height()) {
            $scope.$apply($scope.loadMore());
        }
    });

    $scope.allocationAjaxSuccess = function (response) {
        $scope.allocationData = response;
        ngDialog.openConfirm({
            template: 'app/components/staffAllocation/allocationHistory.html',
            className: 'ngdialog-theme-default ngdialog-search',
            scope: $scope
        });

    };

    $scope.viewAllocations = function (email, empName) {
        $scope.empName = empName;
        appService.getData(host + "/ttn/employee/allocation", "POST", {emailId: email}, $scope.allocationAjaxSuccess, undefined, false, true, true);
    };

    $scope.loadMore = function () {
        if ($scope.empList && $scope.empList.length) {   // undefined check for empList
            ind = $scope.empList.length;
            //ind = ind + 10;
            var r = 10;
            if (ind + 10 >= $scope.employeeListBuffer.length) {
                r = $scope.employeeListBuffer.length - ind;
            }
            $scope.empList = $scope.empList.concat($scope.employeeListBuffer.slice(ind, r + ind));
        }
    };

    $scope.isTitleChecked = function (title) {
        var items = $scope.empTitle;
        for (var i = 0; i < items.length; i++) {
            if (title == items[i])
                return true;
        }
        return false;
    };

    $scope.$watch('searchText', function () {
        searchFilter();
    });
    function searchFilter() {
        if ($scope.empList != undefined) {
            if ($scope.refreshBackup) {
                $scope.empBackUpForSearchFilter = angular.copy($scope.employeeListBuffer);
                $scope.refreshBackup = false;
            }

            $scope.employeeListBuffer = filterFilter($scope.empBackUpForSearchFilter, {'name': $scope.searchText});
            $scope.empList = $scope.employeeListBuffer.slice(0, 10);
        }
    }

    $scope.$watch('$root.loggedUser', function () {
        if (typeof $rootScope.loggedUser != 'undefined') {
            $scope.role = $rootScope.loggedUser.currentRole;
        }
    });
    $scope.billableRangeSlider = {
        minValue: 0,
        maxValue: 100,
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
        if ($scope.empList != undefined) {
            $scope.applyFilter();
        }
    }, true);

    $scope.nonBillableRangeSlider = {
        minValue: 0,
        maxValue: 100,
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
        if ($scope.empList != undefined) {
            $scope.applyFilter();
        }
    }, true);

    $scope.shadowRangeSlider = {
        minValue: 0,
        maxValue: 100,
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
        if ($scope.empList != undefined) {
            $scope.applyFilter();
        }
    }, true);


    $scope.navigate = function (projectName) {
        appService.getData(host + "/project/checkName/" + projectName, "GET", "", $scope.projectAjaxSuccess, undefined, false, true, true);
    };
    $scope.projectAjaxSuccess = function (response) {
        $rootScope.callBackURL = '/employeeSearch';
        var project = response;
        project.startDate = new Date(project.startDate);
        project.endDate = new Date(project.endDate);
        project.fromDL = true;
        appCache.put("project", project);
        $location.path("/staffing-needs");
    };
    $scope.empListAjaxSuccess = function (data) {
        $scope.employeeListBuffer = data;
        $scope.empListBkp = angular.copy($scope.employeeListBuffer);
        $scope.resigned();
        $scope.empList = $scope.employeeListBuffer.slice(0, 10);
        $scope.refreshBackup = true;
        $scope.searchText = "";

        $scope.applyFilter();
    };

    $scope.competencyAjaxSuccess = function (data) {
        $scope.competencyList = data;
    };
    $scope.titleAjaxSuccess = function (data) {
        $scope.titleList = data;
    };
    $scope.sysParamExpAjaxSuccess = function (response) {
        $scope.experienceList = response;
    };

    appService.getData(host + "/competencies", "GET", "", $scope.competencyAjaxSuccess, undefined, true, false, false);
    appService.getData(host + "/ttn/employee/title", "GET", "", $scope.titleAjaxSuccess, undefined, true, false, false);
    appService.getData(host + "/ttn/employee", "GET", "", $scope.empListAjaxSuccess, undefined, false, true, true);
    appService.getData(host + "/system-parameters/experience", "GET", "", $scope.sysParamExpAjaxSuccess, undefined, true, false, true);

    $scope.getEmployeeList = function () {
        if ($scope.currCompetency == undefined || $scope.currCompetency == "") {
            appService.getData(host + "/ttn/employee?", "GET", "", $scope.empListAjaxSuccess, undefined, false, true, true);
        }
        else {
            appService.getData(host + "/ttn/employee?competency=" + $scope.currCompetency, "GET", "", $scope.empListAjaxSuccess, undefined, false, true, true);
        }
    };

    $scope.applyFilter = function () {
        $scope.employeeListBuffer = angular.copy($scope.empListBkp);
        if ($scope.empTitle != undefined && $scope.empTitle.length != 0) {

            var bkup = angular.copy($scope.employeeListBuffer);
            $scope.employeeListBuffer = [];
            angular.forEach(bkup, function (item) {
                angular.forEach($scope.empTitle, function (title) {
                    title = title.trim();
                    if (item.title.toUpperCase() == title.toUpperCase()) {
                        $scope.employeeListBuffer.push(item);
                    }
                });
            });
        }

        if ($scope.experience != undefined && $scope.experience.length != 0) {

            var filteredList = $scope.employeeListBuffer;
            $scope.employeeListBuffer = [];

            angular.forEach(filteredList, function (item) {
                angular.forEach($scope.experience, function (exp) {
                    var thisMin = parseInt(exp.split("-")[0]);
                    var thisMax = parseInt(exp.split("-")[1]);

                    if (item.noOfYearsExp >= thisMin && item.noOfYearsExp <= thisMax) {
                        $scope.employeeListBuffer.push(item);
                    }
                });
            });
        }
        if (!$scope.resignFilter || $scope.resignFilter == undefined) {
            var filteredList = $scope.employeeListBuffer;
            $scope.employeeListBuffer = [];

            angular.forEach(filteredList, function (item) {
                if (!item.resign)
                    $scope.employeeListBuffer.push(item);

            });


        }
        if ($scope.crating != undefined && $scope.crating.length != 0) {

            var filteredList = $scope.employeeListBuffer;
            $scope.employeeListBuffer = [];

            angular.forEach(filteredList, function (item) {
                angular.forEach($scope.crating, function (thisRating) {
                    if (item.competency.rating == thisRating) {
                        $scope.employeeListBuffer.push(item);
                    }
                });
            });
        }

        if ($scope.billableRangeSlider.minValue > 0 || $scope.billableRangeSlider.maxValue < 100 ||
            $scope.nonBillableRangeSlider.minValue > 0 || $scope.nonBillableRangeSlider.maxValue < 100 ||
            $scope.shadowRangeSlider.minValue > 0 || $scope.shadowRangeSlider.maxValue < 100) {

            var filteredList = $scope.employeeListBuffer;
            $scope.employeeListBuffer = [];

            var billableRangeSliderMax = $scope.billableRangeSlider.maxValue;

            if ($scope.billableRangeSlider.maxValue == 100) {
                billableRangeSliderMax = 1000;
            }

            angular.forEach(filteredList, function (item) {
                if ((item.additionalInfo.totalBillableAllocation >= $scope.billableRangeSlider.minValue &&
                    item.additionalInfo.totalBillableAllocation <= billableRangeSliderMax) &&
                    (item.additionalInfo.totalNonBillableAllocation >= $scope.nonBillableRangeSlider.minValue &&
                    item.additionalInfo.totalNonBillableAllocation <= $scope.nonBillableRangeSlider.maxValue) &&
                    (item.additionalInfo.totalShadowAllocation >= $scope.shadowRangeSlider.minValue &&
                    item.additionalInfo.totalShadowAllocation <= $scope.shadowRangeSlider.maxValue)) {
                    $scope.employeeListBuffer.push(item);
                }
            });
        }

        $scope.empList = $scope.employeeListBuffer.slice(0, 10);
        $scope.searchText = "";
        $scope.refreshBackup = true;
    };
    $scope.resigned = function () {
        $http.get(host + "/ttn/employee/resignations")
            .success(function (response) {
                angular.forEach(response, function (value, key) {
                    for (var i = 0; i < $scope.empListBkp.length; i++) {
                        if (value.employeeCode == $scope.empListBkp[i].code) {
                            $scope.empListBkp[i].resign = true;
                            $scope.empListBkp[i].initiationDate = value.initiationDate;
                            $scope.empListBkp[i].lastWorkingDate = value.lastWorkingDate;
                            $scope.empListBkp[i].noticePeriod = value.noticePeriod;
                            break;
                        }

                    }
                    ;

                });
                $scope.employeeListBuffer = $scope.empListBkp;
                var searttxt = $scope.searchText;
                $scope.applyFilter();
                $scope.searchText = searttxt;

                if ($scope.searchText != "")
                    searchFilter();
            });
    };

    $scope.cancelResignation = function (empCode) {
        $http.delete(host + "/ttn/employee/resignations?empCode=" + empCode)
            .success(function (response) {
                for (var i = 0; i < $scope.empListBkp.length; i++) {
                    $scope.empListBkp[i].resign = false;
                }
                $scope.employeeListBuffer = $scope.empListBkp;
                $scope.resigned()


            })
    };
    $scope.applyResignation = function (empCode) {
        var templateUrl = 'app/components/staffAllocation/resignation.html';
        var controller = 'resignationCtrl';
        appService.showModal($modal, controller, templateUrl, {empCode: empCode, scope: $scope});
    };
    $scope.resigntionDetails = function (employee) {
        $scope.emp = employee;
        var templateUrl = 'app/components/staffAllocation/resignationDetails.html';
        var controller = 'resignationDetailCtrl';
        appService.showModal($modal, controller, templateUrl, {scope: $scope});
    };
    $scope.changeMentovisor = function (employee) {
        $scope.emp = employee;
        var templateUrl = 'app/components/staffAllocation/changeMentovisor.html';
        var controller = 'changeMentovisorCtrl';
        appService.showModal($modal, controller, templateUrl, {scope: $scope});

    };
    $scope.showLeaveSummary = function (employee) {
        var templateUrl = 'app/components/staffAllocation/showLeaveSummary.html';
        var controller = 'showLeaveSummaryCtrl';
        if (employee.leavesCount) {
            appService.showModal($modal, controller, templateUrl, {emailAddress: employee.emailAddress, scope: $scope}, '', 'lg');
        }
    };

    $scope.label = {};
    $scope.label.title = "employee title";
    $scope.label.empName = "Employee name";
    $scope.label.competency = "competency";
    $scope.label.reviewerRating = "reviewer rating";
    $scope.label.competencyRating = "competency Ratings";
    $scope.label.priviousProject = "privious Project";
    $scope.label.doj = "Date of Joining";
    $scope.label.experience = "Years of experience";
    $scope.label.action = "action";
    $scope.label.allocate = "ALLOCATE";
    $scope.label.nominate = "NOMINATE";
    $scope.label.reject = "REJECT";

    $scope.projectAssign = function (emailAddress, name, competency, employee) {
        $scope.empName = name;
        $scope.empEmailAddress = emailAddress;
        $scope.competency = competency.name;
        $scope.projectAssign = true;
        $scope.emp = employee;
        ngDialog.openConfirm({
            template: 'app/components/staffingNeeds/openNeeds.html',
            className: 'ngdialog-theme-default ngdialog-search',
            scope: $scope,
            preCloseCallback: function () {
                $route.reload();
                return true;
            }
        });
    };

});
