/**
 * Created by recoluan on 2016/11/25.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('codeTradeRecordController', function ($scope, $state, $rootScope, $location,Principal,Auth,$http) {

                    var array = new Array();
                    $http.get("/api/merchantUser/merchantsInfo").success(function (response) {
                    /*$http.get("api/codeTrade/getMerchants").success(function (response) {*/
                        if(response.status == 200){
                            var data = response.data;

                            $scope.myStore = data;

                            angular.forEach(data,function (data,index) {
                                array[index] = data[0];
                            });
                            $scope.loadCodeOrderInfo();
                        }else{
                            alert("加载门店错误...");
                        }
                    });


                    var currentPage = 1;
                    $scope.loadCodeOrderInfo = function (){
                        var codeOrderCriteria = {};
                        var a =$("#selectStore").val();
                        if(a!=null && a!=""){
                            codeOrderCriteria.storeIds = $.makeArray(a);
                        }else{
                            codeOrderCriteria.storeIds = array;
                        }
                        codeOrderCriteria.rebateWay = $("#rebateWay").val();
                        codeOrderCriteria.payWay = $("#payWay").val();
                        codeOrderCriteria.orderSid = $("#orderSid").val();
                        var completeDate = $("#completeDate").val().split("-");
                        codeOrderCriteria.startDate = completeDate[0];
                        codeOrderCriteria.endDate = completeDate[1];
                        codeOrderCriteria.currentPage = currentPage;
                        $http.post("/api/codeTrade/codeOrderByMerchantUser",codeOrderCriteria).success(function (response) {
                            if(response.status == 200){
                                $scope.codeOrderCriteria = response.data.listCodeOrder;
                                $scope.merchantCount = response.data;
                                $scope.totalPages = $scope.merchantCount.totalPages;
                                $scope.page = currentPage;
                                if($scope.codeOrderCriteria.length>0){
                                    $("#notData").hide();
                                }else{
                                    $("#notData").show();
                                }

                            }else{
                                alert('加载扫码订单数据错误...');
                            }
                            console.log(response);
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
                        $scope.loadCodeOrderInfo();
                    };


                    $scope.searchByCriteria = function () {
                        currentPage = 1;
                        $scope.loadCodeOrderInfo();
                    };

    })
