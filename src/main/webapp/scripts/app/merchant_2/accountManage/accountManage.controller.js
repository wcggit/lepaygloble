 /**
 * Created by recoluan on 2016/11/16.
 */
 'use strict';


angular.module('lepayglobleApp')
    .controller('accountManageController', function ($scope, $state, $rootScope, $location,Principal,Auth,$http,HomePage) {
        $scope.accountInfoState0=true;
        $scope.accountInfoState1=false;
        $scope.accountInfoFun0=function () {
            $scope.accountInfoState0=true;
            $scope.accountInfoState1=false;
        }
        $scope.accountInfoFun1=function () {
            $scope.accountInfoState0=false;
            $scope.accountInfoState1=true;
        }



        // 加载门店
        HomePage.getMerchantsInfo().then(function(response) {
            var data = response.data;
            $scope.merchants = data;
            var firstSid = data[0].sid;
            $scope.firstSid = firstSid;
            $scope.getBankByMerchant();
        });

        // 加载门店对应的银行信息
        $scope.getBankByMerchant = function() {
            var selMerchant = $("#selMerchant").val();
            if(selMerchant==null||selMerchant=='') {
                selMerchant = $scope.firstSid;
            }
            $http.get("/api/merchantUser/merchantBankInfo?id="+selMerchant).success(function (response) {
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
                  if(data.type==8) {
                     $("#p-accnoutInfo").show();
                     $("#div-accnoutInfo").show();
                  }
            });
        }
        $scope.getCurrentLogin();


        $scope.shopOwnerAccount = function () {
            $http.get("/api/merchantUser/owerAccount").success(function (response) {
                var data = response.data;
                var ownerAccountInfo= '<table class="table ol-tab">'+
                    '          <thead class="active">'+
                    '          <tr>'+
                    '          <td>账号类型</td>'+
                    '          <td>用户名</td>'+
                    '          <td>密码</td>'+
                    '          <td>操作</td>'+
                    '          </tr>'+
                    '          </thead>'+
                    '<tbody>';
                if(data==null || data=='') {
                    ownerAccountInfo+='<tr class="tr-empty"><td class="text-center" colspan="3">暂无数据！</td></tr>';
                }else {
                    for(var i=0;i<data.length;i++) {
                        ownerAccountInfo+='<tr class="tr-noEmpty"><td>店主</td>'+
                            '<td><span>'+data[i].name+'</span></td>'+
                            '<td>********</td>'+
                            '<td><a class="a-btn">解除绑定</a></td></tr>';
                    }
                }
                ownerAccountInfo += '</tbody></table>';
                content.innerHTML=ownerAccountInfo;
            });
        };
        $scope.shopOwnerAccount();
        $scope.cashierAccount = function () {
            $http.get("/api/merchantUser/cashierAccount").success(function (response) {
                var data = response.data;
                var cashierAccountInfo= '<table class="table ol-tab">'+
                    '          <thead class="active">'+
                    '          <tr>'+
                    '          <td>账号类型</td>'+
                    '          <td>用户名</td>'+
                    '          <td>密码</td>'+
                    '          <td>操作</td>'+
                    '          </tr>'+
                    '          </thead>'+
                    '<tbody>';
                if(data==null || data=='') {
                    cashierAccountInfo+='<tr class="tr-empty"><td class="text-center" colspan="3">暂无数据！</td></tr>';
                }else {
                    for(var i=0;i<data.length;i++) {
                        cashierAccountInfo+='<tr class="tr-noEmpty"><td>收银员</td>'+
                            '<td><span>'+data[i].name+'</span></td>'+
                            '<td>********</td>'+
                            '<td><a class="a-btn">解除绑定</a></td></tr>';
                    }
                }
                cashierAccountInfo += '</tbody></table>';
                content.innerHTML=cashierAccountInfo;
            });
        };

        // 选择银行
        $scope.selBankName =function(){
            var $selBankName = $("#selBankName");
            if($selBankName.val()!=null && $selBankName.val()!='') {
                $("#bankName").val($selBankName.find("option:selected").text());
            }
        }

        // 保存商户信息
        $scope.saveAccount = function () {
            var merchantUser = {};
            var merchantBank = {};
            var username = $("#username").val();
            var accountType = $("input[name=accountType]:checked").val();
            var passwd = $("#passwd").val();
            var repasswd = $("#repasswd").val();
            var bankName = $("#bankName").val();
            var bankNum = $("#bankNum").val();

            if(username!=null && username!='') {
                merchantUser.name = username;
            }else {
                alert("请输入用户名");
                return;
            }
            if(accountType!=null && accountType!='') {
                merchantUser.type = accountType;
            }else {
                alert("请选择账户类型");
                return;
            }
            if(passwd!=null && passwd!='') {
                merchantUser.passwd = passwd;
            }else {
                alert("请输入密码");
                return;
            }
            if(passwd.length<6) {
                alert("密码长度不小于 6 位");
                return;
            }
            if(repasswd!=passwd) {
                alert("两次输入的密码不一致");
                return;
            }
            if(bankName!=null && bankName!='') {
                merchantBank.bankName = bankName;
            }else {
                alert("请设置所在银行");
                return;
            }
            if(bankNum!=null && bankNum!='') {
                merchantBank.bankNum = bankNum;
            }else {
                alert("请输入银行卡号");
                return;
            }
            if(!$scope.checkInfo()) {
                return;
            }
            if(!$scope.checkNameRepeat()) {
                return;
            }
            merchantUser.merchantBank = merchantBank;
            $http.post('api/merchantUser/createAccount', merchantUser).success(function (response) {
                console.log(JSON.stringify(response));
                if(response.status==200){
                    alert("账号保存成功！");
                    window.location.reload();
                }else {
                    alert("账号创建失败 ！");
                }
            })
        }

        // 校验当前用户
        $scope.checkInfo = function () {
            var data = $("#thisPwd").val();
            var flag = true;
            $http.post('api/merchantUser/checkInfo', data).success(function (response) {
                if(response.status==400){
                    alert("当前账号密码不正确,请重新输入！");
                    $("#thisPwd").val('');
                    flag =false;
                }
            })
            return flag;
        }

        $scope.checkNameRepeat = function () {
            var username = $("#username").val();
            var flag = true;
            $http.post('api/merchantUser/checkRepeat', username).success(function (response) {
                if(response.status==400){
                    alert("用户名已存在,换个试试吧");
                    $("#username").val('');
                    flag =false;
                }
            })
            return flag;
        }
        $scope.resetAll = function () {
            $("#username").val('');
            $("#passwd").val('');
            $("#repasswd").val('');
            $("#bankName").val('');
            $("#bankNum").val('');
            $("#selBankName").val('');
        }
    })
