'use strict';
angular.module('lepayglobleApp')
    .controller('inviteUserController', function ($scope,InviteUser) {
                    $('body').css({background: '#fff'});
                    $('.main-content').css({height: 'auto'});

                    var currentPage = 1;
                    var criteria = {};
                    criteria.offset = 1;
                    loadContent();
                    function loadContent() {
                        InviteUser.getTotalCount().then(function (response) {
                            var data = response.data;
                            $scope.inviteM = data.inviteM;
                            $scope.totalA = data.totalA/100.0;
                            $scope.totalB = data.totalB;
                            $scope.page = currentPage;
                        });
                        InviteUser.getPartnerMerchantInfo(criteria).then(function (response) {
                            var data = response.data;
                            var page = data.page;
                            $scope.totalPages = page.totalPages;
                            $scope.totalElements = page.totalElements;
                            $scope.pulls = page.content;
                            $scope.wxScoreas = data.wxScoreas;
                            $scope.wxScorebs = data.wxScorebs;
                        });
                        InviteUser.getPartnerInfo().then(function (response) {
                            //  QrCode
                            $("#qrCodeImg").attr("src",response.hbQrCodeUrl);

                            //  Gift
                            if(response.scoreAType==0) {
                                $("#stableSaRio").attr("checked",true);
                                $("#stableSaNum").val(response.maxScoreA/100.0);
                            }
                            if(response.scoreAType==1) {
                                $("#randSaRio").attr("checked",true);
                                $("#randSaMaxNum").val(response.maxScoreA/100.0);
                                $("#randSaMinNum").val(response.minScoreA/100.0);
                            }
                            if(response.scoreBType==0) {
                                $("#stableSbRio").attr("checked",true);
                                $("#stableSbNum").val(response.maxScoreB);
                            }
                            if(response.scoreBType==1) {
                                $("#randSbRio").attr("checked",true);
                                $("#randSbMaxNum").val(response.maxScoreB);
                                $("#randSbMinNum").val(response.minScoreB);
                            }
                        });
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
                        criteria.offset = page;
                        loadContent();
                    };
                    $scope.submitPartnerInfo = function() {
                        var partnerInfo = {};
                        if($("#stableSaRio").is(":checked")) {
                            partnerInfo.scoreAType = 0;
                            partnerInfo.maxScoreA = $("#stableSaNum").val();
                            partnerInfo.minScoreA = $("#stableSaNum").val();
                        }
                        if($("#randSaRio").is(":checked")){
                            partnerInfo.scoreAType = 1;
                            partnerInfo.maxScoreA = $("#randSaMaxNum").val();
                            partnerInfo.minScoreA = $("#randSaMinNum").val();
                        }
                        if($("#stableSbRio").is(":checked")) {
                            partnerInfo.scoreBType = 0;
                            partnerInfo.maxScoreB = $("#stableSbNum").val();
                            partnerInfo.minScoreB = $("#stableSbNum").val();
                        }
                        if($("#randSbRio").is(":checked")){
                            partnerInfo.scoreBType = 1;
                            partnerInfo.maxScoreB = $("#randSbMaxNum").val();
                            partnerInfo.minScoreB = $("#randSbMinNum").val();
                        }
                        if($("#noScoreRio").is(":checked")) {
                            partnerInfo.scoreBType = 0;
                            partnerInfo.maxScoreB = 0;
                            partnerInfo.minScoreB = 0;
                        }
                        InviteUser.savePartnerInfo(partnerInfo).then(function (response) {
                            if(response.status==200) {
                                alert("新设置已成功保存 !");
                            }
                        });
                    }
                    $scope.clearRandA = function () {
                        $("#randSaMinNum").val('');
                        $("#randSaMaxNum").val('');
                    }
                    $scope.clearStableA = function () {
                        $("#stableSaNum").val('');
                    }
                    $scope.clearRandB =function () {
                        $("#randSbMinNum").val('');
                        $("#randSbMaxNum").val('');
                    }
                    $scope.clearStableB = function () {
                        $("#stableSbNum").val('');
                    }
                    $scope.clearAllB = function () {
                        $scope.clearRandB();
                        $scope.clearStableB();
                    }
                    $scope.searchByCriteria = function () {
                        var dateStr = $("#timePicker1").val();
                        var phone = $("#phone").val();
                        var merchantName = $("#merchantName").val();
                        if (dateStr != null && dateStr != "") {
                            var startDate = dateStr.split("-")[0].trim();
                            var endDate = dateStr.split("-")[1].trim();
                            criteria.partnerStartDate = startDate;
                            criteria.partnerEndDate = endDate;
                        }
                        criteria.offset = 1;
                        criteria.merchantName = merchantName;
                        criteria.phone = phone;
                        currentPage = 1;
<<<<<<< HEAD
                        getTotalPage()
                    };
                    $scope.testAS = function(e) {
                        alert(1);
                    };
=======
                    }
>>>>>>> 57daf7c2ef4aac4eb5680054d34c4c09e7de7f9b
                    $("input[type=number]").attr("disabled","disabled");
                    $("input[type=radio]").click(function(){
                       var name = $(this).attr("name");
                      $("input[name=" + name + "]").next().next().attr("disabled","disabled");
                       $("input[name=" + name + "]").next().next().next().next().attr("disabled","disabled");
                       $(this).next().next().removeAttr("disabled");
                       $(this).next().next().next().next().removeAttr("disabled");
                   });
                });



