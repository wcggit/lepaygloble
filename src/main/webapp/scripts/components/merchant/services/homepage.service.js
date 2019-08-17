'use strict';

angular.module('lepayglobleApp')
    .factory('HomePage', function Trade($q, $http) {
        return {
            getMerchantData: function() {
                var deferred = $q.defer();
                $http.get('/api/merchant/homePage/merchantData', {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }).success(function (response) {
                    deferred.resolve(response);
                });
                return deferred.promise;
            },
            getMerchantCommissionDetail: function() {
                var deferred = $q.defer();
                $http.get('/api/merchantUser/comission', {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }).success(function (response) {
                    deferred.resolve(response);
                });
                return deferred.promise;
            },
            getCommissionByMerchantUser: function () {
                var deferred = $q.defer();
                $http.get('/api/offLineOrder/merchantUserCommission', {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }).success(function (response) {
                    deferred.resolve(response);
                });
                return deferred.promise;
            },
            getMerchantsInfo: function () {
                var deferred = $q.defer();
                $http.get('/api/merchantUser/merchants', {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }).success(function (response) {
                    deferred.resolve(response);
                });
                return deferred.promise;
            },
            siglOpraBoardInfo: function (id) {                              // 单个门店
                var deferred = $q.defer();
                $http.get('/api/order/dailyOrder/merchant/'+id, {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }).success(function (response) {
                    deferred.resolve(response);
                });
                return deferred.promise;
            },
            siglOpraBoardList: function (id,offset) {                        // 单个门店
                var deferred = $q.defer();
                $http.get('/api/order/orderList/merchant/'+id+'-'+offset, {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }).success(function (response) {
                    deferred.resolve(response);
                });
                return deferred.promise;
            }
        }
    });
