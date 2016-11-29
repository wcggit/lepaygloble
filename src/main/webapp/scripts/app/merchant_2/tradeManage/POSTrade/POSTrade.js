'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('POSTrade', {
                parent: 'merchant_2',
                url: '/POSTrade',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'right-content@merchant_2': {
                        templateUrl: 'scripts/app/merchant_2/tradeManage/POSTrade/POSTrade.html',
                        controller: 'POSTradeController'
                    },
                    'POSTradeTab@POSTrade': {
                        templateUrl: 'scripts/app/merchant_2/tradeManage/POSTrade/POSTradeRecord.html',
                        controller: 'POSTradeRecordController'
                    }
                },
                resolve: {}
            })
            .state('POSTradeRecord', {
                parent: 'POSTrade',
                url: '/POSTradeRecord',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'POSTradeTab@POSTrade': {
                        templateUrl: 'scripts/app/merchant_2/tradeManage/POSTrade/POSTradeRecord.html',
                        controller: 'POSTradeRecordController'
                    }
                },
                resolve: {}
            })
            .state('myPOSTrade', {
                parent: 'POSTrade',
                url: '/myPOSTrade',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'POSTradeTab@POSTrade': {
                        templateUrl: 'scripts/app/merchant_2/tradeManage/POSTrade/myPOSTrade.html',
                        controller: 'myPOSTradeController'
                    }
                },
                resolve: {}
            })
            .state('myPOSEmpty', {
                parent: 'POSTrade',
                url: '/myPOSEmpty',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'POSTradeTab@POSTrade': {
                        templateUrl: 'scripts/app/merchant_2/tradeManage/POSTrade/myPOSEmpty.html',
                        controller: 'myPOSEmptyController'
                    }
                },
                resolve: {}
            });
    });
