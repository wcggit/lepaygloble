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
                    },
                    'yiBaoJieSuanCodeTab@yiBaoCodeTrade': {
                        templateUrl: 'scripts/app/merchant_2/tradeManage/yiBaoJieSuan/yiBaoCodeTrade/allState.html',
                        controller: 'yiBaoCodeTradeAllStateController'
                    }
                },
                resolve: {}
            })
            .state('yiBaoCodeTradeAllState', {
                parent: 'yiBaoCodeTrade',
                url: '/yiBaoCodeTradeAllState',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'yiBaoJieSuanCodeTab@yiBaoCodeTrade': {
                        templateUrl: 'scripts/app/merchant_2/tradeManage/yiBaoJieSuan/yiBaoCodeTrade/allState.html',
                        controller: 'yiBaoCodeTradeAllStateController'
                    }
                },
                resolve: {}
            })
            .state('yiBaoCodeTradeTransfering', {
                parent: 'yiBaoCodeTrade',
                url: '/yiBaoCodeTradeTransfering',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'yiBaoJieSuanCodeTab@yiBaoCodeTrade': {
                        templateUrl: 'scripts/app/merchant_2/tradeManage/yiBaoJieSuan/yiBaoCodeTrade/transfering.html',
                        controller: 'yiBaoCodeTradeTransferingController'
                    }
                },
                resolve: {}
            })
            .state('yiBaoCodeTradeTransferSuccess', {
                parent: 'yiBaoCodeTrade',
                url: '/yiBaoCodeTradeTransferSuccess',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'yiBaoJieSuanCodeTab@yiBaoCodeTrade': {
                        templateUrl: 'scripts/app/merchant_2/tradeManage/yiBaoJieSuan/yiBaoCodeTrade/transferSuccess.html',
                        controller: 'yiBaoCodeTradeTransferSuccessController'
                    }
                },
                resolve: {}
            })
            .state('yiBaoCodeTradeTransferErr', {
                parent: 'yiBaoCodeTrade',
                url: '/yiBaoCodeTradeTransferErr',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'yiBaoJieSuanCodeTab@yiBaoCodeTrade': {
                        templateUrl: 'scripts/app/merchant_2/tradeManage/yiBaoJieSuan/yiBaoCodeTrade/transferErr.html',
                        controller: 'yiBaoCodeTradeTransferErrController'
                    }
                },
                resolve: {}
            })
            .state('yiBaoCodeTradeReturn', {
                parent: 'yiBaoCodeTrade',
                url: '/yiBaoCodeTradeReturn',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'yiBaoJieSuanCodeTab@yiBaoCodeTrade': {
                        templateUrl: 'scripts/app/merchant_2/tradeManage/yiBaoJieSuan/yiBaoCodeTrade/return.html',
                        controller: 'yiBaoCodeTradeReturnController'
                    }
                },
                resolve: {}
            })
    });
