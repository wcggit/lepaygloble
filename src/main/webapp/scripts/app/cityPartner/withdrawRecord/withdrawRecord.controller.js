/**
 * Created by recoluan on 2017/3/15.
 */
/**
 * Created by recoluan on 2016/11/16.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('cp-withdrawRecordController', function ($scope, $state, $rootScope, $location, Principal, Auth, $http) {
        // 设置查询条件
        var currentPage = 1;
        var withdrawCriteria = {};
        withdrawCriteria.offset = 1;
        var state = -1;                          //  0是申请中 1是提现完成   2已驳回


        $scope.ttlWarn1 = true;
        $scope.ttlWarn2 = true;
        $scope.currentTab0 = true;
        $scope.currentTab1 =$scope.currentTab2 =$scope.currentTab3 = false;
        $scope.priviousState = 0;
        $scope.currentState = 0;
        $scope.onClickTab = function (index) {
            $scope.priviousState = $scope.currentState;
            $scope.currentState = index;
            if(index==0) {
                state = -1;
            }else if(index==1) {
                state = 0;
            }else if(index==2) {
                state = 2;
            }else {
                state = 1;
            }
            switch($scope.priviousState)
            {
                case 0:
                    $scope.currentTab0 = false;
                    break;
                case 1:
                    $scope.currentTab1 = false;
                    break;
                case 2:
                    $scope.currentTab2 = false;
                    break;
                default:
                    $scope.currentTab3 = false;
            }
            switch($scope.currentState)
            {
                case 0:
                    $scope.currentTab0 = true;
                    break;
                case 1:
                    $scope.currentTab1 = true;
                    $scope.ttlWarn1 = false;
                    break;
                case 2:console.log($('this').index());
                    $scope.currentTab2 = true;
                    $scope.ttlWarn2 = false;
                    break;
                default:
                    $scope.currentTab3 = true;
            }
            $scope.searchByCriteria();
        };

        loadContent();
        function loadContent() {
            $http.post('/withdraw/cityPartner_withdraw/findAll', withdrawCriteria, {
                headers: {
                    'Content-Type': 'application/json'
                }
            }).success(function (response) {
                var data = response.data;
                $scope.page = currentPage;
                $scope.totalPages = data.totalPages;
                $scope.pulls = data.content;
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
            withdrawCriteria.offset = page;
            loadContent();
        };

        $scope.searchByCriteria = function () {
            if(state!=-1) {
                withdrawCriteria.state = state;
            }else {
                withdrawCriteria.state = null;
            }
            withdrawCriteria.offset = 1;
            currentPage = 1;
            loadContent();
        }

    });
