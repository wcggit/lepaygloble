/**
 * Created by recoluan on 2016/11/16.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('dataOverviewController', function ($scope, $state, $rootScope, $location, Principal, Auth, $http, HomePage) {
        //  数据概览
        HomePage.getMerchantCommissionDetail().then(function (response) {
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
                        angular.forEach(response.data.merchants,function(data){
                            $scope.merchants.push(data);
                        });
                        console.log($scope.merchants);
                        dataOverviewCriteria.merchants = $scope.merchants;
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


    })
