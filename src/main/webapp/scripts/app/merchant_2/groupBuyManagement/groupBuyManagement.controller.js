/**
 * Created by recoluan on 2016/11/16.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('groupBuyManagementController', function ($scope, $state, $rootScope, $location, Principal, Auth, $http) {
        var URL_ = window.location.href.split("/");
        var indexActive = URL_[URL_.length-1];
        switch (indexActive){
            case "groupBuy":
                $scope.currentTab0 = true;
                $scope.currentTab1 = false;
                break;
            case "writeOff":
                $scope.currentTab0 = true;
                $scope.currentTab1 = false;
                break;
            case "info":
                $scope.currentTab0 = false;
                $scope.currentTab1 = true;
                break;
            default:
                $scope.currentTab0 = true;
                $scope.currentTab1 = false;
                break;
        }
        $scope.onClickTab = function () {
            $scope.currentTab0 = !$scope.currentTab0;
            $scope.currentTab1 = !$scope.currentTab1;
        };
    })
