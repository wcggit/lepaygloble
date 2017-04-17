 /**
 * Created by recoluan on 2016/11/16.
 */
 'use strict';


angular.module('lepayglobleApp')
    .controller('withdrawRecordController', function ($scope, $state, $rootScope, $location,Principal,Auth,$http) {

        $('#timePicker1')
            .daterangepicker({
                opens : 'right', //日期选择框的弹出位置
                format : 'YYYY/MM/DD', //控件中from和to 显示的日期格式
                ranges : {
                    '昨日': [moment().subtract('days', 1).startOf('day'), moment().subtract('days', 1).endOf('day')],
                    '最近7日': [moment().subtract('days', 6), moment()],
                    '最近30日': [moment().subtract('days', 29), moment()]
                },
            },function(start, end, label) {});

        var currentPage = null;
        var financialCriteria = {};
        financialCriteria.offset = 1;
        currentPage = 1;

        loadContent();

        function loadContent() {
                $http.post('/api/financial', financialCriteria, {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }).success(function (response) {
                     var page = response.data;
                     $scope.pulls = page.content;
                     $scope.page = currentPage;
                     $scope.totalPages = page.totalPages;
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
            financialCriteria.offset = page;
            loadContent();
        };

        $scope.searchByDate = function () {
            var dateStr = $("#timePicker1").val();
            if(dateStr!=null&&dateStr!="") {
                var startDate = dateStr.split("-")[0];
                var endDate = dateStr.split("-")[1].trim();
                financialCriteria.startDate = startDate;
                financialCriteria.endDate = endDate;
            }else {
                financialCriteria.startDate = null;
                financialCriteria.endDate = null;
            }
            var state = $("#financialState").val();
            if(state!=-1&&state!=null) {
                financialCriteria.state = state;
            }else {
                financialCriteria.state = null;
            }
            financialCriteria.offset = 1;
            currentPage = 1;
            loadContent();
        }

    })
