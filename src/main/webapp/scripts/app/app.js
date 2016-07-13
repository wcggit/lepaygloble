'use strict';

angular.module('lepayglobleApp', ['LocalStorageModule',
                                  'ngResource',
                                  //'ngCookies',
                                  //'ngAria',
                                  'ngCacheBuster',
                                  //'ngFileUpload',
    // jhipster-needle-angularjs-add-module JHipster will add new module here
                                  'ui.bootstrap', 'ui.router',
                                  //'infinite-scroll',
                                  'angular-loading-bar', 'ngBootstrap'])//

    .run(function ($rootScope, $location,$window, $http, $state, Auth, Principal, ENV, VERSION) {
             $rootScope.disappear = function () {
                 $rootScope.personal = false;
             }

             $rootScope.timePickerObj={
                 "ranges": {
                     "今天": [moment().format("YYYY/MM/DD 00:00:00"), moment().format("YYYY/MM/DD 23:59:59")],
                     "昨天": [moment().subtract('days', 1).format("YYYY/MM/DD 00:00:00"), moment().subtract('days', 1).format("YYYY/MM/DD 23:59:59")],
                     "最近七天": [moment().subtract('days', 6).format("YYYY/MM/DD 00:00:00"), moment().format("YYYY/MM/DD 23:59:59")],
                     "最近30天": [moment().subtract('days', 29).format("YYYY/MM/DD 00:00:00"), moment().format("YYYY/MM/DD 23:59:59")]
                 },
                 "locale": {
                     "direction": "ltr",
                     "format": "YYYY/MM/DD HH:mm:ss",
                     "separator": " - ",
                     "applyLabel": "确认",
                     "cancelLabel": "取消",
                     "fromLabel": "从",
                     "toLabel": "至",
                     "customRangeLabel": "自定义",
                     "daysOfWeek": [
                         "日",
                         "一",
                         "二",
                         "三",
                         "四",
                         "五",
                         "六"
                     ],
                     "monthNames": [
                         "一月",
                         "二月",
                         "三月",
                         "四月",
                         "五月",
                         "六月",
                         "七月",
                         "八月",
                         "九月",
                         "十月",
                         "十一月",
                         "十二月"
                     ],
                     "firstDay": 1
                 },
                 "locale1": {
                     "direction": "ltr",
                     "format": "YYYY/MM/DD",
                     "separator": " - ",
                     "applyLabel": "确认",
                     "cancelLabel": "取消",
                     "fromLabel": "从",
                     "toLabel": "至",
                     "customRangeLabel": "自定义",
                     "daysOfWeek": [
                         "日",
                         "一",
                         "二",
                         "三",
                         "四",
                         "五",
                         "六"
                     ],
                     "monthNames": [
                         "一月",
                         "二月",
                         "三月",
                         "四月",
                         "五月",
                         "六月",
                         "七月",
                         "八月",
                         "九月",
                         "十月",
                         "十一月",
                         "十二月"
                     ],
                     "firstDay": 1
                 }
             }

             $rootScope.ENV = ENV;
             $rootScope.VERSION = VERSION;
             $rootScope.$on('$stateChangeStart', function (event, toState, toStateParams) {
                 $rootScope.toState = toState;
                 $rootScope.toStateParams = toStateParams;

                 if (Principal.isIdentityResolved()) {
                     Auth.authorize();
                 }

             });

             $rootScope.$on('$stateChangeSuccess',
                            function (event, toState, toParams, fromState, fromParams) {
                                var titleKey = '乐加支付';

                                // Remember previous state unless we've been redirected to login or
                                // we've just reset the state memory after logout. If we're
                                // redirected to login, our previousState is already set in the
                                // authExpiredInterceptor. If we're going to login directly, we
                                // don't want to be sent to some previous state anyway
                                if (toState.name != 'login') {
                                    $rootScope.previousStateName = fromState.name;
                                    $rootScope.previousStateParams = fromParams;
                                }

                                // Set the page title key to the one configured in state or use
                                // default one
                                if (toState.data.pageTitle) {
                                    titleKey = toState.data.pageTitle;
                                }
                                $window.document.title = titleKey;
                            });

             $rootScope.back = function () {
                 // If previous state is 'activate' or do not exist go to 'home'
                 if ($rootScope.previousStateName === 'activate'
                     || $state.get($rootScope.previousStateName) === null) {
                     $state.go('home');
                 } else {
                     $state.go($rootScope.previousStateName, $rootScope.previousStateParams);
                 }
             };
         })
    .config(function ($stateProvider, $urlRouterProvider, $httpProvider, $locationProvider,
                      httpRequestInterceptorCacheBusterProvider, AlertServiceProvider) {
                // uncomment below to make alerts look like toast
                //AlertServiceProvider.showAsToast(true);

                //enable CSRF
                $httpProvider.defaults.xsrfCookieName = 'CSRF-TOKEN';
                $httpProvider.defaults.xsrfHeaderName = 'X-CSRF-TOKEN';

                //Cache everything except rest api requests
                httpRequestInterceptorCacheBusterProvider.setMatchlist([/.*api.*/, /.*protected.*/],
                                                                       true);

                $urlRouterProvider.otherwise('/');
                $stateProvider.state('site', {
                    'abstract': true,
                    views: {},
                    resolve: {
                        authorize: ['Auth',
                                    function (Auth) {
                                        return Auth.authorize();
                                    }
                        ]
                    }
                });

                $httpProvider.interceptors.push('errorHandlerInterceptor');
                $httpProvider.interceptors.push('authExpiredInterceptor');
                $httpProvider.interceptors.push('notificationInterceptor');
                // jhipster-needle-angularjs-add-interceptor JHipster will add new application
                // interceptor here

            })
    // jhipster-needle-angularjs-add-config JHipster will add new application configuration here
    .config(['$urlMatcherFactoryProvider', function ($urlMatcherFactory) {
                $urlMatcherFactory.type('boolean', {
                    name: 'boolean',
                    decode: function (val) {
                        return val == true ? true : val == "true" ? true : false
                    },
                    encode: function (val) {
                        return val ? 1 : 0;
                    },
                    equals: function (a, b) {
                        return this.is(a) && a === b;
                    },
                    is: function (val) {
                        return [true, false, 0, 1].indexOf(val) >= 0
                    },
                    pattern: /bool|true|0|1/
                });
            }]);
