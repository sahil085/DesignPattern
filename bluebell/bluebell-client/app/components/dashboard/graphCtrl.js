bbApp.controller('graphCtrl', function ($scope, $http, $location, appCache, ngDialog, appService, filterFilter, $rootScope) {

    $scope.blank = [];
    $scope.data = [];
    $scope.invoiceCycle = "Weekly";
    $scope.selectFrequency = "-Select Frequency-";
    $scope.selectStatus = "-Select Status-";
    $scope.selectType = "-Select Type-";
    $scope.graphType = 'Allocations';
    $scope.graphTypes = [];
    $scope.graphTypes.push($scope.graphType);
    $scope.graphTypes.push('Open Requests');
    $scope.projectType = 'Billable';
    $scope.projectTypes = [];
    $scope.projectTypes.push($scope.projectType);
    $scope.projectTypes.push('Non-Billable');
    $scope.projectTypes.push('Shadow');
    $scope.regions = [];
    $scope.comptencyList = [];
    $scope.regionList = [];
    $scope.showGraphORTableLabel = "Show Graph";
    $scope.showGraphORTableFlag = false;
    $scope.allocationsTableLoaded = false;

    $scope.$watch('$root.loggedUser', function () {
        if (typeof $rootScope.loggedUser != 'undefined') {

            if ($rootScope.loggedUser.currentRole == 'REGION HEAD') {
                $scope.isRegionHead = true;
                $scope.regions = $rootScope.loggedUser.regions;
            }
            else if ($rootScope.loggedUser.currentRole == 'STAFFING MANAGER') {
                $scope.isStaffingManager = true;
            }
        }
    });

    $scope.config = {
        visible: true, // default: true
        extended: false, // default: false
        disabled: false, // default: false
        refreshDataOnly: true, // default: true
        deepWatchOptions: true, // default: true
        deepWatchData: true, // default: true
        deepWatchDataDepth: 20, // default: 2
        debounce: 100 // default: 10
    };

    $scope.options = {
        chart: {
            type: 'discreteBarChart',
            height: 330,
            margin: {
                top: 20,
                right: 20,
                bottom: 50,
                left: 55
            },
            x: function (d) {
                return d.label;
            },
            y: function (d) {
                return d.value;
            },
            showValues: true,
            valueFormat: function (d) {
                return d3.format(',f')(d);
            },
            duration: 3000,
            xAxis: {
                axisLabel: 'Competencies'
            },
            yAxis: {
                axisLabel: 'Number of Employees',
                axisLabelDistance: -10
            }
        }
    };

    $scope.upcomingDeallocationsOptions = {
        chart: {
            type: 'discreteBarChart',
            height: 300,
            margin: {
                top: 20,
                right: 20,
                bottom: 50,
                left: 55
            },
            x: function (d) {
                return d.label;
            },
            y: function (d) {
                return d.value;
            },
            showValues: true,
            valueFormat: function (d) {
                return d3.format(',f')(d);
            },
            duration: 3000,
            xAxis: {
                axisLabel: 'Competencies'
            },
            yAxis: {
                axisLabel: 'No. of De-allocation',
                axisLabelDistance: -10
            }
        }
    };

    $scope.showGraphORTable = function () {
        $scope.showGraphORTableLabel = $scope.showGraphORTableFlag ? "Show Graph" : "Show Table";
        $scope.showGraphORTableFlag = !$scope.showGraphORTableFlag;


    };

    $scope.competencyAjaxSuccess = function (response) {
        $scope.competencies = response;
    };

    $scope.benchDataSuccess = function (response) {
        $scope.benchEmpList = response;
    };

    $scope.invoiceCycleAjaxSuccess = function (response) {
        $scope.invoiceCycles = response;
    };

    $scope.weeklyInvoiceSuccess = function (response) {
        $scope.weeklyInvoices = response;
    };

    $scope.upcomingDeallocationsSuccess = function (response) {
        $scope.upcomingDeallocationsData = [];
        $scope.upcomingDeallocations = response;
        var values = [];
        $scope.count = 0;
        Object.keys($scope.upcomingDeallocations).forEach(function (key) {
            var obj = {};
            obj.label = key;
            obj.value = $scope.upcomingDeallocations[key];
            $scope.count += obj.value;
            values.push(obj);
        });
        $scope.upcomingDeallocationsData.push({'values': values});
    };

    $scope.billableSuccess = function (response) {
        $scope.billableData = [];
        $scope.billableData.total = [];
        $scope.billableData.total.total = 0;
        Object.keys(response).forEach(function (key) {
            var values = [];
            $scope.billableData.total[key] = 0;
            Object.keys(response
                [key]).forEach(function (subKey) {
                if (!$scope.billableData.hasOwnProperty(subKey)) {
                    $scope.billableData[subKey] = {};
                }
                if (!$scope.billableData.total.hasOwnProperty(subKey)) {
                    $scope.billableData.total[subKey] = 0;
                }
                $scope.billableData[subKey][key] = response[key][subKey]['count'];
                $scope.billableData.total[key] = $scope.billableData.total[key] + $scope.billableData[subKey][key];
                $scope.billableData.total[subKey] = $scope.billableData.total[subKey] + $scope.billableData[subKey][key];
                if ($scope.regionList.indexOf(subKey) === -1) {
                    $scope.regionList.push(subKey);
                }

            });
            $scope.billableData.total.total = $scope.billableData.total.total + $scope.billableData.total[key];
            if (($scope.comptencyList.indexOf(key) == -1) && (key != "")) {
                $scope.comptencyList.push(key);
            }
        });
        $scope.tempBillableDatacount = Object.keys($scope.billableData).length;
        $scope.allocationsTableLoaded = true;
    };

    $scope.$watch('$root.loggedUser', function () {
        if (typeof $rootScope.loggedUser != 'undefined') {
            $scope.loadUpcomingDeallocationsGraph();
            $scope.upcomingInvoices();
            $scope.regionCompetencyBillable();
        }
    });

    function getRandomColor() {
        var letters = '0123456789ABCDEF';
        var color = '#';
        for (var i = 0; i < 6; i++) {
            color += letters[Math.floor(Math.random() * 16)];
        }
        return color;
    }

    $scope.$watchGroup(['competencies', 'benchEmpList'], function () {
        $scope.compFilter = {};
        $scope.compFilter.competency = {};
        $scope.pieData = [];
        var valuesObj = {};
        valuesObj.values = [];
        angular.forEach($scope.competencies, function (comp) {
            $scope.compFilter.competency.name = comp;
            $scope.compBenchList = filterFilter($scope.benchEmpList, $scope.compFilter);
            if ($scope.compBenchList != undefined && $scope.compBenchList.length > 0) {
                var obj = {};
                obj.label = comp;
                obj.value = $scope.compBenchList.length;
                valuesObj.values.push(obj);
            }
        });
        $scope.pieData.push(valuesObj);
    });

    $scope.loadUpcomingDeallocationsGraph = function (invoiceCycle) {
        $scope.invoiceCycle = invoiceCycle != undefined ? invoiceCycle : $scope.invoiceCycle;
        appService.getData(host + "/reports/upcomingDe-allocation", "POST", {
            days: invoiceCycle != undefined ? invoiceCycle : $scope.invoiceCycle,
            regions: $scope.regions
        }, $scope.upcomingDeallocationsSuccess, undefined, false, false, true);
    };

    $scope.upcomingInvoices = function () {
        appService.getData(host + "/invoice/upcoming/billing-info", "POST", {
            billingCycle: 'Weekly',
            regions: $scope.regions
        }, $scope.weeklyInvoiceSuccess, undefined, false, false, true);
    };

    $scope.regionCompetencyBillable = function () {
        appService.getData(host + "/reports/region-competency-billable", "POST", {
            projectType: $scope.projectType,
            graphType: $scope.graphType,
            filterList: $scope.additionalFilter,
            regions: $scope.regions
        }, $scope.billableSuccess, false, false, false);
    };

    $scope.loadBillable = function () {
        $scope.regionCompetencyBillable();
    };

    $scope.populateProjectList = function () {
        if ($scope.additionalFilterLabel == 'Project') {
            $scope.loadAdditionalFilterList();
        }
        $scope.regionCompetencyBillable();
    };

    $scope.applyMultiselect = function () {
        $('#graphAdditionalFilter').multiselect({
            disableIfEmpty: true,
            buttonWidth: '150px',
            enableFiltering: true,
            nonSelectedText: 'All ' + $scope.additionalFilterLabel + 's',
            onChange: function (option, checked, select) {
                if (checked) {
                    $scope.additionalFilter.push($(option).val());
                } else {
                    var index = $scope.additionalFilter.indexOf($(option).val());
                    $scope.additionalFilter.splice(index, 1);
                }
                $scope.loadBillable()
            }
        });
    };

    $scope.filterListAjaxSuccess = function (data) {
        $scope.additionalFilter = [''];
        $scope.additionalFilterList = data;
        angular.element(document).ready(function () {
            $scope.applyMultiselect();
        });
    };

    $scope.loadAdditionalFilterList = function () {
        if ($rootScope.loggedUser.currentRole == 'REGION HEAD') {
            $scope.additionalFilterLabel = 'Project';
            appService.getData(host + "/project-list", "POST", {
                projectType: $scope.projectType,
                regions: $scope.regions
            }, $scope.filterListAjaxSuccess, false, false, false);
        } else {
            appService.getData(host + "/regions", "GET", "", $scope.filterListAjaxSuccess, undefined, true, false, false);
            $scope.additionalFilterLabel = 'Region';
        }
    };

    appService.getData(host + "/competencies", "GET", "", $scope.competencyAjaxSuccess, undefined, true, false, false);
    appService.getData(host + "/ttn/employee/bench", "GET", "", $scope.benchDataSuccess, undefined, false, false, true);
    appService.getData(host + "/invoice-cycle", "GET", "", $scope.invoiceCycleAjaxSuccess, undefined, true, false, false);

    $scope.loadAdditionalFilterList();

});