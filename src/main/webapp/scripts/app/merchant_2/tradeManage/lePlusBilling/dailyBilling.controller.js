/**
 * Created by recoluan on 2016/11/25.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('dailyBillingController', function ($scope, $state, $rootScope, $location,Principal,Auth,$http,HomePage,LejiaBilling) {
        // 门店列表
        HomePage.getMerchantsInfo().then(function(response) {
            var data = response.data;
            $scope.merchants = data;
        });
        // 根据日期进行查询
        $scope.findByDate = function() {
           var id = $("#selMerchant").val();
           var date =  $("#timePicker1").val();
           if(id==null||id=='') {
                alert("请选择门店 ^_^");
                return;
           }
           if(date==null||date=='') {
                alert("请选定日期");
                return;
           }
           var startDate = date.split('-')[0];
           var dailyOrderCriteria = {};
           dailyOrderCriteria.id = id;
           dailyOrderCriteria.startDate = startDate;
           //  TO - DO
        };

        $('#timePicker1')
            .val(moment().subtract('day', 4).format('YYYY/MM/DD') + ' - ' + moment().format('YYYY/MM/DD'))
            .daterangepicker({
                opens : 'left', //日期选择框的弹出位置
                format : 'YYYY/MM/DD', //控件中from和to 显示的日期格式
                ranges : {
                    '最近7日': [moment().subtract('days', 6), moment()]
                }
            },function(start, end, label) {
                var data = "startDate="
                    + start.format('YYYY/MM/DD')
                    + "&endDate="
                    + end.format('YYYY/MM/DD');

            });
    });

Date.prototype.format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours() % 12 == 0 ? 12 : this.getHours() % 12, //小时
        "H+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    var week = {
        "0": "\u65e5",
        "1": "\u4e00",
        "2": "\u4e8c",
        "3": "\u4e09",
        "4": "\u56db",
        "5": "\u4e94",
        "6": "\u516d"
    };
    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    if (/(E+)/.test(fmt)) {
        fmt =
            fmt.replace(RegExp.$1,
                ((RegExp.$1.length > 1) ? (RegExp.$1.length > 2 ? "\u661f\u671f" : "\u5468")
                    : "") + week[this.getDay() + ""]);
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(fmt)) {
            fmt =
                fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr((""
                + o[k]).length)));
        }
    }
    return fmt;
}

