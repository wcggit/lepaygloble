'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('minShengOrderDetail', {
                parent: 'merchant_2',
                url: '/minShengOrderDetail',
                data: {
                    authorities: ["merchant"]
                },
                params: {
                    ledgerNo: null,
                    tradeDate: null,
                    totalTransfer:null,
                    transferState:null
                },
                views: {
                    'right-content@merchant_2': {
                        templateUrl: 'scripts/app/merchant_2/tradeManage/minShengJieSuan/minShengOrderDetail/minShengOrderDetail.html',
                        controller: 'minShengOrderDetailController'
                    }
                },
                resolve: {}
            })

    });
