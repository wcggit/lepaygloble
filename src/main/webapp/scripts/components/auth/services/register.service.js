'use strict';

angular.module('lepayglobleApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


