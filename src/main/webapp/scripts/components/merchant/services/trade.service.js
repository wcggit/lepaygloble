'use strict';

angular.module('lepayglobleApp')
    .factory('Trade', function Trade($q, $http) {
                 return {
                     getFinancialList: function (financialCriteria) {
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
                         $http.post('/api/financial', financialCriteria, {
                             headers: {
                                 'Content-Type': 'application/json'
                             }
                         }).success(function (response) {
                             deferred.resolve(response);
                         });
                         return deferred.promise;
                     },
                     //exportExcal: function () {
                     //    // logout from the server
                     //    $http.post('/api/financial', financialCriteria, {
                     //        headers: {
                     //            'Content-Type': 'application/json'
                     //        }
                     //    });
                     //},
                     getToken: function () {
                         var token = localStorageService.get('token');
                         return token;
                     },
                     getOrderList: function (orderCriteria) {
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
                         $http.post('/api/offLineOrder', orderCriteria, {
                             headers: {
                                 'Content-Type': 'application/json'
                             }
                         }).success(function (response) {
                             deferred.resolve(response);
                         });
                         return deferred.promise;
                     },
                     getOrderDetail: function () {
                         var deferred = $q.defer();
                         $http.get('/api/order/orderDetail', {
                             headers: {
                                 'Content-Type': 'application/json'
                             }
                         }).success(function (response) {
                             deferred.resolve(response);
                         });
                         return deferred.promise;
                     },
                     getOrderStatistic: function (orderCriteria) {
                         var deferred = $q.defer();
                         $http.post('/api/offLineOrder/statistic', orderCriteria, {
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
                     }
                 };
             });
