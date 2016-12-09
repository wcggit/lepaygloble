/**
 * Created by xf on 16-12-8.
 */
angular.module('lepayglobleApp')
    .factory('LejiaBilling', function Trade($q, $http) {
        return {
            getMerchantData: function(id) {
                var deferred = $q.defer();
                $http.get('/api//merchantData', {
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
