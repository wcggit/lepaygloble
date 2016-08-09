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
                     }
                 };
             });
