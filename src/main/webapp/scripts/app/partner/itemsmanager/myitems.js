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
                               url: '/accountmanager',
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
                    .state('createitems1', {
                               parent: 'myitems',
                               url: '/createitems1',
                               data: {
                                   authorities: ["partner"]
                               },
                               views: {
                                   'content@': {
                                       templateUrl: 'scripts/app/partner/itemsmanager/createitems1.html',
                                       controller: 'createItems1Controller'
                                   }
                               },
                               resolve: {}
                           })
                    .state('createitems2', {
                               parent: 'myitems',
                               url: '/createitems2',
                               data: {
                                   authorities: ["partner"]
                               },
                               views: {
                                   'content@': {
                                       templateUrl: 'scripts/app/partner/itemsmanager/createitems2.html',
                                       controller: 'createItems2Controller'
                                   }
                               },
                               resolve: {}
                           })
                    .state('createitems3', {
                               parent: 'myitems',
                               url: '/createitems3',
                               data: {
                                   authorities: ["partner"]
                               },
                               views: {
                                   'content@': {
                                       templateUrl: 'scripts/app/partner/itemsmanager/createitems3.html',
                                       controller: 'createItems3Controller'
                                   }
                               },
                               resolve: {}
                           });
            });
