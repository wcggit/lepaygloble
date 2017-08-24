/**
 * Created by recoluan on 2016/11/16.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('commissionDetailsController', function ($scope, $state, $rootScope, $location, Principal, Auth, $http) {

        var array = new Array();
        $http.get("/api/merchantUser/merchantsInfo").success(function (response) {
            /*$http.get("api/codeTrade/getMerchants").success(function (response) {*/
            if (response.status == 200) {
                var data = response.data;

                $scope.myStore = data;

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
            var commissionDetailsCriteria = {};
            var a = $("#selectStore").val();
            if (a != "") {
                commissionDetailsCriteria.storeIds = $.makeArray(a);
            } else {
                commissionDetailsCriteria.storeIds = array;
            }
            var completeDate = $("#completeDate").val().split("-");
            commissionDetailsCriteria.startDate = completeDate[0];
            commissionDetailsCriteria.endDate = completeDate[1];
            commissionDetailsCriteria.currentPage = currentPage;
            commissionDetailsCriteria.consumeType = $("#consumeType").val();
            $http.post("/api/commissionDetails/commissionDetailsByMerchantUser", commissionDetailsCriteria).success(function (response) {
                if (response.status == 200) {
                    $scope.commissionDetailsCriteria = response.data.commissionDetails;
                    $scope.commissionDetailsData = response.data;
                    $scope.totalPages = $scope.commissionDetailsData.totalPages;
                    $scope.consumeType = $scope.commissionDetailsData.consumeType;
                    $scope.page = currentPage;
                    if ($scope.commissionDetailsCriteria.length > 0) {
                        $("#notData").hide();
                    } else {
                        $("#notData").show();
                    }

                } else {
                    alert('加载佣金明细数据错误...');
                }
                console.log(response);
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
                    $state.go(stateArr[0]);
                    break;
                case 1:
                    $scope.currentTab1 = true;
                    $scope.ttlWarn1 = false;
                    $state.go(stateArr[1]);
                    break;
                case 2:
                    $scope.currentTab2 = true;
                    $state.go(stateArr[2]);
                    break;
                default:
                    $scope.currentTab3 = true;
                    $scope.ttlWarn2 = false;
                    $state.go(stateArr[3]);
                    break;
            }
        };
    })
