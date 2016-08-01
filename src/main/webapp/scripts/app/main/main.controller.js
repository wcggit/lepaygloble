'use strict';

angular.module('lepayglobleApp')
    .controller('MainController', function ($rootScope, Principal,$state,$scope) {

        //身份确认
        Principal.identity().then(function(account) {
            //$scope.account = account;
            //商户是否已登录的状态值
            if(Principal.hasAnyAuthority(['merchant'] )){
                $scope.merchant = true;
            }
            if(Principal.hasAnyAuthority(['partner'])){
                $scope.partner = true;
            }
            $scope.isAuthenticated = Principal.isAuthenticated();
        });

        //跳转登陆页面
        $scope.login=function(){
            $state.go('login');
        };
        $scope.loginPartner=function(){
            $state.go('loginPartner');
        };
        $scope.stateChange=function(){
            if(Principal.hasAnyAuthority(['merchant'])){
                $state.go('merchant');
            }
            if(Principal.hasAnyAuthority(['partner'])){
                $state.go('partner');
            }
        };
    });
