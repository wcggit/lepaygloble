'use strict';

angular.module('lepayglobleApp')
    .controller('ImportController', function ($scope, $http, $rootScope, $location,Principal,Auth) {
                    Principal.identity().then(function (account) {
                        $scope.account = account;
                    });
                    $http.get('api/merchant').success(function (response) {
                        $scope.merchant = response.data;
                    });
                    $scope.basicInfo={
                        name:"棉花糖",
                        tel:"11111111111",
                        nickname:"faaefaf",
                        passWord:"adadaxxxxxxadad"
                    };
                    $scope.billInfo={
                        payee:"王小二",
                        bankCardNum:"1234567890",
                        bank:"建行支行",
                        billCycle:"T+2（提现申请后两个工作日内到账）"
                    };
                    $scope.changePassword=function () {
                        $("#changePassword").modal("toggle");
                    }

                    $scope.ljkt=function () {
                        $("#ljkt").modal("toggle");
                        $http.get('/api/merchant/open');
                    }


                });

