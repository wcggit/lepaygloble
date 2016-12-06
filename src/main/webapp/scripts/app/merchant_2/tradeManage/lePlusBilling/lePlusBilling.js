'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('lePlusBilling', {
                parent: 'merchant_2',
                url: '/lePlusBilling',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'right-content@merchant_2': {
                        templateUrl: 'scripts/app/merchant_2/tradeManage/lePlusBilling/lePlusBilling.html',
                        controller: 'lePlusBillingController'
                    },
                    'lePlusBillingTab@lePlusBilling': {
                        templateUrl: 'scripts/app/merchant_2/tradeManage/lePlusBilling/dailyBilling.html',
                        controller: 'dailyBillingController'
                    }
                },
                resolve: {}
            })
            .state('dailyBilling', {
                parent: 'lePlusBilling',
                url: '/dailyBilling',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'lePlusBillingTab@lePlusBilling': {
                        templateUrl: 'scripts/app/merchant_2/tradeManage/lePlusBilling/dailyBilling.html',
                        controller: 'dailyBillingController'
                    }
                },
                resolve: {}
            })
            .state('storeBilling', {
                parent: 'lePlusBilling',
                url: '/storeBilling',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'lePlusBillingTab@lePlusBilling': {
                        templateUrl: 'scripts/app/merchant_2/tradeManage/lePlusBilling/storeBilling.html',
                        controller: 'storeBillingController'
                    }
                },
                resolve: {}
            });
    });
