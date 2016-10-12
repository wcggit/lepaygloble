'use strict';

angular.module('lepayglobleApp')
    .factory('AuthServerProvider',
             function loginService($http, localStorageService, $window, Tracker) {
                 return {
                     login: function (credentials) {
                         var data = '&j_username=' + encodeURIComponent(credentials.username) +
                                    '&j_password=' + encodeURIComponent(credentials.password) +
                                    '&remember-me=' + credentials.rememberMe + '&submit=Login';
                         if (credentials.isPartner) {
                             data += '&userRole=' + encodeURIComponent("partner");
                         } else {
                             data += '&userRole=' + encodeURIComponent("merchant");
                         }
                         return $http.post('api/authentication', data, {
                             headers: {
                                 'Content-Type': 'application/x-www-form-urlencoded'
                             }
                         }).success(function (response) {
                             return response;
                         });
                     },
                     logout: function () {
                         Tracker.disconnect();
                         // logout from the server
                         $http.post('api/logout').success(function (response) {
                             localStorageService.clearAll();
                             // to get a new csrf token call the api
                             $http.get('api/account');
                             return response;
                         });
                     },
                     getToken: function () {
                         var token = localStorageService.get('token');
                         return token;
                     },
                     hasValidToken: function () {
                         var token = this.getToken();
                         return !!token;
                     }
                 };
             });
