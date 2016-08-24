'use strict';

angular.module('lepayglobleApp')
    .controller('createItemsController',
                function ($scope, Partner, $http, $stateParams, $q, $state) {
                    $('body').css({background: '#f3f3f3'});
                    $('.main-content').css({height: 'auto'});
                    var cities = [];
                    var currentMerchant = null;
                    $scope.merchant = null;
                    if ($stateParams.id != null && $stateParams.id != "") {
                        $q.all([Partner.getMerchantById($stateParams.id), Partner.getAllCities(),
                                Partner.getAllMerchantType()]).then(function (results) {
                            currentMerchant = results[0].data;
                            $scope.merchant = currentMerchant;
                            cities = results[1];
                            $scope.cities = cities;
                            $scope.merchantTypes = results[2];
                            if (currentMerchant.picture != null) {
                                var beforeStr = '<div class="thumbnail shopPic pull-left">';
                                beforeStr +=
                                '<img class="images merchant-picture" src="'
                                beforeStr += currentMerchant.picture + '" alt="...">' +
                                             '<div><span class="enlarge"></span><span class="delete"data-target="#enlargePic"></span></div></div>';
                                $("#addShopPic1").before(beforeStr);
                            }
                            if (currentMerchant.merchantProtocols != null) {
                                angular.forEach(currentMerchant.merchantProtocols,
                                                function (protocol, index, array) {
                                                    var beforeStr = '<div class="thumbnail shopPic pull-left">';
                                                    beforeStr +=
                                                    '<img class="images protocol" src="';
                                                    beforeStr += protocol.picture + '" alt="...">' +
                                                                 '<div><span class="enlarge"></span><span class="delete"data-target="#enlargePic"></span></div></div>';
                                                    $("#addShopPic2").before(beforeStr);
                                                });
                            }
                            if (currentMerchant.partnership == 1) {
                                $scope.itemsState = true;
                            } else {
                                $scope.itemsState = false;
                            }
                            initShowPicAndDelete();

                        });
                    } else {
                        Partner.getAllCities().then(function (data) {
                            cities = data;

                            $scope.cities = data;
                        });
                        Partner.getAllMerchantType().then(function (data) {
                            $scope.merchantTypes = data;
                        });
                    }
                    // select标签右侧小三角
                    $.fn.openSelect = function () {
                        return this.each(function (idx, domEl) {
                            if (document.createEvent) {
                                var event = document.createEvent("MouseEvents");
                                event.initMouseEvent("mousedown", true, true, window, 0, 0, 0, 0, 0,
                                                     false, false, false, false, 0, null);
                                domEl.dispatchEvent(event);
                            } else if (element.fireEvent) {
                                domEl.fireEvent("onmousedown");
                            }
                        });
                    };
                    $('.select-jiao').on('click', function () {
                        $(this).siblings('select').openSelect();
                    });

                    $scope.$watch('merchant.city.id', function (after, before) {
                        var id = after;
                        if (id != "" && id != null) {
                            angular.forEach(cities, function (data, index, array) {
                                if (data.id == id) {
                                    $scope.areas = data.areas;
                                }
                            });
                        } else {
                            $scope.areas = null;
                        }
                    })

                    // map
                    var map = new AMap.Map("container", {
                        resizeEnable: true
                    });
                    //为地图注册click事件获取鼠标点击出的经纬度坐标
                    var clickEventListener = map.on('click', function (e) {
                        document.getElementById("lnglat").value = e.lnglat.getLng() + ',' + e.lnglat.getLat();
                        $scope.lnglatState = false;
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

                    $scope.subMerchant = function () {
                        if ($("#merchantBankCard").val().trim() == "") {
                            $scope.merchantBankCdState = true;
                        } else {
                            $scope.merchantBankCdState = false;
                        }
                        if ($("#merchantBank").val().trim() == "") {
                            $scope.merchantBankState = true;
                        } else {
                            $scope.merchantBankState = false;
                        }
                        if ($("#payee").val().trim() == "") {
                            $scope.payeeState = true;
                        } else {
                            $scope.payeeState = false;
                        }
                        var merchant = {};
                        var merchantType = {};
                        merchantType.id = $("#merchantType").val().trim().split("number:")[1];
                        merchant.merchantType = merchantType;
                        var city = {};
                        city.id = $("#city").val().trim().split("number:")[1];
                        merchant.city = city;
                        var area = {};
                        area.id = $("#area").val().trim().split("number:")[1];
                        merchant.area = area;
                        merchant.location = $("#location").val().trim()
                        merchant.name = $("#merchantName").val().trim();
                        merchant.contact = $("#merchantContact").val().trim();
                        merchant.merchantPhone = $("#merchantPhone").val().trim();
                        merchant.payee = $("#payee").val().trim();
                        merchant.picture = $(".merchant-picture")[0].src;
                        merchant.cycle = 1;
                        merchant.lat = $("#lnglat").val().split(",")[0]
                        merchant.lng = $("#lnglat").val().split(",")[1]
                        if ($('#optionsRadios1').prop('checked')) {
                            merchant.partnership = 1;
                            merchant.ljCommission = $("#import-commission").val().trim();
                            merchant.memberCommission = $("#member-commission").val().trim();
                            merchant.ljBrokerage = 0.6;
                            merchant.receiptAuth = 1;
                            merchant.userLimit = 100;
                        } else {
                            merchant.partnership = 0;
                            merchant.ljCommission = 0.6;
                            merchant.memberCommission = 0;
                            merchant.ljBrokerage = 0;
                            merchant.receiptAuth = 0;
                            merchant.userLimit = 0;
                        }
                        merchant.scoreARebate = 50;
                        merchant.scoreBRebate = 5;
                        var merchantProtocols = [];
                        $(".protocol").each(function () {
                            var merchantProtocol = {};
                            merchantProtocol.picture = $(this).attr("src");
                            merchantProtocols.push(merchantProtocol);
                        });
                        merchant.merchantProtocols = merchantProtocols;
                        var merchantBank = {};
                        merchantBank.bankName = $("#merchantBank").val().trim();
                        merchantBank.bankNumber = $("#merchantBankCard").val().trim();
                        if (currentMerchant != null) {
                            merchant.id = $scope.merchant.id;
                            merchantBank.id = $scope.merchant.merchantBank.id;
                            merchant.merchantBank = merchantBank;
                            $http({
                                      method: 'put',
                                      url: "/api/merchant/editMerchant",
                                      data: merchant,
                                      headers: {'Content-Type': 'application/json'}
                                  })
                                .success(function (response) {
                                             //上传成功的操作
                                             alert(response.data);
                                             $state.go("myitems");
                                         });
                        } else {
                            merchant.merchantBank = merchantBank;
                            merchant.id = null;
                            $http({
                                      method: 'POST',
                                      url: "/api/merchant/createMerchant",
                                      data: merchant,
                                      headers: {'Content-Type': 'application/json'}
                                  })
                                .success(function (response) {
                                             //上传成功的操作
                                             alert(response.data);
                                             $state.go("myitems");
                                         });
                        }

                    };
                    //判断当前是那一页
                    $scope.currentState = 1;
                    $scope.currentStateFun1 = function () {
                        $('.main-content').css({height: 'auto'});
                        $scope.currentState = 1;
                    };
                    $scope.currentStateFun2 = function () {
                        $('.main-content').css({height: 'auto'});
                        if ($("#merchantName").val().trim() == "") {
                            $scope.merchantNameState = true;
                        } else {
                            $scope.merchantNameState = false;
                        }
                        if ($("#merchantType").val() == "") {
                            $scope.merchantTypeState = true;
                        } else {
                            $scope.merchantTypeState = false;
                        }

                        if ($("#city").val() == "") {
                            $scope.cityState = true;
                        } else {
                            $scope.cityState = false;
                        }
                        if ($("#area").val() == "") {
                            $scope.areaState = true;
                        } else {
                            $scope.areaState = false;
                        }

                        if ($("#location").val().trim() == "") {
                            $scope.locationState = true;
                        } else {
                            $scope.locationState = false;
                        }

                        if ($("#lnglat").val() == ",") {
                            $scope.lnglatState = true;
                        } else {
                            $scope.lnglatState = false;
                        }

                        if ($(".merchant-picture").length == 0) {
                            $scope.merchantPicState = true;
                        } else {
                            $scope.merchantPicState = false;
                        }

                        if ($scope.merchantNameState || $scope.merchantTypeState || $scope.cityState
                            || $scope.areaState || $scope.locationState || $scope.lnglatState) {
                            $scope.currentState = 1;
                        } else {
                            $scope.currentState = 2;
                        }

                    };
                    $scope.currentStateFun3 = function () {
                        $('.main-content').css({height: '100vh'});
                        if ($("#merchantContact").val().trim() == "") {
                            $scope.merchantCtctState = true;
                        } else {
                            $scope.merchantCtctState = false;
                        }
                        if (($("#merchantPhone").val().trim() == "")) {
                            $scope.merchantTelState = true;
                        }else {
                            $scope.merchantTelState = false;
                        }
                        if ($('#optionsRadios1').prop('checked')) {
                            if ($("#member-commission").val().trim() == "") {
                                $scope.memberCmsnState = true;
                            } else {
                                $scope.memberCmsnState = false;
                            }
                            if ($("#import-commission").val().trim() == "") {
                                $scope.importCmsnState = true;
                            } else {
                                $scope.importCmsnState = false;
                            }
                        }

                        if ($scope.merchantCtctState || $scope.merchantTelState
                            || $scope.memberCmsnState || $scope.importCmsnState) {
                            $scope.currentState = 2;
                        } else {
                            $scope.currentState = 3;
                        }
                    };
                    $scope.inputState = function (x, stateName,idName) {
                        if(idName==''||idName==null){
                            if (x.value !== '') {
                                $scope[stateName] = false;
                            }else {
                                $scope[stateName] = true;
                            }
                        }else {
                            if(idName==='merchantPhone'){
                                var isTel=!/^(13[0-9]|14[0-9]|15[0-9]|18[0-9])\d{8}$/i.test($("#merchantPhone").val());
                                if (($("#merchantPhone").val().trim() == "")||isTel) {
                                    $scope[stateName] = true;
                                }else {
                                    $scope[stateName] = false;
                                }
                            }else if(idName==='merchantBankCard'){
                                var isBankCard=!/^(\d{16}|\d{19})$/.test($("#merchantBankCard").val());
                                if (($("#merchantBankCard").val().trim() == "")||isBankCard) {
                                    $scope[stateName] = true;
                                }else {
                                    $scope[stateName] = false;
                                }
                            }
                        }


                    };

                    // tel正则
                    // $scope.isTel=function () {
                    //     if(!/^(13[0-9]|14[0-9]|15[0-9]|18[0-9])\d{8}$/i.test($("#merchantPhone").val())){
                    //         $scope.merchantCtctState = true;
                    //     }
                    // };
                    // $scope.picState = function (stateName) {
                    //     if ($(".merchant-picture").length > 0) {
                    //         $scope[stateName] = false;
                    //     }
                    // };

                    var arr = [];
                    $scope.imgBtnChange = function (x, addShopPic,stateName) {
                        if (addShopPic.attr('id') == "addShopPic1" && $(".merchant-picture").length == 1) {
                            return;
                        }
                        addShopPic.attr('id');
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
                                             var beforeStr = '<div class="thumbnail shopPic pull-left">';
                                             if (addShopPic.attr('id') == "addShopPic1") {
                                                 beforeStr +=
                                                 '<img class="images merchant-picture" src="http://lepluslive-image.oss-cn-beijing.aliyuncs.com/'
                                                 if ($(".merchant-picture").length > 0) {
                                                     $scope[stateName] = false;
                                                 }else {
                                                     $scope[stateName] = true;
                                                 }
                                             } else {
                                                 beforeStr +=
                                                 '<img class="images protocol" src="http://lepluslive-image.oss-cn-beijing.aliyuncs.com/'

                                             }

                                             beforeStr += response.data + '" alt="...">' +
                                                          '<div><span class="enlarge"></span><span class="delete"data-target="#enlargePic"></span></div></div>';
                                             addShopPic.before(beforeStr);
                                             initShowPicAndDelete();

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
                    function initShowPicAndDelete() {
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
                    }
                });
/**
 * Created by recoluan on 2016/8/2 0002.
 */
