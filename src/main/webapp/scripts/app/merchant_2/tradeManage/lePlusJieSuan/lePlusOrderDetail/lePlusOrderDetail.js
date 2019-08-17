'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('lePlusOrderDetail', {
                parent: 'merchant_2',
                url: '/lePlusOrderDetail',
                data: {
                    authorities: ["merchant"]
                },
                params: {
                    mid: null,
                    tradeDate: null
                },
                views: {
                    'right-content@merchant_2': {
                        templateUrl: 'scripts/app/merchant_2/tradeManage/lePlusJieSuan/lePlusOrderDetail/lePlusOrderDetail.html',
                        controller: 'lePlusOrderDetailController'
                    }
                },
                resolve: {}
            })

    });
