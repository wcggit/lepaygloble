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
                    })
            });
