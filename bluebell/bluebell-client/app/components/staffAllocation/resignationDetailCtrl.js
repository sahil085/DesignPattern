bbApp.controller('resignationDetailCtrl', function ($scope,$modalInstance,params,appService,$http) {
    $scope.emp=params.scope.emp
    $scope.close = function () {
        $modalInstance.dismiss('cancel');
    };


});
