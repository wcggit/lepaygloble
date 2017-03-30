/**
 * Created by recoluan on 2016/11/16.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('cp-homePageController', function ($scope, $state, $rootScope, $location, Principal, Auth, $http) {
        $("[data-toggle='tooltip']").tooltip();
        // 获取基本信息
        $http.get('api/partnerManager/wallet').success(function (response) {
            $scope.partner = response.data.partner;
            $scope.partnerManager = response.data.partnerManager;
            $scope.partnerManagerWallet = response.data.partnerManagerWallet;
        });
        $http.get('api/partnerManager/commission').success(function (response) {
           $scope.dailyCommission = response.data;
        });

        // 圆形图
        $http.get('api/partnerManager/members').success(function (response) {
            console.log(JSON.stringify(response));
            // 圆形滚动条
            $scope.clockData=[
                {
                    initNum:response.data.bindUserNumber,
                    maxNum:response.data.userLimit
                },
                {
                    initNum:response.data.bindMerchantNumber,
                    maxNum:response.data.merchantLimit
                },
                {
                    initNum:response.data.bindPartnerNumber,
                    maxNum:response.data.partnerLimit
                }
            ]
            showHours("my-member", $scope.clockData[0].initNum, $scope.clockData[0].maxNum/2);
            showHours("my-store", $scope.clockData[1].initNum, $scope.clockData[1].maxNum/2);
            showHours("my-partner", $scope.clockData[2].initNum, $scope.clockData[2].maxNum/2);
        });


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
            console.log(start+"--------"+end);
        });

        //强制保留两位小数
        $scope.toDecimal = function (x) {
            var f = parseFloat(x);
            if (isNaN(f)) {
                return false;
            }
            var f = Math.round(x * 100) / 100;
            var s = f.toString();
            var rs = s.indexOf('.');
            if (rs < 0) {
                rs = s.length;
                s += '.';
            }
            while (s.length <= rs + 2) {
                s += '0';
            }
            return s;
        }



        function showHours(oClass, num, n) {
            var aEle = getByClass(oClass)[0].children,
                Rdeg = num > n ? n : num,
                Ldeg = num > n ? num - n : 0;
            aEle[2].innerHTML = "<span>" + num + "</span>";

            aEle[1].children[0].style.transform = "rotateZ(" + (360 / (2 * n) * Rdeg - 180) + "deg)";
            aEle[0].children[0].style.transform = "rotateZ(" + (360 / (2 * n) * Ldeg - 180) + "deg)";
        }

        function getByClass(oClass) {
            return document.getElementsByClassName(oClass);
        }

        // 折线图
        var partnerManagerCriteria = {};
        partnerManagerCriteria.type=1;
        // loadContent();
        function loadContent() {
            var dateStr = $("#completeDate").val();
            if(dateStr!=null&&dateStr!='') {
                var startDate = dateStr.split("-")[0].trim();
                var endDate = dateStr.split("-")[1].trim();
                partnerManagerCriteria.startDate = startDate;
                partnerManagerCriteria.endDate = endDate;
            }
            $http.post('/api/partnerManager/members', partnerManagerCriteria, {
                headers: {
                    'Content-Type': 'application/json'
                }
            }).success(function (response) {
                console.log(JSON.stringify(response));
            });
        }
        $scope.chartData={
            title: ['锁定会员', '锁定门店', '佣金收入'],
            xAxisData: [
                [1,2,3,4,5,6,8,0],
                [1,2,3,4,5,6,8,0],
                [1,2,3,4,5,6,8,0],
            ],
            seriesData: [
                [6,7,8,2,4,6,7,7]
                    [2,7,8,2,4,5,7,7],
                [6,7,5,2,4,5,7,8]
            ]
        }

        var myChart = echarts.init(document.getElementById('sjqsEchart'));
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
                data: $scope.chartData.xAxisData[0]
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
                    data:$scope.chartData.seriesData[0]
                }
            ]
        });
        $('.nav-pills li').on('click',function () {
            $('.nav-pills li').removeClass('active');
            $(this).addClass('active');
            var index = $(this).index();
            myChart.setOption({
                title: {
                    text: $scope.chartData.title[index],
                },
                xAxis: [{
                    data: $scope.chartData.xAxisData[index]
                }],
                series: [
                    {
                        data:$scope.chartData.seriesData[index]
                    }
                ]
            })
        })
    });
