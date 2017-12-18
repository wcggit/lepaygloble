/**
 * Created by wanghl on 2017/12/18.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('refundController', function ($scope, $state, $rootScope, $location,Principal,Auth,$http) {
        // 订单类型
        var orderType;

        // 查询订单
        $scope.searchByCriteria = function () {
            var orderId = $("#orderSid").val();
            if(orderId != ''){
                $http.get("/api/channel/refund/" + orderId).success(function (response) {
                    if (response.status == 200) {
                        var data = response.data;
                        console.log()
                    } else {
                        alert("订单号错误...");
                    }
                });
                $(".refundInfo").fadeIn();
            }
        };

        // 确认
        $scope.sub = function () {

        };

        // 取消
        $scope.cancel = function () {
            $(".refundInfo").fadeOut();
        };
    });
