/**
 * Created by recoluan on 2016/11/25.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('yiBaoHistoryTradeController', function ($scope, $state, $rootScope, $location, Principal, Auth, $http, HomePage, LejiaBilling) {

        $('#timePicker1')
            .daterangepicker({
                opens: 'right', //日期选择框的弹出位置
                format: 'YYYY/MM/DD', //控件中from和to 显示的日期格式
                ranges: {
                    '昨日': [moment().subtract('days', 1).startOf('day'), moment().subtract('days', 1).endOf('day')],
                    '最近7日': [moment().subtract('days', 6), moment()],
                    '最近30日': [moment().subtract('days', 29), moment()]
                },
            }, function (start, end, label) {
            });

        var stateArr = ['yiBaoHistoryTradeAllState', 'yiBaoHistoryTradeTransfering', 'yiBaoHistoryTradeTransferSuccess', 'yiBaoHistoryTradeHanged'];
        $scope.ttlWarn1 = true;
        $scope.ttlWarn2 = true;
        $scope.currentTab0 = true;
        $scope.currentTab1 = $scope.currentTab2 = $scope.currentTab3 = false;
        $scope.priviousState = 0;
        $scope.currentState = 0;
        $scope.onClickTab = function (index) {
            $scope.priviousState = $scope.currentState;
            $scope.currentState = index;
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
                case 0:
                    $scope.currentTab0 = true;
                    $state.go(stateArr[0]);
                    break;
                case 1:
                    $scope.currentTab1 = true;
                    $scope.ttlWarn1 = false;
                    $state.go(stateArr[1]);
                    break;
                case 2:
                    $scope.currentTab2 = true;
                    $state.go(stateArr[2]);
                    break;
                default:
                    $scope.currentTab3 = true;
                    $scope.ttlWarn2 = false;
                    $state.go(stateArr[3]);
                    break;
            }
        };
    });

