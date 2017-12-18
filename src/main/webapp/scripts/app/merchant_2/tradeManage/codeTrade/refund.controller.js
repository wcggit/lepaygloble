/**
 * Created by wanghl on 2017/12/18.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('refundController', function ($scope, $state, $rootScope, $location,Principal,Auth,$http) {
        // 订单类型,订单号
        var orderType,ordersId;

        // 查询订单
        $scope.searchByCriteria = function () {
            var orderId = $("#orderSid").val();
            if(orderId != ''){
                $http.get("/api/channel/refund/" + orderId).success(function (response) {
                    // console.log(response);
                    if (response.status == 200) {
                        console.log(response);
                        orderType = response.orderFrom;
                        ordersId = response.orderSid;
                        $scope.orderSid = response.orderSid;            //订单编号
                        $scope.merchantName = response.merchantName;    //交易门店
                        $scope.userInfo = response.userInfo;            //消费者
                        $scope.totalPrice = response.totalPrice/100.0;  //订单金额（1:100）
                        $scope.truePay = response.truePay/100.0;        //实际支付（1:100）
                        $scope.trueScore = response.trueScore/100.0;    //鼓励金（1:100）
                        $scope.backScoreC = response.backScoreC/100.0;  //返金币 （1:100）
                        $scope.backScoreA = response.backScoreA/100.0;  //返鼓励金 （1:100）

                        $(".refundInfo").fadeIn();
                    } else {
                        alert(data.msg);
                    }
                });
            }
        };

        // 确认
        $scope.sub = function () {
            $http.get("/api/refund/request?orderSid=" + ordersId + "&orderFrom=" + orderType).success(function (response) {
                if (response.status == 200) {
                    console.log(response);
                    alert("确认成功！");
                } else {
                    alert(data.msg);
                }
            });
        };

        // 取消
        $scope.cancel = function () {
            $(".refundInfo").fadeOut();
        };
    });
