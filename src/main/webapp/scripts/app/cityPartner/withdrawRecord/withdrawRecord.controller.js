/**
 * Created by recoluan on 2017/3/15.
 */
/**
 * Created by recoluan on 2016/11/16.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('cp-withdrawRecordController', function ($scope, $state, $rootScope, $location, Principal, Auth, $http, Commission, HomePage) {
        $scope.currentTab0 = true;
        $scope.currentTab1 = false;
        $scope.onClickTab = function () {
            $scope.currentTab0 = !$scope.currentTab0;
            $scope.currentTab1 = !$scope.currentTab1;
        };
    });
