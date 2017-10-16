/**
 * Created by recoluan on 2016/11/25.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('gulijinDetailController', function ($scope, $state, $rootScope, $location, Principal, Auth, $http, Trade, $stateParams) {
        $scope.currentTab0 = true;
        $scope.currentTab1 = $scope.currentTab2 = $scope.currentTab3 = false;
        $scope.priviousState = 0;
        $scope.currentState = 0;
        $scope.onClickTab = function (index) {
            $scope.priviousState = $scope.currentState;
            $scope.currentState = index;
            //  切换样式
            switch ($scope.priviousState) {
                case 0:
                    $scope.currentTab0 = false;
                    break;
                case 1:
                    $scope.currentTab1 = false;
                    break;
                case 2:
                    $scope.currentTab2 = false;
                    break;
                default:
                    $scope.currentTab3 = false;
            }
            switch ($scope.currentState) {
                //  全部状态
                case 0:
                    $scope.currentTab0 = true;
                    financialCriteria.state = null;
                    $scope.searchByDate();
                    break;
                //  待划款
                case 1:
                    $scope.currentTab1 = true;
                    $scope.ttlWarn1 = false;
                    financialCriteria.state = 0;
                    $scope.searchByDate();
                    break;
                //  划款成功
                case 2:
                    $scope.currentTab2 = true;
                    financialCriteria.state = 1;
                    $scope.searchByDate();
                    break;
                //  已退回
                default:
                    $scope.currentTab3 = true;
                    financialCriteria.state = 2;
                    $scope.searchByDate();
            }
        };
    });
