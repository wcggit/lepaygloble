'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
                $stateProvider
                    .state('merchant-config', {
                               parent: 'merchant',
                                               url:"/config",
                               data: {
                                   authorities: ['merchant']
                               },
                               views: {
                                   'content@': {
                                       templateUrl: 'scripts/app/merchant/config/config.html',
                                       controller: 'MerchantConfigController'
                                   }
                               },
                               resolve: {

                               }
                           });
            });
