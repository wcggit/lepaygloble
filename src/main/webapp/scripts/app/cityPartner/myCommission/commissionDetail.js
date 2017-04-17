/**
 * Created by recoluan on 2017/3/15.
 */
'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('cp-commissionDetail', {
                parent: 'cityPartner',
                url: '/cp-commissionDetail',
                data: {
                    authorities: ["partnerManager"]
                },
                views: {
                    'right-content@cityPartner': {
                        templateUrl: 'scripts/app/cityPartner/myCommission/commissionDetail.html',
                        controller: 'cp-commissionDetailController'
                    }
                },
                resolve: {
                }
            })
    });
