'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('im_withdrawdetails', {
                parent: 'cp-infoManage',
                url: '/withdrawdetails',
                data: {
                    authorities: ["partnerManager"]
                },
                params: {
                    partnerSid2: {value: null}
                },
                views: {
                    'infoManageTab@cp-infoManage': {
                        templateUrl: 'scripts/app/cityPartner/myPartner/infoManage/im_withdrawdetails.html',
                        controller: 'imWithdrawDetailsController'
                    }
                },
                resolve: {}
            });
            // .state('lefuma', {
            //     parent: 'myitems',
            //     url: '/lefuma',
            //     params: {
            //         id: null
            //     },
            //     data: {
            //         authorities: ["partner"]
            //     },
            //     views: {
            //         'content@': {
            //             templateUrl: 'scripts/app/partner/itemsmanager/lefuma.html',
            //             controller: 'lefumaController'
            //         }
            //     },
            //     resolve: {}
            // })
            // .state('accountmanager', {
            //     parent: 'myitems',
            //     url: '/accountmanager?id',
            //     data: {
            //         authorities: ["partner"]
            //     },
            //     views: {
            //         'content@': {
            //             templateUrl: 'scripts/app/partner/itemsmanager/accountmanager.html',
            //             controller: 'accountManagerController'
            //         }
            //     },
            //     resolve: {}
            // })
            // .state('createitems', {
            //     parent: 'myitems',
            //     url: '/createitems',
            //     params: {
            //         id: null
            //     },
            //     data: {
            //         authorities: ["partner"]
            //     },
            //     views: {
            //         'content@': {
            //             templateUrl: 'scripts/app/partner/itemsmanager/createitems.html',
            //             controller: 'createItemsController'
            //         }
            //     },
            //     resolve: {
            //         check: ['Partner', '$stateParams',
            //             function (Partner, $stateParams) {
            //                 return Partner.countParnterBindMerchant($stateParams.id);
            //             }]
            //     }
            // })
    });
