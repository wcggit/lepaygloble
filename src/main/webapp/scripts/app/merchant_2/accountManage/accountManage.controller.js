/**
 * Created by recoluan on 2016/11/16.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('accountManageController', function ($scope, $state, $rootScope, $location, Principal, Auth, $http, HomePage) {
        $scope.accountInfoState0 = true;
        $scope.accountInfoState1 = false;
        $scope.accountInfoFun0 = function () {
            $scope.accountInfoState0 = true;
            $scope.accountInfoState1 = false;
            $scope.shopOwnerAccount();
        }
        $scope.accountInfoFun1 = function () {
            $scope.accountInfoState0 = false;
            $scope.accountInfoState1 = true;
            $scope.cashierAccount();
        }


        // 加载门店
        HomePage.getMerchantsInfo().then(function (response) {
            var data = response.data;
            $scope.merchants = data;
            var firstSid = data[0].sid;
            $scope.firstSid = firstSid;
            $scope.getBankByMerchant();
        });

        // 加载门店对应的银行信息
        $scope.getBankByMerchant = function () {
            var selMerchant = $("#selMerchant").val();
            if (selMerchant == null || selMerchant == '') {
                selMerchant = $scope.firstSid;
            }
            $http.get("/api/merchantUser/merchantBankInfo?id=" + selMerchant).success(function (response) {
                var data = response.data;
                $scope.bankInfo = data.merchantBank;
                $scope.payee = data.payee;
            });
        }


        // 当进入页面时 , 加载用户信息
        var content = document.getElementById("myTabContent");
        $scope.getCurrentLogin = function () {
            $http.get("/api/merchantUser/merchantInfo").success(function (response) {
                var data = response.data;
                $scope.loginInfo = data;
                $scope.merchantType = data.type;
                if (data.type == 8) {
                    $("#ios").show();
                    $("#iosTab").show();
                    $("#home").show();
                    $("#homeTab").show();
                }
            });
        }
        $scope.getCurrentLogin();


        $scope.shopOwnerAccount = function () {
            $http.get("/api/merchantUser/owerAccount").success(function (response) {
                var data = response.data;
                var acccount = document.getElementById("account");
                var content = "";
                if (data == null || data == '') {
                    content += '<tr class="tr-empty"><td class="text-center" colspan="4">暂无数据！</td></tr>';
                } else {
                    for (var i = 0; i < data.length; i++) {
                        content += '<tr class="tr-noEmpty"><td>店主</td>' +
                            '<td><span>' + data[i].name + '</span></td>' +
                            '<td>********</td>' +
                            '<td><a class="a-btn">解除绑定</a></td></tr>';
                    }
                }
                content += '</tbody></table>';
                acccount.innerHTML = content;
            });
        };
        $scope.shopOwnerAccount();
        $scope.cashierAccount = function () {
            $http.get("/api/merchantUser/cashierAccount").success(function (response) {
                var data = response.data;
                console.log(JSON.stringify(response));
                var crasher = document.getElementById("crasher");
                var content = "";
                if (data == null || data == '') {
                    content += '<tr class="tr-empty"><td class="text-center" colspan="3">暂无数据！</td></tr>';
                } else {
                    for (var i = 0; i < data.length; i++) {
                        content += '<tr class="tr-noEmpty"><td>收银员</td>' +
                            '<td><span>' + data[i].name + '</span></td>' +
                            '<td>********</td>' +
                            '<td><a class="a-btn">解除绑定</a></td></tr>';
                    }
                }
                content += '</tbody></table>';
                crasher.innerHTML = content;
            });
        };

        // 选择银行
        $scope.selBankName = function () {
            var $selBankName = $("#selBankName");
            if ($selBankName.val() != null && $selBankName.val() != '') {
                $("#bankName").val($selBankName.find("option:selected").text());
            }
        }

        // 创建账号
        $scope.saveAccount = function () {
            var merchantUser = {};
            var username = $("#accountName").val();
            var accountType = $("input[name=accountType]:checked").val();
            var passwd = $("#accountPwd").val();
            var repasswd = $("#rePwd").val();
            var checkMerchant = "";              // 获取所有选中的门店
            $('input:checkbox').each(function () {
                if ($(this).is(':checked')) {
                    checkMerchant += "~" + $(this).val();
                } else {
                    $("#checkAll").attr("checked", false);
                }
            });
            if (checkMerchant == null || checkMerchant == '') {
                alert("请选择门店");
                return;
            } else {
                merchantUser.linkMan = checkMerchant;
            }
            if (username != null && username != '') {
                merchantUser.name = username;
            } else {
                alert("请输入用户名");
                return;
            }
            if (accountType != null && accountType != '') {
                merchantUser.type = accountType;
            } else {
                alert("请选择账户类型");
                return;
            }
            if (passwd != null && passwd != '') {
                merchantUser.password = passwd;
            } else {
                alert("请输入密码");
                return;
            }
            if (passwd.length < 6) {
                alert("密码长度不小于 6 位");
                return;
            }
            if (repasswd != passwd) {
                alert("两次输入的密码不一致");
                return;
            }
            if (!$scope.checkInfo()) {
                alert("您输入的管理员密码有误，请重新输入。");
                return;
            }
            if (!$scope.checkNameRepeat()) {
                alert("名称已存在，换个试试吧～")
                return;
            }
            console.log(JSON.stringify(merchantUser));
            $http.post('api/merchantUser/createAccount', merchantUser).success(function (response) {
                console.log(JSON.stringify(response));
                if (response.status == 200) {
                    alert("账号保存成功！");
                    window.location.reload();
                } else {
                    alert("账号创建失败 ！");
                }
            })
        }

        // 校验当前用户
        $scope.checkInfo = function () {
            var data = $("#thisPwd").val();
            var flag = true;
            $http.post('api/merchantUser/checkInfo', data).success(function (response) {
                if (response.status == 400) {
                    alert("当前账号密码不正确,请重新输入！");
                    $("#thisPwd").val('');
                    flag = false;
                }
            })
            return flag;
        }
        //  校验用户名是否重复
        $scope.checkNameRepeat = function () {
            var username = $("#accountName").val();
            var flag = true;
            $http.post('api/merchantUser/checkRepeat', username).success(function (response) {
                if (response.status == 400) {
                    alert("用户名已存在,换个试试吧");
                    $("#username").val('');
                    flag = false;
                }
            })
            return flag;
        }
        $scope.checkRepeat = function () {
            if (!$scope.checkNameRepeat()) {
                alert("名称已存在，换个试试吧～")
                return;
            }
        }
        $scope.resetAll = function () {
            $("#username").val('');
            $("#passwd").val('');
            $("#repasswd").val('');
            $("#bankName").val('');
            $("#bankNum").val('');
            $("#selBankName").val('');
        }

        // 修改密码功能
        $scope.updatePwd = function () {
            var pwdDto = {};
            var oldPwd = $("#inputPassword1").val();
            var newPwd1 = $("#inputPassword2").val();
            var newPwd2 = $("#inputPassword3").val();
            // 校验
            if (oldPwd == '' || oldPwd == null) {
                alert("当前密码不能为空～")
                return;
            } else {
                pwdDto.oldPwd = oldPwd;
            }
            if (newPwd1 == '' || newPwd1 == null) {
                alert("请输入新密码～")
                return;
            } else {
                pwdDto.newPwd = newPwd1;
            }
            if (newPwd2 == '' || newPwd2 == null) {
                alert("请输入确认密码～")
                return;
            }
            if (oldPwd.length < 6 || newPwd1.length < 6) {
                alert("密码长度不少于6位")
                return;
            }
            if (newPwd1 != newPwd2) {
                alert("两次输入的新密码不一致！");
                $("inputPassword3").val('');
                return;
            }
            var r1 = /[0-9]/;
            var r2 = /[a-z]/;
            if (r1.test(newPwd1) && r2.test(newPwd1)) {
            } else {
                alert("新密码应包含数字和字母~");
                return;
            }
            $http.post('api/merchantUser/updatePwd', pwdDto).success(function (response) {
                if (response.status == 400) {
                    alert("您输入的当前密码不正确,请重新输入！");
                    return;
                } else {
                    alert("密码已修改成功,请重新登录 ～");
                    Auth.logout();
                    $('#changePassword').modal('hide');
                    location.reload();
                }
            })
        }

        // 选中/取消选中 全部门店
        $scope.checkAll = function () {
            $('input:checkbox').each(function () {
                $(this).attr('checked', $("#checkAll").is(':checked'));
            });
        }
    })
