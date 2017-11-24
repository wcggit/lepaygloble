/**
 * Created by recoluan on 2016/11/16.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('cp-infoManageController', function ($scope, $state, $rootScope, $location, Principal, Auth, $http, $stateParams) {
        $scope.currentTab0 = true;
        $scope.currentTab1 = false;
        $scope.currentTab2 = false;
        $scope.currentTab3 = false;
        $scope.onClickTab = function ($event) {
            var checkVal = $event.target.innerText;
            switch (checkVal){
                case "数据概览":
                    $scope.currentTab0 = true;
                    $scope.currentTab1 = false;
                    $scope.currentTab2 = false;
                    $scope.currentTab3 = false;
                    break;
                case "基本信息":
                    $scope.currentTab0 = false;
                    $scope.currentTab1 = true;
                    $scope.currentTab2 = false;
                    $scope.currentTab3 = false;
                    break;
                case "合伙人商户":
                    $scope.currentTab0 = false;
                    $scope.currentTab1 = false;
                    $scope.currentTab2 = true;
                    $scope.currentTab3 = false;
                    break;
                case "合伙人佣金":
                    $scope.currentTab0 = false;
                    $scope.currentTab1 = false;
                    $scope.currentTab2 = false;
                    $scope.currentTab3 = true;
                    break;
            }
            // $scope.currentTab0 = !$scope.currentTab0;
            // $scope.currentTab1 = !$scope.currentTab1;
            // $scope.currentTab2 = !$scope.currentTab2;
            // $scope.currentTab3 = !$scope.currentTab3;
        };
        var partnerSid = $stateParams.partnerSid;
        $scope.partnerSid = partnerSid;
        $http.get('api/partnerManager/partner/findBySid?partnerSid=' + partnerSid).success(function (response) {
            $scope.partner = response.data;
        });
    });
