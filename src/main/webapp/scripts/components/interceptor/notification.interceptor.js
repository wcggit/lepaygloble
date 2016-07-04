 'use strict';

angular.module('lepayglobleApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-lepayglobleApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-lepayglobleApp-params')});
                }
                return response;
            }
        };
    });
