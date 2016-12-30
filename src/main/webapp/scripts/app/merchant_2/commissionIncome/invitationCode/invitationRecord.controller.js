/**
 * Created by recoluan on 2016/11/25.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('invitationRecordController', function ($scope, $state, $rootScope, $location,Principal,Auth,$http,HomePage,bindUser) {
        // 门店列表
        HomePage.getMerchantsInfo().then(function(response) {
            var data = response.data;
            $scope.merchants = data;
        });
        // 商户数据信息
        bindUser.countMerchantBindUsers().then(function(data){
            var data = data.data;
            $scope.lockCount = {
                firNum:  data.dailyCount,                      // 今日邀请会员
                secNum:  data.totalCount,                      // 累计邀请会员
                thirNum: data.totalScorea / 100.0,                     // 注册获得红包
                fouNum: data.totalScoreb                               // 注册获得积分
            };
        });
        // 日期选择
        $('#completeDate').daterangepicker({
            timePicker: true, //是否显示小时和分钟
            timePickerIncrement: 1, //时间的增量，单位为分钟
            opens: 'right', //日期选择框的弹出位置
            startDate: moment().format('YYYY/MM/DD HH:mm:00'),
            endDate: moment().format('YYYY/MM/DD HH:mm:59'),
            format: 'YYYY/MM/DD HH:mm:ss', //控件中from和to 显示的日期格式
            ranges: {
                '最近1小时': [moment().subtract('hours', 1), moment()],
                '今日': [moment().startOf('day'), moment()],
                '昨日': [moment().subtract('days', 1).startOf('day'),
                    moment().subtract('days', 1).endOf('day')],
                '最近7日': [moment().subtract('days', 6), moment()],
                '最近30日': [moment().subtract('days', 29), moment()]
            }
        }, function (start, end, label) {

        });


        //  查询条件
        var currentPage = 1;
        var lockMembersCriteria = {};
        lockMembersCriteria.currentPage = 1;
        // 翻页
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
            $scope.loadLockMembers();
        };

        $scope.searchByCriteria = function () {
            currentPage = 1;
            $scope.page = currentPage;
            var completeDate = $("#completeDate").val().split("-");
            lockMembersCriteria.startDate = completeDate[0];
            lockMembersCriteria.endDate = completeDate[1];
            var merchantId = $("#selMerchant").val();
            if(merchantId!=null && merchantId!='') {
                lockMembersCriteria.storeIds = [merchantId];
            }else {
                lockMembersCriteria.storeIds = null;
            }
            $scope.loadLockMembers();
            getTotalPage();
        };


        $scope.loadLockMembers = function() {
            $scope.page = currentPage;
            lockMembersCriteria.currentPage = currentPage;
            bindUser.getMerchantBindUserList(lockMembersCriteria).then(function (response) {
                var data = response.data;
                $scope.lockMembers = data.lockMembers;
                $scope.scoreas = data.scoreas;
                $scope.scorebs = data.scorebs;
            });
        }

        //  计算总页数
        function  getTotalPage(){
            bindUser.countMerchantBindUserTotalPages(lockMembersCriteria).then(function (response) {
                $scope.totalPages = response.data;
            });
        }

        //  默认查询全部
        $scope.loadLockMembers();
        getTotalPage();
    })
