bbApp.config(function ($routeProvider) {
    $routeProvider
        .when('/', {
            title: "Dashboard",
            templateUrl: 'app/components/dashboard/graph.html',
            resolve: {
                initiateUserCache: function (cacheUserData) {
                    return cacheUserData.init();
                }

            }
        })
        .when("/listProjects", {
            title: "Projects",
            templateUrl: "app/components/project/projectList.html"
        })
        .when("/reports", {
            title: "Projects",
            templateUrl: "app/components/staffAllocation/allocationReport.html"
        })
        .when("/listClients", {
            title: "Clients",
            templateUrl: "app/components/client/clientList.html"
        })
        .when("/project", {
            title: "Project Detail",
            templateUrl: "app/components/project/project-detail.html"
        })
        .when("/project/billing-info-fixed", {
            title: "Billing Details",
            templateUrl: "app/components/project/billing-info/fixed-project.html"
        })
        .when("/project/billing-info-tnm", {
            title: "Billing Details ",
            templateUrl: "app/components/project/billing-info/timeNmoney-project.html"
        })
        .when("/project/staff-request", {
            title: "Staffing Request",
            templateUrl: "app/components/project/staff-request/staff-request.html"
        }).when("/client", {
        title: "Client Detail",
        templateUrl: "app/components/client/client-detail.html"
    })
        .when("/login", {
            title: "Login",
            templateUrl: "app/components/login/login.html"
        })
        .when("/staffing-needs", {
            title: "Staffing Details",
            templateUrl: "app/components/staffingNeeds/staffingNeeds.html"
        })
        .when("/assign-roles", {
            title: "User Role Details",
            templateUrl: "app/components/userRole/userMapping.html"
        })
        .when('/access_token=:accessToken', {
            title: "Login Success",
            templateUrl: 'app/components/login/wait.html',
            controller: 'AuthCtrl'
        })
        .when('/employeeSearch', {
            title: "Employee Search",
            templateUrl: 'app/components/staffAllocation/employeeSearch.html'
        })
        .when('/openNeeds', {
            title: "Open Needs",
            templateUrl: 'app/components/staffingNeeds/openNeeds.html'
        })
        .when('/upcomingDeallocations', {
            title: "Bench And Upcoming Deallocations",
            templateUrl: 'app/components/upcomingDeallocation/upcomingDeallocations.html'
        })
        .when('/bench', {
            title: "Employees On Bench",
            templateUrl: 'app/components/bench/bench.html'
        })
        .when('/nonBillable', {
            title: "Non Billable Employees",
            templateUrl: 'app/components/bench/nonBillable.html'
        })
        .when('/dataUpload', {
            title: "Data Upload",
            templateUrl: 'app/components/dataUpload/dataUploadForm.html'
        }).when('/mentovisorAndPerformanceReviewer', {
        title: "Mentovisor And PerformanceReviewer",
        templateUrl: 'app/components/mentovisorAndPerformanceReviewer/mentovisorAndPerformanceReviewer.html'

    });

});


bbApp.run(['$rootScope', '$location', 'appService', '$window', '$http', '$cookies', '$route',
    function ($rootScope, $location, appService, $window, $http, $cookies, $route) {

        $rootScope.$on('$routeChangeStart', function (event) {

            if (!appService.isLoggedIn() && !$location.path().startsWith("/access_token")) {
                event.preventDefault();
                $window.location.assign(sfAuthUrl);
            }
            else {

                $http.defaults.headers.common['X-ACCESS-TOKEN'] = appService.getAuthKey();

                if ($location.path() != "/login") {

                    if (appService.isLoggedIn()) {
                        $http.get(host + '/login/sfResponse/validated')
                            .success(function (response) {
                                if (response.result == 'VALID') {

                                    if ($rootScope.loggedUser == undefined) {
                                        $http.get(host + '/ttn/employee/findOne')
                                            .success(function (empResponse) {
                                                $rootScope.loggedUser = empResponse;
                                                $rootScope.loggedUser.currentRole = $rootScope.loggedUser.roles[0];
                                                //$route.reload();
                                            });
                                    }
                                }
                                else {
                                    $cookies.remove('__bbusrtoken');
                                    $window.location.assign(sfAuthUrl);
                                }
                            })
                            .error(function () {
                                $cookies.remove('__bbusrtoken');
                                $window.location.assign(sfAuthUrl);
                            });
                    }
                }
            }

        });
    }]);