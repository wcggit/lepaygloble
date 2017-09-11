/**
 * Created by recoluan on 2016/11/25.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('tixianDetailController', function ($scope, $state, $rootScope, $location, Principal, Auth, $http, Trade,HomePage) {

        var currentPage = 1;
        var withDrawCriteria = {};
        withDrawCriteria.offset = 1;

        HomePage.getMerchantsInfo().then(function (response) {
            var data = response.data;
            $scope.merchants = data;
        });

        loadContent();

        function loadContent() {
            $http.post('/withdraw/merchant_withdraw/findAll',withDrawCriteria).success(function(response){
                if(response.status == 200){
                    var page = response.data;
                    $scope.pulls = page.content;
                    $scope.page = currentPage;
                    $scope.totalPages = page.totalPages;
                }
            });
        }

        //  根据条件进行查询
        $scope.findByCriteria = function () {
            //  日期
            var dateStr = $("#timePicker1").val();
            if (dateStr != null && dateStr != "") {
                var startDate = dateStr.split("-")[0];
                var endDate = dateStr.split("-")[1].trim();
                withDrawCriteria.withDrawStartDate = startDate;
                withDrawCriteria.withDrawEndDate = endDate;
            } else {
                withDrawCriteria.withDrawStartDate = null;
                withDrawCriteria.withDrawEndDate = null;
            }
            //  门店
            var merchant = {};
            var mid = $("#selMerchant").val();
            if(mid!=-1) {
                merchant.id = mid;
                withDrawCriteria.merchant = merchant;
            }else {
                withDrawCriteria.merchant = null;
            }
            // 状态
            var state = $("#selState").val();
            if(state!=-1) {
                withDrawCriteria.state = state;
            }else {
                withDrawCriteria.state = null;
            }
            withDrawCriteria.offset = 1;
            currentPage = 1;
            loadContent();
        }

        // 加载页数
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
            withDrawCriteria.offset = page;
            loadContent();
        };


        $('#timePicker1').daterangepicker({
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
            },
        }, function (start, end, label) {
        });
        $("#timePicker1").val("");
    });


