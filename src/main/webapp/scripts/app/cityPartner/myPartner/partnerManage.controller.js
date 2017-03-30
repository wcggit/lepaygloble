/**
 * Created by recoluan on 2017/3/13.
 */
/**
 * Created by recoluan on 2016/11/16.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('cp-partnerManagerController', function ($scope, $state, $rootScope, $location, Principal, Auth, $http) {
        // 时间选择器
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
            console.log(start+"--------"+end);
        });

        var currentPage = 1;
        var partnerCriteria = {};
        partnerCriteria.offset = 1;

        loadContent();
        function loadContent() {
            $http.post('api/partnerManager/partners', partnerCriteria, {
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
            partnerCriteria.offset = page;
            loadContent();
        };

        $scope.searchByCriteria = function () {
            var name = $("#partnerName").val();
            var phoneNumber = $("#phoneNumber").val();
            if(name!=null && name!='') {
                partnerCriteria.name = name;
            }
            if(phoneNumber!=null && phoneNumber!='') {
                partnerCriteria.phoneNumber = phoneNumber;
            }
            partnerCriteria.offset = 1;
            currentPage = 1;
            loadContent();
        }
    });
