'use strict';

angular.module('lepayglobleApp')
    .controller('accountManagerController', function ($scope, $state, $location, $http) {
        $scope.accountInfo=[
            {

                username:'小咪咪',
                password:toEncryption('12345678')
            }
        ];

        // 将密码隐藏
        function toEncryption(x) {
            return x.replace(/./g,'*');
        };
        $scope.createAccount = function () {
            $("#createAccount").modal("toggle");
        };
        $scope.deleteAccount = function () {
            $("#deleteAccount").modal("toggle");
        };
    });
/**
 * Created by recoluan on 2016/8/2 0002.
 */
