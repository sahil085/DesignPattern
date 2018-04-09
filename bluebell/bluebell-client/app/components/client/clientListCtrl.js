bbApp.controller('clientListCtrl', function($scope, $location, $http, appCache, $timeout, $q, ngDialog, appService, arrayByKeyValuesFilter, $rootScope, filterFilter, $route,$filter) {

    $scope.$emit('toggleSpinner', true);
    $scope.lrow_c = [];
    $scope.clientList = [];
    $scope.currClientList = [];
    $scope.currClientPage = 1;
    $scope.itemPerPage = 10;
    $scope.maxSize = 5;
    $scope.searchText = "";

    $scope.$watch('searchText', function () {
        if($scope.projectList!=undefined) {
            if($scope.projectListBackup == undefined || $scope.projectListBackup.length == 0){
                $scope.projectListBackup = angular.copy($scope.projectList);
            }
            $scope.projectList = filterFilter($scope.projectListBackup, $scope.searchText);
        }
    });


    $scope.$watch('currClientPage + itemPerPage + clientList', function() {
        changeCurrClientList();
    });
    function changeCurrClientList() {
        var begin = (($scope.currClientPage - 1) * $scope.itemPerPage)
            , end = begin + $scope.itemPerPage;

        $scope.currClientList = $scope.clientList.slice(begin, end);
    }

    if(appCache.get('message') != undefined){
        $scope.showAlertMessage = true;
        $scope.message = appCache.get('message');
        appCache.remove('message');
        fadeOut();
    }

    $scope.clientAjax = $http.get(host + "/client")
        .success(function(response){
            $scope.clientList = response;
            $scope.currClientList = response.slice(0, $scope.itemPerPage);
        })
        .error(function (response) {
            $scope.$emit('toggleSpinner', false);
            ngDialog.open({ template: '<div class="error-dialog"><h4 class="title">Error</h4><p>'
            + response[0].messages[0].message +'</p></div>', plain:true });
        })
        .finally(function () {
            $scope.$emit('toggleSpinner', false);
        });


    $scope.editClient = function(clientId){
        appCache.put('clientId', clientId);
        $location.path("/client");
    };

    $scope.deleteClient = function(cid, index){
        ngDialog.openConfirm({ template: '<div class="confirm-dialog"><h4 class="title">Confirm</h4>'
        + '<p>Are you sure you want to delete the client? All associated projects will also get deleted.</p>'
        + '<div class="btns">'
        + '<button class="btn btn-primary bb-button bb-button-small" data-ng-click="confirm();">Yes</button>'
        + '<button class="btn btn-primary bb-button bb-button-small" data-ng-click="closeThisDialog();">No</button>'
        + '</div></div>', plain:true
        }).then(function (success) {
            var clintObj = $scope.clientList.splice(index, 1)[0];
            $scope.lrow_c[index] = true;
            $scope.clientLoader = true;

            $http.delete(host + '/client/' + cid)
                .success(function(response){
                    $route.reload();
                })
                .error(function(response){
                    $scope.clientList.splice(index, 1, clintObj);
                    ngDialog.open({ template: '<div class="error-dialog"><h4 class="title">Error</h4><p>'
                    + response[0].messages[0].message +'</p></div>', plain:true });
                })
                .finally(function(){
                    $scope.lrow_c[index] = false;
                    $scope.clientLoader = false;
                });

        });
    };

    $scope.sortedList=function(property){
        $scope.clientList=appService.sortedList(property,$scope,$filter,$scope.clientList);
        changeCurrClientList();
    };
    function fadeOut(){
        $timeout(function () { $scope.showAlertMessage = false; }, 3000);
    }



    $scope.label = {};
    $scope.label.clientName = "CLIENT NAME";
    $scope.label.edit = "Edit";
    $scope.label.delete = "Delete";

    $scope.label.clientName = "CLIENT NAME";
    $scope.label.address = "ADDRESS";
    $scope.label.city = "CITY";
    $scope.label.state = "STATE";
    $scope.label.country = "COUNTRY";
    $scope.label.zip = "ZIPCODE";
    $scope.label.no_client = "No Clients Found";

});