'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
                $stateProvider
                    .state('userManager', {
                               parent: 'partner',
                               url: '/myUser',
                               data: {
                                   authorities: ['partner']
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
                                            authorities: ['partner']
                                        },
                                        resolve: {}
                                    }).state('inviteUser', {
                                                 parent: 'userManager',
                                                 url: '/inviteUser',
                                                 data: {
                                                     authorities: ['partner']
                                                 },
                                                 views: {
                                                     'userManagerContent@userManager': {
                                                         templateUrl: 'scripts/app/partner/usermanager/inviteUser.html',
                                                         controller: 'inviteUserController'
                                                     }
                                                 },
                                                 resolve: {}
                                             }).state('robWelfare', {
                                                          parent: 'userManager',
                                                          url: '/robWelfare',
                                                          data: {
                                                              authorities: ['partner']
                                                          },
                                                          views: {
                                                              'userManagerContent@userManager': {
                                                                  templateUrl: 'scripts/app/partner/usermanager/robWelfare.html',
                                                                  controller: 'robWelfareController'
                                                              }
                                                          },
                                                          resolve: {}
                                                      }).state('marketingAccount', {
                                                                   parent: 'userManager',
                                                                   url: '/marketingAccount',
                                                                   data: {
                                                                       authorities: ['partner']
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
