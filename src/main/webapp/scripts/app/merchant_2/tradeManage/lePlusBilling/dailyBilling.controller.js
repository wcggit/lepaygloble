/**
 * Created by recoluan on 2016/11/25.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('dailyBillingController', function ($scope, $state, $rootScope, $location,Principal,Auth,$http) {
        var myChart = echarts.init(document.getElementById('dailyBillingEchart'));
        $scope.item=['12-01','12-02','12-03','12-04','12-05','12-06']

        // 显示标题，图例和空的坐标轴
        myChart.setOption({
            tooltip: {show: true},
            legend: {
                data: ["入账总金额","扫码牌微信入账","POS刷卡入账","POS移动支付入账","红包支付入账"],
                bottom:'0px'
            },
            xAxis: [{
                nameTextStyle:{
                    color:"#FB991A"
                },
                name: "时间",
                type: "category",
                splitLine:{
                    show:false
                },
                splitArea:{
                    show:false
                },
                axisLine:{
                    lineStyle:{
                        color:["#6b6b6b"],
                        width:"1"
                    }
                },
                data: $scope.item
            }],
            yAxis: [{
                nameTextStyle:{
                    color:"#FB991A"
                },
                type: "value",
                name: "金额",
                splitLine:{
                    show:false
                },
                splitArea:{
                    show:false
                },
                axisLine:{
                    lineStyle:{
                        color:["#6b6b6b"],
                        width:"1"
                    }
                }
            }],
            series: [
                {
                    name: "入账总金额",
                    type: "line",
                    itemStyle:{
                        normal:{
                            color:"#FB991A",
                            lineStyle:{
                                color:"#FB991A",
                                borderColor:"#FB991A"
                            }
                        }
                    },
                    data:[6,2,3,4,5,4]
                },
                {
                    name: "扫码牌微信入账",
                    type: "line",
                    itemStyle:{
                        normal:{
                            color:"#44b549",
                            lineStyle:{
                                color:"#44b549",
                                borderColor:"#44b549"
                            }
                        }
                    },
                    data:[4,3,2,5,6,7]
                },
                {
                    name: "POS刷卡入账",
                    type: "line",
                    itemStyle:{
                        normal:{
                            color:"#9932cd",
                            lineStyle:{
                                color:"#9932cd",
                                borderColor:"#9932cd"
                            }
                        }
                    },
                    data:[4,3,5,6,8,7]
                },
                {
                    name: "POS移动支付入账",
                    type: "line",
                    itemStyle:{
                        normal:{
                            color:"#2ca9e1",
                            lineStyle:{
                                color:"#2ca9e1",
                                borderColor:"#2ca9e1"
                            }
                        }
                    },
                    data:[4,5,9,7,8,2]
                },
                {
                    name: "红包支付入账",
                    type: "line",
                    itemStyle:{
                        normal:{
                            color:"#f54339",
                            lineStyle:{
                                color:"#f54339",
                                borderColor:"#f54339"
                            }
                        }
                    },
                    data:[5,2,10,8,9,3]
                }
            ]
        });

        $scope.clickFun=function () {
            $scope.item=['1','2','3','4','5','6'];
            myChart.setOption({
                xAxis: [{
                    data: $scope.item
                }],
                series: [
                    {
                        data:[4,3,5,6,8,7]
                    },
                    {
                        data:[4,3,2,5,6,7]
                    },
                    {
                        data:[6,2,3,4,5,4]
                    },
                    {
                        data:[4,5,9,7,8,2]
                    },
                    {
                        data:[5,2,10,8,9,3]
                    }
                ]
            });
        }
    })

