'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
                $stateProvider
                    .state('realize', {
                               parent: 'merchant',
                               url: '/realize',
                               data: {
                                   authorities: ['merchant']
                               },
                               views: {
                                   'content@': {
                                       templateUrl: 'scripts/app/merchant/userrealize/realize.html',
                                       controller: 'RealizeController'
                                   },
                                   'realizeContent@realize': {
                                       templateUrl: 'scripts/app/merchant/userrealize/dataOverView.html',
                                       controller: 'OverViewController'
                                   }
                               },
                               resolve: {}
                           }).state('overView', {
                                        parent: 'realize',
                                        data: {
                                            authorities: ['merchant']
                                        },
                                        resolve: {}
                                    }).state('fees', {
                                                 parent: 'realize',
                                                 url: '/fees',
                                                 data: {
                                                     authorities: ['merchant']
                                                 },
                                                 views: {
                                                     'realizeContent@realize': {
                                                         templateUrl: 'scripts/app/merchant/userrealize/fees.html',
                                                         controller: 'FeesController'
                                                     }
                                                 },
                                                 resolve: {}
                                             }).state('myMember', {
                                                                          parent: 'realize',
                                                                          url: '/member',
                                                                          data: {
                                                                              authorities: ['merchant']
                                                                          },
                                                                          views: {
                                                                              'realizeContent@realize': {
                                                                                  templateUrl: 'scripts/app/merchant/userrealize/member.html',
                                                                                  controller: 'MemberController'
                                                                              }
                                                                          },
                                                                          resolve: {}
                                                                      })
            })
;
