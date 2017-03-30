/**
 * Created by recoluan on 2017/3/15.
 */
'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('cp-withdrawRecord', {
                parent: 'cityPartner',
                url: '/cp-withdrawRecord',
                data: {
                    authorities: ["partner"]
                },
                views: {
                    'right-content@cityPartner': {
                        templateUrl: 'scripts/app/cityPartner/withdrawRecord/withdrawRecord.html',
                        controller: 'cp-withdrawRecordController'
                    }
                },
                resolve: {
                }
            })
    });
