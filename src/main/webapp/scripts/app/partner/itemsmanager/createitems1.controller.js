'use strict';

angular.module('lepayglobleApp')
    .controller('createItems1Controller', function ($scope, $state, $location, $http) {
        $('body').css({background: '#f3f3f3'});
        $('.main-content').css({height: 'auto'});
        _init_area();

        $scope.imgBtnChange=function(x){
               var docObj=x;
               if(docObj.files&&docObj.files[0]) {
                   //火狐7以上版本不能用上面的getAsDataURL()方式获取，需要一下方式
                   // imgObjPreview.src = window.URL.createObjectURL(docObj.files[0]);
                   var beforeStr = '<div class="thumbnail shopPic pull-left">' +
                       '<img src="'+window.URL.createObjectURL(docObj.files[0])+'" alt="...">' +
                       '<div><span class="enlarge"></span><span class="delete"data-target="#enlargePic"></span></div></div>';
                   $('#addShopPic').before(beforeStr);
                   $('.shopPic').each(function (i) {
                       $('.shopPic').eq(i).find('.delete').unbind().bind('click', function () {
                           $(this).parent('div').parent('.shopPic').remove();
                       });
                       $('.shopPic').eq(i).find('.enlarge').unbind().bind('click', function () {
                           var imgSrc=$(this).parent().siblings('img').attr('src');
                           console.log(imgSrc);
                           $("#enlargePic").find('img').attr('src',imgSrc);
                           $("#enlargePic").modal("toggle");
                       });
                   });
               } else {
                   //IE下，使用滤镜
                   docObj.select();
                   var imgSrc = document.selection.createRange().text;
                   var localImagId = document.getElementById("localImag");
                   //必须设置初始大小
                   localImagId.style.width = "150px";
                   localImagId.style.height = "180px";
                   //图片异常的捕捉，防止用户修改后缀来伪造图片
                   try{
                       localImagId.style.filter="progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)";
                       localImagId.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = imgSrc;
                   }
                   catch(e) {
                       alert("您上传的图片格式不正确，请重新选择!");
                       return false;
                   }
                   imgObjPreview.style.display = 'none';
                   document.selection.empty();
               }
               return true;
        };

        // map
        var map = new AMap.Map("container", {
            resizeEnable: true
        });
        //为地图注册click事件获取鼠标点击出的经纬度坐标
        var clickEventListener = map.on('click', function(e) {
            document.getElementById("lnglat").value = e.lnglat.getLng() + ',' + e.lnglat.getLat()
        });
        var auto = new AMap.Autocomplete({
            input: "tipinput"
        });
        AMap.event.addListener(auto, "select", select);//注册监听，当选中某条记录时会触发
        function select(e) {
            if (e.poi && e.poi.location) {
                map.setZoom(15);
                map.setCenter(e.poi.location);
            }
        }
    });
/**
 * Created by recoluan on 2016/8/2 0002.
 */
