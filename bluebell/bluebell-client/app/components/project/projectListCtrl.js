bbApp.controller('projectListCtrl', function ($scope, $location, $http, appCache, $timeout, $q, ngDialog, appService, arrayByKeyValuesFilter, $rootScope, filterFilter, $route, $filter) {

    $scope.$emit('toggleSpinner', true);
    $scope.lrow = [];
    $scope.projectListOriginal = [];
    $scope.projectList = [];
    $scope.currProjectList = [];
    $scope.currProjectPage = 1;
    $scope.itemPerPage = 10;
    $scope.maxSize = 5;
    $scope.searchText = "";

    $scope.$watch('$root.loggedUser', function () {
        if (typeof $rootScope.loggedUser != 'undefined') {

            if ($rootScope.loggedUser.currentRole == 'STAFFING MANAGER') {
                $scope.isStaffingManager = true;
            }
            else if ($rootScope.loggedUser.currentRole == 'REGION HEAD') {
                $scope.region = $rootScope.loggedUser.regions[0];
            }
        }

    });

    $scope.$watch('searchText', function () {
        if ($scope.projectList != undefined) {
            if ($scope.projectListBackup == undefined || $scope.projectListBackup.length == 0) {
                $scope.projectListBackup = angular.copy($scope.projectList);
            }
            var result = $scope.projectListBackup.map(function (a) {
                return a.projectName;
            });
            var arr = [];
            $scope.projectListBackup.map(function (a) {
                arr.push({
                    'openStaffRequests': a.openStaffRequests,
                    'name': a.projectName,
                    'clientName': a.client.clientName,
                    'region': a.region,
                    'businessUnit': a.businessUnit
                });
            });
            var list = filterFilter(arr, $scope.searchText);
            var temp = [];
            $scope.projectListBackup.map(function (project) {
                list.map(function (a) {
                    if (a.name === project.projectName) {
                        if(!(temp.indexOf(a.name) > -1))
                        temp.push(project)
                    }
                })
            });
            $scope.projectList = temp;
        }
    });

    $scope.regionAjaxSuccess = function (data) {
        $scope.regionList = data;
    };
    appService.getData(host + "/allregions", "GET", "", $scope.regionAjaxSuccess, undefined, true, false, false);

    $scope.regionChanged = function () {
        if ($scope.region == "" || $scope.region == null)
            $scope.projectList = $scope.projectListOriginal;
        else
            $scope.projectList = arrayByKeyValuesFilter($scope.projectListOriginal, "regionvalue.regionName", [$scope.region]);
    };

    $scope.$watch('projectListOriginal', function () {
        $scope.regionChanged();
    }, true);

    $scope.$watchGroup(['currProjectPage', 'itemPerPage', 'projectList'], function () {
        var begin = (($scope.currProjectPage - 1) * $scope.itemPerPage)
            , end = begin + $scope.itemPerPage;

        $scope.currProjectList = $scope.projectList.slice(begin, end);
    }, true);


    if (appCache.get('message') != undefined) {
        $scope.showAlertMessage = true;
        $scope.message = appCache.get('message');
        appCache.remove('message');
        fadeOut();
    }

    $scope.projectAjax = $http.get(host + "/project")
        .success(function (response) {
            $scope.projectListOriginal = response;
        })
        .error(function (response) {
            $scope.$emit('toggleSpinner', false);
            ngDialog.open({
                template: '<div class="error-dialog"><h4 class="title">Error</h4><p>'
                + response[0].messages[0].message + '</p></div>', plain: true
            });
        })
        .finally(function () {
            $scope.$emit('toggleSpinner', false);
        });


    $scope.editProject = function (projectId) {
        appCache.put('projectId', projectId);
        $location.path("/project");
    };

    $scope.allocateStaff = function (project) {
        appCache.put('project', project);
        $location.path("/staffing-needs");
    };

    $scope.deleteProject = function (pid, index) {

        ngDialog.openConfirm({
            template: '<div class="confirm-dialog"><h4 class="title">Confirm</h4>'
            + '<p>Are you sure you want to delete the project?</p>'
            + '<div class="btns">'
            + '<button class="btn btn-primary bb-button bb-button-small" data-ng-click="confirm();">Yes</button>'
            + '<button class="btn btn-primary bb-button bb-button-small" data-ng-click="closeThisDialog();">No</button>'
            + '</div></div>', plain: true
        }).then(function (success) {
            var projectObj = $scope.projectListOriginal.splice(index, 1)[0];
            $scope.lrow[index] = true;
            $scope.projectLoader = true;

            $http.delete(host + '/project/' + pid)
                .success(function (response) {
                    $route.reload();
                })
                .error(function (response) {
                    $scope.projectListOriginal.splice(index, 1, projectObj);
                    ngDialog.open({
                        template: '<div class="error-dialog"><h4 class="title">Error</h4><p>'
                        + response[0].messages[0].message + '</p></div>', plain: true
                    });
                })
                .finally(function () {
                    $scope.lrow[index] = false;
                    $scope.projectLoader = false;
                });

        });
    };
    $scope.sortedList = function (property) {
        $scope.projectList = appService.sortedList(property, $scope, $filter, $scope.projectList)
    };

    function fadeOut() {
        $timeout(function () {
            $scope.showAlertMessage = false;
        }, 3000);
    }


    $scope.label = {};
    $scope.label.projectName = "PROJECT NAME";
    $scope.label.bu = "BUSINESS UNIT";
    $scope.label.region = "REGION";
    $scope.label.billable = "BILLABLE";
    $scope.label.startdate = "START DATE";
    $scope.label.enddate = "END DATE";
    $scope.label.openNeeds = "OPEN NEEDS";
    $scope.label.clientName = "CLIENT NAME";
    $scope.label.no_project = "No Projects Found";
    $scope.label.edit = "Edit";
    $scope.label.delete = "Delete";
    $scope.label.staffRequest = "STAFFING DETAILS";
});