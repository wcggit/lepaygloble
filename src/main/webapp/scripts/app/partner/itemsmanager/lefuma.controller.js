'use strict';

angular.module('lepayglobleApp')
    .controller('lefumaController', function ($scope, $state, $stateParams, Partner) {

                    if ($stateParams.id != null && $stateParams.id != "") {
                        Partner.getMerchantById($stateParams.id).then(function (response) {
                            if (response.status == 200) {

                                $scope.merchant = response.data;
                            } else {
                                $state.go("myitems");
                            }
                        });
                    } else {
                        $state.go("myitems");
                    }
                    $scope.downloadQrCode = function () {
                        location.href = "/api/merchant/downLoadQrCode?sid=" + $stateParams.id;
                    }
                });
