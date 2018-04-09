var bbApp = angular.module('bb-app', ["ngRoute", "bbAppServices", "ngDialog", "countrySelect", "ngCookies", "checklist-model", "ui.bootstrap", "nvd3", "rzModule"]);

bbApp.controller('MainCtrl', function ($scope, $location, $route, $rootScope, $timeout) {

    $scope.showSpinner = false;
    $scope.showCreateSteps = false;
    $scope.sideMenuActive = false;
    $scope.loginSuccess = false;

    $(function () {
        $("#spinner").height($(document).height() - 70);
    });

    $scope.$watch('$root.loggedUser', function () {

        if (typeof $rootScope.loggedUser != 'undefined') {
            $scope.loginSuccess = true;
        }
    });

    $scope.$watch('$root.sideMenuActive', function () {
        if ($rootScope.sideMenuActive != undefined) {
            $scope.sideMenuActive = $rootScope.sideMenuActive;

            $timeout(function () {
                window.dispatchEvent(new Event('resize'));
            }, 600);
        }
    });

    $scope.$on('toggleSpinner', function (event, flag) {
        $scope.showSpinner = flag;
    });

    function detectRoute() {

        if ($location.path().startsWith("/project")) {
            $scope.showCreateSteps = true;
        }
        else {
            $scope.showCreateSteps = false;
        }

        $("#page-caption").html($route.current.title);

        var links = $(".menu-link");

        for (var i = 0; i < links.length; i++) {
            if (links[i].href.endsWith($location.path())) {
                $(links[i]).addClass("active-menu-item");
            }
            else {
                $(links[i]).removeClass("active-menu-item");
            }
        }
    }

    $scope.$on('$routeChangeSuccess', detectRoute);

});

bbApp.controller('AuthCtrl', function ($scope, $location, appService, $cookies) {

    var hash = $location.path().substr(1);
    var param = hash.split('=:');
    if (param.length > 1) {
        var authKey = param[1];
        appService.setAuthKey(authKey);
        var expireDate = new Date();
        expireDate.setDate(expireDate.getDate() + 30);
        $cookies.put("__bbusrtoken", authKey, {'expires': expireDate});
    }
    $location.path("/");

});


var bbAppServices = angular.module('bbAppServices', []);

bbAppServices.factory('appCache', function ($cacheFactory) {
    return $cacheFactory('appCache');
});

bbApp.factory("cacheUserData", function ($rootScope, $http, $q) {
    return {
        init: function () {
            if ($rootScope.loggedUser == undefined) {
                return $http.get(host + '/ttn/employee/findOne')
                    .success(function (empResponse) {
                        $rootScope.loggedUser = empResponse;
                        $rootScope.loggedUser.currentRole = $rootScope.loggedUser.roles[0];
                    });
            }
            else
                return $q.when($rootScope.loggedUser);

        }

    };
});

