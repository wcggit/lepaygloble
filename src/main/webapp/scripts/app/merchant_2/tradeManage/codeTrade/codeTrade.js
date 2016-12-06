'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('codeTrade', {
                parent: 'merchant_2',
                url: '/codeTrade',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'right-content@merchant_2': {
                        templateUrl: 'scripts/app/merchant_2/tradeManage/codeTrade/codeTrade.html',
                        controller: 'codeTradeController'
                    },
                    'codeTradeTab@codeTrade': {
                        templateUrl: 'scripts/app/merchant_2/tradeManage/codeTrade/codeTradeRecord.html',
                        controller: 'codeTradeRecordController'
                    }
                },
                resolve: {}
            })
            .state('codeTradeRecord', {
                parent: 'codeTrade',
                url: '/codeTradeRecord',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'codeTradeTab@codeTrade': {
                        templateUrl: 'scripts/app/merchant_2/tradeManage/codeTrade/codeTradeRecord.html',
                        controller: 'codeTradeRecordController'
                    }
                },
                resolve: {}
            })
            .state('myCode', {
                parent: 'codeTrade',
                url: '/myCode',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'codeTradeTab@codeTrade': {
                        templateUrl: 'scripts/app/merchant_2/tradeManage/codeTrade/myCode.html',
                        controller: 'myCodeController'
                    }
                },
                resolve: {}
            })
    });
