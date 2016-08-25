'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
                $stateProvider
                    .state('myitems', {
                               parent: 'partner',
                               url: '/myitems',
                               data: {
                                   authorities: ["partner"]
                               },
                               views: {
                                   'content@': {
                                       templateUrl: 'scripts/app/partner/itemsmanager/myitems.html',
                                       controller: 'myItemsController'
                                   }
                               },
                               resolve: {}
                           })
                    .state('lefuma', {
                               parent: 'myitems',
                               url: '/lefuma',
                               params: {
                                   id: null
                               },
                               data: {
                                   authorities: ["partner"]
                               },
                               views: {
                                   'content@': {
                                       templateUrl: 'scripts/app/partner/itemsmanager/lefuma.html',
                                       controller: 'lefumaController'
                                   }
                               },
                               resolve: {}
                           })
                    .state('accountmanager', {
                               parent: 'myitems',
                               url: '/accountmanager?id',
                               data: {
                                   authorities: ["partner"]
                               },
                               views: {
                                   'content@': {
                                       templateUrl: 'scripts/app/partner/itemsmanager/accountmanager.html',
                                       controller: 'accountManagerController'
                                   }
                               },
                               resolve: {}
                           })
                    .state('createitems', {
                               parent: 'myitems',
                               url: '/createitems',
                               params: {
                                   id: null
                               },
                               data: {
                                   authorities: ["partner"]
                               },
                               views: {
                                   'content@': {
                                       templateUrl: 'scripts/app/partner/itemsmanager/createitems.html',
                                       controller: 'createItemsController'
                                   }
                               },
                               resolve: {
                                   check: ['Partner', '$stateParams',
                                           function (Partner, $stateParams) {
                                             return  Partner.countParnterBindMerchant($stateParams.id);
                                           }]
                               }
                           })
            });
