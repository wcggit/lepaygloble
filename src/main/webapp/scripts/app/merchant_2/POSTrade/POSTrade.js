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
                        templateUrl: 'scripts/app/merchant_2/POSTrade/POSTrade.html',
                        controller: 'POSTradeController'
                    },
                    'POSTradeTab@POSTrade': {
                        templateUrl: 'scripts/app/merchant_2/POSTrade/tradeRecord.html',
                        controller: 'tradeRecordController'
                    }
                },
                resolve: {}
            })
            .state('tradeRecord', {
                parent: 'POSTrade',
                url: '/tradeRecord',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'POSTradeTab@POSTrade': {
                        templateUrl: 'scripts/app/merchant_2/POSTrade/tradeRecord.html',
                        controller: 'tradeRecordController'
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
                        templateUrl: 'scripts/app/merchant_2/POSTrade/myPOSTrade.html',
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
                        templateUrl: 'scripts/app/merchant_2/POSTrade/myPOSEmpty.html',
                        controller: 'myPOSEmptyController'
                    }
                },
                resolve: {}
            });
    });
