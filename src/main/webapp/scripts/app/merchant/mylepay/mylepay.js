'use strict';

angular.module('lepayglobleApp')
    .config(function ($stateProvider) {
                $stateProvider
                    .state('mylepay', {
                               parent: 'merchant',
                               data: {
                                   authorities: ['merchant']
                               },
                               views: {
                                   'content@': {
                                       templateUrl: 'scripts/app/merchant/mylepay/mylepay.html',
                                       controller: 'MyLePayController'
                                   }
                               },
                               resolve: {

                               }
                           });
            });
