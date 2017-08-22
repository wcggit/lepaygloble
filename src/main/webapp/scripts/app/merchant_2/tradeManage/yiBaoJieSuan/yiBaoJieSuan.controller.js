 /**
 * Created by recoluan on 2016/11/16.
 */
 'use strict';


angular.module('lepayglobleApp')
    .controller('yiBaoJieSuanController', function ($scope, $state, $rootScope, $location,Principal,Auth,$http) {
            $scope.currentTab0 = true;
            $scope.currentTab1 = false;
            $scope.onClickTab = function () {
                $scope.currentTab0 = !$scope.currentTab0;
                $scope.currentTab1 = !$scope.currentTab1;
            };
    })
