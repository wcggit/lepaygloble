/**
 * Created by xf on 16-12-8.
 */
angular.module('lepayglobleApp')
    .factory('LejiaBilling', function Trade($q, $http) {
        return {
            getMerchantData: function(id) {
                var deferred = $q.defer();
                $http.get('/api/homePage/merchantData', {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }).success(function (response) {
                    deferred.resolve(response);
                });
                return deferred.promise;
            },
            findWeekBillByMerchant: function (dailyOrderCriteria) {
                var deferred = $q.defer();
                $http.post('/api/lejiaOrder/daily',dailyOrderCriteria).success(function (response) {
                    deferred.resolve(response);
                });
                return deferred.promise;
            }
        }
    });
