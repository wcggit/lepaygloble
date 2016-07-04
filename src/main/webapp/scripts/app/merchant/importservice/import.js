'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
                $stateProvider
                    .state('import', {
                               parent: 'merchant',
                               url: '/merchant/import',
                               data: {
                                   authorities: ["merchant"]
                               },
                               views: {
                                   'content@': {
                                       templateUrl: 'scripts/app/merchant/importservice/import.html',
                                       controller: 'MyLePayController'
                                   }
                               },
                               resolve: {
                               }
                           });
            });
