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
                     }
                 };
             });
