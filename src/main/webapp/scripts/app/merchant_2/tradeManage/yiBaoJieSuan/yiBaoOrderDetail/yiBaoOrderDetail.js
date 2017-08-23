'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('yiBaoOrderDetail', {
                parent: 'merchant_2',
                url: '/yiBaoOrderDetail',
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
                        templateUrl: 'scripts/app/merchant_2/tradeManage/yiBaoJieSuan/yiBaoOrderDetail/yiBaoOrderDetail.html',
                        controller: 'yiBaoOrderDetailController'
                    }
                },
                resolve: {}
            })

    });
