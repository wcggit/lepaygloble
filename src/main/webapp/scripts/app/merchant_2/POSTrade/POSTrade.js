'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('POSTrade', {
                parent: 'merchant_2',
                url: '/POSTrade',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'right-content@merchant_2': {
                        templateUrl: 'scripts/app/merchant_2/POSTrade/POSTrade.html',
                        controller: 'POSTradeController'
                    }
                },
                resolve: {
                }
            });
    });