bbApp.service('appService', function ($http, $cookies, $rootScope, ngDialog) {

    this.authKey = '';
    this.resultMap = {};

    this.setAuthKey = function (authKey) {
        this.authKey = authKey;
    };

    this.isLoggedIn = function () {
        if ($cookies.get("__bbusrtoken") == undefined) {
            return false;
        }
        else {
            if (this.authKey == '') {
                this.authKey = $cookies.get("__bbusrtoken");
            }
        }
        return this.authKey == '' ? false : true;
    };

    this.getAuthKey = function () {
        return this.authKey;
    };

    this.convertDate = function (input) {
        function pad(s) {
            return (s < 10) ? '0' + s : s;
        }

        var d = input;
        return [d.getFullYear(), pad(d.getMonth() + 1), pad(d.getDate())].join('-');
    };

    this.isEmpty = function (myObject) {
        for (var key in myObject) {
            if (myObject.hasOwnProperty(key)) {
                return false;
            }
        }
        return true;
    };

    this.getData = function (url, method, data, successCallback, errorCallback, cache, showSpinner, showError) {

        if (this.resultMap[url] != undefined) {
            successCallback(this.resultMap[url]);
        }
        else {

            if (showSpinner) {
                $rootScope.$broadcast('toggleSpinner', true);
            }

            var _map = this.resultMap;
            var _authKey = this.authKey;
            $http({
                method: method,
                url: url,
                data: data,
                headers: {
                    'X-ACCESS-TOKEN': _authKey
                }
            }).then(function mySuccess(response) {
                if (cache) {
                    _map[url] = response.data;
                }
                successCallback(response.data);
            }, function myError(response) {
                if (showError) {
                    ngDialog.open({
                        template: '<div class="error-dialog"><h4 class="title">Error</h4><p>'
                        + response.data[0].messages[0].message + '</p></div>', plain: true
                    });
                }
                if (errorCallback != undefined) {
                    errorCallback(response.data);
                }
            }).finally(function () {
                if (showSpinner)
                    $rootScope.$broadcast('toggleSpinner', false);
            });
        }
    };
    this.sortedList = function (property, scope, filter, collection) {
        scope.property = property;
        if (scope.currentProperty == property) {
            collection = sort(collection, property, true, filter);
            scope.currentProperty = " ";
            scope.sortingclass = "pl1 glyphicon glyphicon-arrow-up";
        } else {
            collection = sort(collection, property, false, filter);
            scope.currentProperty = property;
            scope.sortingclass = "pl1 glyphicon glyphicon-arrow-down";
        }
        return collection;
    };
    function sort(collection, property, reverse, filter) {
        return filter('orderBy')(collection, property, reverse)
    }

    this.showModal = function (modal, controller, templateUrl, params, className, sizeClass) {
        var modalInstance = modal.open({
            templateUrl: templateUrl,
            windowClass: className,
            controller: controller,
            resolve: {
                params: function () {
                    return params;
                }
            },
            size: sizeClass
        });
    }
});
bbApp.constant('regions', {
    'US East': ['US East'],
    'US West': ['US West'],
    'US Central + ANZ': ['US Central', 'Australia-New Zealand'],
    'ME': ['Middle East and Africa'],
    'India': ['India'],
    'Video': ['Video'],
    'Video Ready': ['Video Ready'],
    'EU': ['European Union'],
    'SmartTV': ['SmartTV'],
    'SEA': ['South East Asia'],
    'HDFC': ['HDFC'],
    'Corp': ['Corporate Marketing'],
    'GIH': ['Global Innovation Hub'],
    'NAC': ['NAC']
});
bbApp.directive('elemReady', function ($parse) {
    return {
        restrict: 'A',
        link: function ($scope, elem, attrs) {
            elem.ready(function () {
                $scope.$apply(function () {
                    var func = $parse(attrs.elemReady);
                    func($scope);
                })
            })
        }
    }
});

bbApp.directive('lazyLoad', function () {
    return {
        restrict: 'A',
        link: function (scope, elem) {
            var scroller = elem[0].parentElement;
            $(scroller).bind('scroll', function () {
                if (scroller.scrollTop > 40) {
                    $(elem[0]).find("#filterWrap").css({'top': (scroller.scrollTop - 40 ) + 'px'});
                }
                else {
                    $(elem[0]).find("#filterWrap").css({'top': '0px'});
                }

                if (scroller.scrollTop + scroller.offsetHeight + 800 >= scroller.scrollHeight) {
                    scope.$apply('loadMore()');
                }
            })
        }
    }
});

bbApp.directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function (scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;

            element.bind('change', function () {
                scope.$apply(function () {
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
}]);

bbApp.filter("arrayByKeyValues", function () {

    return function (input, key, value) {
        if (key == "" || typeof key == undefined || typeof value == undefined)
            return input;
        output = [];
        angular.forEach(input, function (data) {
            var newKey = key.split('.');
            if (newKey[1] != undefined) {
                if (value.indexOf(data[newKey[0]][newKey[1]]) != -1)
                    output.push(data);
            }
            else {
                if (value.indexOf(data[key]) != -1)
                    output.push(data);
            }

        });
        return output;
    }
});

bbApp.service('fileUpload', ['$http', 'ngDialog', '$rootScope', function ($http, ngDialog, $rootScope) {
    this.uploadFileToUrl = function (file, uploadUrl) {
        $rootScope.$broadcast('toggleSpinner', true);
        var fd = new FormData();
        fd.append('file', file);
        $http.post(uploadUrl, fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        })
            .success(function () {
                ngDialog.open({
                    template: '<div class="success-dialog"><h4 class="title">Success</h4><p>'
                    + 'Data Uploaded Successfully!' + '</p></div>', plain: true
                });
            })
            .error(function (response) {
                ngDialog.open({
                    template: '<div class="error-dialog"><h4 class="title">Error</h4><p>'
                    + response[0].messages[0].message + '</p></div>', plain: true
                });
            })
            .finally(function () {
                $rootScope.$broadcast('toggleSpinner', false);
            });
    }
}]);


var host = "/bluebell";
var  sfAuthUrl= window.location.origin+"/bluebell/login/sso";