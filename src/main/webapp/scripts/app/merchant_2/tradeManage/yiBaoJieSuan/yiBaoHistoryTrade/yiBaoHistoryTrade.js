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
                    },
                    'yiBaoHistoryTab@yiBaoHistoryTrade': {
                        templateUrl: 'scripts/app/merchant_2/tradeManage/yiBaoJieSuan/yiBaoHistoryTrade/allState.html',
                        controller: 'yiBaoHistoryTradeAllStateController'
                    }
                },
                resolve: {}
            })
            .state('yiBaoHistoryTradeAllState', {
                parent: 'yiBaoHistoryTrade',
                url: '/yiBaoHistoryTradeAllState',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'yiBaoHistoryTab@yiBaoHistoryTrade': {
                        templateUrl: 'scripts/app/merchant_2/tradeManage/yiBaoJieSuan/yiBaoHistoryTrade/allState.html',
                        controller: 'yiBaoHistoryTradeAllStateController'
                    }
                },
                resolve: {}
            })
            .state('yiBaoHistoryTradeTransfering', {
                parent: 'yiBaoHistoryTrade',
                url: '/yiBaoHistoryTradeTransfering',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'yiBaoHistoryTab@yiBaoHistoryTrade': {
                        templateUrl: 'scripts/app/merchant_2/tradeManage/yiBaoJieSuan/yiBaoHistoryTrade/transfering.html',
                        controller: 'yiBaoHistoryTradeTransferingController'
                    }
                },
                resolve: {}
            })
            .state('yiBaoHistoryTradeTransferSuccess', {
                parent: 'yiBaoHistoryTrade',
                url: '/yiBaoHistoryTradeTransferSuccess',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'yiBaoHistoryTab@': {
                        templateUrl: 'scripts/app/merchant_2/tradeManage/yiBaoJieSuan/yiBaoHistoryTrade/transferSuccess.html',
                        controller: 'yiBaoHistoryTradeTransferSuccessController'
                    }
                },
                resolve: {}
            })
            .state('yiBaoHistoryTradeHanged', {
                parent: 'yiBaoHistoryTrade',
                url: '/yiBaoHistoryTradeHanged',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'yiBaoHistoryTab@': {
                        templateUrl: 'scripts/app/merchant_2/tradeManage/yiBaoJieSuan/yiBaoHistoryTrade/hanged.html',
                        controller: 'yiBaoHistoryTradeHangedController'
                    }
                },
                resolve: {}
            })
    });
