'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('minShengJieSuan', {
                parent: 'merchant_2',
                url: '/minShengJieSuan',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'right-content@merchant_2': {
                        templateUrl: 'scripts/app/merchant_2/tradeManage/minShengJieSuan/minShengJieSuan.html',
                        controller: 'minShengJieSuanController'
                    },
                    'minShengJieSuanTab@minShengJieSuan': {
                        templateUrl: 'scripts/app/merchant_2/tradeManage/minShengJieSuan/minShengCodeTrade/minShengCodeTrade.html',
                        controller: 'minShengCodeTradeController'
                    }
                },
                resolve: {}
            })
            .state('minShengCodeTrade', {
                parent: 'minShengJieSuan',
                url: '/minShengCodeTrade',
                data: {
                    authorities: ["merchant"]
                },
                params: {
                    mid: null
                },
                views: {
                    'minShengJieSuanTab@minShengJieSuan': {
                        templateUrl: 'scripts/app/merchant_2/tradeManage/minShengJieSuan/minShengCodeTrade/minShengCodeTrade.html',
                        controller: 'minShengCodeTradeController'
                    }
                },
                resolve: {}
            })

    });
