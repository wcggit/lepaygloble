angular.module('lepayglobleApp')
    .controller('TradeListController', function ($scope, $state, $location, $q, Trade, $rootScope) {
                    $('#timePicker1').daterangepicker({
                                                          "autoApply": true,
                                                          "showDropdowns": true,
                                                          //"showWeekNumbers": true,
                                                          //"showISOWeekNumbers": true,
                                                          //"timePicker": true,
                                                          "timePicker24Hour": true,
                                                          "timePickerSeconds": true,
                                                          "ranges": $rootScope.timePickerObj.ranges,
                                                          "locale": $rootScope.timePickerObj.locale1,
                                                          "alwaysShowCalendars": true,
                                                          "startDate": moment().subtract(1, 'day'),
                                                          "endDate": moment().subtract(1, 'day'),
                                                          "opens": "right"
                                                      }, function (start, end, label) {
                    });
                    $("#timePicker1").val("");

                    var currentPage = null;
                    var financialCriteria = {};
                    financialCriteria.offset = 1;
                    currentPage = 1;
                    loadContent();

                    function loadContent() {
                        Trade.getFinancialList(financialCriteria).then(function (results) {
                            var page = results.data;

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
                        if (dateStr == "" || dateStr == null) {
                            alert("请输入时间");
                            return;
                        }
                        var startDate = dateStr.split("-")[0];
                        var endDate = dateStr.split("-")[1].trim();
                        financialCriteria.startDate = startDate;
                        financialCriteria.endDate = endDate;
                        financialCriteria.offset = 1;
                        currentPage = 1;
                        loadContent();
                    }

                    $scope.exportExcel = function () {
                        var data = "?";
                        if (financialCriteria.startDate != null) {
                            data+="startDate="+financialCriteria.startDate+"&";
                            data+="endDate="+financialCriteria.endDate;
                        }
                        location.href = "/api/financial/export"+data;
                    }

                    $scope.showDetail = function (date) {
                        $scope.$parent.currentTab = "orderList";
                        $state.go("orderList", {date: date});
                    }

                });

function post(URL, PARAMS) {
    var temp = document.createElement("form");
    temp.action = URL;
    temp.method = "post";
    temp.style.display = "none";
    for (var x in PARAMS) {
        var opt = document.createElement("textarea");
        opt.name = x;
        opt.value = PARAMS[x];
        // alert(opt.name)
        temp.appendChild(opt);
    }
    document.body.appendChild(temp);
    temp.submit();
    return temp;
}




