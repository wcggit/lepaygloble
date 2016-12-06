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
            opraBoardInfo: function () {
                var deferred = $q.defer();
                $http.get('/api/order/dailyOrder', {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }).success(function (response) {
                    deferred.resolve(response);
                });
                return deferred.promise;
            },
            opraBoardList: function (offset) {
                var deferred = $q.defer();
                $http.get('/api/order/orderList/'+offset, {
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
