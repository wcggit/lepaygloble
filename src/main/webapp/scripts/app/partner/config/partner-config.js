'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
                $stateProvider
                    .state('partner-config', {
                               parent: 'partner',
                               url: "/config",
                               data: {
                                   authorities: ['partner']
                               },
                               views: {
                                   'content@': {
                                       templateUrl: 'scripts/app/partner/config/partner-config.html',
                                       controller: 'PartnerConfigController'
                                   }
                               },
                               resolve: {}
                           });
            });
