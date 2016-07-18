angular.module('lepayglobleApp')
    .controller('MemberController', function ($scope, $state, Commission, $q, $rootScope) {

                    // $('#timePicker1').daterangepicker({
                    //     "autoApply": true,
                    //     "showDropdowns": true,
                    //     "showWeekNumbers": true,
                    //     "showISOWeekNumbers": true,
                    //     "timePicker": true,
                    //     "timePicker24Hour": true,
                    //     "timePickerSeconds": true,
                    //     "ranges": $rootScope.timePickerObj.ranges,
                    //     "locale": $rootScope.timePickerObj.locale,
                    //     "alwaysShowCalendars": true,
                    //     "startDate": moment().subtract(1,'day').format("YYYY/MM/DD 00:00:00"),
                    //     "endDate": moment().subtract(1,'day').format("YYYY/MM/DD 23:59:59"),
                    //     "opens": "right"
                    // }, function (start, end, label) {});
                    // $("#timePicker1").val("");
                    $('#timePicker1')
                        .val(moment().subtract('day', 1).format('YYYY/MM/DD HH:mm:00') + ' - ' + moment().format('YYYY/MM/DD HH:mm:59'))
                        .daterangepicker({
                            timePicker: true, //是否显示小时和分钟
                            timePickerIncrement: 1, //时间的增量，单位为分钟
                            opens : 'left', //日期选择框的弹出位置
                            format : 'YYYY/MM/DD HH:mm:ss', //控件中from和to 显示的日期格式
                            ranges : {
                                '最近1小时': [moment().subtract('hours',1), moment()],
                                '今日': [moment().startOf('day'), moment()],
                                '昨日': [moment().subtract('days', 1).startOf('day'), moment().subtract('days', 1).endOf('day')],
                                '最近7日': [moment().subtract('days', 6), moment()],
                                '最近30日': [moment().subtract('days', 29), moment()]
                            },
                        },function(start, end, label) {});

                    var currentPage = 1;
                    var olOrderCriteria = {};
                    olOrderCriteria.offset = 1;
                    getTotalPage();
                    function loadContent(){
                        Commission.getMerchantBindUserList(olOrderCriteria).then(function (response) {
                            var data = response.data;
                            $scope.page = currentPage;
                            $scope.pulls =data;
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
                        olOrderCriteria.offset = page;
                        loadContent();
                    };


                   function  getTotalPage(){
                       Commission.getMerchantBindUserTotalPages(olOrderCriteria).then(function (response) {
                           $scope.totalPages = response.data;
                           loadContent();
                       });
                    }

                    $scope.searchByCriteria = function () {
                        var dateStr = $("#timePicker6").val();
                        if (dateStr == "" || dateStr == null) {
                            alert("请输入时间");
                            return;
                        }
                        var startDate = dateStr.split("-")[0].trim();
                        var endDate = dateStr.split("-")[1].trim();
                        olOrderCriteria.startDate = startDate;
                        olOrderCriteria.endDate = endDate;
                        olOrderCriteria.offset = 1;
                        currentPage = 1;
                        getTotalPage()
                    }


                });




