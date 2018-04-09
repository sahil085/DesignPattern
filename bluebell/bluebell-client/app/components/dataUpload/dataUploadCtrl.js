/**
 * Created by ashutoshmeher on 30/12/16.
 */

bbApp.controller('dataUploadCtrl', function($scope, fileUpload) {


    $scope.uploadReviewerData = function () {
        if($scope.excelFile == undefined){
            $scope.uploadForm.$invalid = true;
        }
        else{
            $scope.uploadForm.$invalid = false;
            var extn = $scope.excelFile.name.substring($scope.excelFile.name.lastIndexOf('.')+1, $scope.excelFile.length);
            if(extn == 'xls' || extn == 'xlsx'){
                fileUpload.uploadFileToUrl($scope.excelFile, host+'/reviewerDataUpload');
            }
            else {
                $scope.uploadForm.$invalid = true;
            }
        }
    };
});