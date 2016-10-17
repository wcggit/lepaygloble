'use strict';
angular.module('lepayglobleApp')
    .controller('inviteUserController', function ($scope, Commission) {
                    $('body').css({background: '#fff'});
                    $('.main-content').css({height: 'auto'});

                    $("#timePicker1").val("");
                    var currentPage = 1;
                    var criteria = {};
                    criteria.offset = 1;
                    getTotalPage();
                    function loadContent() {
                        Commission.getUsersByBindPartner(criteria).then(function (response) {
                            var data = response.data;
                            $scope.page = currentPage;
                            $scope.pulls = data;
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
                    };
                    $scope.testAS = function(e) {
                        alert(1);
                    };
                    $("input[type=number]").attr("disabled","disabled");
                   $("input[type=radio]").click(function(){
                       var name = $(this).attr("name");
                      $("input[name=" + name + "]").next().next().attr("disabled","disabled");
                       $("input[name=" + name + "]").next().next().next().next().attr("disabled","disabled");
                       $(this).next().next().removeAttr("disabled");
                       $(this).next().next().next().next().removeAttr("disabled");
                   });
                });
