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
            // .state('withdrawRecord', {
            //     parent: 'merchant_2',
            //     url: '/withdrawRecord',
            //     data: {
            //         authorities: ["merchant"]
            //     },
            //     views: {
            //         'right-content@merchant_2': {
            //             templateUrl: 'scripts/app/merchant_2/homePage/withdrawRecord.html',
            //             controller: 'withdrawRecordController'
            //         }
            //     },
            //     resolve: {
            //     }
            //  });
    });
