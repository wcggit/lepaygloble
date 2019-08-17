/**
 * Created by recoluan on 2016/11/25.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('myCodeController', function ($scope, $state, $rootScope, $location,Principal,Auth,$http) {
                    $http.get('api/codeTrade/getMyCodes').success(function (response) {
                        if(response.status == 200){
                            var data = response.data;
                            $scope.myCodes = data;

                            if($scope.myCodes.length>0){
                                //$("#notData").hide();
                            }else{
                                //$("#notData").show();
                            }
                        }else{
                            alert("加载二维码错误...");
                        }

                    });

                    $scope.downloadMyCode = function(sid){
                        //alert("merchantId: " + sid);
                        location.href = "/api/merchant/downLoadQrCode?sid=" + sid;
                    }
    })
