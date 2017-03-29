 /**
 * Created by recoluan on 2016/11/16.
 */
 'use strict';


angular.module('lepayglobleApp')
    .controller('homePageController', function ($scope, $state, $rootScope, $location,Principal,Auth,$http,Commission,HomePage) {
        $("[data-toggle='tooltip']").tooltip();


        //强制保留两位小数
        $scope.toDecimal = function (x) {
            var f = parseFloat(x);
            if (isNaN(f)) {
                return false;
            }
            var f = Math.round(x*100)/100;
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
        //  商户信息
        Principal.identity().then(function (account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });
        $http.get('api/merchantUser').success(function (response) {
            $scope.shopName = response.data.merchantName;
        });

        HomePage.getMerchantsInfo().then(function(response) {
            var data = response.data;
            $scope.merchants = data;
        });

        //  基本信息
        HomePage.getMerchantCommissionDetail().then(function (response) {
            var data = response.data;
            $scope.available = $scope.toDecimal(data.available*0.01);
            $scope.totalCommission = $scope.toDecimal(data.totalCommission*0.01);
            if (data.currentBind == 0 && data.totalCommission == 0) {
                $scope.per = 0;
            } else {
                $scope.per = $scope.toDecimal(data.totalCommission / data.currentBind);
            }
        });

        //  首页 - 商户数据
        HomePage.getMerchantData().then(function(data){
            var data = data.data;
            $scope.srgl = {
                firNum:  data.transfering / 100.0,                      // 今日入账
                secNum:  data.totalTransfering / 100.0,                 // 总共入账
                thirNum: data.totalSales / 100.0,                       // 会员消费
                fouNum: data.totalCount,                                // 会员消费次数
                fifNum:  data.totalRebate / 100.0                       // 红包总额
            };
        });

        //  首页 - 交易看板 (全部门店)
        $scope.findAll = function(p) {
            // 交易看板 - 订单提示
            HomePage.opraBoardList(p).then(function (data) {
                var data = data.data;
                $scope.offset=p;
                $scope.orderList = data;
            });
            //  交易看板 - 交易信息
            HomePage.opraBoardInfo().then(function(data) {
                var data = data.data;
                $scope.obinfo = {
                    firNum: data.offLineDailyCount/100.0,
                    secNum: data.posDailyCount/100.0,
                    thirNum: data.dailySales/100.0,
                    fouNum: data.dailyCount
                };
            });
        }
        $scope.findAll(0);

        // 按钮绑定事件 - 查看更多
        $scope.findMore = function(){
            $scope.offset=$scope.offset+1;
            var p = $scope.offset;
            var selMerchant = $("#selMerchant").val();
            if(selMerchant==''||selMerchant==null) {
                $scope.findAll(p);                                               // 所有门店
            }else {
                HomePage.siglOpraBoardList(selMerchant,p).then(function (data) {  // 指定门店
                    var data = data.data;
                    $scope.orderList = data;
                });
            }
        }


        // 首页 - 切换门店时进行查询
        $scope.changeStore = function(id) {
            $scope.offset=0;
            // 查询所有
            if(id==""||id==null) {
                $scope.findAll(0);
                return;
            }
            // 门店交易信息
            HomePage.siglOpraBoardInfo(id).then(function(data) {
                var data = data.data;
                $scope.obinfo = {
                    firNum: data.offLineDailyCount/100.0,
                    secNum: data.posDailyCount/100.0,
                    thirNum: data.dailySales/100.0,
                    fouNum: data.dailyCount
                };
            });
            // 门店订单列表
            HomePage.siglOpraBoardList(id,$scope.offset).then(function (data) {
                var data = data.data;
                $scope.orderList = data;
            });
        }

    });
