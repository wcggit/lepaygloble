'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('invitationCode', {
                parent: 'merchant_2',
                url: '/invitationCode',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'right-content@merchant_2': {
                        templateUrl: 'scripts/app/merchant_2/commissionIncome/invitationCode/invitationCode.html',
                        controller: 'invitationCodeController'
                    },
                    'invitationCodeTab@invitationCode': {
                        templateUrl: 'scripts/app/merchant_2/commissionIncome/invitationCode/invitationRecord.html',
                        controller: 'invitationRecordController'
                    }
                },
                resolve: {}
            })
            .state('invitationRecord', {
                parent: 'invitationCode',
                url: '/invitationRecord',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'invitationCodeTab@invitationCode': {
                        templateUrl: 'scripts/app/merchant_2/commissionIncome/invitationCode/invitationRecord.html',
                        controller: 'invitationRecordController'
                    }
                },
                resolve: {}
            })
            .state('myInvitationCode', {
                parent: 'invitationCode',
                url: '/myInvitationCode',
                data: {
                    authorities: ["merchant"]
                },
                views: {
                    'invitationCodeTab@invitationCode': {
                        templateUrl: 'scripts/app/merchant_2/commissionIncome/invitationCode/myInvitationCode.html',
                        controller: 'myInvitationCodeController'
                    }
                },
                resolve: {}
            })
    });
