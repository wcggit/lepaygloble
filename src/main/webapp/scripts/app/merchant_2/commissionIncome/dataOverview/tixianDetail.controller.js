/**
 * Created by recoluan on 2016/11/25.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('tixainDetailController', function ($scope, $state, $rootScope, $location, Principal, Auth, $http, Trade, $stateParams) {


        $scope.defaultId = $stateParams.mid;
        var currentPage = 1;
        var olOrderCriteria = {};
        olOrderCriteria.offset = 1;
        if($stateParams.mid!=null) {
            var merchant = {};
            merchant.id = $stateParams.mid;
            olOrderCriteria.merchant = merchant;
        }

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


