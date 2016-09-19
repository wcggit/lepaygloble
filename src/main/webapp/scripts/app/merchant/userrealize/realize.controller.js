'use strict';

angular.module('lepayglobleApp')
    .controller('RealizeController', function ($scope, $state, $location) {
                    $scope.lefts = [
                        {
                            pic: 'left-menu-icon iconfont icon-shujugailan left-menu-icon1',
                            name: "数据概览",
                            state: "overView"
                        },
                        {
                            pic: "left-menu-icon left-menu-icon2 iconfont icon-jinbi",
                            name: "佣金收入",
                            state: "fees"
                        },
                        {
                            pic: "left-menu-icon iconfont icon-tixianjilu small-font",
                            name: "提现记录",
                            state: "withdrawList"
                        },
                        {
                            pic: "left-menu-icon left-menu-icon3 iconfont icon-vip",
                            name: "我的会员",
                            state: "myMember"
                        }
                    ];

                    //$scope.currentTab = $scope.lefts[0].state;
                    if ($location.url() == "/merchant/realize") {
                        $scope.currentTab = "overView";
                    } else if ($location.url().indexOf("fees") != -1) {
                        $scope.currentTab = "fees";
                    } else if ($location.url().indexOf("member") != -1) {
                        $scope.currentTab = "myMember";
                    }


                    $scope.onClickTab = function (tab) {
                        $scope.currentTab = tab.state;
                        $state.go(tab.state);
                    };
                    $scope.isActiveTab = function (tabState) {
                        return tabState == $scope.currentTab;
                    };
                })
