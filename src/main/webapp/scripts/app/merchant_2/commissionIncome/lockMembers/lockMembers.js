'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('lockMembers', {
                parent: 'merchant_2',
                url: '/lockMembers',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'right-content@merchant_2': {
                        templateUrl: 'scripts/app/merchant_2/commissionIncome/lockMembers/lockMembers.html',
                        controller: 'lockMembersController'
                    }
                },
                resolve: {
                }
            })
    });
