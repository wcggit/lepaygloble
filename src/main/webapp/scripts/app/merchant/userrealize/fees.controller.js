angular.module('lepayglobleApp')
    .controller('FeesController',
                function ($scope, $state, Commission, $q, $rootScope, Commission) {
                    $('#timePicker7').daterangepicker({
                                                          "autoApply": true,
                                                          "showDropdowns": true,
                                                          "showWeekNumbers": true,
                                                          "showISOWeekNumbers": true,
                                                          "timePicker": true,
                                                          "timePicker24Hour": true,
                                                          "timePickerSeconds": true,
                                                          "ranges": $rootScope.timePickerObj.ranges,
                                                          "locale": $rootScope.timePickerObj.locale,
                                                          "alwaysShowCalendars": true,
                                                          "startDate": moment().subtract(1,
                                                                                         'day').format("YYYY/MM/DD 00:00:00"),
                                                          "endDate": moment().subtract(1,
                                                                                       'day').format("YYYY/MM/DD 23:59:59"),
                                                          "opens": "right"
                                                      }, function (start, end, label) {
                    });
                    $("#timePicker7").val("");

                    var currentPage = 1;
                    var olOrderCriteria = {};
                    olOrderCriteria.offset = 1;
                    loadContent();
                    loadStatistic()

                    function loadContent() {
                        Commission.getOrderShareList(olOrderCriteria).then(function (response) {
                            var page = response.data;
                            $scope.pulls = page.content;
                            $scope.page = currentPage;
                            $scope.totalElements = page.totalElements;
                            $scope.totalPages = page.totalPages;
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
                        olOrderCriteria.offset = page;
                        loadContent();
                    };

                    $scope.searchByCriteria = function () {
                        var dateStr = $("#timePicker7").val();
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
                        loadContent();
                        loadStatistic()
                    }

                    function loadStatistic() {
                        Commission.getOrderShareStatistic(olOrderCriteria).then(function (results) {
                            var data = results.data;
                            $scope.statistic = {
                                sales:      data.sales / 100.0,
                                commission: data.commission / 100.0
                            };
                        });
                    }

                });




