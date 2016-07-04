'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
                $stateProvider
                    .state('merchant', {
                               parent: 'site',
                               url: '/merchant',
                               data: {
                                   authorities: ["merchant"]
                               },
                               views: {
                                   'navbar@': {
                                       templateUrl: 'scripts/app/merchant/merchant.html',
                                       controller: 'MerchantController'
                                   }
                               },
                               resolve: {
                               }
                           });
            });
