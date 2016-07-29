'use strict';

angular.module('lepayglobleApp')
    .controller('LoginPartnerController',
        function ($rootScope, $scope, $state, $timeout, Auth, Principal) {
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
                               rememberMe: $scope.rememberMe

                           }).then(function () {
                    $scope.authenticationError = false;
                    if ($rootScope.previousStateName === 'home'
                        || $rootScope.previousStateName == null
                        || $rootScope.previousStateName == "") {
                        if (Principal.hasAuthority('merchant')) {
                            $state.go('merchant');
                        }
                    } else {
                        $rootScope.back();
                    }
                }).catch(function () {
                    $scope.authenticationError = true;
                });
            };
        });
