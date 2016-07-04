'use strict';

angular.module('lepayglobleApp')
    .controller('TradeController', function ($scope, $state, $location) {
                    $scope.lefts = [
                        {
                            pic: 'left-menu-icon iconfont icon-fcstubiao06',
                            name: "每日账单",
                            state: "tradeList"
                        },
                        {
                            pic: "left-menu-icon iconfont icon-jiaoyijilu",
                            name: "交易记录",
                            state: "orderList"
                        },
                        {
                            pic: "left-menu-icon iconfont icon-tixianjilu",
                            name: "提现记录",
                            state: "withdrawList"
                        },
                        {
                            pic: "left-menu-icon iconfont icon-erweima",
                            name: "我的乐付码",
                            state: "qrCode"
                        }
                    ];
                    if ($location.url() == "/merchant/trade") {
                        $scope.currentTab = "tradeList";
                    } else if ($location.url().indexOf("orderList") != -1) {
                        $scope.currentTab = "orderList";
                    } else if ($location.url().indexOf("withdrawList") != -1) {
                        $scope.currentTab = "withdrawList";
                    } else if ($location.url().indexOf("qrCode") != -1) {
                        $scope.currentTab = "qrCode";
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
