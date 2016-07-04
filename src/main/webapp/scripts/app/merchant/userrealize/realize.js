'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
                $stateProvider
                    .state('realize', {
                               parent: 'merchant',
                               url: '/realize',
                               data: {
                                   authorities: ['merchant']
                               },
                               views: {
                                   'content@': {
                                       templateUrl: 'scripts/app/merchant/userrealize/realize.html',
                                       controller: 'RealizeController'
                                   },
                                   'realizeContent@realize': {
                                       templateUrl: 'scripts/app/merchant/userrealize/dataOverView.html',
                                       controller: 'OverViewController'
                                   }
                               },
                               resolve: {}
                           }).state('OverView', {
                                                        parent: 'realize',
                                                        data: {
                                                            authorities: ['merchant']
                                                        },
                                                        resolve: {}
                                                    })
                                    //}).state('orderList', {
                                    //             parent: 'trade',
                                    //             url: '/orderList',
                                    //             data: {
                                    //                 authorities: []
                                    //             },
                                    //             views: {
                                    //                 'tradeContent@trade': {
                                    //                     templateUrl: 'scripts/app/merchant/trademanage/orderList.html',
                                    //                     controller: 'OrderListController'
                                    //                 }
                                    //             },
                                    //             resolve: {}
                                    //         }).state('withdrawList', {
                                    //                      parent: 'trade',
                                    //                      url: '/withdrawList',
                                    //                      data: {
                                    //                          authorities: []
                                    //                      },
                                    //                      views: {
                                    //                          'tradeContent@trade': {
                                    //                              templateUrl: 'scripts/app/merchant/trademanage/withdraw.html',
                                    //                              controller: 'TradeController'
                                    //                          }
                                    //                      },
                                    //                      resolve: {}
                                    //                  }).state('qrCode', {
                                    //                               parent: 'trade',
                                    //                               url: '/qrCode',
                                    //                               data: {
                                    //                                   authorities: []
                                    //                               },
                                    //                               views: {
                                    //                                   'tradeContent@trade': {
                                    //                                       templateUrl: 'scripts/app/merchant/trademanage/qrCode.html',
                                    //                                       controller: 'QrCodeController'
                                    //                                   }
                                    //                               },
                                    //                               resolve: {}
                                    //                           })
            });
