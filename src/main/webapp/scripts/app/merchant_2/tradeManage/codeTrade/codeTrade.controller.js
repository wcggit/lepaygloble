/**
 * Created by recoluan on 2016/11/16.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('codeTradeController', function ($scope, $state, $rootScope, $location, Principal, Auth, $http) {
        // $scope.currentTab0 = true;
        // $scope.currentTab1 = false;
        // $scope.onClickTab = function () {
        //     $scope.currentTab0 = !$scope.currentTab0;
        //     $scope.currentTab1 = !$scope.currentTab1;
        // };
        var URL_ = window.location.href.split("/");
        var indexActive = URL_[URL_.length-1];
        console.log(indexActive);
        switch (indexActive){
            case "codeTradeRecord":
                $scope.currentTab0 = true;
                $scope.currentTab1 = false;
                $scope.currentTab2 = false;
                $scope.currentTab3 = false;
                break;
            case "myCode":
                $scope.currentTab0 = false;
                $scope.currentTab1 = true;
                $scope.currentTab2 = false;
                $scope.currentTab3 = false;
                break;
            case "refund":
                $scope.currentTab0 = false;
                $scope.currentTab1 = false;
                $scope.currentTab2 = true;
                $scope.currentTab3 = false;
                break;
            case "refundList":
                $scope.currentTab0 = false;
                $scope.currentTab1 = false;
                $scope.currentTab2 = false;
                $scope.currentTab3 = true;
                break;
            default:
                $scope.currentTab0 = true;
                $scope.currentTab1 = false;
                $scope.currentTab2 = false;
                $scope.currentTab3 = false;
                break;
        }
        $scope.onClickTab = function (n) {
            switch (n){
                case 0:
                    $scope.currentTab0 = true;
                    $scope.currentTab1 = false;
                    $scope.currentTab2 = false;
                    $scope.currentTab3 = false;
                    break;
                case 1:
                    $scope.currentTab0 = false;
                    $scope.currentTab1 = true;
                    $scope.currentTab2 = false;
                    $scope.currentTab3 = false;
                    break;
                case 2:
                    $scope.currentTab0 = false;
                    $scope.currentTab1 = false;
                    $scope.currentTab2 = true;
                    $scope.currentTab3 = false;
                    break;
                case 3:
                    $scope.currentTab0 = false;
                    $scope.currentTab1 = false;
                    $scope.currentTab2 = false;
                    $scope.currentTab3 = true;
                    break;
                default:
                    $scope.currentTab0 = true;
                    $scope.currentTab1 = false;
                    $scope.currentTab2 = false;
                    $scope.currentTab3 = false;
                    break;
            }
        };
    })
