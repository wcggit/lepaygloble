/**
 * Created by recoluan on 2017/3/15.
 */
/**
 * Created by recoluan on 2016/11/16.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('cp-withdrawRecordController', function ($scope, $state, $rootScope, $location, Principal, Auth, $http) {
        $scope.currentTab0 = true;
        $scope.currentTab1 =$scope.currentTab2 =$scope.currentTab3 = false;
        $scope.priviousState = 0;
        $scope.currentState = 0;
        $scope.onClickTab = function (index) {
            $scope.priviousState = $scope.currentState;
            $scope.currentState = index;

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
                    break;
                case 2:
                    $scope.currentTab2 = true;
                    break;
                default:
                    $scope.currentTab3 = true;
            }
        };


        var currentPage = 1;
        var withdrawCriteria = {};
        withdrawCriteria.offset = 1;

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
            /*if($("#state").val()!=-1) {
                withdrawCriteria.state = $("#state").val();
            }else {
                withdrawCriteria.state = null;
            }*/
            withdrawCriteria.offset = 1;
            currentPage = 1;
            loadContent();
        }

    });
