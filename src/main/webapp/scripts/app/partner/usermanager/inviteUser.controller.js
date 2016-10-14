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

<<<<<<< HEAD
=======
                    function getTotalPage() {
                        Commission.getTotalPagesByBindPartner(criteria).then(function (response) {
                            $scope.totalPages = response.data;
                            loadContent();
                        });
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
                        getTotalPage()
                    }
                    $("input[type=number]").attr("disabled","disabled");
                   $("input[type=radio]").click(function(){
                       var name = $(this).attr("name");
                      $("input[name=" + name + "]").next().next().attr("disabled","disabled");
                       $("input[name=" + name + "]").next().next().next().next().attr("disabled","disabled");
                       $(this).next().next().removeAttr("disabled");
                       $(this).next().next().next().next().removeAttr("disabled");
                   });
>>>>>>> 3031fe6ae75c8526a3328ced1ed535237330a0d3
                });
