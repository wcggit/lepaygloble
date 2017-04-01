/**
 * Created by recoluan on 2016/11/25.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('imDataOverviewController', function ($scope, $state, $rootScope, $location, Auth, $http, $stateParams) {
        // 时间选择器
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
            console.log(start + "--------" + end);
        });


        var partnerManagerCriteria = {};
        var partner = {};
        partner.partnerSid = $stateParams.partnerSid;
        $scope.chartData={};
        var myChart = echarts.init(document.getElementById('sjqsEchart'));
        loadContent();
        function loadContent() {
            partnerManagerCriteria.partner = partner;
            // console.log(JSON.stringify(partnerManagerCriteria));
            $http.post('api/partnerManager/chart/partner', partnerManagerCriteria, {
                headers: {
                    'Content-Type': 'application/json'
                }
            }).success(function (response) {
                // console.log(JSON.stringify(response));
                $scope.dailyCommission = response.data.dayCommission;
                $scope.bindUsers = response.data.bindUsers;
                $scope.bindMerchants = response.data.bindMerchants;
                $scope.userLimit = response.data.userLimit;
                $scope.merchantLimit = response.data.merchantLimit;
                $scope.wallet = response.data.partnerWallet;

                $scope.dates = response.data.dates;
                $scope.userNumbers = response.data.userNumbers;
                $scope.merchantNumbers = response.data.merchantNumbers;
                $scope.totalCommission = response.data.totalCommission;

                var dates = response.data.dates;
                var merchantNumbers = response.data.merchantNumbers;
                var totalCommission = response.data.totalCommission;
                var userNumbers = response.data.userNumbers;
                $scope.chartData.title = ['锁定会员', '锁定门店', '佣金收入'];
                $scope.chartData.xAxisData = [dates, dates, dates];
                $scope.chartData.seriesData = [merchantNumbers, userNumbers, totalCommission];
                myChart.setOption({
                    title: {
                        text: $scope.chartData.title[0],
                        left: 'center'
                    },
                    tooltip: {
                        show: true,
                        trigger: 'axis'
                    },
                    xAxis: [{
                        nameTextStyle: {
                            color: "#FB991A"
                        },
                        name: "时间",
                        type: "category",
                        splitLine: {
                            show: false
                        },
                        splitArea: {
                            show: false
                        },
                        axisLine: {
                            lineStyle: {
                                color: ["#6b6b6b"],
                                width: "1"
                            }
                        },
                        data: $scope.chartData.xAxisData[0]
                    }],
                    yAxis: [{
                        nameTextStyle: {
                            color: "#FB991A"
                        },
                        type: "value",
                        name: "金额",
                        splitLine: {
                            show: false
                        },
                        splitArea: {
                            show: false
                        },
                        axisLine: {
                            lineStyle: {
                                color: ["#6b6b6b"],
                                width: "1"
                            }
                        }
                    }],
                    series: [
                        {
                            name: "入账总金额",
                            type: "line",
                            itemStyle: {
                                normal: {
                                    color: "#FB991A",
                                    lineStyle: {
                                        color: "#FB991A",
                                        borderColor: "#FB991A"
                                    }
                                }
                            },
                            data: $scope.chartData.seriesData[0]
                        }
                    ]
                });

            });
        }

        $scope.searchByCriteria = function () {
            if ($("#completeDate").val() != null && $("#completeDate").val() != '') {
                var completeDate = $("#completeDate").val().split("-");
                partnerManagerCriteria.startDate = completeDate[1];
            } else {
                partnerManagerCriteria.startDate = null;
            }
            loadContent();
        }

        // 折线图
        $('.nav-pills li').on('click', function () {
            $('.nav-pills li').removeClass('active');
            $(this).addClass('active');
            var index = $(this).index();
            myChart.setOption({
                title: {
                    text: $scope.chartData.title[index]
                },
                xAxis: [{
                    data: $scope.chartData.xAxisData[index]
                }],
                series: [
                    {
                        data: $scope.chartData.seriesData[index]
                    }
                ]
            })
        })

    });
