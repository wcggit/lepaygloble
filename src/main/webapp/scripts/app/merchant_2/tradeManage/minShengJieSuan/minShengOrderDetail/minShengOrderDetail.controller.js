/**
 * Created by recoluan on 2016/11/25.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('minShengOrderDetailController', function ($scope, $state, $rootScope, $location, Principal, Auth, $http, HomePage, $stateParams) {
        $scope.currentTopTapState = 0;
        $scope.currentState = 0;
        $scope.topTabClick = function (index) {
            $scope.currentTopTapState = index;
            $scope.currentState = 0;
        };
        $scope.onClickTab = function (index) {
            $scope.currentState = index;
        };
    });

