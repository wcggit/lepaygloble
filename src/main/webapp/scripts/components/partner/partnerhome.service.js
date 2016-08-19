'use strict';

angular.module('lepayglobleApp')
    .factory('Partner', function Trade($q, $http) {
                 return {
                     getPartnerHomePageData: function () {
                         var deferred = $q.defer();
                         $http.get('/api/partner/homeData', {
                             headers: {
                                 'Content-Type': 'application/json'
                             }
                         }).success(function (response) {
                             deferred.resolve(response);
                         });
                         return deferred.promise;
                     },
                     getMerchantTop5: function (sortType) {
                         var deferred = $q.defer();
                         $http.get('/api/partner/merchant_top5?sortType=' + sortType, {
                             headers: {
                                 'Content-Type': 'application/json'
                             }
                         }).success(function (response) {
                             deferred.resolve(response);
                         });
                         return deferred.promise;
                     },
                     getPartnerBindMerchantList: function (data) {
                         var deferred = $q.defer();
                         $http.post('/api/partner/merchant_list', data, {
                             headers: {
                                 'Content-Type': 'application/json'
                             }
                         }).success(function (response) {
                             deferred.resolve(response);
                         });
                         return deferred.promise;
                     },
                     getPartnerBindMerchantListPage: function (data) {
                         var deferred = $q.defer();
                         $http.post('/api/partner/merchant_list_page', data, {
                             headers: {
                                 'Content-Type': 'application/json'
                             }
                         }).success(function (response) {
                             deferred.resolve(response);
                         });
                         return deferred.promise;
                     },
                     countFullMerchant: function () {
                         var deferred = $q.defer();
                         $http.get('/api/partner/count_full_merchant', {
                             headers: {
                                 'Content-Type': 'application/json'
                             }
                         }).success(function (response) {
                             deferred.resolve(response);
                         });
                         return deferred.promise;
                     },
                     getMerchantById: function (id) {
                         var deferred = $q.defer();
                         $http.get('/api/partner/merchant?sid=' + id, {
                             headers: {
                                 'Content-Type': 'application/json'
                             }
                         }).success(function (response) {
                             deferred.resolve(response);
                         });
                         return deferred.promise;
                     },
                     getMerchantUserById: function (id) {
                         var deferred = $q.defer();
                         $http.get('/api/partner/merchant_user?sid=' + id, {
                             headers: {
                                 'Content-Type': 'application/json'
                             }
                         }).success(function (response) {
                             deferred.resolve(response);
                         });
                         return deferred.promise;
                     },
                     getAllCities: function () {
                         var deferred = $q.defer();
                         $http.get('/api/city/ajax', {
                             headers: {
                                 'Content-Type': 'application/json'
                             }
                         }).success(function (response) {
                             deferred.resolve(response);
                         });
                         return deferred.promise;
                     },
                     getAllMerchantType: function () {
                         var deferred = $q.defer();
                         $http.get('/api/merchant/merchantType', {
                             headers: {
                                 'Content-Type': 'application/json'
                             }
                         }).success(function (response) {
                             deferred.resolve(response);
                         });
                         return deferred.promise;
                     }
                 };
             });
