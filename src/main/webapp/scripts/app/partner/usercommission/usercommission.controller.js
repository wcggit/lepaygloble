'use strict';

angular.module('lepayglobleApp')
    .controller('usercommissionController', function ($scope, $state, $location) {
        $scope.lefts = [
            {
                pic: 'left-menu-icon iconfont icon-yxzh',
                name: "佣金明细",
                state: "commissioninfo"
            },
            {
                pic: "left-menu-icon iconfont icon-tixianjilu",
                name: "提现记录",
                state: "txjl"
            }
        ];
        if ($location.url().indexOf("commissioninfo") != -1) {
            $scope.currentTab = "commissioninfo";
        } else if ($location.url().indexOf("txjl") != -1) {
            $scope.currentTab = "txjl";
        } else {
            $scope.currentTab = "commissioninfo";
        }
        $scope.onClickTab = function (tab) {
            $scope.currentTab = tab.state;
            $state.go(tab.state);
        };
        $scope.isActiveTab = function (tabState) {
            return tabState == $scope.currentTab;
        };
    }).controller('SampleCtrl', function ($scope, $filter) {
        $scope.dates1 = {
            startDate: moment().subtract(1, 'day'),
            endDate: moment().subtract(1, 'day'),
            timePicker: true
        };
        $scope.ranges = {
            '今天': [moment(), moment()],
            '昨天': [moment().subtract('days', 1),
                   moment().subtract('days', 1)],
            '最近7天': [moment().subtract('days', 7), moment()],
            '最近30天': [moment().subtract('days', 30), moment()],
            '这个月': [moment().startOf('month'), moment().endOf('month')]
        };
  });
