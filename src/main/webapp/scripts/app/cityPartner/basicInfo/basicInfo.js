'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('cp-basicInfo', {
                parent: 'cityPartner',
                url: '/cp-basicInfo',
                data: {
                    authorities: ["partnerManager"]
                },
                views: {
                    'right-content@cityPartner': {
                        templateUrl: 'scripts/app/cityPartner/basicInfo/basicInfo.html',
                        controller: 'cp-basicInfoController'
                    }
                },
                resolve: {
                }
            })
    });
