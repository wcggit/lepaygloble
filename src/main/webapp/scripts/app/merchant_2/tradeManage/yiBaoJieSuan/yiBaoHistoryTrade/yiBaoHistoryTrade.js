'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('yiBaoHistoryTrade', {
                parent: 'merchant_2',
                url: '/yiBaoHistoryTrade',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'right-content@merchant_2': {
                        templateUrl: 'scripts/app/merchant_2/tradeManage/yiBaoJieSuan/yiBaoHistoryTrade/yiBaoHistoryTrade.html',
                        controller: 'yiBaoHistoryTradeController'
                    }
                },
                resolve: {}
            })
    });
