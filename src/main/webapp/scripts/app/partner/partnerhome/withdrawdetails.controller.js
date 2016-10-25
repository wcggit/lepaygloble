'use strict';

angular.module('lepayglobleApp')
    .controller('withdrawDetailsController', function ($scope,$http) {
        $('body').css({background: '#f3f3f3'});
        $('.main-content').css({height: 'auto'});
        $('#timePicker1')
        // .val(moment().subtract('day', 1).format('YYYY/MM/DD HH:mm:00') + ' - ' +
        // moment().format('YYYY/MM/DD HH:mm:59'))
            .daterangepicker({
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
        $("#timePicker1").val("");
        $('#timePicker2')
        // .val(moment().subtract('day', 1).format('YYYY/MM/DD HH:mm:00') + ' - ' +
        // moment().format('YYYY/MM/DD HH:mm:59'))
            .daterangepicker({
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
        $("#timePicker2").val("");


        var currentPage = 1;
        var withdrawCriteria = {};
        withdrawCriteria.offset = 1;

        loadContent();
        function loadContent() {
            $http.post('/withdraw/partner_withdraw/findAll', withdrawCriteria, {
                headers: {
                    'Content-Type': 'application/json'
                }
            }).success(function (response) {
                var data = response.data;
                $scope.page = currentPage;
                $scope.totalPages = data.totalPages;
                $scope.pulls = data.content;
            });
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
            withdrawCriteria.offset = page;
            loadContent();
        };

        $scope.searchByCriteria = function () {

            if($("#timePicker1").val()!=null&&$("#timePicker1").val()!='') {
                var startDateStr = $("#timePicker1").val();
                var withDrawStartDate = startDateStr.split("-")[0].trim();
                var withDrawEndDate = startDateStr.split("-")[1].trim();
                withdrawCriteria.withDrawStartDate = withDrawStartDate;
                withdrawCriteria.withDrawEndDate = withDrawEndDate;
            }
            if($("#timePicker2").val()!=null&&$("#timePicker2").val()!='') {
                var completeDateStr = $("#timePicker2").val();
                var completeDateStartDate = completeDateStr.split("-")[0].trim();
                var completeDateEndDate = completeDateStr.split("-")[1].trim();
                withdrawCriteria.completeDateStartDate = completeDateStartDate;
                withdrawCriteria.completeDateEndDate = completeDateEndDate;
            }
            if($("#state").val()!=-1) {
                withdrawCriteria.state = $("#state").val();
            }else {
                withdrawCriteria.state = null;
            }
            withdrawCriteria.offset = 1;
            currentPage = 1;
            loadContent();

        }
    });

