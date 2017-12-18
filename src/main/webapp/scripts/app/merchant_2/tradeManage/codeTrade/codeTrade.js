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
            .state('historyTradeRecord', {
                parent: 'codeTrade',
                url: '/historyTradeRecord',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'codeTradeTab@codeTrade': {
                        templateUrl: 'scripts/app/merchant_2/tradeManage/codeTrade/historyTradeRecord.html',
                        controller: 'historyTradeRecordController'
                    }
                },
                resolve: {}
            })
            .state('refund', {
                parent: 'codeTrade',
                url: '/refund',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'codeTradeTab@codeTrade': {
                        templateUrl: 'scripts/app/merchant_2/tradeManage/codeTrade/refund.html',
                        controller: 'refundController'
                    }
                },
                resolve: {}
            })
            .state('refundList', {
                parent: 'codeTrade',
                url: '/refundList',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'codeTradeTab@codeTrade': {
                        templateUrl: 'scripts/app/merchant_2/tradeManage/codeTrade/refundList.html',
                        controller: 'refundListController'
                    }
                },
                resolve: {}
            })
    });
