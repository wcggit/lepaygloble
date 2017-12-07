'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('groupBuyManagement', {
                parent: 'merchant_2',
                url: '/groupBuy',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'right-content@merchant_2': {
                        templateUrl: 'scripts/app/merchant_2/groupBuyManagement/groupBuyManagement.html',
                        controller: 'groupBuyManagementController'
                    },
                    'hxTab@groupBuyManagement': {
                        templateUrl: 'scripts/app/merchant_2/groupBuyManagement/grouphxIndex.html',
                        controller: 'grouphxIndexController'
                    }
                },
                resolve: {}
            })
            .state('grouphxIndex', {
                parent: 'groupBuyManagement',
                url: '/writeOff',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'hxTab@groupBuyManagement': {
                        templateUrl: 'scripts/app/merchant_2/groupBuyManagement/grouphxIndex.html',
                        controller: 'grouphxIndexController'
                    }
                },
                resolve: {}
            })
            .state('hxInfo', {
                parent: 'groupBuyManagement',
                url: '/info',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'hxTab@groupBuyManagement': {
                        templateUrl: 'scripts/app/merchant_2/groupBuyManagement/hxInfo.html',
                        controller: 'hxInfoController'
                    }
                },
                resolve: {}
            })
    });
