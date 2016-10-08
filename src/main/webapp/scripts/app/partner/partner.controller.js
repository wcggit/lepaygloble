'use strict';

angular.module('lepayglobleApp')
    .controller('PartnerController',
                function ($scope, $state, $rootScope, $location, Principal, Auth, $http) {

                    Principal.identity().then(function (account) {
                        $scope.account = account;
                        $scope.isAuthenticated = Principal.isAuthenticated;
                    });
                    $rootScope.personal = false;
                    $scope.personalClick = function () {
                        $rootScope.personal = !$rootScope.personal;
                        $rootScope.personal = $rootScope.personal;
                    };
                    $http.get('api/partner').success(function (response) {
                        $scope.partnerName = response.data.partnerName;
                        $scope.partneraccount = response.data.name;
                    });
                    $scope.headers = [
                        {
                            ttl: "首页",
                            state: "partnerhome"
                        },
                        {
                            ttl: "我的商户",
                            state: "myitems"
                        },
                        {
                            ttl: "我的会员",
                            state: "usermanager"
                        }
                    ];
                    if ($location.url().indexOf( "/partnerhome")!=-1) {
                        $scope.currentTab = "partnerhome";
                        $state.go("partnerhome");
                    }
                    if ($location.url().indexOf("myitems") != -1) {
                        $scope.currentTab = "myitems";
                    }
                    if ($location.url().indexOf("/usermanager") != -1) {
                        $scope.currentTab = "usermanager";
                        $('body').css({background: '#f3f3f3'});
                    }
                    $scope.onClickTab = function (tab) {
                        $scope.currentTab = tab.state;
                        $state.go(tab.state);
                    };
                    $scope.szClick = function () {
                        $scope.currentTab = "";
                        $state.go('partner-config')
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

