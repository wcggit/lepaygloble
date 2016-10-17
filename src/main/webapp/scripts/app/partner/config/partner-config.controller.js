'use strict';

angular.module('lepayglobleApp')
    .controller('PartnerConfigController',
                function ($scope, $http, $rootScope, $location, Principal, Auth) {
                    $('body').css({background: '#f3f3f3'});
                    // $('.main-content').css({height: '100vh'});
                    $http.get('api/partner').success(function (response) {
                        $scope.partner = response.data;
                    });
                    $scope.changePassword = function () {
                        $("#changePassword").modal("toggle");
                    };
                    $scope.confirmEditPwd = function () {
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

                        $http.post('api/partner/resetPassword', data, {
                            headers: {
                                'Content-Type': 'application/x-www-form-urlencoded'
                            }
                        }).success(function (response) {
                            if (response.status == 400) {
                                alert("密码不正确");
                            } else {
                                alert("修改成功");
                                $("#inputPassword3").val("");
                                $("#inputPassword2").val("");
                                $("#inputPassword1").val("");
                                $("#changePassword").modal("hide");
                            }
                        })
                    };
                    //  解除绑定
                    $scope.unbindWx = function() {
                            if($scope.partner.weiXinUser!=null && $scope.partner.weiXinUser!='') {
                                $http.get('api/partner/unbind_wx_user').success(function (response) {
                                    if(response.status==200) {
                                        alert("解除绑定成功 !");
                                        window.location.reload();
                                    }
                                });
                            }else {
                                        alert("尚未绑定微信账号 !");
                            }
                    }

                });

