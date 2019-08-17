'use strict';

angular.module('lepayglobleApp')
    .controller('imCommissionInfoController', function ($scope, Partner, $state,$http,$stateParams) {
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
        var wshow = {}
        var offlineOrigins,onlineOrigins;
        criteria.offset = 1;
        criteria.lineType = 1;      //线上线下表示：0=线上；1=线下
        criteria.partnerSid = partnerSid;

        //上方数据展示
        $http.get("/api/partner/commission/data?partnerSid="+partnerSid).success(function (response) {
            if (response.status == 200) {
                var data = response.data;
                $scope.showData = data;
                offlineOrigins = data.offlineOrigins;
                onlineOrigins = data.onlineOrigins;
                appendSelect(offlineOrigins);
            } else {
                alert("加载错误...");
            }

        });
        function appendSelect(arry) {
            $("#changeState").empty();
            $("#changeState").append(
                $("<option></option>").attr("value","-1").html("全部")
            );
            for(var i = 0;i<arry.length;i++){
                $("#changeState").append(
                    $("<option></option>").attr("value",arry[i].id).html(arry[i].typeExplain)
                )
            }
        }

        loadContent();
        function loadContent() {
            $http.post("/api/partner/commission/findByPage",criteria).success(function (response) {
                if (response.status == 200) {
                    var data = response.data;
                    $scope.page = currentPage;
                    $scope.pulls = data.data;
                    $scope.totalPages = data.totalPages;
                } else {
                    alert("加载错误...");
                }

            });
        }
        //线上线下转换
        $scope.changeLineType = function (type) {
            $("#timePicker").val("");
            criteria.startDate = null;
            criteria.endDate = null;
            criteria.type = null;
            criteria.lineType = type;
            criteria.offset = 1;
            currentPage = 1;
            if(type == 0){
                appendSelect(onlineOrigins);
            } else {
                appendSelect(offlineOrigins);
            }
            loadContent();
        };
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
            //获取时间
            var dateStr = $("#timePicker").val();
            //获取变更来源
            var changeState = $("#changeState").val();
            if (dateStr != null && dateStr != "") {
                var startDate = dateStr.split("-")[0].trim();
                var endDate = dateStr.split("-")[1].trim();
                criteria.startDate = startDate;
                criteria.endDate = endDate;
            }else {
                criteria.startDate = null;
                criteria.endDate = null;
            }
            if (changeState != -1) {
                criteria.type = changeState;
            } else {
                criteria.type = null;
            }

            criteria.offset = 1;
            currentPage = 1;
            loadContent();
        };
        $scope.tixian = function (id) {
            $state.go("im_withdrawdetails", {id: id})
        };

        // 导出表格
        $scope.exportExcel = function () {
            var data = "?";
            if(criteria.type != '' && criteria.type != undefined && criteria.type != null){
                data += "&type=" + criteria.type;
            }
            data += "&lineType=" + criteria.lineType;
            data += "&partnerSid=" + criteria.partnerSid;
            if(criteria.startDate != '' && criteria.startDate != undefined && criteria.startDate != null){
                data += "&startDate=" + criteria.startDate;
            }
            if(criteria.endDate != '' && criteria.endDate != undefined && criteria.endDate != null){
                data += "&endDate=" + criteria.endDate;
            }
            location.href = "/api/partner/commission/findByPage/export" + data;
        }
    });

