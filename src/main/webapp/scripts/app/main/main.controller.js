'use strict';

angular.module('lepayglobleApp')
    .controller('MainController', function ($scope, Principal,$state) {

        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated();
        });
                    $scope.login=function(){
                        $state.go('login');
                    }
                    $scope.stateChange=function(){
                        if(Principal.hasAuthority('merchant')){

                            $state.go('merchant');
                        }
                    }
    });
