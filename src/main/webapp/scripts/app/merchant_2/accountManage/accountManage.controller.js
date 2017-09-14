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
        var array = new Array();
        $http.get("/api/merchantUser/merchantsInfo").success(function (response) {
            if (response.status == 200) {
                console.log(JSON.stringify(response));
                var data = response.data;
                $scope.merchants = data;
                $scope.defaultId = data[0][0];
                $scope.getBankByMerchant($scope.defaultId);
            } else {
                alert("加载门店错误...");
            }
        });


        // 加载门店对应的银行信息
        $scope.getBankByMerchant = function (mid) {
            var selMerchant = $("#selMerchant").val();
            if (mid != null && mid != '') {
                selMerchant = $scope.defaultId;
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
            });
        }
        $scope.getCurrentLogin();


        $scope.shopOwnerAccount = function () {
            $http.get("/api/merchantUser/userAccount").success(function (response) {
                var data = response.data;
                $scope.accounts = data;
            });
        };
        $scope.shopOwnerAccount();

        $scope.cashierAccount = function () {
            $http.get("/api/merchantUser/cashierAccount").success(function (response) {
                var data = response.data;
                $scope.cashiers = data.cashiers;
                $scope.cashierAccount = data.cashierAccount;
                // console.log(JSON.stringify(data));
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
            var accountType = 0;
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
            /*if (accountType != null && accountType != '') {
                merchantUser.type = accountType;
            } else {
                alert("请选择账户类型");
                return;
            }*/
            merchantUser.type = accountType;
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
            if (!$scope.checkNameRepeat()) {
                alert("名称已存在，换个试试吧～")
                return;
            }
            if (!$scope.checkInfo()) {
                alert("您输入的管理员密码有误，请重新输入。");
                $("#thisPwd").val('');
                return;
            }else {
                $http.post('api/merchantUser/createAccount', merchantUser).success(function (response) {
                    if (response.status == 200) {
                        alert("账号保存成功！");
                        window.location.reload();
                    } else {
                        alert("账号创建失败 ！");
                    }
                })
            }
        }

        // 校验当前用户
        $scope.checkInfo = function () {
            var data = $("#thisPwd").val();
            var flag = true;
            $http.post('api/merchantUser/checkInfo', data).success(function (response) {
                if (response.status == 400) {
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
        $scope.changePwd = function (sid) {
            $("#currEditId").val(sid);
            $("#changePassword").modal();
        }
        $scope.updatePwd = function () {
            var sid = $("#currEditId").val();
            var pwdDto = {};
            pwdDto.userSid = sid;
            var newPwd1 = $("#inputPassword1").val();
            var newPwd2= $("#inputPassword2").val();
            var oldPwd= $("#inputPassword3").val();
            // 校验
            if (newPwd1 == '' || newPwd1 == null) {
                alert("新密码不能为空～")
                return;
            } else {
                pwdDto.newPwd = newPwd1;
            }
            if (newPwd2 == '' || newPwd2 == null) {
                alert("请再次输入新密码～")
                return;
            }
            if (oldPwd == '' || oldPwd == null) {
                alert("请输入管理员密码～")
                return;
            }else {
                pwdDto.oldPwd = oldPwd;
            }
            if (newPwd1.length < 6 || newPwd2.length < 6) {
                alert("密码长度不少于6位")
                return;
            }
            if (newPwd1 != newPwd2) {
                alert("两次输入的新密码不一致！");
                $("inputPassword2").val('');
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
                    $("inputPassword1").val('');
                    $("inputPassword2").val('');
                    $("inputPassword3").val('');
                    alert(response.msg);
                    return;
                } else {
                    alert("密码已修改成功 ^_^ !");
                    // Auth.logout();
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

        // 解除绑定 - 删除关系
        /*$scope.unbindAccount = function (id) {
            var result = confirm("是否解除该账号的权限?")
            if(result) {
                $http.get("/api/merchantUser/accountUnbind/"+id).success(function (response) {
                    if(response.status==200) {
                        alert("已解除该账号绑定！");
                        window.location.reload();
                    }else {
                        alert(response.msg);
                    }
                });
            }
        }*/
        // 解除绑定 - 解除微信绑定
        $scope.unbindWx = function (wxUser) {
            $scope.currWxUser = wxUser;
            $("#unbind").modal();
        }
        $scope.unbindAccount = function (id) {
                $http.get("/api/merchant/unBindWeiXinUser/"+id).success(function (response) {
                    if(response.status==200) {
                        $("#jb-success").modal();
                    }else {
                        alert(response.msg);
                    }
                });
        }
        // 刷新页面
        $scope.refresh =function () {
            window.location.reload();
        }

    })

