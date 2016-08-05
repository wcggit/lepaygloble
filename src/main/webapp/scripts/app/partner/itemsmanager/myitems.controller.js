'use strict';

angular.module('lepayglobleApp')
    .controller('myItemsController', function ($scope, $http, $rootScope, $location,Principal,Auth) {
        $('body').css({background: '#f3f3f3'});
        $('.main-content').css({height: 'auto'});
        $('#timePicker1')
        // .val(moment().subtract('day', 1).format('YYYY/MM/DD HH:mm:00') + ' - ' + moment().format('YYYY/MM/DD HH:mm:59'))
            .daterangepicker({
                timePicker: true, //是否显示小时和分钟
                timePickerIncrement: 1, //时间的增量，单位为分钟
                opens : 'right', //日期选择框的弹出位置
                startDate: moment().format('YYYY/MM/DD HH:mm:00'),
                endDate: moment().format('YYYY/MM/DD HH:mm:59'),
                format : 'YYYY/MM/DD HH:mm:ss', //控件中from和to 显示的日期格式
                ranges : {
                    '最近1小时': [moment().subtract('hours',1), moment()],
                    '今日': [moment().startOf('day'), moment()],
                    '昨日': [moment().subtract('days', 1).startOf('day'), moment().subtract('days', 1).endOf('day')],
                    '最近7日': [moment().subtract('days', 6), moment()],
                    '最近30日': [moment().subtract('days', 29), moment()]
                },
            },function(start, end, label) {});
        $("#timePicker1").val("");

        Principal.identity().then(function (account) {
            $scope.account = account;
        });
        $http.get('api/merchant').success(function (response) {
            $scope.merchant = response.data;
        });


        $scope.changePassword=function () {
            $("#changePassword").modal("toggle");
        };
        $scope.ljkt=function () {
            $("#ljkt").modal("toggle");
            $http.get('/api/merchant/open');
        };

        $scope.itemsInfo=[
            {
                itemsName:'一品江南',
                contractType:'0',
                itemsAddress:'北京市朝阳区',
                memberNumber:'20',
                shopCommission:'23.33',
                myCommission:'12.56'
            }
        ];
    });

