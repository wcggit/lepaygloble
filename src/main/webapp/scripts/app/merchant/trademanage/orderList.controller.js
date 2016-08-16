'use strict';

angular.module('lepayglobleApp')
    .controller('OrderListController',
                function ($scope, $state, $location, Trade, $rootScope, $stateParams) {

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
                    $("#timePicker1").val("");
                    var currentPage = 1;
                    var olOrderCriteria = {};
                    olOrderCriteria.offset = 1;
                    if ($stateParams.date != null && $stateParams.date != "") {
                        var end = new Date($stateParams.date);

                        var start = new Date($stateParams.date);
                        start.setMinutes(0, 0, 0);
                        start.setHours(0);
                        $("#timePicker1").val(start.format("yyyy/MM/dd HH:mm:ss") + " - "
                                              + end.format("yyyy/MM/dd HH:mm:ss"));
                        olOrderCriteria.startDate = start.format("yyyy/MM/dd HH:mm:ss");
                        olOrderCriteria.endDate = end.format("yyyy/MM/dd HH:mm:ss");
                        loadContent();
                    } else {

                        loadContent();
                    }
                    loadStatistic();

                    function loadContent() {
                        Trade.getOrderList(olOrderCriteria).then(function (results) {
                            var page = results.data;

                            $scope.pulls = page.content;
                            $scope.page = currentPage;
                            $scope.totalElements = page.totalElements;
                            $scope.totalPages = page.totalPages;
                        });
                    }

                    function loadStatistic() {
                        Trade.getOrderStatistic(olOrderCriteria).then(function (results) {
                            var data = results.data;
                            $scope.statistic = {
                                sales:      data.sales / 100.0,
                                commission: data.commission / 100.0,
                                trueSales:  data.trueSales / 100.0
                            };
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
                        olOrderCriteria.offset = page;
                        loadContent();
                    };

                    $scope.searchByCriteria = function () {
                        var dateStr = $("#timePicker1").val();
                        if(dateStr != "" && dateStr != null){
                            var startDate = dateStr.split("-")[0].trim();
                            var endDate = dateStr.split("-")[1].trim();
                            olOrderCriteria.startDate = startDate;
                            olOrderCriteria.endDate = endDate;
                        }
                        olOrderCriteria.offset = 1;
                        olOrderCriteria.orderSid = $("#order-num").val();
                        if ($("#rebateWay").val() != null && $("#rebateWay").val() != 0) {
                            olOrderCriteria.rebateWay = $("#rebateWay").val();
                        }else{
                            olOrderCriteria.rebateWay =null;
                        }
                        currentPage = 1;
                        loadContent();
                        loadStatistic();
                    }

                    $scope.exportExcel = function () {
                        var data = "?";
                        if (olOrderCriteria.startDate != null) {
                            data += "startDate=" + olOrderCriteria.startDate + "&";
                            data += "endDate=" + olOrderCriteria.endDate;
                        }
                        if (olOrderCriteria.orderSid != null) {
                            data += "&orderSid=" + olOrderCriteria.orderSid;
                        }
                        location.href = "/api/offLineOrder/export" + data;
                    }

                });

Date.prototype.format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours() % 12 == 0 ? 12 : this.getHours() % 12, //小时
        "H+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    var week = {
        "0": "\u65e5",
        "1": "\u4e00",
        "2": "\u4e8c",
        "3": "\u4e09",
        "4": "\u56db",
        "5": "\u4e94",
        "6": "\u516d"
    };
    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    if (/(E+)/.test(fmt)) {
        fmt =
        fmt.replace(RegExp.$1,
                    ((RegExp.$1.length > 1) ? (RegExp.$1.length > 2 ? "\u661f\u671f" : "\u5468")
                        : "") + week[this.getDay() + ""]);
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(fmt)) {
            fmt =
            fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr((""
                                                                                             + o[k]).length)));
        }
    }
    return fmt;
}
