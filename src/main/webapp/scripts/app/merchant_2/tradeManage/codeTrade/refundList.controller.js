/**
 * Created by wanghl on 2017/12/18.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('refundListController', function ($scope, $state, $rootScope, $location, Principal, Auth, $http) {

        var currentPage = 1;
        var codeOrderCriteria = {};
        codeOrderCriteria.offset = 1;
        var array = new Array();
        $http.get("/api/merchantUser/merchantsInfo").success(function (response) {
            if (response.status == 200) {
                var data = response.data;
                $scope.myStore = data;
                $scope.defaultId = data[0][0];
                $scope.defaultSid = data[0][2];
                angular.forEach(data, function (data, index) {
                    array[index] = data[0];
                });
                codeOrderCriteria.merchantId = data[0][0];
                loadData();
            } else {
                alert("加载门店错误...");
            }
        });


        function loadData() {
            $http.post("/api/channel/refundList", codeOrderCriteria).success(function (response) {
                if (response.status == 200) {
                    var data = response.data;
                    console.log(data);
                    $scope.page = currentPage;
                    $scope.totalPages = data.totalPages;
                    $scope.totalElements = data.totalElements;
                    $scope.orderList = data.content;
                } else {
                    alert('列表数据错误...');
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
                '昨日9时-今日9时': [GetDateStr(-1),GetDateStr(0)],
                '最近7日': [moment().subtract('days', 6), moment()],
                '最近30日': [moment().subtract('days', 29), moment()]
            }
        }, function (start, end, label) {
        });

        function GetDateStr(AddDayCount) {
            var dd = new Date();
            dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期
            var y = dd.getFullYear();
            var m = dd.getMonth()+1;//获取当前月份的日期
            var d = dd.getDate();
            if(m<10){
                m = "0" + m;
            }
            if(d<10){
                d = "0"+d;
            }
            return y+"/"+m+"/"+d + " 09:00:00";
        }


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
            codeOrderCriteria.offset = page;
            loadData();
        };

        // 设置查询条件
        $scope.searchByCriteria = function () {
            var merchantId = $("#selectStore").val().split("-")[0];
            var completeDate = $("#completeDate").val() == ''?null:$("#completeDate").val();
            var orderSid = $("#orderSid").val() == ''?null:$("#orderSid").val();
            var orderType = $("#orderType").val() == -1?null:$("#orderType").val();
            var orderStatus = $("#orderState").val() == -1?null:$("#orderState").val();
            var startDate,endDate;
            if(completeDate != null){
                startDate  = completeDate.split("-")[0];
                endDate  = completeDate.split("-")[1];
            }else {
                startDate  = null;
                endDate  = null;
            }
            console.log(startDate,endDate);
            codeOrderCriteria.offset = 1;
            codeOrderCriteria.merchantId = merchantId;
            codeOrderCriteria.state = orderStatus;
            codeOrderCriteria.orderType = orderType;
            codeOrderCriteria.orderSid = orderSid;
            codeOrderCriteria.startDate = startDate;
            codeOrderCriteria.endDate = endDate;
            loadData();
        }
    });
