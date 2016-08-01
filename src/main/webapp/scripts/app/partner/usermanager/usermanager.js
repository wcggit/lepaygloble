'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
                $stateProvider
                    .state('usermanager', {
                               parent: 'partner',
                               url: '/usermanager',
                               data: {
                                   authorities: ["partner"]
                               },
                               views: {
                                   'content@': {
                                       templateUrl: 'scripts/app/partner/usermanager/usermanager.html',
                                       controller: 'UserManagerController'
                                   }
                               },
                               resolve: {
                               }
                           });
            });
