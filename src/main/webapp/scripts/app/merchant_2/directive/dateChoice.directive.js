/**
 * Created by recoluan on 2016/11/21.
 */

angular.module('lepayglobleApp')
    .directive('dateChoice', function(){
        return {
            restrict: 'EA', //E表示element, A表示attribute,C表示class，M表示commnent,即注释
            template: '<input type="text" name="reservation" class="form-control timePicker"/><i class="glyphicon glyphicon-calendar fa fa-calendar"></i>',
            link:function($scope, element, attrs){
                var dataChoice=element.find('input');
                dataChoice
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
                dataChoice.val("");
            }
        };
    })
