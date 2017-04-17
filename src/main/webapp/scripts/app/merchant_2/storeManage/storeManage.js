'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('storeManage', {
                parent: 'merchant_2',
                url: '/storeManage',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'right-content@merchant_2': {
                        templateUrl: 'scripts/app/merchant_2/storeManage/storeManage.html',
                        controller: 'storeManageController'
                    }
                },
                resolve: {
                }
            });
    });
