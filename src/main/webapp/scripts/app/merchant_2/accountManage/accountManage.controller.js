 /**
 * Created by recoluan on 2016/11/16.
 */
 'use strict';


angular.module('lepayglobleApp')
    .controller('accountManageController', function ($scope, $state, $rootScope, $location,Principal,Auth,$http) {
        $scope.accountInfoState0=true;
        $scope.accountInfoState1=false;
        $scope.accountInfoFun0=function () {
            $scope.accountInfoState0=true;
            $scope.accountInfoState1=false;
        }
        $scope.accountInfoFun1=function () {
            $scope.accountInfoState0=false;
            $scope.accountInfoState1=true;
        }

    })
