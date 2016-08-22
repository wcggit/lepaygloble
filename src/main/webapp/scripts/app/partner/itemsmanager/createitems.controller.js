'use strict';

angular.module('lepayglobleApp')
    .controller('createItemsController', function ($scope, Partner, $http, Upload) {
                    $('body').css({background: '#f3f3f3'});
                    $('.main-content').css({height: 'auto'});
                    var cities = [];
                    var merchant = {};
                    $scope.merchant = null;

                    //$scope.currentCity = {id: 1};
                    Partner.getAllCities().then(function (data) {
                        cities = data;

                        $scope.cities = data;
                    });
                    Partner.getAllMerchantType().then(function (data) {
                        $scope.merchantTypes = data;
                    });
                    $scope.$watch('currentCity', function () {
                        var id = $("#city").val();
                        //$("#city").val("-1");
                        if (id == "") {
                            $scope.areas = null;
                            $("#city").val("")
                        } else {
                            angular.forEach(cities, function (data, index, array) {
                                if (data.id == id) {
                                    $scope.areas = data.areas;
                                }
                            });
                        }
                    })

                    //$scope.cityChange = function () {
                    //    var id = $("#city").val();
                    //    alert(id)
                    //}

                    // map
                    var map = new AMap.Map("container", {
                        resizeEnable: true
                    });
                    //为地图注册click事件获取鼠标点击出的经纬度坐标
                    var clickEventListener = map.on('click', function (e) {
                        document.getElementById("lnglat").value =
                        e.lnglat.getLng() + ',' + e.lnglat.getLat()
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

                    // 判断是都是联盟商户
                    $scope.itemsState = true;
                    $scope.itemsStateFun = function () {
                        if ($('#optionsRadios1').prop('checked')) {
                            $scope.itemsState = true;
                        } else {
                            $scope.itemsState = false;
                        }
                    };

                    $scope.sumMerchant = function () {
                        if ($("#merchantBankCard").val().trim() == "") {
                            alert("请输入银行卡号");
                            return;
                        }
                        if ($("#merchantBank").val().trim() == "") {
                            alert("请输入支行地址");
                            return;
                        }
                        if ($("#payee").val().trim() == "") {
                            alert("请输入收款人");
                            return;
                        }
                        var merchant = {};
                        merchant.id = $scope.merchant.id;
                        var merchantType = {};
                        merchantType.id = $("#merchantType").val();
                        merchant.merchantType = merchantType;
                        var city = {};
                        city.id = $("#city").val();
                        merchant.city = city;
                        var area = {};
                        area.id = $("#area").val();
                        merchant.area = area;
                        merchant.location = $("#location").val()
                        merchant.name = $("#merchantName").val();
                        merchant.contact = $("#contact").val();
                        merchant.merchantPhone = $("#phone").val();
                        $http({
                                  method: 'POST',
                                  url: "/api/merchant/createMerchant",
                                  data: fd,
                                  headers: {'Content-Type': undefined},
                                  transformRequest: angular.identity
                              })
                            .success(function (response) {
                                         //上传成功的操作
                                         alert("uplaod success");
                                     });

                    }

                    //判断当前是那一页
                    $scope.currentState = 1;
                    $scope.currentStateFun1 = function () {
                        $scope.currentState = 1;
                    };
                    $scope.currentStateFun2 = function () {
                        if ($("#merchantName").val().trim() == "") {
                            alert("请输入商户名");
                            return;
                        }
                        if ($("#merchantType").val() == "") {
                            alert("请选择商户分类");
                            return;
                        }

                        if ($("#city").val() == "") {
                            alert("请选择商户所在城市");
                            return;
                        }
                        if ($("#area").val() == "") {
                            alert("请选择商户所在地区");
                            return;
                        }
                        if ($("#location").val().trim() == "") {
                            alert("请输入商户详细地址");
                            return;
                        }
                        if ($("#lnglat").val() == "") {
                            alert("请选择经纬度");
                            return;
                        }
                        if ($(".merchant-picture").length == 0) {
                            alert("请选择图片");
                            return;
                        }

                        $scope.currentState = 2;
                    };
                    $scope.currentStateFun3 = function () {
                        if ($("#merchantContact").val().trim() == "") {
                            alert("请输入签约人");
                            return;
                        }
                        if ($("#merchantPhone").val().trim() == "") {
                            alert("请输入绑定手机号");
                            return;
                        }
                        if ($('#optionsRadios1').prop('checked')) {
                            if ($("#member-commission").val().trim() == "") {
                                alert("请输入会员订单手续费");
                                return;
                            }
                            if ($("#import-commission").val().trim() == "") {
                                alert("请输入导流订单手续费");
                                return;
                            }
                        }
                        $scope.currentState = 3;
                    };
                    var arr = [];
                    $scope.imgBtnChange = function (x, addShopPic) {
                        if (addShopPic.attr('id') == "addShopPic1" && $(".merchant-picture").length
                                                                      == 1) {
                            return;
                        }
                        addShopPic.attr('id')
                        var docObj = x;
                        if (docObj.files && docObj.files[0]) {
                            //火狐7以上版本不能用上面的getAsDataURL()方式获取，需要一下方式
                            // imgObjPreview.src = window.URL.createObjectURL(docObj.files[0]);
                            arr.push(docObj.files[0]);
                            var fd = new FormData();
                            fd.append('file', docObj.files[0]);
                            $http({
                                      method: 'POST',
                                      url: "/api/file/saveImage",
                                      data: fd,
                                      headers: {'Content-Type': undefined},
                                      transformRequest: angular.identity
                                  })
                                .success(function (response) {
                                             var beforeStr = '<div class="thumbnail shopPic pull-left">'
                                             if (addShopPic.attr('id') == "addShopPic1") {
                                                 beforeStr +=
                                                 '<img class="images merchant-picture" src="http://lepluslive-image.oss-cn-beijing.aliyuncs.com/'
                                             } else {
                                                 beforeStr +=
                                                 '<img class="images protocol" src="http://lepluslive-image.oss-cn-beijing.aliyuncs.com/'
                                             }

                                             beforeStr += response.data
                                                          + '" alt="...">' +
                                                          '<div><span class="enlarge"></span><span class="delete"data-target="#enlargePic"></span></div></div>';
                                             addShopPic.before(beforeStr);
                                             $('.shopPic').each(function (i) {
                                                 $('.shopPic').eq(i).find('.delete').unbind().bind('click',
                                                                                                   function () {
                                                                                                       $(this).parent('div').parent('.shopPic').remove();
                                                                                                   });
                                                 $('.shopPic').eq(i).find('.enlarge').unbind().bind('click',
                                                                                                    function () {
                                                                                                        var imgSrc = $(this).parent().siblings('img').attr('src');
                                                                                                        console.log(imgSrc);
                                                                                                        $("#enlargePic").find('img').attr('src',
                                                                                                                                          imgSrc);
                                                                                                        $("#enlargePic").modal("toggle");
                                                                                                    });
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
                            try {
                                localImagId.style.filter =
                                "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)";
                                localImagId.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src =
                                imgSrc;
                            }
                            catch (e) {
                                alert("您上传的图片格式不正确，请重新选择!");
                                return false;
                            }
                            imgObjPreview.style.display = 'none';
                            document.selection.empty();
                        }
                        return true;
                    };
                });
/**
 * Created by recoluan on 2016/8/2 0002.
 */
