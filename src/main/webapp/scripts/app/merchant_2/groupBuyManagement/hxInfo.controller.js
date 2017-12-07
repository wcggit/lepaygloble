/**
 * Created by recoluan on 2016/11/25.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('hxInfoController', function ($scope, $state, $rootScope, $location, Principal, Auth, $http) {
        var array = new Array();
        var currentPage = 1;
        var olOrderCriteria = {};
        olOrderCriteria.offset = 1;

        $http.get("/api/merchantUser/merchantsInfo").success(function (response) {
            if (response.status == 200) {
                var data = response.data;
                $scope.myStore = data;
                $scope.defaultId = data[0][0];
                olOrderCriteria.merchantId = data[0][0];
                showData();
            } else {
                alert("加载门店错误...");
            }
        });

        function showData() {
            $http.post("/api/grouponCode/findByCriteria",olOrderCriteria).success(function (response) {
                if (response.status == 200) {
                    var data = response.data;
                    $scope.hx = data.content;
                    $scope.page = currentPage;
                    $scope.totalPages = data.totalPages;
                    $scope.totalData = data.totalElements;
                } else {
                    alert("加载数据错误...");
                }
            });
        }

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
            olOrderCriteria.offset = page;
            showData();
        };



        // 设置查询条件
        $scope.searchByCriteria = function () {
            var selectStore = $("#selectStore").val();
            var selectStatus = $("#selectStatus").val();
            var phone = $("#phone").val();
            var orderMoney = $("#orderMoney").val();
            olOrderCriteria.offset = 1;
            olOrderCriteria.merchantId = selectStore;
            if(selectStatus != -1){
                olOrderCriteria.state = selectStatus;
            }else {
                olOrderCriteria.state = null;
            }
            if(phone != ''){
                olOrderCriteria.phoneNumber = phone;
            }else {
                olOrderCriteria.phoneNumber = null;
            }
            if(orderMoney != ''){
                olOrderCriteria.orderPrice = (orderMoney)*100;
            }else {
                olOrderCriteria.orderPrice = null;
            }
            console.log()
            showData();
        }

    });
