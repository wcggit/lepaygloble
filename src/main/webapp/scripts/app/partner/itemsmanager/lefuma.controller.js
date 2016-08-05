'use strict';

angular.module('lepayglobleApp')
    .controller('lefumaController', function ($scope, $state, $location, $http) {

        //$('.left-main').css({'height':'100vw'})

        $http.get('api/merchant').success(function (response) {
            $scope.qrCode = response.data.qrCodePicture;
            $scope.shopName = response.data.name;
        });
        $scope.downloadQrCode = function(){
            location.href = "/api/merchant/downLoadQrCode"
        }
    });
/**
 * Created by recoluan on 2016/8/2 0002.
 */
