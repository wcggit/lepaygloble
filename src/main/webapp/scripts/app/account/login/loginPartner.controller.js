'use strict';

angular.module('lepayglobleApp')
    .controller('LoginPartnerController',
                function ($rootScope, $scope, $state, $timeout, Auth, Principal,Tracker) {
                    $scope.user = {};
                    $scope.errors = {};

                    $scope.rememberMe = true;
                    $timeout(function () {
                        angular.element('[ng-model="username"]').focus();
                    });
                    $scope.login = function (event) {
                        event.preventDefault();
                        Auth.login({
                                       username: $scope.username.trim(),
                                       password: $scope.password.trim(),
                                       isPartner: true
                                   }).then(function () {
                            //登录成功开启webSocket
                            Tracker.connect();
                            $scope.authenticationError = false;
                            if ($rootScope.previousStateName === 'home'
                                || $rootScope.previousStateName == null
                                || $rootScope.previousStateName == ""
                                || $rootScope.previousStateName == "loginPartner") {
                                if (Principal.hasAuthority('partner')) {
                                    $state.go('partnerhome');
                                }
                            } else {
                                $rootScope.back();
                            }
                        }).catch(function () {
                            $scope.authenticationError = true;
                        });
                    };
                });
