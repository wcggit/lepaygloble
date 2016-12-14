/**
 * Created by recoluan on 2016/11/25.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('dailyBillingController', function ($scope, $state, $rootScope, $location,Principal,Auth,$http,HomePage,LejiaBilling) {
        // 门店列表
        HomePage.getMerchantsInfo().then(function(response) {
            var data = response.data;
            $scope.merchants = data;
            $scope.defaultId = data[0].id;
            $scope.findByDate();
        });

        // Echart 报表
        var myChart = echarts.init(document.getElementById('dailyBillingEchart'));
        $scope.item=[]
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
                    data:[]
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
                    data:[]
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
                    data:[]
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
                    data:[]
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
                    data:[]
                }
            ]
        });


        // 根据日期进行查询
        $scope.findByDate = function() {
            // 校验,设置查询条件
           var id = $("#selMerchant").val();
           var date =  $("#timePicker1").val();
           var startDate = date.split('-')[0];
           var dailyOrderCriteria = {};
           var merchant = {};
           //  默认查询条件
           if(id==null||id=='') {
                if($scope.defaultId==null) {
                   alert("请选择门店 ^_^");
                   return;
                }else {
                    merchant.id = $scope.defaultId;
                }
           }else {
                merchant.id = id;
            }
           if(date==null||date=='') {
                alert("请选定日期");
                return;
           }
           dailyOrderCriteria.startDate = startDate;
           dailyOrderCriteria.merchant = merchant;
           // 查询数据
           LejiaBilling.findWeekBillByMerchant(dailyOrderCriteria).then(function (response) {
                var data = response.data;
                var dates = data.dates;
                $scope.dates = dates;
                dates.forEach(function (date,i) {
                    dates[i] = date.substring(5);
                });
                var totalTransfer = data.totalTransfer;
                var wxTransfer = data.wxTransfer;
                var posCardTransfer = data.posCardTransfer;
                var posMobileTransfer = data.posMobileTransfer;
                var scoreTransfer = data.scoreTransfer;
                // 折线图
                myChart.setOption({
                    xAxis: [{
                        data: dates
                    }],
                    series: [
                        {
                            data:totalTransfer
                        },
                        {
                            data:wxTransfer
                        },
                        {
                            data:posCardTransfer
                        },
                        {
                            data:posMobileTransfer
                        },
                        {
                            data:scoreTransfer
                        }
                    ]
                });
                //  表格
                $scope.totalTransfer=totalTransfer;
                $scope.wxTransfer=wxTransfer;
                $scope.posCardTransfer=posCardTransfer;
                $scope.posMobileTransfer=posMobileTransfer;
                $scope.scoreTransfer=scoreTransfer;
                $scope.posScores=data.posScores;
                $scope.offScores=data.offScores;
                $scope.wxMobileTransfer=data.wxMobileTransfer;
                $scope.aliMobileTransfer=data.aliMobileTransfer;
            });
        };

        $('#timePicker1')
            .val(moment().subtract('day', 4).format('YYYY/MM/DD') + ' - ' + moment().format('YYYY/MM/DD'))
            .daterangepicker({
                opens : 'left', //日期选择框的弹出位置
                format : 'YYYY/MM/DD', //控件中from和to 显示的日期格式
                ranges : {
                    '最近7日': [moment().subtract('days', 6), moment()]
                }
            },function(start, end, label) {
                var data = "startDate="
                    + start.format('YYYY/MM/DD')
                    + "&endDate="
                    + end.format('YYYY/MM/DD');

            });
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

