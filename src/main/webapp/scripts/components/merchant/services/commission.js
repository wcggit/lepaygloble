'use strict';

angular.module('lepayglobleApp')
    .factory('Commission', function Trade($q, $http) {
                 return {
                     getMerchantCommissionDetail: function () {
                         //return  $.ajax({
                         //                   type: "post",
                         //                   url: "/api/financial",
                         //                   async: false,
                         //                   contentType: "application/json",
                         //                   data: JSON.stringify(financialCriteria),
                         //                   success: function (data) {
                         //                       return data;
                         //                   }
                         //               });
                         var deferred = $q.defer();
                         $http.get('/api/offLineOrder/commission', {
                             headers: {
                                 'Content-Type': 'application/json'
                             }
                         }).success(function (response) {
                             deferred.resolve(response);
                         });
                         return deferred.promise;
                     },
                     getDayCommissionDetail: function () {
                         var deferred = $q.defer();
                         $http.get('/api/offLineOrder/dayCommission', {
                             headers: {
                                 'Content-Type': 'application/json'
                             }
                         }).success(function (response) {
                             deferred.resolve(response);
                         });
                         return deferred.promise;
                     },
                     getOrderShareList: function (orderCriteria) {
                         //return  $.ajax({
                         //                   type: "post",
                         //                   url: "/api/financial",
                         //                   async: false,
                         //                   contentType: "application/json",
                         //                   data: JSON.stringify(financialCriteria),
                         //                   success: function (data) {
                         //                       return data;
                         //                   }
                         //               });
                         var deferred = $q.defer();
                         $http.post('/api/offLineOrder/share', orderCriteria, {
                             headers: {
                                 'Content-Type': 'application/json'
                             }
                         }).success(function (response) {
                             deferred.resolve(response);
                         });
                         return deferred.promise;
                     },
                     getOrderShareStatistic: function (data) {
                         var deferred = $q.defer();
                         $http.post('/api/offLineOrder/share/statistic', data, {
                             headers: {
                                 'Content-Type': 'application/json'
                             }
                         }).success(function (response) {
                             deferred.resolve(response);
                         });
                         return deferred.promise;
                     },
                     getMerchantBindUserList: function (data) {
                         var deferred = $q.defer();
                         $http.post('/api/merchant/bindUsers', data, {
                             headers: {
                                 'Content-Type': 'application/json'
                             }
                         }).success(function (response) {
                             deferred.resolve(response);
                         });
                         return deferred.promise;
                     },getMerchantBindUserTotalPages: function (data) {
                         var deferred = $q.defer();
                         $http.post('/api/merchant/totalPages', data, {
                             headers: {
                                 'Content-Type': 'application/json'
                             }
                         }).success(function (response) {
                             deferred.resolve(response);
                         });
                         return deferred.promise;
                     },
                     getUsersByBindPartner: function (data) {
                         var deferred = $q.defer();
                         $http.post('/api/partner/bindUsers', data, {
                             headers: {
                                 'Content-Type': 'application/json'
                             }
                         }).success(function (response) {
                             deferred.resolve(response);
                         });
                         return deferred.promise;
                     },
                     getTotalPagesByBindPartner: function (data) {
                         var deferred = $q.defer();
                         $http.post('/api/partner/userTotalPages', data, {
                             headers: {
                                 'Content-Type': 'application/json'
                             }
                         }).success(function (response) {
                             deferred.resolve(response);
                         });
                         return deferred.promise;
                     },
                     hasValidToken: function () {
                         var token = this.getToken();
                         return !!token;
                     },
                     getOnLineCommissionIncome: function () {
                         var deferred = $q.defer();
                         $http.get('/api/merchant/getOnLineCommissionIncome', {
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
