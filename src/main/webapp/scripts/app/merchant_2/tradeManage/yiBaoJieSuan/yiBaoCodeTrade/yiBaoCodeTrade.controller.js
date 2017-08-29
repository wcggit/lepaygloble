/**
 * Created by recoluan on 2016/11/25.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('yiBaoCodeTradeController', function ($scope, $state, $rootScope, $location, Principal, Auth, $http, $stateParams,Trade) {
        var currentPage = null;
        var settlementCriteria = {};
        settlementCriteria.offset = 1;
        currentPage = 1;
        getMerchantPayWay();
        // 门店列表和支付方式
        function getMerchantPayWay() {
            $http.get('api/merchantUser/merchantsAndPayWay').success(function (response) {
                var data = response.data;
                $scope.payway = data;
                $scope.merchants = data.merchants;
                $scope.defaultId = data.merchants[0][0];
                var merchant = {};
                merchant.id = $scope.defaultId;
                settlementCriteria.merchant = merchant;
                var payWay = data["merchant-"+$scope.defaultId];    // 根据支付通道选择页面
                if(payWay==3) {                 // 易宝
                    loadContent();
                    loadMerchantLedger($scope.defaultId);
                }else {                         // 乐加
                    $state.go("lePlusCodeTrade", {mid: $scope.defaultId});
                }
            });
        }

        // 加载易宝商户数,同子商户门店
        function loadMerchantLedger(mid) {
            $http.get('/api/settelement/merchantLedger/'+mid).success(function (response) {
                var map = response.data;
                $scope.ledgerCount = map.count;
                $scope.ledgerNames = map.merchantNames;
            });
        }

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
            });
        }

        // 加载页数
        $scope.loadPage = function (page) {
            if (page == 0) {
                return;
            }
            if (page > $scope.totalPages) {
                return;
            }
            if (currentPage == $scope.totalPages && page == $scope.totalPages) {
                return;
            }
            if (currentPage == 1 && page == 1) {
                return;
            }
            currentPage = page;
            settlementCriteria.offset = page;
            loadContent();
        };
        //  根据条件进行查询
        $scope.searchByDate = function () {
            //  日期
            var dateStr = $("#timePicker1").val();
            if (dateStr != null && dateStr != "") {
                var startDate = dateStr.split("-")[0];
                var endDate = dateStr.split("-")[1].trim();
                settlementCriteria.startDate = startDate;
                settlementCriteria.endDate = endDate;
            } else {
                settlementCriteria.startDate = null;
                settlementCriteria.endDate = null;
            }
            //  门店
            var merchant = {};
            var mid = $("#selMerchant").val();
            if(mid!=-1) {
                merchant.id = mid;
                settlementCriteria.merchant = merchant;
            }else {
                var deftId = $scope.defaultId;
                $("#selMerchant").val(deftId);
                merchant.id = deftId;
                settlementCriteria.merchant = merchant;
            }
            // alert(JSON.stringify(settlementCriteria));
            settlementCriteria.offset = 1;
            currentPage = 1;
            var payWay = $scope.payway["merchant-"+mid];    // 根据支付通道选择页面
            if(payWay==3) {                 // 易宝
                loadContent();
            }else {                         // 乐加
                $state.go("lePlusCodeTrade", {mid: mid});
            }
        }
        // 跳转到详情页面
        $scope.goDetail = function (ledgerNo,tradeDate,totalTransfer,transferState) {
            $scope.$parent.currentTab = "yiBaoOrderDetail";
            $state.go("yiBaoOrderDetail", {ledgerNo: ledgerNo,tradeDate:tradeDate,totalTransfer:totalTransfer,transferState:transferState});
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
            // 设置页数
            settlementCriteria.offset=1;
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
                case 3:
                    $scope.currentTab3 = false;
                    break;
                default:
                    $scope.currentTab4 = false;
            }
            switch ($scope.currentState) {
                //  全部状态
                case 0:
                    $scope.currentTab0 = true;
                    settlementCriteria.state = null;
                    break;
                //  待划款
                case 1:
                    $scope.currentTab1 = true;
                    $scope.ttlWarn1 = false;
                    settlementCriteria.state = 4;
                    break;
                //  划款成功
                case 2:
                    $scope.currentTab2 = true;
                    settlementCriteria.state = 1;
                    break;
                //  划款失败
                case 3:
                    $scope.currentTab3 = true;
                    $scope.ttlWarn2 = false;
                    settlementCriteria.state = -1;
                    break;
                //  已退回
                default:
                    $scope.currentTab4 = true;
                    settlementCriteria.state = 2;
            }
            // 重新加载数据
            loadContent();
        };


        $('#timePicker1')
        // .val(moment().subtract('day', 1).format('YYYY/MM/DD HH:mm:00') + ' - ' +
        // moment().format('YYYY/MM/DD HH:mm:59'))
            .daterangepicker({
                timePicker: true, //是否显示小时和分钟
                timePickerIncrement: 1, //时间的增量，单位为分钟
                opens: 'right', //日期选择框的弹出位置
                startDate: moment().format('YYYY/MM/DD HH:mm:00'),
                endDate: moment().format('YYYY/MM/DD HH:mm:59'),
                format: 'YYYY/MM/DD HH:mm:ss', //控件中from和to 显示的日期格式
                ranges: {
                    '最近1小时': [moment().subtract('hours', 1), moment()],
                    '今日': [moment().startOf('day'), moment()],
                    '昨日': [moment().subtract('days', 1).startOf('day'),
                        moment().subtract('days', 1).endOf('day')],
                    '最近7日': [moment().subtract('days', 6), moment()],
                    '最近30日': [moment().subtract('days', 29), moment()]
                },
            }, function (start, end, label) {
            });

    });

