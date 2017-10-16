'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('POSBilling', {
                parent: 'merchant_2',
                url: '/POSBilling',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'right-content@merchant_2': {
                        templateUrl: 'scripts/app/merchant_2/tradeManage/POSBilling/POSBilling.html',
                        controller: 'POSBillingController'
                    }
                },
                resolve: {}
            })
    });
