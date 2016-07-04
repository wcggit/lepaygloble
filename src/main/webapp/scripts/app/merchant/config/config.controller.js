'use strict';

angular.module('lepayglobleApp')
    .controller('MerchantConfigController', function ($scope, $http, $rootScope, $location,Principal,Auth) {
                    Principal.identity().then(function (account) {
                        $scope.account = account;
                    });
                    $http.get('api/merchant').success(function (response) {
                        $scope.merchant = response.data;
                    });
                    $scope.changePassword=function () {
                        $("#changePassword").modal("toggle");
                    }

                });

