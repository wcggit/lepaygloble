/**
 * Created by xf on 16-12-16.
 */
angular.module('lepayglobleApp')
    .factory('bindUser', function Trade($q, $http) {
        return {
            getMerchantBindUserList: function (criteria) {
                var deferred = $q.defer();
                $http.post('/api/merchantUser/bindUsers', criteria, {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }).success(function (response) {
                    deferred.resolve(response);
                });
                return deferred.promise;
            },
            countMerchantBindUserTotalPages:function(criteria) {
                var deferred = $q.defer();
                $http.post('/api/merchantUser/bindUsers/count', criteria, {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }).success(function (response) {
                    deferred.resolve(response);
                });
                return deferred.promise;
            },
            countMerchantBindUsers:function() {
                var deferred = $q.defer();
                $http.get('/api/merchantUser/findLockerInfoByMerchantUser').success(function (response) {
                    deferred.resolve(response);
                });
                return deferred.promise;
            }
        }
});
