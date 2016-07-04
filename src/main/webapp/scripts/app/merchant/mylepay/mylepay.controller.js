'use strict';
angular.module("lepayglobleApp").controller("MyLePayController", function ($scope, MyLePay) {
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

})

angular.module("lepayglobleApp").controller('lineCtrl', function ($scope, MyLePay, $q, $rootScope) {
    $scope.legend = ['日交易收入'];
    $scope.item = ["11"];
    $scope.data = ["11"];
    $q.all([MyLePay.getDayTrade(), MyLePay.getTodayOrderDetail()]).then(function (results) {
        var financials = results[0].data;
        var day = results[1].data.trueSales;
        angular.forEach(financials, function (financial, index, array) {
            $scope.item.push(financial.balanceDate.toString());
            $scope.data.push(financial.transferPrice);
        });
        $scope.item.push(new Date().toString());
        $scope.data.push(day);

    });

});

angular.module("lepayglobleApp").directive('line', function () {
    return {
        scope: {
            id: "@",
            legend: "=",
            item: "=",
            data: "="
        },
        restrict: 'E',
        template: '<div style="height:400px;"></div>',
        replace: true,
        link: function ($scope, $rootScope, attrs, controller) {
            var option = {
                // 提示框，鼠标悬浮交互时的信息提示
                tooltip: {
                    show: true,
                    trigger: 'item'
                },
                // 图例
                legend: {
                    data: $scope.legend,
                    color: "#FB991A"
                },

                // 横轴坐标轴
                xAxis: {
                    nameTextStyle: {
                        color: "#FB991A"
                    },
                    type: 'category',
                    name: '时间',
                    data: $scope.item,
                    splitLine: {
                        show: false
                    },
                    splitArea: {
                        show: false
                    },
                    axisLine: {
                        lineStyle: {
                            color: ["#6b6b6b"],
                            width: '1'
                        }
                    }
                },
                // 纵轴坐标轴
                yAxis: {
                    nameTextStyle: {
                        color: "#FB991A"
                    },
                    type: 'value',
                    name: '金额',
                    splitLine: {
                        show: false
                    },
                    splitArea: {
                        show: false
                    },
                    axisLine: {
                        lineStyle: {
                            color: ["#6b6b6b"],
                            width: '1'
                        }
                    }
                },
                // 网格
                grid: {
                    borderColor: "#fff"
                },
                // 数据内容数组
                // series: function(){
                //     var serie=[];
                //     for(var i=0;i<$scope.legend.length;i++){
                //         var item = {
                //             name : $scope.legend[i],
                //             type: 'line',
                //             data: $scope.data[i]
                //         };
                //         serie.push(item);
                //     }
                //     return serie;
                // }()

                // 数据
                series: [{
                             name: '日交易收入',
                             type: 'line',
                             itemStyle: {
                                 normal: {
                                     color: "#FB991A",
                                     lineStyle: {
                                         color: "#FB991A",
                                         borderColor: "#FB991A"
                                     }
                                 }
                             },
                             data: $scope.data
                         }]
            };
            var myChart = echarts.init(document.getElementById($scope.id), 'macarons');
            myChart.setOption(option);
        }
    };
});
