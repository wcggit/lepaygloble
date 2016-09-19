'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
                $stateProvider
                    .state('myPOS', {
                               parent: 'merchant',
                               url: '/myPOS',
                               data: {
                                   authorities: []
                               },
                               views: {
                                   'content@': {
                                       templateUrl: 'scripts/app/merchant/myPOS/myPOS.html',
                                       controller: 'myPOSController'
                                   },
                                   'tradeContent@myPOS': {
                                       templateUrl: 'scripts/app/merchant/myPOS/POStradeList.html',
                                       controller: 'POStradeListController'
                                   }
                               },
                               resolve: {}
                           }).state('POStradeList', {
                                        parent: 'myPOS',
                                        data: {
                                            authorities: []
                                        },
                                                    resolve: {}
                           }).state('POSorderList', {
                                parent: 'myPOS',
                                params: {
                                    date: null
                                },
                                url: '/POSorderList',
                                data: {
                                    authorities: []
                                },
                                views: {
                                    'tradeContent@myPOS': {
                                        templateUrl: 'scripts/app/merchant/myPOS/POSorderList.html',
                                        controller: 'POSorderListController'
                                    }
                                },
                                resolve: {}
                           }).state('POSmanager', {
                                parent: 'myPOS',
                                params: {
                                    date: null
                                },
                                url: '/POSmanager',
                                data: {
                                    authorities: []
                                },
                                views: {
                                    'tradeContent@myPOS': {
                                        templateUrl: 'scripts/app/merchant/myPOS/POSmanager.html',
                                        controller: 'POSmanagerController'
                                    }
                                },
                                resolve: {}
                            })
            })
