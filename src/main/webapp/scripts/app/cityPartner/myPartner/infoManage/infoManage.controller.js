 /**
 * Created by recoluan on 2016/11/16.
 */
 'use strict';


angular.module('lepayglobleApp')
    .controller('cp-infoManageController', function ($scope, $state, $rootScope, $location,Principal,Auth,$http) {
         $scope.currentTab0 = true;
         $scope.currentTab1 = false;
         $scope.currentTab2 = false;
         $scope.currentTab3 = false;
         $scope.onClickTab = function () {
             $scope.currentTab0 = !$scope.currentTab0;
             $scope.currentTab1 = !$scope.currentTab1;
             $scope.currentTab2 = !$scope.currentTab2;
             $scope.currentTab3 = !$scope.currentTab3;
         };
    })
