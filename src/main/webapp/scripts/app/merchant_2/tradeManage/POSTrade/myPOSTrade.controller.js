/**
 * Created by recoluan on 2016/11/25.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('myPOSTradeController', function ($scope, $state, $rootScope, $location,Principal,Auth,$http) {
        $scope.merchantPosIds = null;
        $scope.xuanz = new Array();
        $http.get("/api/merchantUser/findByMerchantPosUser").success(function (response) {
            if(response.status == 200){
                var data = response.data;
                $scope.merchantPosId = data;
                angular.forEach($scope.merchantPosId,function (data,index) {
                    $scope.xuanz[index] = data[0];
                })
                $scope.loadPosInfo();
            }
        });
        var currentPage = 1;
        $scope.loadPosInfo = function (){
            var posCriteria = {};
            posCriteria.currentPage = currentPage;
            if($scope.merchantPosIds == null || $scope.merchantPosIds == ""){
                posCriteria.merchantPosIds = $scope.xuanz;
            }else{
                posCriteria.merchantPosIds = new Array($scope.merchantPosIds);
            }
            $http.post("/api/merchantUser/findPosInfoByMerchantUser",posCriteria).success(function (response) {
                if(response.status == 200){
                    $scope.data = response.data;
                    $scope.totalPages = $scope. data.posPage.totalPages;
                    $scope.page = currentPage;
                    $scope.posContent = $scope.data.posPage.content;
                    if($scope.posContent.length>0){
                        $("#notData").hide();
                    }else{
                        $("#notData").show();
                    }
                }else{
                    alert("数据加载错误...");
                }
            })
        }
        $scope.searchByPosCriteria = function () {
            currentPage = 1;
            $scope.loadPosInfo();

        }
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
            $scope.loadPosInfo();
        };
    })
