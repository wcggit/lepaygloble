/**
 * Created by recoluan on 2017/3/13.
 */
'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('cp-partnerManage', {
                parent: 'cityPartner',
                url: '/cp-partnerManage',
                data: {
                    authorities: ["partner"]
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
