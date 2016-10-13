'use strict';

angular.module('lepayglobleApp')
    .controller('myUserController', function ($scope, Commission) {
                    $scope.inclusiveMap = []; //个选包含的数组
                    $scope.exclusiveMap = [];//全选排开的数组
                    $scope.selectedCheckbox = 0;
                    $scope.selected = false; //全选
                    $('body').css({background: '#fff'});
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
                        Commission.getUsersByBindPartner(criteria).then(function (response) {
                            var data = response.data;
                            $scope.page = currentPage;
                            $scope.pulls = data;
                        })
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
                        Commission.getTotalPagesByBindPartner(criteria).then(function (response) {
                            $scope.totalPages = response.data.totalPages;
                            $scope.totalElements = response.data.totalElements;
                            $scope.totalIncome = response.data.totalIncome;
                            loadContent();
                        });
                    }

                    $scope.searchByCriteria = function () {
                        var dateStr = $("#timePicker1").val();
                        var phone = $("#phone").val();
                        var merchantName = $("#merchantName").val();
                        if (dateStr != null && dateStr != "") {
                            var startDate = dateStr.split("-")[0].trim();
                            var endDate = dateStr.split("-")[1].trim();
                            criteria.partnerStartDate = startDate;
                            criteria.partnerEndDate = endDate;
                        }
                        criteria.offset = 1;
                        criteria.merchantName = merchantName;
                        criteria.phone = phone;
                        criteria.nickname = $("#weixinNickNmame").val();
                        criteria.consumptionCount = $("#consumptionCount").val();
                        criteria.countSelect = $("#countSelect").val();
                        criteria.timeSelect = $("#timeSelect").val();
                        criteria.consumptionTimes = $("#consumptionTimes").val();
                        currentPage = 1;
                        getTotalPage()
                    }

                    // 复选框
                    $('#checkbox-1').click(function () {
                        if ($('#checkbox-1').prop('checked')) {
                            $scope.exclusiveMap = [];
                            $scope.inclusiveMap = [];
                            $scope.selected = true;
                            $scope.selectedCheckbox = $scope.totalElements;
                            $(this).next('label').removeClass('chbx-init').addClass('chbx-focus');
                            $('.checkbox-2').next('label').removeClass('chbx-init').addClass('chbx-focus');
                            $('.checkbox-2').prop('checked', 'true');

                        } else {
                            $scope.exclusiveMap = [];
                            $scope.inclusiveMap = [];
                            $scope.selected = true;
                            $scope.selectedCheckbox = 0;
                            $(this).next('label').removeClass('chbx-focus').addClass('chbx-init');
                            $('.checkbox-2').next('label').removeClass('chbx-focus').addClass('chbx-init');
                            $('.checkbox-2').removeAttr('checked');
                        }
                    });
                    $scope.checkClick = function (id) {
                        var idName = document.getElementById(id);
                        if ($(idName).prop('checked')) {
                            $(idName).next('label').removeClass('chbx-init').addClass('chbx-focus');
                            $scope.selectedCheckbox = $scope.selectedCheckbox + 1;
                            if ($('#checkbox-1').prop('checked')) {
                                $scope.exclusiveMap =
                                $scope.exclusiveMap.filter(function (item) {
                                    return item !== id;
                                });

                            } else {
                                if ($scope.inclusiveMap.indexOf(id) == -1) {
                                    $scope.inclusiveMap.push(id);
                                }
                            }
                        } else {
                            $scope.selectedCheckbox = $scope.selectedCheckbox - 1;
                            $(idName).next('label').removeClass('chbx-focus').addClass('chbx-init');
                            if ($('#checkbox-1').prop('checked')) {
                                if ($scope.exclusiveMap.indexOf(id) == -1) {
                                    $scope.exclusiveMap.push(id);
                                }
                            } else {
                                $scope.inclusiveMap =
                                $scope.inclusiveMap.filter(function (item) {
                                    return item !== id;
                                });
                            }
                        }
                    }

                    $scope.welfare = function (id) {
                        //Welfare.checkUserWelfare(id).then(function (data) {
                        //
                        //});
                    };


                });
angular.module('lepayglobleApp')
    .directive('myRepeatDirective', function () {
                   return function (scope, element, attrs) {
                       if (scope.$parent.selected) {//代表全选
                           var map = scope.$parent.exclusiveMap;
                           if (map.length > 0) {
                               var currentId = scope.x[0];
                               try {
                                   angular.forEach(map, function (id) {//全选去除
                                       if (id == currentId) {
                                           throw Error();
                                       }
                                   });
                                   angular.element(element).prop('checked', 'true');
                                   angular.element(element).next('label').removeClass('chbx-init').addClass('chbx-focus');
                               } catch (e) {
                                   angular.element(element).prop('checked', 'false');
                               }
                           } else {
                               angular.element(element).prop('checked', 'true');
                               angular.element(element).next('label').removeClass('chbx-init').addClass('chbx-focus');
                           }
                       } else {
                           var map = scope.$parent.inclusiveMap;
                           if (map.length > 0) {
                               var currentId = scope.x[0];
                               angular.forEach(map, function (id) {//全部选择
                                   if (id == currentId) {
                                       angular.element(element).prop('checked', 'true');
                                       angular.element(element).next('label').removeClass('chbx-init').addClass('chbx-focus');
                                   }
                               });
                           }
                       }
                   };
               })

