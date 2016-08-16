'use strict';

angular.module('lepayglobleApp')
    .controller('myItemsController',
                function ($scope, Partner) {
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
                    var currentPage = 1;
                    var criteria = {};
                    criteria.offset = 1;
                    getTotalPage();
                    function loadContent() {
                        Partner.getPartnerBindMerchantList(criteria).then(function (response) {
                            var data = response.data;
                            $scope.page = currentPage;
                            $scope.pulls = data;
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
                        criteria.offset = page;
                        loadContent();
                    };

                    function getTotalPage() {
                        Partner.getPartnerBindMerchantListPage(criteria).then(function (response) {
                            $scope.totalPages = response.data;
                            loadContent();
                        });
                    }

                    $scope.searchByCriteria = function () {
                        var dateStr = $("#timePicker1").val();
                        var merchantName = $("#merchantName").val().trim();
                        var partnerShip = $("#partnerShip").val();
                        var userBindState = $("#userBindState").val();
                        if (dateStr != null && dateStr != "") {
                            var startDate = dateStr.split("-")[0].trim();
                            var endDate = dateStr.split("-")[1].trim();
                            criteria.startDate = startDate;
                            criteria.endDate = endDate;
                        }
                        if (partnerShip != -1) {
                            criteria.partnerShip = partnerShip;
                        }else{
                            criteria.partnerShip = null;
                        }
                        if (userBindState != -1) {
                            criteria.userBindState = userBindState;
                        }else{
                            criteria.userBindState = null;
                        }
                        criteria.offset = 1;
                        criteria.merchantName = merchantName;
                        currentPage = 1;
                        getTotalPage()
                    }

                    $scope.itemsInfo = [
                        {
                            itemsName: '一品江南',
                            contractType: '0',
                            itemsAddress: '北京市朝阳区',
                            memberNumber: '20',
                            shopCommission: '23.33',
                            myCommission: '12.56'
                        }
                    ];
                });

