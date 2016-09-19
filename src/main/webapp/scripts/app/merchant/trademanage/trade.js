'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
                $stateProvider
                    .state('trade', {
                               parent: 'merchant',
                               url: '/trade',
                               data: {
                                   authorities: []
                               },
                               views: {
                                   'content@': {
                                       templateUrl: 'scripts/app/merchant/trademanage/trade.html',
                                       controller: 'TradeController'
                                   },
                                   'tradeContent@trade': {
                                       templateUrl: 'scripts/app/merchant/trademanage/tradeList.html',
                                       controller: 'TradeListController'
                                   }
                               },
                               resolve: {}
                           }).state('tradeList', {
                                        parent: 'trade',
                                        data: {
                                            authorities: []
                                        },
                                        resolve: {}
                                    }).state('orderList', {
                                                 parent: 'trade',
                                                 params: {
                                                     date: null
                                                 },
                                                 url: '/orderList',
                                                 data: {
                                                     authorities: []
                                                 },
                                                 views: {
                                                     'tradeContent@trade': {
                                                         templateUrl: 'scripts/app/merchant/trademanage/orderList.html',
                                                         controller: 'OrderListController'
                                                     }
                                                 },
                                                 resolve: {}
                                             }).state('qrCode', {
                                                                   parent: 'trade',
                                                                   url: '/qrCode',
                                                                   data: {
                                                                       authorities: []
                                                                   },
                                                                   views: {
                                                                       'tradeContent@trade': {
                                                                           templateUrl: 'scripts/app/merchant/trademanage/qrCode.html',
                                                                           controller: 'QrCodeController'
                                                                       }
                                                                   },
                                                                   resolve: {}
                                                               })
            });
