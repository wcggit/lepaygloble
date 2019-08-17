'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('lePlusJieSuan', {
                parent: 'merchant_2',
                url: '/lePlusJieSuan',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'right-content@merchant_2': {
                        templateUrl: 'scripts/app/merchant_2/tradeManage/lePlusJieSuan/lePlusJieSuan.html',
                        controller: 'lePlusJieSuanController'
                    },
                    'lePlusJieSuanTab@lePlusJieSuan': {
                        templateUrl: 'scripts/app/merchant_2/tradeManage/lePlusJieSuan/lePlusCodeTrade/lePlusCodeTrade.html',
                        controller: 'lePlusCodeTradeController'
                    }
                },
                resolve: {}
            })
            .state('lePlusCodeTrade', {
                parent: 'lePlusJieSuan',
                url: '/lePlusCodeTrade',
                data: {
                    authorities: ["merchant"]
                },params: {
                    mid: null
                },
                views: {
                    'lePlusJieSuanTab@lePlusJieSuan': {
                        templateUrl: 'scripts/app/merchant_2/tradeManage/lePlusJieSuan/lePlusCodeTrade/lePlusCodeTrade.html',
                        controller: 'lePlusCodeTradeController'
                    }
                },
                resolve: {}
            })

    });
