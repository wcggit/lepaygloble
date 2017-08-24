/**
 * Created by recoluan on 2016/11/25.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('yiBaoOrderDetailController', function ($scope, $state, $rootScope, $location, Principal, Auth, $http, HomePage, $stateParams) {
        //  结算
        var ledgerNo = $stateParams.ledgerNo;
        var tradeDate = $stateParams.tradeDate;
        var totalTransfer = $stateParams.totalTransfer;
        var transferState = $stateParams.transferState;
        $scope.totalTransfer = totalTransfer;
        $scope.transferState = transferState;
        //  查询条件
        var currentPage=1;
        var detailCriteria = {};              // 交易记录
        detailCriteria.offset = 1;
        detailCriteria.tradeDate = tradeDate;
        var refundCriteria = {};              // 退款记录
        refundCriteria.offset = 1;
        refundCriteria.tradeDate = tradeDate;
        //  加载门店详情
        loadStoreSettlement();
        function loadStoreSettlement() {
            $http.get('/api/settelement/detail?tradeDate='+tradeDate+'&ledgerNo='+ledgerNo).success(function (response) {
                $scope.mlist = response.data;
                $scope.defaultId = response.data[0][2];
                detailCriteria.merchantId = $scope.defaultId;
                refundCriteria.merchantId = $scope.defaultId;
                loadTradeCount();
                loadTradeList();
            });
        }
        //加载交易记录上方统计
        function loadTradeCount() {
            $http.post('/api/settlement/tradeData', detailCriteria, {
                headers: {
                    'Content-Type': 'application/json'
                }
            }).success(function (response) {
                var data = response.data;
                if(data.totalData!=null&&data.totalData[0]!=null) {
                    $scope.totalData =  data.totalData[0];
                }else {
                    $scope.totalData =  [0,0,0];
                }
                if(data.totalData!=null&&data.lejiaData[0]!=null) {
                    $scope.lejiaData =  data.lejiaData[0];
                }else {
                    $scope.lejiaData =  [0,0,0];
                }
                if(data.totalData!=null&&data.commonData[0]!=null) {
                    $scope.commonData =  data.commonData[0];
                }else {
                    $scope.commonData =  [0,0,0];
                }
            });
        }
        //  加载交易记录
        function loadTradeList() {
            $http.post('/api/settlement/tradeList', detailCriteria, {
                headers: {
                    'Content-Type': 'application/json'
                }
            }).success(function (response) {
                var page = response.data;
                $scope.tradeList = page.content;
                $scope.page = currentPage;
                $scope.totalPages = page.totalPages;
            });
        }
        // 加载退款记录
        function loadRefundList() {
            $http.post('/api/settlement/refundList', refundCriteria, {
                headers: {
                    'Content-Type': 'application/json'
                }
            }).success(function (response) {
                var page = response.data.page;
                $scope.refundList = page.content;
                $scope.page = currentPage;
                $scope.totalPages = page.totalPages;
                $scope.dailyRefundTotal = response.data.dailyTotal;
                $scope.dailyRefundCount = response.data.dailyCount;
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
            detailCriteria.offset = page;
            loadTradeList();
        };

        //  根据条件进行查询
        $scope.searchByCriteria = function () {
            //  门店
            var mid = $("#selMerchant").val();
            if(mid!=-1) {
                detailCriteria.merchantId = $scope.mid;
            }else {
                detailCriteria.merchantId = $scope.defaultId;
            }
            // 订单类型
            var orderType = $("#orderType").val();
            if(orderType!=-1) {
                detailCriteria.orderType = orderType;
            }else {
                detailCriteria.merchant = null;
            }
            // 支付方式
            var payType = $("#payStyle").val();
            if(payType!=-1) {
                detailCriteria.payType = payType;
            }else {
                detailCriteria.payType = null;
            }
            detailCriteria.offset = 1;
            currentPage = 1;
            loadTradeCount();
            loadTradeList();
        }

        $scope.searchRefundByCriteria = function () {
            //  门店
            var mid = $("#selMerchant").val();
            if (mid != -1) {
                detailCriteria.merchantId = $scope.mid;
            } else {
                detailCriteria.merchantId = $scope.defaultId;
            }
            loadRefundList();
        }

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
                    break;
                default:
                    $scope.currentTab1 = true;
                    loadRefundList();
                    break;
            }
        };
    });

