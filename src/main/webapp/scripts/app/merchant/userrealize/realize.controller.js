'use strict';

angular.module('lepayglobleApp')
    .controller('RealizeController', function ($scope, $state, $location) {
                    $scope.lefts = [
                        {
                            pic: 'left-menu-icon iconfont icon-fcstubiao06',
                            name: "数据概览",
                            state: "sjgl"
                        },
                        {
                            pic: "left-menu-icon iconfont icon-jiaoyijilu",
                            name: "佣金输入",
                            state: "yjsr"
                        },
                        {
                            pic: "left-menu-icon iconfont icon-tixianjilu",
                            name: "我的会员",
                            state: "wdhy"
                        }
                    ];
                    $scope.currentTab = $scope.lefts[0].state;
                    $scope.onClickTab = function (tab) {
                        $scope.currentTab = tab.state;
                        $state.go(tab.state);
                    };
                    $scope.isActiveTab = function (tabState) {
                        return tabState == $scope.currentTab;
                    };
                })
