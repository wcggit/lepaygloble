'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('login', {
                parent: 'account',
                params:{data:"show"},
                url: '/login',
                data: {
                    authorities: [],
                    pageTitle: 'Sign in'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/account/login/Loginsd.html',
                        controller: 'LoginController'
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
