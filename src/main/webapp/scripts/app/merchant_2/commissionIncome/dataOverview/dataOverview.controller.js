/**
 * Created by recoluan on 2016/11/16.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('dataOverviewController', function ($scope, $state, $rootScope, $location, Principal, Auth, $http, HomePage) {
        //  数据概览
        HomePage.getCommissionByMerchantUser().then(function (response) {
            var data = response.data;
            $scope.available = data.available * 0.01;
            $scope.totalCommission = data.totalCommission * 0.01;
        });
        $http.get("/api/dataOverview/findCommissionAndLockNumber").success(function (response) {
            if(response.status==200){
                $scope.data = response.data;
                dataOverviewCriteria.offset = 0;
                dataOverviewCriteria.merchantIds = $scope.data.merchantIds;
                $scope.findPage();
            }else{
                alert('数据加载错误...');
            }
        })
        var dataOverviewCriteria = {};
        $scope.merchants = [];
        $scope.findPage = function(){
            $http.post("/api/dataOverview/findPageMerchantMemberLockNumber",dataOverviewCriteria).success(function (response) {
                if(response.status==200){
                    if(response.data.merchants!=null && response.data.merchants.length>0){
                        var totaUserLockLimit = 0;
                        var userLockLimit = 0;
                        $scope.merchants = response.data.merchants;
                        angular.forEach(response.data.merchants,function(data){
                            totaUserLockLimit+=data[2]
                            userLockLimit+=data[1]
                        });
                        $scope.userTotalLockLimit = totaUserLockLimit;
                        $scope.merchantUserLockLimit = userLockLimit;
                        dataOverviewCriteria.merchants = $scope.merchants;
                        $scope.availableCommissions = response.data.availableCommissions;
                        $scope.totalCommissions =  response.data.totalCommissions;
                        dataOverviewCriteria.offset = dataOverviewCriteria.offset+1;
                    }
                    if(response.data.merchants!=null && response.data.merchants.length<6){
                        $("#seeMore").hide();
                    }else{
                        $("#seeMore").show();
                    }
                }else{
                    alert('数据加载错误...');
                }
            })
        }

        $scope.moreMerchant = function () {
            $scope.findPage();
        }
        //  提现窗口展示
        $scope.txShow = function () {
            $("#tx").modal();
        };
        $scope.txHide = function () {
            $("#tx").modal("toggle");
            $("#tx-success").modal("toggle");
            $scope.refresh();
        };
        $scope.txSuc = function () {
            $("#tx-success").modal();
        }
        $scope.refresh = function () {
            window.location.reload();
        }
        $scope.withDrawWindow = function (mid) {
            $http.get('/withdraw/merchant_with_info/findById/'+mid).success(function (response) {
                 var data = response.data;
                 $scope.merchantName = data.merchantName;
                 $scope.bankNumb = data.bankNumb;
                 $scope.bankName = data.bankName;
                 $scope.avaiWith = data.avaiWith;
                 $scope.avaiWithId = mid;
                 console.log(JSON.stringify(data));
                 $scope.txShow();
             });
        }
        //  提现
        $scope.withDraw = function () {
            var amount = $("#withDrawInput").val();
            var available = $scope.avaiWith;
            if (amount > available) {
                alert("余额不足！");
                $("#withDrawInput").val('');
                return;
            } else if (amount < 200) {
                alert("提现金额不小于 200 元。");
                $("#withDrawInput").val('');
                return;
            }
            var data = 'amount='+ encodeURIComponent(amount.trim())+"&mid="+encodeURIComponent($scope.avaiWithId);
            $http.post('/withdraw/merchant_withdraw', data, {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            }).success(function (response) {
                if (response.status == 400) {
                    alert("服务繁忙,请稍后尝试!");
                } else {
                    $("#withDrawInput").val('');
                    $scope.txSuc();
                }
            })
        }
    })
