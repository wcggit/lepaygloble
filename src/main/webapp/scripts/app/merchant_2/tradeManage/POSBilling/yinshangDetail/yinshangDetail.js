'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('yinshangDetail', {
                parent: 'merchant_2',
                url: '/yinshangDetail',
                data: {
                    authorities: ["merchant"]
                },
                params: {
                    merNum: null,
                    settleDate: null
                },
                views: {
                    'right-content@merchant_2': {
                        templateUrl: 'scripts/app/merchant_2/tradeManage/POSBilling/yinshangDetail/yinshangDetail.html',
                        controller: 'yinshangDetailController'
                    }
                },
                resolve: {}
            })

    });
