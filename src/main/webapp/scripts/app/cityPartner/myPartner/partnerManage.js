'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('cp-partnerManage', {
                parent: 'cityPartner',
                url: '/cp-partnerManage',
                data: {
                    authorities: ["partnerManager"]
                },
                views: {
                    'right-content@cityPartner': {
                        templateUrl: 'scripts/app/cityPartner/myPartner/partnerManage.html',
                        controller: 'cp-partnerManagerController'
                    }
                },
                resolve: {
                }
            })
    });
