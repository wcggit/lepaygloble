'use strict';

angular.module('lepayglobleApp')
    .factory('MyLePay', function Trade($q, $http) {
                 return {
                     getAvaliableCommission: function () {
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
                         $http.get('/api/merchant/getCommission', {
                             headers: {
                                 'Content-Type': 'application/json'
                             }
                         }).success(function (response) {
                             deferred.resolve(response);
                         });
                         return deferred.promise;
                     },
                     getTodayOrderDetail: function () {
                         var deferred = $q.defer();
                         $http.get('/api/order/todayOrderDetail', {
                             headers: {
                                 'Content-Type': 'application/json'
                             }
                         }).success(function (response) {
                             deferred.resolve(response);
                         });
                         return deferred.promise;
                     },
                     getMonthOrderDetail: function () {
                         var deferred = $q.defer();
                         $http.get('/api/order/monthOrderDetail', {
                             headers: {
                                 'Content-Type': 'application/json'
                             }
                         }).success(function (response) {
                             deferred.resolve(response);
                         });
                         return deferred.promise;
                     },
                     getDayTrade: function (data) {
                         var deferred = $q.defer();
                         $http.get('/api/financial/dayTrade',data, {
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
