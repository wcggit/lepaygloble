'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('dataOverview', {
                parent: 'merchant_2',
                url: '/dataOverview',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'right-content@merchant_2': {
                        templateUrl: 'scripts/app/merchant_2/commissionIncome/dataOverview/dataOverview.html',
                        controller: 'dataOverviewController'
                    }
                },
                resolve: {
                }
            })
    });
