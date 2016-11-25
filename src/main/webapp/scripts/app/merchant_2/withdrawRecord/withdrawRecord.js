'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('withdrawRecord', {
                parent: 'merchant_2',
                url: '/withdrawRecord',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'right-content@merchant_2': {
                        templateUrl: 'scripts/app/merchant_2/withdrawRecord/withdrawRecord.html',
                        controller: 'withdrawRecordController'
                    }
                },
                resolve: {
                }
            });
    });
