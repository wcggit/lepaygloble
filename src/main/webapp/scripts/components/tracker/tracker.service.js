'use strict';

angular.module('lepayglobleApp')
    .factory('Tracker', function ($rootScope, $cookies, $http, $q, Principal) {
                 var stompClient = null;
                 var subscriber = null;
                 var listener = $q.defer();
                 var connected = $q.defer();
                 var bindWx = $q.defer();
                 var alreadyConnectedOnce = false;

                 function sendActivity() {
                     if (stompClient != null && stompClient.connected) {
                         stompClient
                             .send('/topic/activity',
                                   {},
                                   JSON.stringify({'page': $rootScope.toState.name}));
                     }
                 }

                 return {
                     connect: function () {
                         //building absolute path so that websocket doesnt fail when deploying with
                         // a context path
                         if (!alreadyConnectedOnce) {
                             var loc = window.location;
                             var url = '//' + loc.host + loc.pathname + 'websocket/tracker';
                             var socket = new SockJS(url);
                             stompClient = Stomp.over(socket);
                             var headers = {};
                             headers['X-CSRF-TOKEN'] = $cookies[$http.defaults.xsrfCookieName];
                             stompClient.connect(headers, function (frame) {
                                 //注册合伙人绑定微信号事件
                                 Principal.identity().then(function (account) {
                                     if (account != null && account.login != null) {
                                         alreadyConnectedOnce = true;
                                         subscriber =
                                         stompClient.subscribe("/user/" + account.login + "/reply",
                                                               function (data) {
                                                                   bindWx.notify(1);
                                                               });
                                     }
                                 });
                                 //if (!alreadyConnectedOnce) {
                                 //    $rootScope.$on('$stateChangeStart', function (event) {
                                 //        sendActivity();
                                 //    });
                                 //    alreadyConnectedOnce = true;
                                 //}
                             });
                         }
                     },
                     subscribe: function () {
                         connected.promise.then(function () {
                             subscriber = stompClient.subscribe("/topic/tracker", function (data) {
                                 listener.notify(JSON.parse(data.body));
                             });
                         }, null, null);
                     },
                     //subscribeOffLineOrder: function() {
                     //
                     //},
                     unsubscribe: function () {
                         if (subscriber != null) {
                             subscriber.unsubscribe();
                         }
                         listener = $q.defer();
                     },
                     receive: function () {
                         return listener.promise;
                     },
                     receiveWx: function () {
                         return bindWx.promise;
                     },
                     sendActivity: function () {
                         if (stompClient != null) {
                             sendActivity();
                         }
                     },
                     disconnect: function () {
                         alreadyConnectedOnce = false;
                         if (stompClient != null) {
                             if (stompClient.connected) {
                                 stompClient.disconnect();
                             }
                             stompClient = null;
                         }
                     }
                 };
             });
