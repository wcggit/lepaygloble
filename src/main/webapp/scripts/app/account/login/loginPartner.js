'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('loginPartner', {
                parent: 'account',
                url: '/loginPartner',
                data: {
                    authorities: [],
                    pageTitle: 'Sign in'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/account/login/LoginPartner.html',
                        controller: 'LoginPartnerController'
                    }
                },
                resolve: {
                    authorize: ['Auth',
                                function (Auth) {
                                    return Auth.authorize();
                                }
                    ]
                }
            });
    });
