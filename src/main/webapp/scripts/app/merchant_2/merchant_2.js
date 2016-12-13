'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('merchant_2', {
                parent: 'site',
                url: '/merchant_2',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'navbar@': {
                        templateUrl: 'scripts/app/merchant_2/merchant_2.html',
                        controller: 'merchant_2Controller'
                    },
                    'right-content@merchant_2': {
                        templateUrl: 'scripts/app/merchant_2/homePage/homePage.html',
                        controller: 'homePageController'
                    }
                },
                resolve: {
                }
            });
    });
