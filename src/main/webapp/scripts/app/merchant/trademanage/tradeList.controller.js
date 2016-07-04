angular.module('lepayglobleApp')
    .controller('TradeListController', function ($scope, $state, $location, $q, Trade) {

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
                        if (currentPage == $scope.totalPages&&page==$scope.totalPages) {
                            return;
                        }
                        if (currentPage == 1&&page==1) {
                            return;
                        }
                        currentPage = page;
                        financialCriteria.offset = page;
                        loadContent();
                    };

                    $scope.searchByDate = function(){
                        var dateStr = $(".date0").val();
                        alert(dateStr);
                        var startDate =dateStr[0].replace(/-/g, "/");
                        var endDate = dateStr[1].replace(/-/g, "/");
                    }

                    $scope.exportExcel = function(){
                        if (financialCriteria.startDate == null) {
                            financialCriteria.startDate = null;
                        }
                        if (financialCriteria.endDate == null) {
                            financialCriteria.endDate = null;
                        }
                        if (financialCriteria.state == null) {
                            financialCriteria.state = null;
                        }

                        if (financialCriteria.merchant == null) {
                            financialCriteria.merchant = null;
                        }
                        location.href = "/api/financial/export";
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




