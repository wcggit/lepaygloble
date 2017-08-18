 /**
 * Created by recoluan on 2016/11/16.
 */
 'use strict';


angular.module('lepayglobleApp')
    .controller('cp-infoManageController', function ($scope, $state, $rootScope, $location,Principal,Auth,$http,$stateParams) {
         $scope.currentTab0 = true;
         $scope.currentTab1 = false;
         $scope.onClickTab = function () {
             $scope.currentTab0 = !$scope.currentTab0;
             $scope.currentTab1 = !$scope.currentTab1;
         }
         var partnerSid = $stateParams.partnerSid;
         $scope.partnerSid = partnerSid;
         $http.get('api/partnerManager/partner/findBySid?partnerSid='+partnerSid).success(function (response) {
             $scope.partner = response.data;
         });
    })
