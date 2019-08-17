/**
 * Created by recoluan on 2016/11/25.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('yinshangDetailController', function ($scope, $state, $rootScope, $location, Principal, Auth, $http, Trade, $stateParams) {
        var importOrderCriteria = {};
        var currentPage = 1;
        importOrderCriteria.settleDate = $stateParams.settleDate;
        importOrderCriteria.merNum = $stateParams.merNum;
        $scope.currentTab0 = true;
        $scope.currentTab1 = false;
        $scope.priviousState = 0;
        $scope.currentState = 0;
        loadContent();
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
                //  银行卡
                case 0:
                    $scope.currentTab0 = true;
                    importOrderCriteria.payWay = 0;
                    loadContent();
                    break;
                //  微信支付宝
                case 1:
                    $scope.currentTab1 = true;
                    $scope.ttlWarn1 = false;
                    importOrderCriteria.payWay = 1;
                    loadContent();
                    break;
            }
        };


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
            loadContent();
        };

        /*$scope.findByCriteria = function () {
            // if()
        }*/

        function loadContent() {
            $http.post('/api/unionImportOrder/findByCriteria', importOrderCriteria, {
                headers: {
                    'Content-Type': 'application/json'
                }
            }).success(function (response) {
                var page = response.data;
                $scope.page = currentPage;
                $scope.totalPages = page.totalPages;
                if(page.content.length>0){
                    if($scope.currentState==0) {
                        var bankOrders = page.content;
                        var bankOrderDatas = new Array();
                        for(var i=0;i<bankOrders.length;i++) {
                            bankOrderDatas.push(JSON.parse(bankOrders[i].data));
                        }
                        $scope.bankOrders = bankOrders;
                        $scope.bankOrderDatas = bankOrderDatas;
                    }else {
                        var importOrders = page.content;
                        var importOrderDatas = new Array();
                        for(var i=0;i<importOrders.length;i++) {
                            importOrderDatas.push(JSON.parse(importOrders[i].data));
                        }
                        $scope.importOrders = importOrders;
                        $scope.importOrderDatas = importOrderDatas;
                    }
                }
            });
        }
    });


