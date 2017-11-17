'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('itemsmanager', {
                parent: 'partner',
                url: '/itemsmanager',
                data: {
                    authorities: ['partner']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/partner/itemsmanager/itemsmanager.html',
                        controller: 'itemsmanagerController'
                    },
                    'itemsmanagerContent@itemsmanager': {
                        templateUrl: 'scripts/app/partner/itemsmanager/mymerchant.html',
                        controller: 'myMerchantController'
                    }
                },
                resolve: {}
            }).state('myitems', {
            parent: 'itemsmanager',
            url: '/myitems',
            data: {
                authorities: ['partner']
            },
            views: {
                'itemsmanagerContent@itemsmanager': {
                    templateUrl: 'scripts/app/partner/itemsmanager/myitems.html',
                    controller: 'myItemsController'
                }
            },
            resolve: {}
        }).state('mymerchant', {
            parent: 'itemsmanager',
            url: '/mymerchant',
            data: {
                authorities: ['partner']
            },
            views: {
                'itemsmanagerContent@itemsmanager': {
                    templateUrl: 'scripts/app/partner/itemsmanager/mymerchant.html',
                    controller: 'myMerchantController'
                }
            },
            resolve: {}
        })
        .state('lefuma', {
            parent: 'itemsmanager',
            url: '/lefuma',
            params: {
                id: null
            },
            data: {
                authorities: ["partner"]
            },
            views: {
                'itemsmanagerContent@itemsmanager': {
                    templateUrl: 'scripts/app/partner/itemsmanager/lefuma.html',
                    controller: 'lefumaController'
                }
            },
            resolve: {}
        })
        .state('accountmanager', {
            parent: 'itemsmanager',
            url: '/accountmanager?id',
            data: {
                authorities: ["partner"]
            },
            views: {
                'itemsmanagerContent@itemsmanager': {
                    templateUrl: 'scripts/app/partner/itemsmanager/accountmanager.html',
                    controller: 'accountManagerController'
                }
            },
            resolve: {}
        })
    });
