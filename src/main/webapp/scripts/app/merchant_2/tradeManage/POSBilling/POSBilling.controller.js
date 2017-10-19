/**
 * Created by recoluan on 2016/11/16.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('POSBillingController', function ($scope, $state, $rootScope, $location, Principal, Auth, $http) {
        //  加载用户ID
        var settlementCriteria = {};
        var currentPage = 1;
        $http.get('api/merchantUser').success(function (response) {
            $scope.merchantUserId = response.data.id;
            settlementCriteria.merchantUserId = $scope.merchantUserId;
            loadImportSettlement()
        });

        $('#settleDate').daterangepicker({
            opens: 'right', //日期选择框的弹出位置
            format: 'YYYY/MM/DD', //控件中from和to 显示的日期格式
            ranges: {
                '昨日': [moment().subtract('days', 1).startOf('day'), moment().subtract('days', 1).endOf('day')],
                '最近7日': [moment().subtract('days', 6), moment()],
                '最近30日': [moment().subtract('days', 29), moment()]
            },
        }, function (start, end, label) {

        });

        $scope.searchSettlementByCriteria = function () {
            var settleDate = $("#settleDate").val().split("-");
            if (settleDate != null && settleDate.length > 0) {
                var settleDate = $("#settleDate").val().split("-");
                if (settleDate != null && settleDate.length > 0) {
                    settlementCriteria.startDate = settleDate[0];
                    settlementCriteria.endDate = settleDate[1];
                }
            }
            currentPage=1;
            settlementCriteria.offset=1;
            if($scope.currentState==0) {
                loadImportSettlement();
            }else {
                loadSettlement();
            }
        }

        function loadSettlement() {
            $http.post('/api/unionSettlement/findByCriteria', settlementCriteria, {
                headers: {
                    'Content-Type': 'application/json'
                }
            }).success(function (response) {
                var page = response.data;
                $scope.page = currentPage;
                $scope.totalPages = page.totalPages;
                if(page.content.length>0)
                $scope.settlements = page.content;
            });
        }

        function loadImportSettlement() {
            $http.post('/api/unionImportSettlement/findByCriteria', settlementCriteria, {
                headers: {
                    'Content-Type': 'application/json'
                }
            }).success(function (response) {
                var page = response.data;
                $scope.page = currentPage;
                $scope.totalPages = page.totalPages;
                if(page.content.length>0)
                $scope.importSettlements = page.content;
            });
        }

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
            if($scope.currentState==0) {
                loadImportSettlement();
            }else {
                loadSettlement();
            }
        };

        $scope.currentTab0 = true;
        $scope.currentTab1 = false;
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
            }
            switch ($scope.currentState) {
                //  全部状态
                case 0:
                    $scope.currentTab0 = true;
                    loadImportSettlement();
                    break;
                //  待划款
                case 1:
                    $scope.currentTab1 = true;
                    $scope.ttlWarn1 = false;
                    loadSettlement();
                    break;
            }
        };

        $scope.yinshangshowDetail = function (tradeDate, mid) {
            $state.go("yinshangDetail", {tradeDate: tradeDate, mid: mid});
        }
        $scope.gulijinshowDetail = function (tradeDate, mid) {
            $state.go("gulijinDetail", {tradeDate: tradeDate, mid: mid});
        }


    })
