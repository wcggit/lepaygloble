///**
// * Created by recoluan on 2016/6/1 0001.
// */
////var myApp = angular.module("myseekApp", ['ui.router','ngBootstrap']);
//
////myApp.config(function ($stateProvider, $urlRouterProvider) {
//
//    $urlRouterProvider.when("", "/home");
//
//    $stateProvider
//        // 父路由
//        .state("home", {
//            url:"/home",
//            views: {
//                "header@": {
//                    templateUrl: "./tpls/head/head.html"
//                },
//                "content@": {
//                    templateUrl: "./tpls/zhsz/zhsz.html"
//                }
//            }
//        })
//
//            // 子路由-账户设置
//            .state("zhsz", {
//                parent:"home",
//                url:"/zhsz",
//                views: {
//                    "content@": {
//                        templateUrl: "./tpls/zhsz/zhsz.html",
//                    }
//                }
//            })
//
//            //子路由-交易管理
//            .state("jygl", {
//                parent:"home",
//                url:"/jygl",
//                views: {
//                    "content@": {
//                        templateUrl: "./tpls/jygl/jygl.html"
//                    },
//                    "jyglLeft@jygl": {
//                        templateUrl: "./tpls/jygl/jyglLeft.html"
//                    },
//                    "jyglRight@jygl": {
//                        templateUrl: "./tpls/jygl/mrtz.html"
//                    }
//                }
//            })
//
//                // 孙路由-每日提现
//                .state("mrtz", {
//                    parent:"jygl",
//                    url:"/jygl/metz",
//                    views: {
//                        "jyglRight@jygl": {
//                            templateUrl: "./tpls/jygl/mrtz.html"
//                        }
//                    }
//                })
//                //孙路由-交易记录
//                .state("jyjl", {
//                    parent:"jygl",
//                    url:"/jygl/jyjl",
//                    views: {
//                        "jyglRight@jygl": {
//                            templateUrl: "./tpls/jygl/jyjl.html"
//                        }
//                    }
//                })
//                //孙路由-提现记录
//                .state("txjl", {
//                    parent:"jygl",
//                    url:"/jygl/txjl",
//                    views: {
//                        "jyglRight@jygl": {
//                            templateUrl: "./tpls/jygl/txjl.html"
//                        }
//                    }
//                })
//                //孙路由-提现记录
//                .state("wdlfm", {
//                    parent:"jygl",
//                    url:"/jygl/wdlfm",
//                    views: {
//                        "jyglRight@jygl": {
//                            templateUrl: "./tpls/jygl/wdlfm.html"
//                        }
//                    }
//                })
//
//            //子路由-会员变现
//            .state("hybx", {
//                parent:"home",
//                url:"/hybx",
//                views: {
//                    "content@": {
//                        templateUrl: "./tpls/hybx/hybx.html"
//                    },
//                    "hybxLeft@hybx": {
//                        templateUrl: "./tpls/hybx/hybxLeft.html"
//                    },
//                    "hybxRight@hybx": {
//                        templateUrl: "./tpls/hybx/yjsr.html"
//                    }
//                }
//            })
//
//                //孙路由-我的会员
//                .state("wdhy", {
//                    parent:"hybx",
//                    url:"/hybx/wdhy",
//                    views: {
//                        "hybxRight@hybx": {
//                            templateUrl: "./tpls/hybx/wdhy.html"
//                        }
//                    }
//                })
//                //孙路由-佣金收入
//                .state("yjsr", {
//                    parent:"hybx",
//                    url:"/hybx/yjsr",
//                    views: {
//                        "hybxRight@hybx": {
//                            templateUrl: "./tpls/hybx/yjsr.html"
//                        },
//                        "yjsrContent@yjsr": {
//                            templateUrl: "./tpls/hybx/yjsrDownline.html"
//                        }
//                    }
//                })
//                    //重孙路由-线上佣金
//                    .state("yjsrOnline", {
//                        parent:"yjsr",
//                        url:"/hybx/yjsr/yjsrOnline",
//                        views: {
//                            "yjsrContent@yjsr": {
//                                templateUrl: "./tpls/hybx/yjsrOnline.html"
//                            }
//                        }
//                    })
//                    //重孙路由-线下佣金
//                    .state("yjsrDownline", {
//                        parent:"yjsr",
//                        url:"/hybx/yjsr/yjsrDownline",
//                        views: {
//                            "yjsrContent@yjsr": {
//                                templateUrl: "./tpls/hybx/yjsrDownline.html"
//                            }
//                        }
//                    })
//});
