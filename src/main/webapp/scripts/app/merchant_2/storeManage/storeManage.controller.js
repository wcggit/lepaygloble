 /**
 * Created by recoluan on 2016/11/16.
 */
 'use strict';


angular.module('lepayglobleApp')
    .controller('storeManageController', function ($scope, $state, $rootScope, $location,Principal,Auth,$http) {
        var currentPage = 1;
        var merchantCriteria = {};
        $scope.loadMerchantInfo = function(){
            merchantCriteria.currentPage = currentPage;
            $http.post('api/merchantUser/pageFindMerchantInfoByMerchantUser',merchantCriteria).success(function(response){
                if(response.status == 200){
                    $scope.merchantData = response.data.mList;
                    $scope.totalPages = response.data.totalPages;
                    $scope.page = currentPage;
                    if($scope.merchantData.length>0){
                        $("#notData").hide();
                    }else{
                        $("#notData").show();
                    }
                }
            });
        }

        $scope.loadMerchantInfo();
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
            $scope.loadMerchantInfo();
        };
    })
