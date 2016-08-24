'use strict';

angular.module('lepayglobleApp')
    .controller('partnerHomeController',
                function ($scope, $http, $rootScope, $location, Principal, Partner) {
                    $('body').css({background: '#f3f3f3'});
                    $('.main-content').css({height: 'auto'});

                    $http.get('api/partner').success(function (response) {
                        $scope.payee = response.data.payee;
                        var bankNumber = response.data.bankNumber;
                        $scope.bank = bankNumber.substring(bankNumber.length - 4, bankNumber.length);
                    });

                    Principal.identity().then(function (account) {
                        $scope.account = account;
                    });
                    Partner.getPartnerHomePageData().then(function (result) {
                        $scope.userLimit = result['userLimit'];
                        $scope.merchantLimit = result['merchantLimit'];
                        $scope.perCommission = result['perCommission'];
                        $scope.bindMerchant = result['bindMerchant'];
                        $scope.bindLeJiaUser = result['bindLeJiaUser'];
                        $scope.available = result['available'];
                        $scope.total = result['total'];
                        $scope.dayCommission = result['dayCommission'];
                        $scope.dayBindLeJiaUser = result['dayBindLeJiaUser'];
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
                    $('.progress-bar1').css('width', '50%');
                    $('.progress-bar2').css('width', '50%');

                    $scope.tx = function () {
                        $("#tx").modal("toggle");
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

                    $scope.homeTab = [
                        {
                            num: 1,
                            name: '一品江南（朝阳大悦城店）',
                            itemsMon: '32.34',
                            myMon: '43.23'
                        },
                        {
                            num: 2,
                            name: '一品江南（朝阳大悦城店）',
                            itemsMon: '32.34',
                            myMon: '43.23'
                        },
                        {
                            num: 3,
                            name: '一品江南（朝阳大悦城店）',
                            itemsMon: '32.34',
                            myMon: '43.23'
                        }
                    ]
                });

