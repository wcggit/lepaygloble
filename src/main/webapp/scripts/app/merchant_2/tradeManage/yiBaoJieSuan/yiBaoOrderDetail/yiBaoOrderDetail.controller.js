/**
 * Created by recoluan on 2016/11/25.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('yiBaoOrderDetailController', function ($scope, $state, $rootScope, $location, Principal, Auth, $http, HomePage, LejiaBilling) {

        var stateArr = ['yiBaoTradeRecord', 'yiBaoReturnRecord'];
        $scope.currentTab0 = true;
        $scope.currentTab1 = false;
        $scope.priviousState = 0;
        $scope.currentState = 0;
        $scope.onClickTab = function (index) {
            $scope.priviousState = $scope.currentState;
            $scope.currentState = index;
            switch ($scope.priviousState) {
                case 0:
                    $scope.currentTab0 = false;
                    break;
                default:
                    $scope.currentTab1 = false;
            }
            switch ($scope.currentState) {
                case 0:
                    $scope.currentTab0 = true;
                    $state.go(stateArr[0]);
                    break;
                default:
                    $scope.currentTab1 = true;
                    $state.go(stateArr[1]);
                    break;
            }
        };
    });

