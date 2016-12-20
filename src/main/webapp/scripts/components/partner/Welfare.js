'use strict';

angular.module('lepayglobleApp')
    .factory('Welfare', function Trade($q, $http) {
                 return {
                     checkUserWelfare: function (id) {

                         var deferred = $q.defer();
                         $http.get('/api/partner/welfare/' + id, {
                             headers: {
                                 'Content-Type': 'application/json'
                             }
                         }).success(function (response) {
                             deferred.resolve(response);
                         });
                         return deferred.promise;
                     },
                     welfareOneUser: function (map) {

                         var deferred = $q.defer();
                         $http.post('/api/partner/welfare', map, {
                             headers: {
                                 'Content-Type': 'application/json'
                             }
                         }).success(function (response) {
                             deferred.resolve(response);
                         });
                         return deferred.promise;
                     },
                     exclusiveCheck: function (exclusiveArrayDto) {
                         var deferred = $q.defer();
                         $http.post('/api/partner/welfare/exclusive_check', exclusiveArrayDto, {
                             headers: {
                                 'Content-Type': 'application/json'
                             }
                         }).success(function (response) {
                             deferred.resolve(response);
                         });
                         return deferred.promise;
                     },
                     batchWelfareExclusive: function (exclusiveArrayDto) {
                         var deferred = $q.defer();
                         $http.post('/api/partner/welfare/exclusive', exclusiveArrayDto, {
                             headers: {
                                 'Content-Type': 'application/json'
                             }
                         }).success(function (response) {
                             deferred.resolve(response);
                         });
                         return deferred.promise;
                     },
                     inclusiveCheck: function (inclusiveArray) {
                         var deferred = $q.defer();
                         $http.post('/api/partner/welfare/inclusive_check', inclusiveArray, {
                             headers: {
                                 'Content-Type': 'application/json'
                             }
                         }).success(function (response) {
                             deferred.resolve(response);
                         });
                         return deferred.promise;
                     }, batchWelfareInclusive: function (exclusiveArrayDto) {
                         var deferred = $q.defer();
                         $http.post('/api/partner/welfare/inclusive', exclusiveArrayDto, {
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
