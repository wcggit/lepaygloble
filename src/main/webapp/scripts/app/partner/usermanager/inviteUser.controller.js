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

                });

