'use strict';

angular.module('lepayglobleApp')
    .controller('MerchantConfigController',
                function ($scope, $http, $rootScope, $location, Principal, Auth) {
                    $('body').css({background: '#f3f3f3'});
                    $('.main-content').css({height: '100vh'})
                    Principal.identity().then(function (account) {
                        $scope.account = account;
                    });
                    $http.get('api/merchant').success(function (response) {
                        $scope.merchant = response.data;
                    });
                    $scope.changePassword = function () {
                        $("#changePassword").modal("toggle");
                    };
                    $scope.confirmEdit = function () {
                        if ($("#inputPassword1").val().trim() == "") {
                            $("#inputPassword1").css({'border-color': 'red'});
                            return;
                        }
                        if ($("#inputPassword3").val().trim() == "") {
                            $("#inputPassword3").css({'border-color': 'red'});
                            return;
                        }

                        if ($("#inputPassword2").val().trim() == "") {
                            $("#inputPassword2").css({'border-color': 'red'});
                            return;
                        }

                        if ($("#inputPassword3").val().trim()
                            != $("#inputPassword2").val().trim()) {
                            $("#inputPassword3").val("");
                            $("#inputPassword2").val("");
                            alert("2次输入的密码不一致");
                            return;
                        }
                        if (!new RegExp("^[0-9A-Za-z]{6,}$").test($("#inputPassword3").val().trim())) {
                            alert("输入正确密码格式");
                            return;
                        }

                        var data = 'password='
                                   + encodeURIComponent($("#inputPassword1").val().trim())
                                   + '&reset='
                                   + encodeURIComponent($("#inputPassword3").val().trim());

                        $http.post('api/merchant/resetPassword', data, {
                            headers: {
                                'Content-Type': 'application/x-www-form-urlencoded'
                            }
                        }).success(function (response) {
                            if(response.status==400){
                                alert("密码不正确");
                            }else{
                                alert("修改成功");
                                $("#inputPassword3").val("");
                                $("#inputPassword2").val("");
                                $("#inputPassword1").val("");
                                $("#changePassword").modal("hide");
                            }
                        })
                    };

                });

