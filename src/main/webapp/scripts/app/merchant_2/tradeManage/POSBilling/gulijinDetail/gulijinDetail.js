'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('gulijinDetail', {
                parent: 'merchant_2',
                url: '/gulijinDetail',
                data: {
                    authorities: ["merchant"]
                },
                params: {
                    mid: null,
                    tradeDate: null
                },
                views: {
                    'right-content@merchant_2': {
                        templateUrl: 'scripts/app/merchant_2/tradeManage/POSBilling/gulijinDetail/gulijinDetail.html',
                        controller: 'gulijinDetailController'
                    }
                },
                resolve: {}
            })

    });
