'use strict';

angular.module('lepayglobleApp')
    .controller('PartnerController', function ($scope, $state, $rootScope, $location,Principal,Auth,$http) {

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
        if ($location.url() == "/partner") {
            $scope.currentTab = "partnerhome";
            $state.go("partnerhome");
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

