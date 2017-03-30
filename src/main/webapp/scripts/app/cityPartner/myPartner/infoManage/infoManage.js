'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('cp-infoManage', {
                parent: 'cityPartner',
                url: '/cp-infoManage',
                data: {
                    authorities: ["partner"]
                },
                views: {
                    'right-content@cityPartner': {
                        templateUrl: 'scripts/app/cityPartner/myPartner/infoManage/infoManage.html',
                        controller: 'cp-infoManageController'
                    },
                    'infoManageTab@cp-infoManage': {
                        templateUrl: 'scripts/app/cityPartner/myPartner/infoManage/im_dataOverView.html',
                        controller: 'imDataOverviewController'
                    }
                },
                resolve: {}
            })
            .state('imDataOverview', {
                parent: 'cp-infoManage',
                url: '/imDataOverview',
                data: {
                    authorities: ["partner"]
                },
                views: {
                    'infoManageTab@cp-infoManage': {
                        templateUrl: 'scripts/app/cityPartner/myPartner/infoManage/im_dataOverView.html',
                        controller: 'imDataOverviewController'
                    }
                },
                resolve: {}
            })
            .state('imBasicInfo', {
                parent: 'cp-infoManage',
                url: '/imBasicInfo',
                data: {
                    authorities: ["partner"]
                },
                views: {
                    'infoManageTab@cp-infoManage': {
                        templateUrl: 'scripts/app/cityPartner/myPartner/infoManage/im_basicInfo.html',
                        controller: 'imBasicInfoController'
                    }
                },
                resolve: {}
            });
    });
