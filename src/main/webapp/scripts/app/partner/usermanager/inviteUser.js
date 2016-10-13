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
                     }

                 }
             });
