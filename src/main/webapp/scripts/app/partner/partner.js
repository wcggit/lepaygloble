'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('partner', {
                parent: 'site',
                url: '/partner',
                data: {
                    authorities: ["partner"]
                },
                views: {
                    'navbar@': {
                        templateUrl: 'scripts/app/partner/partner.html',
                        controller: 'PartnerController'
                    }
                },
                resolve: {
                }
            });
    });
