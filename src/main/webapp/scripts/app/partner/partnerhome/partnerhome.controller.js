'use strict';

angular.module('lepayglobleApp')
    .controller('partnerHomeController', function ($scope, $http, $rootScope, $location,Principal,Auth) {
        $('body').css({background: '#f3f3f3'});
        $('.main-content').css({height: 'auto'});

        $scope.home={
            firNum:23,
            secNum:32.34,
            thirNum:32.34,
            forNum:32.34
        };
        $('.progress-bar1').css('width','50%');
        $('.progress-bar2').css('width','50%');

        $scope.tx=function () {
            $("#tx").modal("toggle");
        }

        $scope.homeTab=[
            {
                num:1,
                name:'一品江南（朝阳大悦城店）',
                itemsMon:'32.34',
                myMon:'43.23'
            },
            {
                num:2,
                name:'一品江南（朝阳大悦城店）',
                itemsMon:'32.34',
                myMon:'43.23'
            },
            {
                num:3,
                name:'一品江南（朝阳大悦城店）',
                itemsMon:'32.34',
                myMon:'43.23'
            }
        ]
    });

