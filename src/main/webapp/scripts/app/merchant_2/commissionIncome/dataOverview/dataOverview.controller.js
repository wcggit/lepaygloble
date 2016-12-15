/**
 * Created by recoluan on 2016/11/16.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('dataOverviewController', function ($scope, $state, $rootScope, $location, Principal, Auth, $http, HomePage) {
        //  数据概览
        HomePage.getMerchantCommissionDetail().then(function (response) {
            var data = response.data;
            // $scope.available = $scope.toDecimal(data.available * 0.01);
            // $scope.totalCommission = $scope.toDecimal(data.totalCommission * 0.01);
            console.log(data);
            $scope.available = data.available * 0.01;
            $scope.totalCommission = data.totalCommission * 0.01;
        });
        $http.get("/api/offLineOrder/todayCommissionAndTodayNumber").success(function (response) {
            console.log(response);
        })
    })
