'use strict';

angular.module('lepayglobleApp')
    .controller('ImportController', function ($scope, $http, $rootScope, $location,Principal,Auth) {
                    Principal.identity().then(function (account) {
                        $scope.account = account;
                    });
                    $http.get('api/merchant').success(function (response) {
                        $scope.merchant = response.data;
                    });


                    $scope.changePassword=function () {
                        $("#changePassword").modal("toggle");
                    };
                    $scope.ljkt=function () {
                        $("#ljkt").modal("toggle");
                        $http.get('/api/merchant/open');
                    }

                    $scope.memberInfo=[
                        {
                            lockTime:'2016-07-29 18:34:34',
                            wxInfo:{
                                headImg:'1.jpg',
                                nickname:'努力的小羔羊',
                            }

                        }
                    ]
                });

