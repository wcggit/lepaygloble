/**
 * Created by recoluan on 2016/11/25.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('yiBaoHistoryTradeController', function ($scope, $state, $rootScope, $location, Principal, Auth, $http, HomePage, $stateParams) {
        var currentPage = null;
        var financialCriteria = {};
        financialCriteria.offset = 1;
        currentPage = 1;
        $scope.topDisplay = false;
        // 门店列表
        HomePage.getMerchantsInfo().then(function (response) {
            var data = response.data;
            $scope.merchants = data;
            $scope.defaultId = data[0].id;
            var merchant = {};
            merchant.id = $scope.defaultId;
            financialCriteria.merchant = merchant;
            loadContent();
        });


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

        //  查看详情
        $scope.showDetail = function(tradeDate,mid) {
            $state.go("lePlusOrderDetail", {tradeDate:tradeDate,mid: mid});
        }

        //  展示到账记录
        function loadContent() {
            $http.post('/api/financial', financialCriteria, {
                headers: {
                    'Content-Type': 'application/json'
                }
            }).success(function (response) {
                var page = response.data;
                $scope.pulls = page.content;
                $scope.page = currentPage;
                $scope.totalPages = page.totalPages;
                var dates = [];
                var totalPrice = [];
                var transferPrice = [];
                var posTransfer = [];
                var appTransfer = [];
                for (var i = 0; i < page.content.length; i++) {
                    dates[i] = page.content[i].balanceDate.substring(0, 10);
                    transferPrice[i] = page.content[i].transferPrice / 100.0;
                    posTransfer[i] = page.content[i].posTransfer / 100.0;
                    appTransfer[i] = page.content[i].appTransfer / 100.0;
                    totalPrice[i] = (page.content[i].transferPrice + page.content[i].posTransfer + page.content[i].appTransfer) / 100.0;
                }

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
            financialCriteria.offset = page;
            loadContent();
        };

        $scope.searchByDate = function () {
            //  日期
            var dateStr = $("#timePicker1").val();
            if (dateStr != null && dateStr != "") {
                var startDate = dateStr.split("-")[0];
                var endDate = dateStr.split("-")[1].trim();
                financialCriteria.startDate = startDate;
                financialCriteria.endDate = endDate;
            } else {
                financialCriteria.startDate = null;
                financialCriteria.endDate = null;
            }
            //  门店
            var merchant = {};
            var mid = $("#selMerchant").val();
            if (mid != -1) {
                merchant.id = mid;
                financialCriteria.merchant = merchant;
            } else {
                var deftId = $scope.defaultId;
                $("#selMerchant").val(deftId);
                merchant.id = deftId;
                financialCriteria.merchant = merchant;
            }
            financialCriteria.offset = 1;
            currentPage = 1;
            loadContent();
        }

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
                    financialCriteria.state = null;
                    $scope.searchByDate();
                    break;
                case 1:
                    $scope.currentTab1 = true;
                    $scope.ttlWarn1 = false;
                    financialCriteria.state = 0;
                    $scope.searchByDate();
                    break;
                case 2:
                    $scope.currentTab2 = true;
                    financialCriteria.state = 1;
                    $scope.searchByDate();
                    break;
                default:
                    $scope.currentTab3 = true;
                    $scope.ttlWarn2 = false;
                    financialCriteria.state = 2;
                    $scope.searchByDate();
                    break;
            }
        };


        //  导出表格
        $scope.exportExcel = function() {
            var data = "?";
            if (financialCriteria.startDate != null) {
                data += "startDate=" + financialCriteria.startDate + "&";
                data += "endDate=" + financialCriteria.endDate;
            }
            if (financialCriteria.state != null) {
                data += "&state=" + financialCriteria.state;
            }
            if ($("#selMerchant").val() != null && $("#selMerchant").val()!="") {
                data += "&mid=" + $("#selMerchant").val();
            } else if ($scope.defaultId != null && $scope.defaultId != '') {
                data += "&mid=" + $scope.defaultId;
            }
            location.href = "/api/financial/export" + data;
        }

    });

