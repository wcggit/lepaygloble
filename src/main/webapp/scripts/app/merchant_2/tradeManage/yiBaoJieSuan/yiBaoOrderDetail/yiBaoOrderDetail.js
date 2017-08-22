'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('yiBaoOrderDetail', {
                parent: 'merchant_2',
                url: '/yiBaoOrderDetail',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'right-content@merchant_2': {
                        templateUrl: 'scripts/app/merchant_2/tradeManage/yiBaoJieSuan/yiBaoOrderDetail/yiBaoOrderDetail.html',
                        controller: 'yiBaoOrderDetailController'
                    },
                    'yiBaoOrderDetailTab@yiBaoOrderDetail': {
                        templateUrl: 'scripts/app/merchant_2/tradeManage/yiBaoJieSuan/yiBaoOrderDetail/yiBaoTradeRecord.html',
                        controller: 'yiBaoTradeRecordController'
                    }
                },
                resolve: {}
            })
            .state('yiBaoTradeRecord', {
                parent: 'yiBaoOrderDetail',
                url: '/yiBaoTradeRecord',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'yiBaoOrderDetailTab@yiBaoOrderDetail': {
                        templateUrl: 'scripts/app/merchant_2/tradeManage/yiBaoJieSuan/yiBaoOrderDetail/yiBaoTradeRecord.html',
                        controller: 'yiBaoTradeRecordController'
                    }
                },
                resolve: {}
            })
            .state('yiBaoReturnRecord', {
                parent: 'yiBaoOrderDetail',
                url: '/yiBaoReturnRecord',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'yiBaoOrderDetailTab@yiBaoOrderDetail': {
                        templateUrl: 'scripts/app/merchant_2/tradeManage/yiBaoJieSuan/yiBaoOrderDetail/yiBaoReturnRecord.html',
                        controller: 'yiBaoReturnRecordController'
                    }
                },
                resolve: {}
            })
    });
