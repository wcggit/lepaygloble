/**
 * Created by recoluan on 2016/11/25.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('minShengOrderDetailController', function ($scope, $state, $rootScope, $location, Principal, Auth, $http, HomePage, $stateParams) {
        //  查询条件
        var currentPage = 1;
        var orderCriteria = {};
        var refundCriteria = {};
        orderCriteria.offset = 1;
        refundCriteria.offset = 1;
        orderCriteria.settleDate = $stateParams.settleDate;
        refundCriteria.tradeDate = $stateParams.settleDate;
        //  账单金额、状态
        $scope.totalActual = $stateParams.totalActual;
        $scope.transState = $stateParams.transState;
        loadMerchantLedger($stateParams.cmbcMerNo);
        loadStoreSettlement($stateParams.orderId);
        // 加载民生商户数,同子商户门店
        function loadMerchantLedger(merNo) {
            $http.get('/api/cmbcMerchant/findByMerNO?merNo=' + merNo).success(function (response) {
                var merchants = response.data;
                $scope.merchants = merchants;
                $scope.defaultId = merchants[0].id;
                $scope.selMerchantName = merchants[0].name;
                loadTradeList(null, 0);
            });
        }

        /*加载顶部交易信息*/
        function loadStoreSettlement(cid) {
            $http.get('/api/cmbcSettlement/settlementInfo/' + cid).success(function (response) {
                $scope.settlement = response.data;
            });
        }

         $scope.searchByCriteria = function() {
            var mid = $("#selMerchant").val();
            if (mid != null && mid != '') {
                orderCriteria.merchantId = mid;
                $scope.selMerchantName= $("#selMerchant").text();
            } else {
                orderCriteria.merchantId = $scope.defaultId;
            }
             $http.post('/api/scanCodeOrder/findByCriteria', orderCriteria, {
                headers: {
                    'Content-Type': 'application/json'
                }
            }).success(function (response) {
                var page = response.data;
                $scope.page = currentPage;
                $scope.totalPages = page.totalPages;
                if ($scope.payType==0) {          // 微信订单
                    if(page.content.length>0)
                        $scope.wxOrderList = page.content;
                } else if($scope.payType==1) {
                    if(page.content.length>0)
                        $scope.aliOrderList = page.content;
                } else {
                    if(page.content.length>0)
                        $scope.ljOrderList = page.content;
                }
            });
        }
        //  加载交易记录
        function loadTradeList(orderType, payType) {
            var mid = $("#selMerchant").val();
            if (orderType!=null&&orderType!='') {
                orderCriteria.orderType = orderType;
                orderCriteria.payType=null;
                $scope.payType = null;
                $scope.orderType = orderType;
            } else {
                orderCriteria.orderType = 0;
                orderCriteria.payType=payType;
                $scope.payType = payType;
                $scope.orderType = 0;
            }
            if (mid != null && mid != '') {
                orderCriteria.merchantId = mid;
            } else {
                orderCriteria.merchantId = $scope.defaultId;
            }
            orderCriteria.gatewayType=2;
            orderCriteria.state=1;
            orderCriteria.offset = 1;
            $http.post('/api/scanCodeOrder/findByCriteria', orderCriteria, {
                headers: {
                    'Content-Type': 'application/json'
                }
            }).success(function (response) {
                var page = response.data;
                $scope.page = currentPage;
                $scope.totalPages = page.totalPages;
                if (payType==0) {          // 微信订单
                    if(page.content.length>0)
                    $scope.wxOrderList = page.content;
                } else if(payType==1) {
                    if(page.content.length>0)
                    $scope.aliOrderList = page.content;
                    // console.log(JSON.stringify($scope.aliOrderList));
                } else {
                    if(page.content.length>0)
                    $scope.ljOrderList = page.content;
                }
            });
        }

        // 加载退款记录
        function loadRefundList(orderType, payType) {
            var mid = $("#selMerchant").val();
            if (mid == null || mid == '') {
                refundCriteria.merchantId = $scope.defaultId;
            } else {
                refundCriteria.merchantId = mid;
            }
            if (orderType!=null&&orderType!='') {
                refundCriteria.orderType = orderType;
                refundCriteria.payType=null;
            } else {
                refundCriteria.orderType = 0;
                refundCriteria.payType=payType;
            }
            $http.post('/api/channel/refundList', refundCriteria, {
                headers: {
                    'Content-Type': 'application/json'
                }
            }).success(function (response) {
                var page = response.data;
                // $scope.page = currentPage;
                // $scope.totalPages = page.totalPages;
                if (payType==0) {          // 微信订单
                    if(page.content.length>0)
                        $scope.wxRefundList = page.content;
                } else if(payType==1) {
                    if(page.content.length>0)
                        $scope.aliRefundList = page.content;
                } else {
                    if(page.content.length>0)
                        $scope.ljRefundList = page.content;
                }
            });
        }

        // alert($stateParams.cmbcMerNo+"-"+$stateParams.settleDate+"-"+);
        $scope.currentTopTapState = 0;
        $scope.currentState = 0;
        $scope.topTabClick = function (index) {
            $scope.currentTopTapState = index;
            $scope.currentState = 0;
            //  加载订单数据
            if(index==0) {
                loadTradeList(null, 0);
            }else if(index==1) {
                loadTradeList(null, 1);
            }else {
                loadTradeList(1,null);
            }
        };
        $scope.onClickTab = function (index) {
            $scope.currentState = index;
            if(index==1) {      // 退款记录
                loadRefundList($scope.orderType,$scope.payType);
            }/*else {           // 订单记录
                loadTradeList($scope.orderType,$scope.payType);
            }*/
        };

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
            orderCriteria.offset = page;
            $scope.searchByCriteria();
        };

        // 导出表格
        $scope.exportExcel = function () {
            var data = "?";
            var mid = $("#selMerchant").val();
            if (mid!=null && mid!='') {
                data += "merchantId=" + mid;
            } else if ($scope.defaultId != null && $scope.defaultId != '') {
                data += "merchantId=" + $scope.defaultId;
            }
            if (orderCriteria.settleDate != null) {
                data += "&settleDate=" + orderCriteria.settleDate;
            }
            if (orderCriteria.orderType != null) {
                data += "&orderType=" + orderCriteria.orderType;
            }
            if (orderCriteria.payType!=null) {
                data += "&payType=" + orderCriteria.payType;
            }
            data += "&state=" + 1;
            data += "&gatewayType=" + 2;
            location.href = "/api/scanCodeOrder/export" + data;
        }
    });

