'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('yiBaoJieSuan', {
                parent: 'merchant_2',
                url: '/yiBaoJieSuan',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'right-content@merchant_2': {
                        templateUrl: 'scripts/app/merchant_2/tradeManage/yiBaoJieSuan/yiBaoJieSuan.html',
                        controller: 'yiBaoJieSuanController'
                    },
                    'yiBaoJieSuanTab@yiBaoJieSuan': {
                        templateUrl: 'scripts/app/merchant_2/tradeManage/yiBaoJieSuan/yiBaoCodeTrade/yiBaoCodeTrade.html',
                        controller: 'yiBaoCodeTradeController'
                    }
                },
                resolve: {}
            })
            .state('yiBaoCodeTrade', {
                parent: 'yiBaoJieSuan',
                url: '/yiBaoCodeTrade',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'yiBaoJieSuanTab@yiBaoJieSuan': {
                        templateUrl: 'scripts/app/merchant_2/tradeManage/yiBaoJieSuan/yiBaoCodeTrade/yiBaoCodeTrade.html',
                        controller: 'yiBaoCodeTradeController'
                    }
                },
                resolve: {}
            })

    });
