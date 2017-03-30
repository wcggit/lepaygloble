 /**
 * Created by recoluan on 2016/11/16.
 */
 'use strict';


angular.module('lepayglobleApp')
    .controller('lockMembersController', function ($scope, $state, $rootScope, $location,Principal,Auth,$http) {
                    var array = new Array();
                    $http.get("/api/merchantUser/merchantsInfo").success(function (response) {
                        /*$http.get("api/codeTrade/getMerchants").success(function (response) {*/
                        if(response.status == 200){
                            var data = response.data;

                            $scope.myStore = data;

                            angular.forEach(data,function (data,index) {
                                array[index] = data[0];
                            });
                            $scope.loadLockMembersInfo();
                        }else{
                            alert("加载门店错误...");
                        }
                    });

                    var currentPage = 1;
                    $scope.loadLockMembersInfo = function (){
                        var lockMembersCriteria = {};
                        var a =$("#selectStore").val();
                        if(a!=""){
                            lockMembersCriteria.storeIds = $.makeArray(a);
                        }else{
                            lockMembersCriteria.storeIds = array;
                        }
                        var completeDate = $("#completeDate").val().split("-");
                        lockMembersCriteria.startDate = completeDate[0];
                        lockMembersCriteria.endDate = completeDate[1];
                        lockMembersCriteria.currentPage = currentPage;
                        $http.post("/api/lockMenber/lockMenberByMerchantUser",lockMembersCriteria).success(function (response) {
                            if(response.status == 200){
                                $scope.lockMembersCriteria = response.data.lockMembers;
                                $scope.lockMembersData = response.data;
                                $scope.page = currentPage;
                                $scope.totalPages = $scope.lockMembersData.totalPages;
                                if($scope.lockMembersCriteria.length>0){
                                    $("#notData").hide();
                                }else{
                                    $("#notData").show();
                                }

                            }else{
                                alert('加载绑定会员数据错误...');
                            }
                            console.log(response);
                        });
                    };

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
                        console.log(start+"--------"+end);
                    });


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
                        $scope.loadLockMembersInfo();
                    };


                    $scope.searchByCriteria = function () {
                        currentPage = 1;
                        $scope.loadLockMembersInfo();
                    };
    })
