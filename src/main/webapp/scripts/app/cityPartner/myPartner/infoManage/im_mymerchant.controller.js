'use strict';

angular.module('lepayglobleApp')
    .controller('imMyMerchantController', function ($scope, Partner, $state,$http,$stateParams) {
        //商户列表
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

        $('#timePicker')
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

        $("#timePicker").val("");

        var partnerSid = $stateParams.partnerSid;   //获取天使合伙人的Sid
        var currentPage = 1;
        var criteria = {};
        criteria.offset = 1;
        criteria.partnerSid = partnerSid;

        loadContent();
        function loadContent() {
            $http.post("/api/merchantUser/findByCriteria",criteria).success(function (response) {
                console.log(response);
                if (response.status == 200) {
                    var data = response.data;
                    $scope.page = currentPage;
                    $scope.pulls = data.data;
                    $scope.totalPages = data.totalPages;
                } else {
                    alert("加载商户错误...");
                }

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
            criteria.offset = page;
            loadContent();
        };

        $scope.searchByCriteria = function () {
            //获取商户名称
            var storeName = $("#merchantsNameName").val();
            //获取负责人名称
            var linkMan = $("#linkMan").val();
            //过去交易时间
            var dateStr = $("#timePicker").val();
            //获取负责人手机号
            var merchantPhone = $("#merchantPhone").val();

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
                criteria.merchantName = storeName;
            } else {
                criteria.merchantName = null;
            }
            if (linkMan != -1) {
                criteria.linkMan = linkMan;
            } else {
                criteria.linkMan = null;
            }
            if (merchantPhone != -1) {
                criteria.phoneNum = merchantPhone;
            } else {
                criteria.phoneNum = null;
            }
            // console.log(criteria);
            criteria.offset = 1;
            currentPage = 1;
            // getTotalPage();
            loadContent();
        };

        // 导出表格
        $scope.exportExcel = function () {
            var data = "?";
            data += "partnerSid=" + criteria.partnerSid;
            if(criteria.merchantName != '' && criteria.merchantName != undefined && criteria.merchantName != null){
                data += "&merchantName=" + criteria.merchantName;
            }
            if(criteria.phoneNum != '' && criteria.phoneNum != undefined && criteria.phoneNum != null){
                data += "&phoneNum=" + criteria.phoneNum;
            }
            if(criteria.startDate != '' && criteria.startDate != undefined && criteria.startDate != null){
                data += "&startDate=" + criteria.startDate;
            }
            if(criteria.endDate != '' && criteria.endDate != undefined && criteria.endDate != null){
                data += "&endDate=" + criteria.endDate;
            }
            if(criteria.linkMan != '' && criteria.linkMan != undefined && criteria.linkMan != null){
                data += "&linkMan=" + criteria.linkMan;
            }
            location.href = "/api/merchantUser/findByCriteria/export" + data;
        };



        //门店数据
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
        var currentPage_m = 1;
        var criteria_m = {};
        criteria_m.offset = 1;
        criteria_m.partnerSid = partnerSid;

        loadContent_m();
        function loadContent_m() {
            $http.post("/api/merchantUser/merchantList",criteria_m).success(function (response) {
                console.log(response);
                if (response.status == 200) {
                    var data = response.data;
                    $scope.page_m = currentPage_m;
                    $scope.pulls_m = data.data;
                    $scope.totalPages_m = response.data.totalPages;
                } else {
                    alert("加载门店错误...");
                }
            });
        }

        $scope.loadPage_m = function (page) {
            if (page == 0) {
                return;
            }
            if (page > $scope.totalPages_m) {
                return;
            }
            if (currentPage_m == $scope.totalPages_m && page == $scope.totalPages_m) {
                return;
            }
            if (currentPage_m == 1 && page == 1) {
                return;
            }
            currentPage_m = page;
            criteria_m.offset = page;
            loadContent_m();
        };


        $scope.searchByCriteria_m = function () {
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
                criteria_m.startDate = startDate;
                criteria_m.endDate = endDate;
            } else {
                criteria_m.startDate = null;
                criteria_m.endDate = null;
            }
            if (storeName != -1) {
                criteria_m.merchant = storeName;
            } else {
                criteria_m.merchant = null;
            }
            if (merchantName != -1) {
                criteria_m.merchantUserName = merchantName;
            } else {
                criteria_m.merchantUserName = null;
            }
            if (partnerShip != -1) {
                criteria_m.partnership = parseInt(partnerShip);
            } else {
                criteria_m.partnership = null;
            }
            criteria_m.offset = 1;
            currentPage_m = 1;
            loadContent_m();
        };


        // $scope.goLePayCode = function (id) {
        //     $state.go("lefuma", {id: id})
        // };
        // $scope.goMerchantUser = function (id) {
        //     $state.go("accountmanager", {id: id})
        // };


        // 导出表格
        $scope.exportExcel_m = function () {
            var data = "?";
            data += "partnerSid=" + criteria_m.partnerSid;
            if(criteria_m.merchant != '' && criteria_m.merchant != undefined && criteria_m.merchant != null){
                data += "&merchant=" + criteria_m.merchant;
            }
            if(criteria_m.merchantUserName != '' && criteria_m.merchantUserName != undefined && criteria_m.merchantUserName != null){
                data += "&merchantUserName=" + criteria_m.merchantUserName;
            }
            if(criteria_m.partnership != '' && criteria_m.partnership != undefined && criteria_m.partnership != null){
                data += "&partnership=" + criteria_m.partnership;
            }
            if(criteria_m.startDate != '' && criteria_m.startDate != undefined && criteria_m.startDate != null){
                data += "&startDate=" + criteria_m.startDate;
            }
            if(criteria_m.endDate != '' && criteria_m.endDate != undefined && criteria_m.endDate != null){
                data += "&endDate=" + criteria_m.endDate;
            }

            location.href = "/api/merchantUser/merchantList/export" + data;
        }
    });

