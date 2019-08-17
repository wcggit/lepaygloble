'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('usercommission', {
                parent: 'partner',
                url: '/usercommission',
                data: {
                    authorities: ['partner']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/partner/usercommission/usercommission.html',
                        controller: 'usercommissionController'
                    },
                    'usercommissionContent@usercommission': {
                        templateUrl: 'scripts/app/partner/usercommission/commissioninfo.html',
                        controller: 'commissionInfoController'
                    }
                },
                resolve: {}
            }).state('txjl', {
            parent: 'usercommission',
            url: '/withdrawdetails',
            data: {
                authorities: ['partner']
            },
            views: {
                'usercommissionContent@usercommission': {
                    templateUrl: 'scripts/app/partner/partnerhome/withdrawdetails.html',
                    controller: 'withdrawDetailsController'
                }
            },
            resolve: {}
        }).state('commissioninfo', {
            parent: 'usercommission',
            url: '/commissioninfo',
            data: {
                authorities: ['partner']
            },
            views: {
                'usercommissionContent@usercommission': {
                    templateUrl: 'scripts/app/partner/usercommission/commissioninfo.html',
                    controller: 'commissionInfoController'
                }
            },
            resolve: {}
        })
    });
