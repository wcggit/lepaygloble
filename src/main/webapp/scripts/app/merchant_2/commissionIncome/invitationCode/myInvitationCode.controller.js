/**
 * Created by recoluan on 2016/11/25.
 */
'use strict';

angular.module('lepayglobleApp')
    .controller('myInvitationCodeController', function ($scope, $state, $rootScope, $location,Principal,Auth,$http) {
        // 加载二维码
        $http.get("/api/merchantUser/inviteCodes").success(function (response) {
            $scope.merchantNames = response.data.merchantNames;
            $scope.merchantCodes = response.data.merchantCodes;
        });
        //  下载二维码
        $scope.downLoad =  function (imgUrl) {
            location.href= "/invitationCode/downLoadInvitationCode?qrCodeUrl="+imgUrl;
        }
    })
