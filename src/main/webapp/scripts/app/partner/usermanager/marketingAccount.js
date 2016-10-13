'use strict';

angular.module('lepayglobleApp')
    .factory('marketingAccount', function Trade($q, $http) {
                 return {
                     getPartnerScoreLog: function (scoreLogCriteria) {
                         var deferred = $q.defer();
                         $http.post('/api/partner/scorelog/findByCriteria', scoreLogCriteria, {
                             headers: {
                                 'Content-Type': 'application/json'
                             }
                         }).success(function (response) {
                             deferred.resolve(response);
                         });
                         return deferred.promise;

                     },getPartnerTotalScore: function () {
                         var deferred = $q.defer();
                         $http.get('/api/partner/scorelog/issuedScore', {
                             headers: {
                                 'Content-Type': 'application/json'
                             }
                         }).success(function (response) {
                             deferred.resolve(response);
                         });
                         return deferred.promise;

                     },requestRecharge: function(partnerRecharge) {
                         var deferred = $q.defer();
                         $http.post('/api/partner/recharge/save',partnerRecharge,{
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
