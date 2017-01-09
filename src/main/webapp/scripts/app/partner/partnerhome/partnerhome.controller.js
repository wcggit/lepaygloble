'use strict';

angular.module('lepayglobleApp')
    .controller('partnerHomeController',
                function ($scope, $http, $rootScope, $location, Principal, Partner, Tracker) {
                    $('body').css({background: '#f3f3f3'});
                    $('.main-content').css({height: 'auto'});
                    //else{
                    $scope.flag = false;
                    $scope.nickName = "立即绑定微信号";
                    $scope.headUrl = "../../../../assets/styles/images/head/touxiang.png";
                    $http.get('api/partner/info').success(function (response) {
                        $scope.payee = response.data.partner.payee;
                        var bankNumber = response.data.partner.bankNumber;
                        $scope.bank =
                        bankNumber.substring(bankNumber.length - 4, bankNumber.length);
                        $scope.qrCodeUrl = response.data.qrCodeUrl;
                        if (response.data.partner.weiXinUser != null) {
                            $scope.nickName = response.data.partner.weiXinUser.nickname;
                            $scope.headUrl = response.data.partner.weiXinUser.headImageUrl;
                            $scope.flag = true;
                        }
                    });
                    Tracker.receiveWx().then(null, null, function (data) {//等待websocket发送消息
                        $http.get('api/partner').success(function (response) {
                            if (response.data.weiXinUser != null) {
                                $("#bindWeixin").modal("hide");
                                $scope.nickName = response.data.weiXinUser.nickname;
                                $scope.headUrl = response.data.weiXinUser.headImageUrl;
                                $scope.flag = true;
                            }
                        });
                    });

                    Partner.getPartnerHomePageData().then(function (result) {
                        $scope.userLimit = result['userLimit'];
                        $scope.merchantLimit = result['merchantLimit'];
                        $scope.perCommission = result['perCommission'];
                        $scope.bindMerchant = result['bindMerchant'];
                        $scope.bindLeJiaUser = result['bindLeJiaUser'];
                        $scope.available = toDecimal(result['available']);
                        $scope.withdrawTotalPrice = toDecimal(result['withdrawTotalPrice']);
                        $scope.total = result['total'];
                        $scope.dayCommission = result['dayCommission'];
                        $scope.dayBindLeJiaUser = result['dayBindLeJiaUser'];
                        $scope.availableScoreA = result['availableScoreA'];
                        $scope.availableScoreB = result['availableScoreB'];
                        $scope.availableResult = toDecimal($scope.available-$scope.withdrawTotalPrice);
                        var progressWidth1 = $scope.bindMerchant / $scope.merchantLimit * 100;
                        var progressWidth2 = $scope.bindLeJiaUser / $scope.userLimit * 100;
                        $('.progress-bar1').css('width', progressWidth1 + '%');
                        $('.progress-bar2').css('width', progressWidth2 + '%');

                        function toDecimal(x) {
                            var f = parseFloat(x);
                            if (isNaN(f)) {
                                return false;
                            }
                            var f = Math.round(x*100)/100;
                            var s = f.toString();
                            var rs = s.indexOf('.');
                            if (rs < 0) {
                                rs = s.length;
                                s += '.';
                            }
                            while (s.length <= rs + 2) {
                                s += '0';
                            }
                            return s;
                        }
                    });

                    Partner.getMerchantTop5(0).then(function (result) {
                        $scope.homeTab = result.data;
                    });

                    $scope.home = {
                        firNum: 23,
                        secNum: 32.34,
                        thirNum: 32.34,
                        forNum: 32.34
                    };

                    $scope.tx = function () {
                        var withdrawTotalPrice=$scope.withdrawTotalPrice;
                        var available= $scope.available;
                        if(available-withdrawTotalPrice>=200){
                            $("#tx").modal("toggle");
                        }
                    }

                    // 提现功能
                    $scope.withDraw = function () {
                        var amount = $("#inputPassword1").val();
                        if (amount < 200) {
                            return;
                        }
                        var data = 'amount='
                                   + encodeURIComponent($("#inputPassword1").val().trim());
                        $http.post('/withdraw/partner_withdraw', data, {
                            headers: {
                                'Content-Type': 'application/x-www-form-urlencoded'
                            }
                        }).success(function (response) {
                            if (response.status == 400) {
                                alert("服务繁忙,请稍后尝试!");
                            } else {
                                alert("提现申请成功 !");
                                $("#inputPassword1").val('');
                                $scope.tx();
                                Partner.getPartnerHomePageData().then(function (result) {
                                    $scope.userLimit = result['userLimit'];
                                    $scope.merchantLimit = result['merchantLimit'];
                                    $scope.perCommission = result['perCommission'];
                                    $scope.bindMerchant = result['bindMerchant'];
                                    $scope.bindLeJiaUser = result['bindLeJiaUser'];
                                    $scope.available = toDecimal(result['available']);
                                    $scope.withdrawTotalPrice = toDecimal(result['withdrawTotalPrice']);
                                    $scope.total = result['total'];
                                    $scope.dayCommission = result['dayCommission'];
                                    $scope.dayBindLeJiaUser = result['dayBindLeJiaUser'];
                                    $scope.availableScoreA = result['availableScoreA'];
                                    $scope.availableScoreB = result['availableScoreB'];
                                    $scope.availableResult = toDecimal($scope.available-$scope.withdrawTotalPrice);
                                    var progressWidth1 = $scope.bindMerchant / $scope.merchantLimit * 100;
                                    var progressWidth2 = $scope.bindLeJiaUser / $scope.userLimit * 100;
                                    $('.progress-bar1').css('width', progressWidth1 + '%');
                                    $('.progress-bar2').css('width', progressWidth2 + '%');

                                    function toDecimal(x) {
                                        var f = parseFloat(x);
                                        if (isNaN(f)) {
                                            return false;
                                        }
                                        var f = Math.round(x*100)/100;
                                        var s = f.toString();
                                        var rs = s.indexOf('.');
                                        if (rs < 0) {
                                            rs = s.length;
                                            s += '.';
                                        }
                                        while (s.length <= rs + 2) {
                                            s += '0';
                                        }
                                        return s;
                                    }
                                });
                            }
                        })
                    };

                    // TOP5
                    var navBtn = $('.partner-home .nav-pills li');
                    var navDiv = $('.partner-home .tab-content .tab-pane');
                    navBtn.each(function (i) {
                        navBtn.eq(i).click(function () {
                            for (var t = 0; t < 3; t++) {
                                navBtn.eq(t).removeClass('active');
                                navDiv.eq(t).removeClass('active')
                            }
                            var index = $(this).index();
                            Partner.getMerchantTop5(index).then(function (result) {
                                $scope.homeTab = result.data;
                            });
                            navBtn.eq(index).addClass('active');
                            navDiv.eq(index).addClass('active')
                        })
                    })

                });

