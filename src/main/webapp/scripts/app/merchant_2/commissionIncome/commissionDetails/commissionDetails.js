'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('commissionDetails', {
                parent: 'merchant_2',
                url: '/commissionDetails',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'right-content@merchant_2': {
                        templateUrl: 'scripts/app/merchant_2/commissionIncome/commissionDetails/commissionDetails.html',
                        controller: 'commissionDetailsController'
                    }
                },
                resolve: {
                }
            })
    });
