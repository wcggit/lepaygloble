/**
 * Created by recoluan on 2016/11/25.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('dailyBillingController', function ($scope, $state, $rootScope, $location, Principal, Auth, $http, HomePage, LejiaBilling) {
        var currentPage = null;
        var financialCriteria = {};
        financialCriteria.offset = 1;
        currentPage = 1;

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
        //  Echart 报表
        // 显示标题，图例和空的坐标轴
        // var myChart = echarts.init(document.getElementById('dailyBillingEchart'));


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
                    dates[i] = page.content[i].balanceDate.substring(0,10);
                    transferPrice[i] = page.content[i].transferPrice / 100.0;
                    posTransfer[i] = page.content[i].posTransfer / 100.0;
                    appTransfer[i] = page.content[i].appTransfer / 100.0;
                    totalPrice[i] = (page.content[i].transferPrice + page.content[i].posTransfer + page.content[i].appTransfer) / 100.0;
                }
                // console.log(JSON.stringify(transferPrice));
                // console.log(JSON.stringify(totalPrice));
                // console.log(JSON.stringify(posTransfer));
                // console.log(JSON.stringify(appTransfer));
                // console.log(JSON.stringify(dates));
               /* $scope.dates = dates;
                $scope.totalPrice = totalPrice;
                $scope.transferPrice = transferPrice;
                $scope.posTransfer = posTransfer;
                $scope.appTransfer = appTransfer;*/
                // myChart.setOption({
                //     tooltip: {show: true},
                //     legend: {
                //         data: ["入账总金额", "扫码牌微信入账", "POS支付入账", "红包支付入账"],
                //         bottom: '0px'
                //     },
                //     xAxis: [{
                //         nameTextStyle: {
                //             color: "#FB991A"
                //         },
                //         name: "时间",
                //         type: "category",
                //         splitLine: {
                //             show: false
                //         },
                //         splitArea: {
                //             show: false
                //         },
                //         axisLine: {
                //             lineStyle: {
                //                 color: ["#6b6b6b"],
                //                 width: "1"
                //             }
                //         },
                //         data: dates
                //     }],
                //     yAxis: [{
                //         nameTextStyle: {
                //             color: "#FB991A"
                //         },
                //         type: "value",
                //         name: "金额",
                //         splitLine: {
                //             show: false
                //         },
                //         splitArea: {
                //             show: false
                //         },
                //         axisLine: {
                //             lineStyle: {
                //                 color: ["#6b6b6b"],
                //                 width: "1"
                //             }
                //         }
                //     }],
                //     series: [
                //         {
                //             name: "入账总金额",
                //             type: "line",
                //             itemStyle: {
                //                 normal: {
                //                     color: "#44b549",
                //                     lineStyle: {
                //                         color: "#44b549",
                //                         borderColor: "#44b549"
                //                     }
                //                 }
                //             },
                //             data: totalPrice
                //         },
                //         {
                //             name: "扫码牌微信入账",
                //             type: "line",
                //             itemStyle: {
                //                 normal: {
                //                     color: "#FB991A",
                //                     lineStyle: {
                //                         color: "#FB991A",
                //                         borderColor: "#FB991A"
                //                     }
                //                 }
                //             },
                //             data: transferPrice
                //         },
                //         {
                //             name: "POS支付入账",
                //             type: "line",
                //             itemStyle: {
                //                 normal: {
                //                     color: "#9932cd",
                //                     lineStyle: {
                //                         color: "#9932cd",
                //                         borderColor: "#9932cd"
                //                     }
                //                 }
                //             },
                //             data: posTransfer
                //         },
                //         {
                //             name: "红包支付入账",
                //             type: "line",
                //             itemStyle: {
                //                 normal: {
                //                     color: "#f54339",
                //                     lineStyle: {
                //                         color: "#f54339",
                //                         borderColor: "#f54339"
                //                     }
                //                 }
                //             },
                //             data: appTransfer
                //         }
                //     ]
                // });
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
            //  状态
            var state = $("#financialState").val();
            if (state != -1 && state != null) {
                financialCriteria.state = state;
            } else {
                financialCriteria.state = null;
            }
            //  门店
            var merchant = {};
            var mid = $("#selMerchant").val();
            if(mid!=-1) {
                merchant.id = mid;
                financialCriteria.merchant = merchant;
            }else {
                var deftId = $scope.defaultId;
                $("#selMerchant").val(deftId);
                merchant.id = deftId;
                financialCriteria.merchant = merchant;
            }
            financialCriteria.offset = 1;
            currentPage = 1;
            loadContent();
        }
    });

