/**
 * Created by recoluan on 2016/11/25.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('imBasicInfoController',function ($scope, $state, $rootScope, $location,Principal,Auth,$http,$stateParams) {
        var partnerSid = $stateParams.partnerSid2;
        $http.get('api/partnerManager/partner/findBySid?partnerSid='+partnerSid).success(function (response) {
            $scope.partner = response.data;
        });
    })
