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
        getMerchantPayWay();
        // 门店列表和支付方式
        function getMerchantPayWay() {
            $http.get('api/merchantUser/merchantsAndPayWay').success(function (response) {
                var data = response.data;
                $scope.payway = data;
                $scope.merchants = data.merchants;
                if($stateParams.mid!=null && $stateParams.mid!="") {
                    $scope.defaultId = $stateParams.mid;
                }else {
                    $scope.defaultId = data.merchants[0][0];
                }
                settlementCriteria.merchantId = $scope.defaultId;
                var payWay = data["merchant-"+$scope.defaultId];    // 根据支付通道选择页面
                if(payWay==4) {                 // 易宝
                    loadContent();
                    loadMerchantLedger($scope.defaultId);
                }else {                         // 乐加
                    $state.go("lePlusCodeTrade", {mid: $scope.defaultId});
                }
            });
        }

        // 加载民生商户数,同子商户门店
        function loadMerchantLedger(mid) {
            $http.get('/api/cmbcMerchant/findByMerchant?mid='+mid).success(function (response) {
                var map = response.data;
                $scope.ledgerCount = map.ledgerCount;
                $scope.ledgerNames = map.ledgerNames;
            });
        }

        //  加载通道结算单
        function loadContent() {
            $http.post('/api/cmbcSettlement/findByPage', settlementCriteria, {
                headers: {
                    'Content-Type': 'application/json'
                }
            }).success(function (response) {
                console.log(JSON.stringify(response));
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
                $scope.defaultId = mid;
                merchant.id = mid;
                settlementCriteria.merchant = merchant;
            }else {
                var deftId = $scope.defaultId;
                $("#selMerchant").val(deftId);
                merchant.id = deftId;
                settlementCriteria.merchant = merchant;
            }
            settlementCriteria.offset = 1;
            currentPage = 1;
            var payWay = $scope.payway["merchant-"+$scope.defaultId];    // 根据支付通道选择页面
            if(payWay==4) {                 // 易宝
                loadContent();
            }else if(payWay==3) {                                           // 易宝
                $state.go("yiBaoCodeTrade", {mid: $scope.defaultId});
            }else {
                $state.go("lePlusCodeTrade", {mid: $scope.defaultId});      // 乐加
            }
        }
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
            if(index!=0) {
                settlementCriteria.state=index-1;
            }else {
                settlementCriteria.state=null;
            }
            loadContent();
        };

    });

