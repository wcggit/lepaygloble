/**
 * Created by recoluan on 2016/11/25.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('minShengCodeTradeController', function ($scope, $state, $rootScope, $location, Principal, Auth, $http, $stateParams,Trade) {
        var currentPage = null;
        var settlementCriteria = {};
        settlementCriteria.offset = 1;
        currentPage = 1;
        // 跳转到详情页面
        $scope.goDetail = function (ledgerNo,tradeDate,totalTransfer,transferState) {
            $scope.$parent.currentTab = "minShengOrderDetail";
            $state.go("minShengOrderDetail", {ledgerNo: ledgerNo,tradeDate:tradeDate,totalTransfer:totalTransfer,transferState:transferState});
        }

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


        $scope.currentState = 0;
        $scope.onClickTab = function (index) {
            $scope.currentState = index;
        };

    });

