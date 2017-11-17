'use strict';

angular.module('lepayglobleApp')
    .controller('lefumaController', function ($scope, $state, $stateParams, Partner) {
        $('body').css({background: '#f3f3f3'});
        $('.main-content').css({height: '100vh'});
        $('.main-content .container').css({height: '100vh'});
        if ($stateParams.id != null && $stateParams.id != "") {
            Partner.getMerchantById($stateParams.id).then(function (response) {
                if (response.status == 200) {

                    $scope.merchant = response.data;
                } else {
                    $state.go("myitems");
                }
            });
        } else {
            // alert(3);
            $state.go("myitems");
        }
        $scope.downloadQrCode = function () {
            location.href = "/api/merchant/downLoadQrCode?sid=" + $stateParams.id;
        }
    });
