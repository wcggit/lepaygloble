/**
 * Created by recoluan on 2016/11/16.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('commissionDetailsController', function ($scope, $state, $rootScope, $location, Principal, Auth, $http) {
        var commissionDetailsCriteria = {};
        var array = new Array();
        $http.get("/api/merchantUser/merchantsInfo").success(function (response) {
            if (response.status == 200) {
                var data = response.data;
                $scope.myStore = data;
                $scope.defaultId = data[0][0];
                angular.forEach(data, function (data, index) {
                    array[index] = data[0];
                });
                $scope.loadCommissionDetailsInfo();
            } else {
                alert("加载门店错误...");
            }
        });

        var currentPage = 1;
        $scope.loadCommissionDetailsInfo = function () {
            var mid = $("#selectStore").val();
            if (mid != "" && mid!=null) {
                commissionDetailsCriteria.merchantId = mid;
            } else {
                commissionDetailsCriteria.merchantId = $scope.defaultId;
            }
            var completeDate = $("#completeDate").val().split("-");
            commissionDetailsCriteria.startDate = completeDate[0];
            commissionDetailsCriteria.endDate = completeDate[1];
            commissionDetailsCriteria.currentPage = currentPage;
            if(commissionDetailsCriteria.consumeType == null || commissionDetailsCriteria.consumeType =='') {
                commissionDetailsCriteria.consumeType = 1;
            }
            $http.post("/api/commissionDetails/commissionDetailsByMerchantUser", commissionDetailsCriteria).success(function (response) {
                if (response.status == 200) {
                    $scope.commissionDetailsCriteria = response.data.commissionDetails;
                    $scope.commissionDetailsData = response.data;
                    $scope.totalPages = $scope.commissionDetailsData.totalPages;
                    $scope.consumeType = $scope.commissionDetailsData.consumeType;
                    $scope.page = currentPage;
                } else {
                    alert('加载佣金明细数据错误...');
                }
            });
        };

        $('#completeDate').daterangepicker({
            timePicker: true, //是否显示小时和分钟
            timePickerIncrement: 1, //时间的增量，单位为分钟
            opens: 'right', //日期选择框的弹出位置
            startDate: moment().format('YYYY/MM/DD HH:mm:00'),
            endDate: moment().format('YYYY/MM/DD HH:mm:59'),
            format: 'YYYY/MM/DD HH:mm:ss', //控件中from和to 显示的日期格式
            ranges: {
                '最近1小时': [moment().subtract('hours', 1), moment()],
                '今日': [moment().startOf('day'), moment()],
                '昨日': [moment().subtract('days', 1).startOf('day'),
                    moment().subtract('days', 1).endOf('day')],
                '最近7日': [moment().subtract('days', 6), moment()],
                '最近30日': [moment().subtract('days', 29), moment()]
            }
        }, function (start, end, label) {
            console.log(start + "--------" + end);
        });

        $scope.loadPage = function (page) {
            if (page == 0) {
                return;
            }
            if (page > $scope.totalPages) {
                return;
            }
            if (currentPage == $scope.totalPages && page == $scope.totalPages) {
                return;
            }
            if (currentPage == 1 && page == 1) {
                return;
            }
            currentPage = page;
            $scope.loadCommissionDetailsInfo();
        };


        $scope.searchByCriteria = function () {
            currentPage = 1;
            $scope.loadCommissionDetailsInfo();
        };

        $scope.currentTab0 = true;
        $scope.currentTab1 = $scope.currentTab2 = $scope.currentTab3 = false;
        $scope.priviousState = 0;
        $scope.currentState = 0;
        $scope.onClickTab = function (index) {
            $scope.priviousState = $scope.currentState;
            $scope.currentState = index;
            switch ($scope.priviousState) {
                case 0:
                    $scope.currentTab0 = false;
                    break;
                case 1:
                    $scope.currentTab1 = false;
                    break;
                case 2:
                    $scope.currentTab2 = false;
                    break;
                default:
                    $scope.currentTab3 = false;
            }
            switch ($scope.currentState) {
                case 0:
                    $scope.currentTab0 = true;
                    commissionDetailsCriteria.consumeType = 1;
                    $scope.searchByCriteria();
                    break;
                case 1:
                    $scope.currentTab1 = true;
                    $scope.ttlWarn1 = false;
                    commissionDetailsCriteria.consumeType = 2;
                    $scope.searchByCriteria();
                    break;
                default:
                    $scope.currentTab0 = true;
                    commissionDetailsCriteria.consumeType = 1;
                    break;
            }
        };
    })
