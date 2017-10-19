/**
 * Created by recoluan on 2016/11/25.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('gulijinDetailController', function ($scope, $state, $rootScope, $location, Principal, Auth, $http, Trade, $stateParams) {
        var unionPosOrderCriteria = {};
        var currentPage = 1;
        unionPosOrderCriteria.mid = $stateParams.mid;
        unionPosOrderCriteria.tradeDate = $stateParams.tradeDate;
        loadContent();
        $scope.currentTab0 = true;
        $scope.priviousState = 0;
        $scope.currentState = 0;
        $scope.onClickTab = function (index) {
            $scope.priviousState = $scope.currentState;
            $scope.currentState = index;
            //  切换样式
            switch ($scope.priviousState) {
                case 0:
                    $scope.currentTab0 = false;
                    break;
            }
            switch ($scope.currentState) {
                //  全部状态
                case 0:
                    $scope.currentTab0 = true;
                    break;
            }
        };


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
            loadContent();
        };


        function loadContent() {
            $http.post('/api/unionPosOrder/findByCriteria', unionPosOrderCriteria, {
                headers: {
                    'Content-Type': 'application/json'
                }
            }).success(function (response) {
                var page = response.data;
                $scope.page = currentPage;
                $scope.totalPages = page.totalPages;
                if (page.content.length > 0)
                    $scope.posOrders = page.content;
            });
        }
    });
