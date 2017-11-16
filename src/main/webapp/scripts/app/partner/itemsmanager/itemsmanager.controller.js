'use strict';

angular.module('lepayglobleApp')
    .controller('itemsmanagerController', function ($scope, $state, $location) {
        $scope.lefts = [
            {
                pic: 'left-menu-icon iconfont icon-myUser',
                name: "商户列表",
                state: "mymerchant"
            },
            {
                pic: "left-menu-icon iconfont icon-myUser",
                name: "门店数据",
                state: "myitems"
            }
        ];
        if ($location.url().indexOf("mymerchant") != -1) {
            $scope.currentTab = "mymerchant";
        } else if ($location.url().indexOf("myitems") != -1) {
            $scope.currentTab = "myitems";
        } else {
            $scope.currentTab = "mymerchant";
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
