/**
 * Created by recoluan on 2016/11/25.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('storeBillingController', function ($scope, $state, $rootScope, $location,Principal,Auth,$http,LejiaBilling) {
        //  单击查询
        $scope.findByDate = function() {
            var date = $("#date_picker").val();
            if(date==''||date==null) {
                alert('请选择日期 ^_^ ');
                return;
            }
            var startDate = date.split('-')[0];
            var endDate = date.split('-')[1];
            var dailyOrderCriteria = {};
            dailyOrderCriteria.startDate = startDate;
            dailyOrderCriteria.endDate = endDate;
            $scope.loadMerchantBill(dailyOrderCriteria);
        }
        //  加载数据
        $scope.loadMerchantBill = function (dailyOrderCriteria) {
            LejiaBilling.findMerchantBill(dailyOrderCriteria).then(function(response) {
                var data = response.data;
                $scope.merchantBills = data;
            });
        }
        // 初次加载数据
        function loadPageDate() {
            var dailyOrderCriteria = {};
            dailyOrderCriteria.startDate = null;
            dailyOrderCriteria.endDate = null;
            $scope.loadMerchantBill(dailyOrderCriteria);
        }
        loadPageDate();
        // 日期选择
        $('#date_picker').daterangepicker({
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
        }, function (start, end, label) {});
    })


