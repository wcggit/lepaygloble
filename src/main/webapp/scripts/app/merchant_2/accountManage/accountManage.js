'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('accountManage', {
                parent: 'merchant_2',
                url: '/accountManage',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'right-content@merchant_2': {
                        templateUrl: 'scripts/app/merchant_2/accountManage/accountManage.html',
                        controller: 'accountManageController'
                    }
                },
                resolve: {
                }
            })
            .state('withdrawRecord2', {
                parent: 'merchant_2',
                url: '/withdrawRecord2',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'right-content@merchant_2': {
                        templateUrl: 'scripts/app/merchant_2/accountManage/withdrawRecord.html',
                        controller: 'withdrawRecordController'
                    }
                },
                resolve: {
                }
             });
    });
