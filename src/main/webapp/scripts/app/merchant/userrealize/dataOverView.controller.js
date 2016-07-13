angular.module('lepayglobleApp')
    .controller('OverViewController', function ($scope, $state, Commission, $http) {

                    Commission.getMerchantCommissionDetail().then(function (response) {
                        var data = response.data;
                        $scope.available = data.available;
                        $scope.totalCommission = data.totalCommission;
                        $scope.userLimit = data.userLimit;
                        $scope.currentBind = data.currentBind;
                        if (data.currentBind == 0 && data.totalCommission == 0) {
                            $scope.per = 0;
                        } else {

                            $scope.per = data.totalCommission / data.currentBind;
                        }
                        var percent = data.currentBind / data.userLimit * 100;
                        $(".sjgl .progress-bar").css({width: "" + percent + "%"})
                    });

                    Commission.getDayCommissionDetail().then(function (response) {
                        var data = response.data;
                        $scope.dayCommission = data.dayCommission / 100;
                        $scope.count = data.count;
                        $scope.shareCount = data.shareCount;
                    });

                    $http.get('api/merchant').success(function (response) {
                        $scope.payee = response.data.payee;
                        var bankNumber = response.data.merchantBank.bankNumber;
                        $scope.bank =
                        bankNumber.substring(bankNumber.length - 4, bankNumber.length);
                    });

                    $scope.tx = function () {
                        $("#tx").modal("toggle");
                    }

                });




