'use strict';

angular.module('lepayglobleApp')
    .factory('InviteUser', function Trade($q, $http) {
                 return {
                      getTotalCount: function () {
                          var deferred = $q.defer();
                          $http.get('/api/partner/merchantInfo/count', {
                              headers: {
                                'Content-Type': 'application/json'
                              }
                          }).success(function (response) {
                             deferred.resolve(response);
                          });
                          return deferred.promise;
                      },
                      getPartnerMerchantInfo: function (criteria) {
                         var deferred = $q.defer();
                         $http.post('/api/partner/merchantInfo', criteria, {
                             headers: {
                                 'Content-Type': 'application/json'
                             }
                         }).success(function (response) {
                             deferred.resolve(response);
                         });
                         return deferred.promise;
                     },
                     getPartnerInfo: function() {
                         var deferred = $q.defer();
                         $http.get('/api/partner/loadParnerInfo', {
                             headers: {
                                 'Content-Type': 'application/json'
                             }
                         }).success(function (response) {
                             deferred.resolve(response);
                         });
                         return deferred.promise;
                     },
                     savePartnerInfo: function(partnerInfo) {
                         var deferred = $q.defer();
                         $http.post('/api/partner/partnerInfo/save',partnerInfo, {
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
