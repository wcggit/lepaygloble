/**
 * Created by recoluan on 2016/11/25.
 * Upset by whl on 2017/12/6
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('grouphxIndexController', function ($scope, $state, $rootScope, $location, Principal, Auth, $http) {

        var array = new Array();
        var storeId;
        $http.get("/api/merchantUser/merchantsInfo").success(function (response) {
            if (response.status == 200) {
                var data = response.data;
                console.log(data);
                $scope.myStore = data;
                $scope.selectDefault = data[0][0];
                storeId = data[0][0];
                showStoreData(data[0][0]);
            } else {
                alert("加载门店错误...");
            }
        });
        function showStoreData(sid) {
            $http.get("/api/grouponCode/grouponStatistics?merchantId=" + sid).success(function (response) {
                if (response.status == 200) {
                    var data = response.data;
                    $scope.daily = data.dailyData[0];
                    $scope.total = data.totalData[0];
                    $scope.productList = data.dailyDetail;
                    var allC = 0,allP = 0;
                    for(var i in data.dailyDetail){
                        allC+= parseInt(data.dailyDetail[i].checkCount);
                        allP+= parseFloat(data.dailyDetail[i].totalPrice)
                    }
                    $scope.allC = allC;
                    $scope.allP = allP;

                } else {
                    alert("数据加载错误...");
                }
            });
        }
        $scope.changeStore = function (sid) {
            showStoreData(sid);
            storeId = sid;
        };

        //验证码核销通道
        $scope.YZMHx = function () {
            var sid = $("#yzmInput").val();
            if(sid == ''){
                $scope.errorMsg = "验证码不能为空！";
            }
            $http.get("/api/grouponCode/check/" + sid + "/" + storeId).success(function (response) {
                if (response.status == 200) {
                    var data = response.data;
                    console.log(data);

                } else {
                    console.log(response.msg);
                    $scope.errorMsg = response.msg;
                }
            });
        }
        $scope.hideBG = function () {
            $(".modal-backdrop").fadeOut();
        }

    });
