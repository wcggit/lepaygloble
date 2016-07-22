'use strict';

angular.module('lepayglobleApp')
    .controller('MerchantController', function ($scope, $state, $rootScope, $location,Principal,Auth,$http) {
                    Principal.identity().then(function (account) {
                        $scope.account = account;
                        $scope.isAuthenticated = Principal.isAuthenticated;
                    });
                    $rootScope.personal=false;
                    $scope.personalClick=function () {
                        $rootScope.personal =!$rootScope.personal;
                        $rootScope.personal=$rootScope.personal;
                    };
                    $http.get('api/merchant').success(function (response) {
                        $scope.shopName = response.data.name;
                    });
                    $scope.headers = [
                        {
                            ttl: "我的乐付",
                            state: "mylepay"
                        },
                        {
                            ttl: "交易管理",
                            state: "trade"
                        },
                        {
                            ttl: "导流服务",
                            state: "import"
                        },
                        {
                            ttl: "会员变现",
                            state: "realize"
                        }
                    ];
                    if ($location.url() == "/merchant") {
                        $scope.currentTab = "mylepay";
                        $state.go("mylepay");
                    }
                    if ($location.url().indexOf("trade") != -1) {
                        $scope.currentTab = "trade";
                    }
                    if ($location.url().indexOf("/import") != -1) {
                        $scope.currentTab = "import";
                        $('body').css({background: '#f3f3f3'});
                    }
                    if ($location.url().indexOf("/realize") != -1) {
                        $scope.currentTab = "realize";
                    }
                    $scope.onClickTab = function (tab) {
                        $scope.currentTab = tab.state;
                        $state.go(tab.state);
                        if ($scope.currentTab == "mylepay") {
                        } else {
                            $('body').css({background: '#fff'});
                            $('.main-content').css({height: '100vh'})
                        }
                    };
                    $scope.szClick = function () {
                        $scope.currentTab = "";

                        $state.go('merchant-config')
                    }
                    $scope.logout = function () {
                        Auth.logout();
                        $state.go('home')
                    }
                    $scope.isActiveTab = function (tabState) {
                        return tabState == $scope.currentTab;
                    };
                    $('.dropdown').dropdown('toggle')
                });

