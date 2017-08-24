/**
 * Created by recoluan on 2016/11/25.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('lePlusOrderDetailController', function ($scope, $state, $rootScope, $location, Principal, Auth, $http, HomePage, $stateParams) {

        var stateArr = ['lePlusTradeRecord', 'lePlusReturnRecord'];
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
                    break;
                default:
                    $scope.currentTab1 = true;
                    break;
            }
        };
    });

