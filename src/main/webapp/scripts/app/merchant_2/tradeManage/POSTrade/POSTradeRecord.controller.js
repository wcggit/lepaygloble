/**
 * Created by recoluan on 2016/11/25.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('POSTradeRecordController', function ($scope, $state, $rootScope, $location,Principal,Auth,$http) {
        $http.get("/api/merchantUser/findByMerchantPosUser").success(function (response) {
            if(response.status == 200){
                var data = response.data;
                $scope.merchantPosIds = data;
                console.log(data);
            }
        });
        var array = new Array();
        $http.get("/api/merchantUser/merchantsInfo").success(function (response) {
            if(response.status == 200){
                var data = response.data;
                $scope.myStore = data;
                angular.forEach(data,function (data,index) {
                    array[index] = data[0];
                });
                $scope.loadPosOrderInfo();
            }else{
                alert("加载门店错误...");
            }
        });
        var currentPage = 1;
        $scope.loadPosOrderInfo = function (){
            var posOrderCriteria = {};
            var a =$("#selectStore").val();
            if(a!=""){
                posOrderCriteria.storeIds = $.makeArray(a);
            }else{
                posOrderCriteria.storeIds = array;
            }
            posOrderCriteria.rebateWay = $("#rebateWay").val();
            posOrderCriteria.tradeFlag = $("#tradeFlag").val();
            posOrderCriteria.merchantPosId = $("#merchantPosId").val();
            posOrderCriteria.orderSid = $("#orderSid").val();
            var completeDate = $("#completeDate").val().split("-");
            posOrderCriteria.startDate = completeDate[0];
            posOrderCriteria.endDate = completeDate[1];
            posOrderCriteria.currentPage = currentPage;
            $http.post("/api/merchantUser/posOrderByMerchantUser",posOrderCriteria).success(function (response) {
                if(response.status == 200){
                    $scope.posOrderCriteria = response.data.page.content;
                    $scope.merchantPosPage = response.data.page;
                    $scope.totalPages = $scope.merchantPosPage.totalPages;
                    $scope.page = currentPage;
                    $scope.merchantCount = response.data;
                    if($scope.posOrderCriteria.length>0){
                        $("#notData").hide();
                    }else{
                        $("#notData").show();
                    }

                }else{
                    alert('加载pos订单数据错误...');
                }
               console.log(response);
            });
        };
        $('#completeDate')
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
                }
            }, function (start, end, label) {
                console.log(start+"--------"+end);
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
            $scope.loadPosOrderInfo();
        };

        $scope.searchByCriteria = function () {
            currentPage = 1;
            $scope.loadPosOrderInfo();
        };

    })
