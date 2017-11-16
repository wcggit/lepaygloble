'use strict';

angular.module('lepayglobleApp')
    .controller('myItemsController', function ($scope, Partner, $state,$http) {
        // $('body').css({background: '#f3f3f3'});
        $('.main-content').css({height: 'auto'});
        // select标签右侧小三角
        $.fn.openSelect = function() {
            return this.each(function(idx,domEl) {
                if (document.createEvent) {
                    var event = document.createEvent("MouseEvents");
                    event.initMouseEvent("mousedown", true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
                    domEl.dispatchEvent(event);
                } else if (element.fireEvent) {
                    domEl.fireEvent("onmousedown");
                }
            });
        };
        $('.select-jiao').on('click', function() {
            $(this).siblings('select').openSelect();
        });

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
        // Partner.countFullMerchant().then(function (response) {
        //     var data = response.data;
        //     $scope.fullMerchant = data;
        // });
        loadContent();
        // getTotalCount();
        function loadContent() {
            $http.post("/api/merchantUser/merchantList",criteria).success(function (response) {
                console.log(response);
                if (response.status == 200) {
                    var data = response.data;
                    $scope.page = currentPage;
                    $scope.pulls = data.data;
                    $scope.totalPages = response.data.totalPages;
                } else {
                    alert("加载门店错误...");
                }
            });

            // Partner.getPartnerBindMerchantList(criteria).then(function (response) {
            //     var data = response.data;
            //     $scope.page = currentPage;
            //     $scope.pulls = data;
            // });
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

        // function getTotalPage() {
        //     $http.post("/api/merchantUser/merchantList",criteria).success(function (response) {
        //         console.log(response);
        //         if (response.status == 200) {
        //             $scope.totalPages = response.data.totalPages;
        //             loadContent();
        //         } else {
        //
        //         }
        //     });
        //     // Partner.getPartnerBindMerchantListPage(criteria).then(function (response) {
        //     //     $scope.totalPages = response.data;
        //     //     loadContent();
        //     // });
        // }

        // function getTotalCount() {
        //     Partner.getPartnerBindMerchantListCount(criteria).then(function (response) {
        //         $scope.totalCount = response.data;
        //         loadContent();
        //     });
        // }

        $scope.searchByCriteria = function () {
            //获取门店名称
            var storeName = $("#storeName").val();
            //获取所述商户
            var merchantName = $("#merchantsName").val();
            //获取时间
            var dateStr = $("#timePicker1").val();
            //获取协议类型
            var partnerShip = $("#partnerShip").val();
            // var userBindState = $("#userBindState").val();
            if (dateStr != null && dateStr != "") {
                var startDate = dateStr.split("-")[0].trim();
                var endDate = dateStr.split("-")[1].trim();
                criteria.startDate = startDate;
                criteria.endDate = endDate;
            } else {
                criteria.startDate = null;
                criteria.endDate = null;
            }
            if (storeName != -1) {
                criteria.merchant = storeName;
            } else {
                criteria.merchant = null;
            }
            if (merchantName != -1) {
                criteria.merchantUserName = merchantName;
            } else {
                criteria.merchantUserName = null;
            }
            if (partnerShip != -1) {
                criteria.partnership = parseInt(partnerShip);
            } else {
                criteria.partnership = null;
            }
            // if (userBindState != -1) {
            //     criteria.userBindState = userBindState;
            // } else {
            //     criteria.userBindState = null;
            // }
            criteria.offset = 1;
            // criteria.merchantName = merchantName;
            currentPage = 1;
            getTotalPage();
            // getTotalCount()
        };

        // $scope.showFullMerchant = function (page) {
        //     $("#timePicker1").val("");
        //     $("#merchantName").val("");
        //     $("#userBindState").val("2");
        //     $("#partnerShip").val("-1");
        //     criteria.offset = 1;
        //     criteria.merchantName = null;
        //     criteria.userBindState = 2;
        //     criteria.partnerShip = null;
        //     criteria.startDate = null;
        //     criteria.endDate = null;
        //     getTotalPage()
        //     getTotalCount()
        // };
        $scope.goLePayCode = function (id) {
            $state.go("lefuma", {id: id})
        };
        // $scope.goEdit = function (id) {
        //     $state.go("createitems", {id: id})
        // };
        $scope.goMerchantUser = function (id) {
            $state.go("accountmanager", {id: id})
        };

    });

