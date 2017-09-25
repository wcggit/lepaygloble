/**
 * Created by recoluan on 2016/11/25.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('codeTradeRecordController', function ($scope, $state, $rootScope, $location, Principal, Auth, $http) {

        var array = new Array();
        $http.get("/api/merchantUser/merchantsInfo").success(function (response) {
            if (response.status == 200) {
                var data = response.data;
                $scope.myStore = data;
                $scope.defaultId = data[0][0];
                angular.forEach(data, function (data, index) {
                    array[index] = data[0];
                });
                $scope.loadCodeOrderInfo();
                $scope.loadStatistic();
            } else {
                alert("加载门店错误...");
            }
        });


        var currentPage = 1;
        var codeOrderCriteria = {};
        codeOrderCriteria.offset = 1;
        $scope.loadCodeOrderInfo = function () {
            setCriteria();
            $http.post("/api/codeTrade/codeOrderByCriteria", codeOrderCriteria).success(function (response) {
                if (response.status == 200) {
                    var data = response.data;
                    var page = data.page;
                    $scope.payWay = data.payWay;
                    $scope.orderList = page.content;
                    $scope.page = currentPage;
                    $scope.totalPages = page.totalPages;
                } else {
                    alert('加载扫码订单数据错误...');
                }
            });
        };


        $('#completeDate').daterangepicker({
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
            }
        }, function (start, end, label) {
        });


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
            codeOrderCriteria.offset = page;
            $scope.loadCodeOrderInfo();
        };


        $scope.searchByCriteria = function () {
            currentPage = 1;
            $scope.loadCodeOrderInfo();
            $scope.loadStatistic();
        };

        // 加载数据
        $scope.loadStatistic = function () {
            setCriteria();
            $http.post("/api/codeTrade/codeOrderStatistic", codeOrderCriteria).success(function (response) {
                if (response.status == 200) {
                    var data = response.data;
                    var totalData = data.totalData;
                    var lejiaData = data.lejiaData;
                    var commonData = data.commonData;
                    $scope.totalData = data.totalData;
                    $scope.lejiaData = data.lejiaData;
                    $scope.commonData = data.commonData;
                } else {
                    alert('加载扫码订单数据错误...');
                }
            });
        }

        // 设置查询条件
        function setCriteria() {
            var mid = $("#selectStore").val();
            var merchant = {};
            if (mid != null && mid != "") {
                merchant.id = mid;
                codeOrderCriteria.merchant = merchant;
            } else {
                merchant.id = $scope.defaultId;
                codeOrderCriteria.merchant = merchant;
            }
            var payWay = $("#payWay").val();
            var orderType = $("#orderType").val();
            var orderSid = $("#orderSid").val();
            var state = $("#orderState").val();
            if (payWay != null && payWay != '') {
                codeOrderCriteria.payWay = payWay;
            } else {
                codeOrderCriteria.payWay = null;
            }
            if (orderType != null && orderType != '') {
                codeOrderCriteria.orderType = orderType;
            } else {
                codeOrderCriteria.orderType = null;
            }
            if (orderSid != null && orderSid != '') {
                codeOrderCriteria.orderSid = orderSid;
            } else {
                codeOrderCriteria.orderSid = null;
            }
            if (state != null && state != '') {
                codeOrderCriteria.state = state;
            } else {
                codeOrderCriteria.state = null;
            }
            if ($("#completeDate").val() != null && $("#completeDate").val() != '') {
                var completeDate = $("#completeDate").val().split("-");
                codeOrderCriteria.startDate = completeDate[0];
                codeOrderCriteria.endDate = completeDate[1];
            } else {
                codeOrderCriteria.startDate = null;
                codeOrderCriteria.endDate = null;
            }
            codeOrderCriteria.offset = currentPage;
        }

        // 导出表格
        $scope.exportExcel = function () {
            setCriteria();
            var data = "?";
            if (codeOrderCriteria.startDate != null) {
                data += "startDate=" + codeOrderCriteria.startDate + "&";
                data += "endDate=" + codeOrderCriteria.endDate;
            }
            if (codeOrderCriteria.orderSid != null) {
                data += "&orderSid=" + codeOrderCriteria.orderSid;
            }
            if (codeOrderCriteria.orderType != null) {
                data += "&orderType=" + codeOrderCriteria.orderType;
            }
            if (codeOrderCriteria.payWay != null) {
                data += "&payWay=" + codeOrderCriteria.payWay;
            }
            if (codeOrderCriteria.state != null) {
                data += "&state=" + codeOrderCriteria.state;
            }
            if ($("#selectStore").val() != null && $("#selectStore").val() != '') {
                data += "&merchantId=" + $("#selectStore").val();
            } else if ($scope.defaultId != null && $scope.defaultId != '') {
                data += "&merchantId=" + $scope.defaultId;
            }
            location.href = "/api/codeTradeList/export" + data;
        }

        $scope.historyTradeRecord = function () {
            $state.go("historyTradeRecord");
        }

    })
