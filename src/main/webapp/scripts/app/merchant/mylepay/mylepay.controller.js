'use strict';
angular.module("lepayglobleApp").controller("MyLePayController",
    function ($q, $scope, MyLePay, $rootScope, $http) {
        $('body').css({background: '#f3f3f3'});
        $('.main-content').css({height: 'auto'});
        $('#timePicker0')
            .val(moment().subtract('day', 4).format('YYYY/MM/DD') + ' - ' + moment().format('YYYY/MM/DD'))
            .daterangepicker({
                opens : 'left', //日期选择框的弹出位置
                format : 'YYYY/MM/DD', //控件中from和to 显示的日期格式
                ranges : {
                    '最近7日': [moment().subtract('days', 6), moment()],
                    '最近30日': [moment().subtract('days', 29), moment()]
                },
            },function(start, end, label) {
                if (start.format('YYYY/MM/DD')
                    == new Date().format('yyyy/MM/dd')) {
                    MyLePay.getTodayOrderDetail().then(function (data) {
                        var datas = data.data;
                        var item = [];
                        var data = [];
                        var day = datas.trueSales;
                        item.push(new Date().format("MM/dd"));
                        data.push(day
                            / 100);
                        $scope.item = [];
                        $scope.data = [];
                        $scope.item = item;
                        $scope.data = data;
                    });
                    return;
                }
                var data = "startDate="
                    + start.format('YYYY/MM/DD')
                    + "&endDate="
                    + end.format('YYYY/MM/DD')
                MyLePay.getDayTrade(data).then(function(result){
                    var financials = result.data;
                    var item = [];
                    var data = [];
                    angular.forEach(financials,
                        function (financial, index,
                                  array) {
                            var date = new Date(financial.balanceDate);
                            item.push(date.format("MM/dd"));
                            data.push(financial.transferPrice
                                / 100);
                        });
                    if(end.format('YYYY/MM/DD')==new Date().format('yyyy/MM/dd')){
                        item.push(new Date().format("MM/dd"));
                        data.push($scope.day.trueSales);
                    }
                    $scope.item = item;
                    $scope.data = data;
                });
            });
        $scope.srgl = {
            firNum: "0",
            secNum: "0",
            thirNum: "0",
            forNum: "0"
        };
        $scope.day = {
            count: "0",
            sales: "0",
            commission: "0",
            trueSales: "0"
        };

        $scope.item = ["1"];
        $scope.data = ["1"];
        MyLePay.getAvaliableCommission().then(function (data) {
            var data = data.data;
            $scope.srgl = {
                firNum:  data.transferingMoney / 100.0,
                secNum:  data.totalTransferMoney / 100.0,
                thirNum: data.availableCommission / 100.0,
                forNum:  data.totalCommission / 100.0
            };
        });
        MyLePay.getTodayOrderDetail().then(function (data) {
            var data = data.data;
            $scope.day = {
                count: data.count,
                sales:      data.sales / 100.0,
                commission: data.commission / 100.0,
                trueSales:  data.trueSales / 100.0
            };
        });

        $scope.tx=function () {
            $("#tx").modal("toggle");
        }

        MyLePay.getMonthOrderDetail().then(function (data) {
            var data = data.data;
            $scope.month = {
                count: data.count,
                sales:      data.sales / 100.0,
                commission: data.commission / 100.0,
                trueSales:  data.trueSales / 100.0
            };
        });

        $scope.currentTab0 = true;
        $scope.currentTab1 = false;
        $scope.onClickTab = function () {
            $scope.currentTab0 = !$scope.currentTab0;
            $scope.currentTab1 = !$scope.currentTab1;
        };

        //$scope.legend = ['日交易收入'];
        $q.all([MyLePay.getDayTrade(),
                MyLePay.getTodayOrderDetail()]).then(function (results) {
            var financials = results[0].data;
            var item = [];
            var data = [];
            var day = results[1].data.trueSales;

            angular.forEach(financials,
                            function (financial, index,
                                      array) {
                                var date = new Date(financial.balanceDate);
                                item.push(date.format("MM/dd"));
                                data.push(financial.transferPrice
                                          / 100);
                            });
            item.push(new Date().format("MM/dd"));
            data.push(day / 100);
            $scope.item = item;
            $scope.data = data;

        });

        $http.get('api/merchant').success(function (response) {
            $scope.payee = response.data.payee;
           var bankNumber = response.data.merchantBank.bankNumber;
            $scope.bank = bankNumber.substring(bankNumber.length - 4, bankNumber.length);
        });

    })
angular.module("lepayglobleApp").directive("eChart", function () {
    function link($scope, element, attrs) {
        // 基于准备好的dom，初始化echarts图表
        var myChart = echarts.init(element[0]);
        //监听options变化
        if (attrs.uiOptions) {
            attrs.$observe("uiOptions", function () {
                myChart = echarts.init(element[0]);
                var options = $scope.$eval(attrs.uiOptions);
                if (angular.isObject(options)) {
                    myChart.setOption(options);
                }
            }, true);
        }
    }

    return {
        restrict: 'A',
        link: link
    };
})

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



