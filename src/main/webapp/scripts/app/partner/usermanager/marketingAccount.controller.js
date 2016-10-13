'use strict';

angular.module('lepayglobleApp')
    .controller('marketingAccountController', function ($scope, marketingAccount,Partner) {
                    $('body').css({background: '#fff'});
                    $('.main-content').css({height: 'auto'});
                    var currentPage = 1;
                    var scoreLogCriteria = {};
                    scoreLogCriteria.offset = 1;
                    // *  Loading ...
                    loadContent();
                    function loadContent() {
                        marketingAccount.getPartnerScoreLog(scoreLogCriteria).then(function (response) {
                            var data = response.data;
                            $scope.page = currentPage;
                            $scope.homeTab = data.content;
                            $scope.totalPages = data.totalPages;
                        });
                    }
                    $scope.loadAll = function() {
                        delete scoreLogCriteria['numberType'];
                        loadContent();
                    }
                    $scope.loadIncrease = function() {
                        scoreLogCriteria.numberType = 1;
                        loadContent();
                    }
                    $scope.loadDecrease = function () {
                        scoreLogCriteria.numberType = -1;
                        loadContent();
                    }
                    $scope.loadPage = function (page) {
                        if (page == 0) {
                            return;
                        }
                        if (page > $scope.totalPages) {
                            return;
                        }
                        if (currentPage == $scope.totalPages && page == $scope.totalPages) {
                            return;
                        }
                        if (currentPage == 1 && page == 1) {
                            return;
                        }
                        currentPage = page;
                        scoreLogCriteria.offset = page;
                        loadContent();
                    };
                    Partner.getPartnerHomePageData().then(function (result) {
                        $scope.availableScoreA = result['availableScoreA'];
                        $scope.availableScoreB = result['availableScoreB'];
                        $scope.bindMerchant = result['bindMerchant'];
                        $scope.bindLeJiaUser = result['bindLeJiaUser'];
                    });
                    marketingAccount.getPartnerTotalScore().then(function (result) {
                        $scope.totalScorea = result['totalScorea'];
                        $scope.totalScoreb = result['totalScoreb'];
                    });
                    // * Recharge
                    $scope.pushRecharge = function() {
                        if($("#inScorea").val()=='' && $("#inScoreb").val()=='') {
                            alert("请输入有效的充值金额 !");
                            return;
                        }
                        var partnerRecharge = {}
                        partnerRecharge.scorea = $("#inScorea").val();
                        partnerRecharge.scoreb = $("#inScoreb").val();
                        marketingAccount.requestRecharge(partnerRecharge).then(function (result) {
                            if(result.status==200) {

                                $("#inScorea").val('');
                                $("#inScoreb").val('');
                            }
                        });
                    }
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
                            navBtn.eq(index).addClass('active');
                            navDiv.eq(index).addClass('active')
                        })
                    })
                });

