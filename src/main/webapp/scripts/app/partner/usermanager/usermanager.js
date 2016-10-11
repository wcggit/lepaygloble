'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
                $stateProvider
                    .state('userManager', {
                        parent: 'partner',
                        url: '/myUser',
                        data: {
                            authorities: []
                        },
                        views: {
                            'content@': {
                                templateUrl: 'scripts/app/partner/usermanager/usermanager.html',
                                controller: 'userManagerController'
                            },
                            'userManagerContent@userManager': {
                                templateUrl: 'scripts/app/partner/usermanager/myUser.html',
                                controller: 'myUserController'
                            }
                        },
                        resolve: {}
                    }).state('myUser', {
                        parent: 'userManager',
                        data: {
                            authorities: []
                        },
                        resolve: {}
                    }).state('inviteUser', {
                        parent: 'userManager',
                        url: '/inviteUser',
                        data: {
                            authorities: []
                        },
                        views: {
                            'userManagerContent@userManager': {
                                templateUrl: 'scripts/app/partner/usermanager/inviteUser.html',
                                controller: 'inviteUserController'
                            }
                        },
                        resolve: {}
                    }).state('marketingAccount', {
                        parent: 'userManager',
                        url: '/marketingAccount',
                        data: {
                            authorities: []
                        },
                        views: {
                            'userManagerContent@userManager': {
                                templateUrl: 'scripts/app/partner/usermanager/marketingAccount.html',
                                controller: 'marketingAccountController'
                            }
                        },
                        resolve: {}
                    })
            });
