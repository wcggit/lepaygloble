'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('cp-homePage', {
                parent: 'cityPartner',
                url: '/cp-homePage',
                data: {
                    authorities: ["partner"]
                },
                views: {
                    'right-content@cityPartner': {
                        templateUrl: 'scripts/app/cityPartner/homePage/homePage.html',
                        controller: 'cp-homePageController'
                    }
                },
                resolve: {
                }
            })
    });
