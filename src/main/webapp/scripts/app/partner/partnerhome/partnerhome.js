'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('partnerhome', {
                parent: 'partner',
                url: '/partnerhome',
                data: {
                    authorities: ["partner"]
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/partner/partnerhome/partnerhome.html',
                        controller: 'partnerHomeController'
                    }
                },
                resolve: {}
            })
            .state('withdrawdetails', {
                parent: 'partner',
                url: '/withdrawdetails',
                data: {
                    authorities: ["partner"]
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/partner/partnerhome/withdrawdetails.html',
                        controller: 'withdrawDetailsController'
                    }
                },
                resolve: {
                    tracker: ['Tracker',
                              function (Tracker) {
                                  Tracker.connect();
                              }
                    ]
                }
            });
    });
