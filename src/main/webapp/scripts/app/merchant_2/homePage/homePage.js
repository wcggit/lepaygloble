'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('homePage', {
                parent: 'merchant_2',
                url: '/homePage',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'right-content@merchant_2': {
                        templateUrl: 'scripts/app/merchant_2/homePage/homePage.html',
                        controller: 'homePageController'
                    }
                },
                resolve: {

                }
            })
            .state('withdrawRecord', {
                parent: 'merchant_2',
                url: '/withdrawRecord',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'right-content@merchant_2': {
                        templateUrl: 'scripts/app/merchant_2/homePage/withdrawRecord.html',
                        controller: 'withdrawRecordController'
                    }
                },
                resolve: {

                }
             });
    });
