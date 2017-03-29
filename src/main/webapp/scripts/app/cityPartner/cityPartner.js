'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('cityPartner', {
                parent: 'site',
                url: '/cityPartner',
                data: {
                    authorities: ["partner"]
                },
                views: {
                    'navbar@': {
                        templateUrl: 'scripts/app/cityPartner/cityPartner.html',
                        controller: 'cityPartnerController'
                    },
                    'right-content@cityPartner': {
                        templateUrl: 'scripts/app/cityPartner/homePage/homePage.html',
                        controller: 'cp-homePageController'
                    }
                },
                resolve: {
                }
            });
    });
