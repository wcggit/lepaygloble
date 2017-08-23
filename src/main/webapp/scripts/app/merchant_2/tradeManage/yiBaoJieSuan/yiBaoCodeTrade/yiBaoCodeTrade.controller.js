/**
 * Created by recoluan on 2016/11/25.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('yiBaoCodeTradeController', function ($scope, $state, $rootScope, $location, Principal, Auth, $http, HomePage, LejiaBilling) {
        var currentPage = null;
        var settlementCriteria = {};
        settlementCriteria.offset = 1;
        currentPage = 1;

        // 门店列表
        HomePage.getMerchantsInfo().then(function (response) {
            var data = response.data;
            $scope.merchants = data;
            $scope.defaultId = data[0].id;
            var merchant = {};
            merchant.id = $scope.defaultId;
            settlementCriteria.merchant = merchant;
            loadContent();
        });


        //  加载通道结算单
        function loadContent() {
            $http.post('/api/ledgerSettlement/findByPage', settlementCriteria, {
                headers: {
                    'Content-Type': 'application/json'
                }
            }).success(function (response) {
                var page = response.data;
                $scope.pulls = page.content;
                $scope.page = currentPage;
                $scope.totalPages = page.totalPages;
                console.log(JSON.stringify(page));
            });
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

        $scope.lookHistoryTrade = function () {
            $state.go('yiBaoHistoryTrade');
        }


        var stateArr = ['yiBaoCodeTradeAllState', 'yiBaoCodeTradeTransfering', 'yiBaoCodeTradeTransferSuccess', 'yiBaoCodeTradeTransferErr', 'yiBaoCodeTradeReturn']
        $scope.ttlWarn1 = true;
        $scope.ttlWarn2 = true;
        $scope.currentTab0 = true;
        $scope.currentTab1 = $scope.currentTab2 = $scope.currentTab3 = $scope.currentTab4 = false;
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
                case 3:
                    $scope.currentTab3 = false;
                    break;
                default:
                    $scope.currentTab4 = false;
            }
            switch ($scope.currentState) {
                case 0:
                    $scope.currentTab0 = true;
                    break;
                case 1:
                    $scope.currentTab1 = true;
                    $scope.ttlWarn1 = false;
                    break;
                case 2:
                    $scope.currentTab2 = true;
                    break;
                case 3:
                    $scope.currentTab3 = true;
                    $scope.ttlWarn2 = false;
                    break;
                default:
                    $scope.currentTab4 = true;
            }
        };

    });

