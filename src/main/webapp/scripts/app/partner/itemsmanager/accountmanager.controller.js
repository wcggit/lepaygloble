'use strict';

angular.module('lepayglobleApp')
    .controller('accountManagerController',
                function ($scope, Partner, $location, $http, $stateParams, $state) {
                    var param = null;

                    if ($stateParams.id != null && $stateParams.id != "") {
                        Partner.getMerchantById($stateParams.id).then(function (response) {
                            if (response.status == 200) {

                                $scope.merchant = response.data;
                            } else {
                                $state.go("myitems");
                            }
                        });
                        Partner.getMerchantUserById($stateParams.id).then(function (response) {
                            if (response.status == 200) {

                                $scope.accounts = response.data;
                            } else {
                                $state.go("myitems");
                            }
                        });
                    } else {
                        $state.go("myitems");
                    }
                    // 将密码隐藏
                    function toEncryption(x) {
                        return x.replace(/./g, '*');
                    };
                    $scope.createAccount = function () {
                        $("#createAccount").modal("toggle");
                    };
                    $scope.deleteAccount = function (id) {
                        $("#deleteAccount").modal("toggle");
                        param = id;
                    };
                    $scope.createMerchantUser = function () {
                        var username = $("#account").val().trim();
                        var flag = true;
                        angular.forEach($scope.accounts,
                                        function (account, index, array) {
                                            if (account.name == username) {
                                                alert("用户名不能重复");
                                                flag = false;
                                            }
                                        });
                        if (flag) {
                            var data = 'username='
                                       + encodeURIComponent(username)
                                       + '&password='
                                       + encodeURIComponent($("#inputPassword2").val().trim())
                                       + '&sid='
                                       + encodeURIComponent($stateParams.id);

                            $http.post('api/partner/create_merchant_user', data, {
                                headers: {
                                    'Content-Type': 'application/x-www-form-urlencoded'
                                }
                            }).success(function (response) {
                                alert(response.data);
                                $("#createAccount").removeClass('fade').modal('hide')
                                $state.go("accountmanager", {id: $stateParams.id}, {reload: true});

                            })
                        }
                    }
                    $scope.deleteMerchantUser = function () {
                        var data = 'param='
                                   + encodeURIComponent(param)
                        $http.post('api/partner/delete_merchant_user', data, {
                            headers: {
                                'Content-Type': 'application/x-www-form-urlencoded'
                            }
                        }).success(function (response) {
                            alert(response.data);
                            $("#deleteAccount").removeClass('fade').modal('hide')
                            $state.go("accountmanager", {id: $stateParams.id}, {reload: true});
                        })
                    }
                });
/**
 * Created by recoluan on 2016/8/2 0002.
 */
