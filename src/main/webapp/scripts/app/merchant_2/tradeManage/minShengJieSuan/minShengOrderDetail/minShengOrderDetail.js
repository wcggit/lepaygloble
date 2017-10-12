'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('minShengOrderDetail', {
                parent: 'merchant_2',
                url: '/minShengOrderDetail',
                data: {
                    authorities: ["merchant"]
                },
                params: {
                    orderId:null,
                    cmbcMerNo: null,
                    settleDate: null,
                    totalActual:null,
                    transState:null
                },
                views: {
                    'right-content@merchant_2': {
                        templateUrl: 'scripts/app/merchant_2/tradeManage/minShengJieSuan/minShengOrderDetail/minShengOrderDetail.html',
                        controller: 'minShengOrderDetailController'
                    }
                },
                resolve: {}
            })

    });
