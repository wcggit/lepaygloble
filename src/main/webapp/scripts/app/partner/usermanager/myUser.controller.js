'use strict';

angular.module('lepayglobleApp')
    .controller('myUserController', function ($scope, Commission, Welfare, $http, $filter) {
                    $scope.inclusiveArray = []; //个选包含的数组
                    $scope.exclusiveArray = [];//全选排开的数组
                    $scope.selectedCheckbox = 0;
                    $scope.date = new Date();
                    $scope.selected = false; //全选
                    $('body').css({background: '#fff'});
                    $('.main-content').css({height: 'auto'});
                    $('#timePicker1')
                        // .val(moment().subtract('day', 1).format('YYYY/MM/DD HH:mm:00') + ' - ' +
                        // moment().format('YYYY/MM/DD HH:mm:59'))
                        .daterangepicker({
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
                    $("#timePicker1").val("");
                    var currentPage = 1;
                    var criteria = {};
                    criteria.offset = 1;
                    getTotalPage();
                    function loadContent() {
                        Commission.getUsersByBindPartner(criteria).then(function (response) {
                            var data = response.data;
                            $scope.page = currentPage;
                            $scope.pulls = data;
                        })
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
                        criteria.offset = page;
                        loadContent();
                    };

                    function getTotalPage() {
                        Commission.getTotalPagesByBindPartner(criteria).then(function (response) {
                            $scope.totalPages = response.data.totalPages;
                            $scope.totalElements = response.data.totalElements;
                            $scope.totalIncome = response.data.totalIncome;
                            if ($('#checkbox-1').prop('checked')){
                                $scope.selectedCheckbox = response.data.totalElements;
                            }
                            loadContent();
                        });
                    }

                    $scope.searchByCriteria = function () {
                        var dateStr = $("#timePicker1").val();
                        var phone = $("#phone").val();
                        var merchantName = $("#merchantName").val();
                        if (dateStr != null && dateStr != "") {
                            var startDate = dateStr.split("-")[0].trim();
                            var endDate = dateStr.split("-")[1].trim();
                            criteria.partnerStartDate = startDate;
                            criteria.partnerEndDate = endDate;
                        }
                        criteria.offset = 1;
                        criteria.merchantName = merchantName;
                        criteria.phone = phone;
                        criteria.nickname = $("#weixinNickNmame").val();
                        criteria.consumptionCount = $("#consumptionCount").val();
                        criteria.countSelect = $("#countSelect").val();
                        criteria.timeSelect = $("#timeSelect").val();
                        criteria.consumptionTimes = $("#consumptionTimes").val();
                        currentPage = 1;
                        getTotalPage()
                    }

                    // 复选框
                    $('#checkbox-1').click(function () {
                        if ($('#checkbox-1').prop('checked')) {
                            $scope.exclusiveArray = [];
                            $scope.inclusiveArray = [];
                            $scope.selected = true;
                            $scope.selectedCheckbox = $scope.totalElements;
                            $(this).next('label').removeClass('chbx-init').addClass('chbx-focus');
                            $('.checkbox-2').next('label').removeClass('chbx-init').addClass('chbx-focus');
                            $('.checkbox-2').prop('checked', 'true');

                        } else {
                            $scope.exclusiveArray = [];
                            $scope.inclusiveArray = [];
                            $scope.selected = false;
                            $scope.selectedCheckbox = 0;
                            $(this).next('label').removeClass('chbx-focus').addClass('chbx-init');
                            $('.checkbox-2').next('label').removeClass('chbx-focus').addClass('chbx-init');
                            $('.checkbox-2').removeAttr('checked');
                        }
                    });
                    $scope.checkClick = function (id) {
                        var idName = document.getElementById(id);
                        if ($(idName).prop('checked')) {
                            $(idName).next('label').removeClass('chbx-init').addClass('chbx-focus');
                            $scope.selectedCheckbox = $scope.selectedCheckbox + 1;
                            if ($('#checkbox-1').prop('checked')) {
                                $scope.exclusiveArray =
                                $scope.exclusiveArray.filter(function (item) {
                                    return item !== id;
                                });

                            } else {
                                if ($scope.inclusiveArray.indexOf(id) == -1) {
                                    $scope.inclusiveArray.push(id);
                                }
                            }
                        } else {
                            $scope.selectedCheckbox = $scope.selectedCheckbox - 1;
                            $(idName).next('label').removeClass('chbx-focus').addClass('chbx-init');
                            if ($('#checkbox-1').prop('checked')) {
                                if ($scope.exclusiveArray.indexOf(id) == -1) {
                                    $scope.exclusiveArray.push(id);
                                }
                            } else {
                                $scope.inclusiveArray =
                                $scope.inclusiveArray.filter(function (item) {
                                    return item !== id;
                                });
                            }
                        }
                    }

                    $scope.welfare = function (user) {
                        Welfare.checkUserWelfare(user[0]).then(function (data) {
                            $http.get('api/partner/wallet').success(function (response) {
                                $scope.partnerWallet = response.data;
                                $scope.currentUser = user;
                                if (data.data) {
                                    $("#ffl").modal("toggle");
                                } else {
                                    $("#ffl-limit").modal("toggle");
                                }
                            });
                        });
                    };
                    $scope.$watch('hbNum', function (newVal, oldVal) {
                        if (Number(newVal)) {

                            if (newVal <= $scope.partnerWallet.availableScoreA / 100.0) {
                                $scope.hbNum = parseFloat($filter('number')(newVal, 2));
                                ;
                            } else {
                                $scope.hbNum =
                                $scope.partnerWallet.availableScoreA / 100.0.toFixed(2);
                            }
                        } else {
                            if (!newVal == null) {
                                alert("请输入有效数字");
                            }
                        }
                    });
                    $scope.$watch('jfNum', function (newVal, oldVal) {
                        if (Number(newVal)) {

                            if (newVal <= $scope.partnerWallet.availableScoreB) {
                                $scope.jfNum = newVal;
                            } else {
                                $scope.jfNum =
                                $scope.partnerWallet.availableScoreB;
                            }
                        } else {
                            if (!newVal == null) {
                                alert("请输入有效数字");
                            }
                        }
                    });
                    $scope.$watch('selectedCheckbox', function (newVal, oldVal) {
                        if (newVal > 0) {
                            $("#batchWelfare").removeClass("w-cantCheck");
                        } else {
                            $("#batchWelfare").addClass("w-cantCheck");
                        }
                    });
                    $scope.welfareOneUser = function () {
                        var map = {};
                        map.userId = $scope.currentUser[0];
                        map.scoreA = $scope.hbNum;
                        map.scoreB = $scope.jfNum;
                        map.description = $scope.description;
                        if ($scope.hbNum == null && $scope.jfNum == null) {
                            alert("至少发放红包或积分");
                        }
                        map.redirectUrl = $("input:radio[name='optionsRadios-one']:checked").val();
                        Welfare.welfareOneUser(map).then(function () {
                            $http.get('api/partner/wallet').success(function (response) {
                                $scope.partnerWallet = response.data;
                                $("#ffl").modal("hide");
                                $("#ffl-success").modal("toggle");
                            });
                        });
                    }

                    $scope.batchWelfareCheck = function () {
                        if ($('#checkbox-1').prop('checked')) {
                            var exclusiveArrayDto = {};
                            exclusiveArrayDto.ids = $scope.exclusiveArray;
                            exclusiveArrayDto.leJiaUserCriteria = criteria;
                            Welfare.exclusiveCheck(exclusiveArrayDto).then(function (response) {
                                var data = response.data;
                                $scope.conflict = data.conflict;
                                $scope.conflictList = data.conflictList;
                                if ($scope.selectedCheckbox == $scope.conflict) {
                                    $("#pffl-limit").modal("toggle");
                                } else {
                                    $http.get('api/partner/wallet').success(function (response) {
                                        $scope.partnerWallet = response.data;
                                        $("#pffl").modal("toggle");
                                    });
                                }
                            });
                        } else {
                            Welfare.inclusiveCheck($scope.inclusiveArray).then(function (response) {
                                var data = response.data;
                                $scope.conflict = data.conflict;
                                $scope.filterArray = data.filterArray;
                                if ($scope.selectedCheckbox == $scope.conflict) {
                                    $("#pffl-limit").modal("toggle");
                                } else {
                                    $http.get('api/partner/wallet').success(function (response) {
                                        $scope.partnerWallet = response.data;
                                        $("#pffl").modal("toggle");
                                    });
                                }
                            });
                        }
                    }

                    $scope.$watch('hbNumBatch', function (newVal, oldVal) {
                        if (Number(newVal)) {
                            if (newVal * ($scope.selectedCheckbox - $scope.conflict)
                                <= $scope.partnerWallet.availableScoreA / 100.0) {
                                $scope.hbNumBatch = parseFloat($filter('number')(newVal, 2));
                            } else {
                                var result = $scope.partnerWallet.availableScoreA / 100.0
                                             / ($scope.selectedCheckbox
                                                - $scope.conflict);

                                $scope.hbNumBatch =
                                Math.floor(result * 100) / 100;
                            }
                        } else {
                            if (!newVal == null) {
                                alert("请输入有效数字");
                            }
                        }
                    });
                    $scope.$watch('jfNumBatch', function (newVal, oldVal) {
                        if (Number(newVal)) {

                            if (newVal * ($scope.selectedCheckbox - $scope.conflict)
                                <= $scope.partnerWallet.availableScoreB) {
                                $scope.jfNumBatch = parseInt(new BigDecimal(newVal + ''));
                            } else {
                                var result = new BigDecimal($scope.partnerWallet.availableScoreB
                                                            + '').divide(new BigDecimal($scope.selectedCheckbox
                                                                                        - $scope.conflict
                                                                                        + ''),
                                                                         MathContext.ROUND_FLOOR);
                                $scope.jfNumBatch = Math.floor(result);
                            }
                        } else {
                            if (!newVal == null) {
                                alert("请输入有效数字");
                            }
                        }
                    });

                    $scope.batchWelfare = function () {
                        if ($scope.jfNumBatch == null && $scope.hbNumBatch == null) {
                            $(".P-writeTrueNumber").show();
                            return;
                        }
                        var partnerWelfareLog = {};
                        partnerWelfareLog.userCount = $scope.selectedCheckbox - $scope.conflict;
                        if ($scope.hbNumBatch != null) {
                            partnerWelfareLog.scoreA =
                            parseInt(new BigDecimal($scope.hbNumBatch
                                                    + '').multiply(new BigDecimal('100')));
                        }
                        partnerWelfareLog.scoreB = $scope.jfNumBatch;
                        partnerWelfareLog.description = $scope.liuyan;
                        partnerWelfareLog.redirectUrl =
                        $("input:radio[name='optionsRadios']:checked").val();
                        var exclusiveArrayDto = {};
                        exclusiveArrayDto.partnerWelfareLog = partnerWelfareLog;
                        if ($('#checkbox-1').prop('checked')) {
                            angular.forEach($scope.conflictList, function (obj) {//全选去除
                                $scope.exclusiveArray.push(obj[0]);
                            });
                            exclusiveArrayDto.ids = $scope.exclusiveArray;
                            exclusiveArrayDto.leJiaUserCriteria = criteria;
                            Welfare.batchWelfareExclusive(exclusiveArrayDto).then(function (response) {
                                $http.get('api/partner/wallet').success(function (response) {
                                    $("#pffl").modal("hide");
                                    $("#pffl-success").modal("toggle");
                                    $scope.partnerWallet = response.data;
                                });
                            })
                        } else {
                            if ($scope.filterArray != null) {
                                exclusiveArrayDto.ids = $scope.filterArray;
                            } else {
                                exclusiveArrayDto.ids = $scope.inclusiveArray;
                            }
                            Welfare.batchWelfareInclusive(exclusiveArrayDto).then(function (response) {
                                $http.get('api/partner/wallet').success(function (response) {
                                    $("#pffl").modal("hide");
                                    $("#pffl-success").modal("toggle");
                                    $scope.partnerWallet = response.data;
                                });
                            })
                        }

                    }

                }
)
;
angular.module('lepayglobleApp')
    .directive('myRepeatDirective', function () {
                   return function (scope, element, attrs) {
                       if (scope.$parent.selected) {//代表全选
                           var map = scope.$parent.exclusiveArray;
                           if (map.length > 0) {
                               var currentId = scope.x[0];
                               try {
                                   angular.forEach(map, function (id) {//全选去除
                                       if (id == currentId) {
                                           throw Error();
                                       }
                                   });
                                   angular.element(element).prop('checked',
                                                                 'true');
                                   angular.element(element).next('label').removeClass('chbx-init').addClass('chbx-focus');
                               } catch (e) {
                                   angular.element(element).prop('checked',
                                                                 'false');
                               }
                           } else {
                               angular.element(element).prop('checked', 'true');
                               angular.element(element).next('label').removeClass('chbx-init').addClass('chbx-focus');
                           }
                       } else {
                           var map = scope.$parent.inclusiveArray;
                           if (map.length > 0) {
                               var currentId = scope.x[0];
                               angular.forEach(map, function (id) {//全部选择
                                   if (id == currentId) {
                                       angular.element(element).prop('checked',
                                                                     'true');
                                       angular.element(element).next('label').removeClass('chbx-init').addClass('chbx-focus');
                                   }
                               });
                           }
                       }
                   };
               })

