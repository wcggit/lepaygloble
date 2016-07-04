'use strict';

angular.module('lepayglobleApp')
    .controller('OrderListController', function ($scope, $state, $location, Trade) {
                    var currentPage = 1;
                    var olOrderCriteria = {};
                    olOrderCriteria.offset = 1;
                    loadContent();

                    function loadContent() {
                        Trade.getOrderList(olOrderCriteria).then(function (results) {
                            var page = results.data;

                            $scope.pulls = page.content;
                            $scope.page = currentPage;
                            $scope.totalPages = page.totalPages;
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
                        olOrderCriteria.offset = page;
                        loadContent();
                    };

                });
