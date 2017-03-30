 /**
 * Created by recoluan on 2016/11/16.
 */
 'use strict';


angular.module('lepayglobleApp')
    .controller('cp-basicInfoController', function ($scope, $state, $rootScope, $location,Principal,Auth,$http) {
        $scope.accountInfoState0=true;
        $scope.accountInfoState1=false;
        $scope.accountInfoFun0=function () {
            $scope.accountInfoState0=true;
            $scope.accountInfoState1=false;
        }
        $scope.accountInfoFun1=function () {
            $scope.accountInfoState0=false;
            $scope.accountInfoState1=true;
        }
        $http.get('/api/partnerManager/basic').success(function (response) {
            $scope.city = response.data.city;
            $scope.partnerName = response.data.name;
            $scope.userLimit = response.data.userLimit;
            $scope.bankNumber = response.data.bankNumber;
            $scope.bankName = response.data.bankName;
            $scope.bindMerchantLimit = response.data.bindMerchantLimit;
            $scope.bindPartnerLimit = response.data.bindPartnerLimit;
            $scope.phoneNumber = response.data.phoneNumber;
            $scope.payee = response.data.payee;
        });
    })
