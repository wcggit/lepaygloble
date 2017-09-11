/**
 * Created by recoluan on 2016/11/16.
 */
'use strict';


angular.module('lepayglobleApp')
    .controller('homePageController', function ($scope, $state, $rootScope, $location, Principal, Auth, $http, Commission, HomePage, Tracker) {
        $("[data-toggle='tooltip']").tooltip();
        $scope.tsContentShow = false;
        $scope.yuyinState = true;
        $scope.toggleYuYin=function () {
            $scope.yuyinState = !$scope.yuyinState;
            var player = document.getElementById("voicePlayer")
            if($scope.yuyinState==true){
                player.muted=false;         // 开启静音
                $("#voideText").text("已开启");
            }else {
                player.muted=true;          // 关闭静音
                $("#voideText").text("已关闭");
            }
        }
        //强制保留两位小数
        $scope.toDecimal = function (x) {
            var f = parseFloat(x);
            if (isNaN(f)) {
                return false;
            }
            var f = Math.round(x * 100) / 100;
            var s = f.toString();
            var rs = s.indexOf('.');
            if (rs < 0) {
                rs = s.length;
                s += '.';
            }
            while (s.length <= rs + 2) {
                s += '0';
            }
            return s;
        }
        //  商户信息
        Principal.identity().then(function (account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });
        $http.get('api/merchantUser').success(function (response) {
            $scope.shopName = response.data.merchantName;
            $scope.loginName = response.data.name;
        });

        HomePage.getMerchantsInfo().then(function (response) {
            var data = response.data;
            $scope.merchants = data;
            // 默认门店
            var firstId = data[0].id;
            $scope.firstSid = firstId;
            // 查询
            $scope.findAll(0);
        });

        //  基本信息
        HomePage.getMerchantCommissionDetail().then(function (response) {
            var data = response.data;
            $scope.available = $scope.toDecimal(data.available * 0.01);
            $scope.totalCommission = $scope.toDecimal(data.totalCommission * 0.01);
            if (data.currentBind == 0 && data.totalCommission == 0) {
                $scope.per = 0;
            } else {
                $scope.per = $scope.toDecimal(data.totalCommission / data.currentBind);
            }
        });


        //  首页 - 交易看板 (默认门店)
        $scope.findAll = function (p) {
            // 交易看板
            $scope.changeStore($scope.firstSid);
        }

        // 按钮绑定事件 - 查看更多
        $scope.findMore = function () {
            $scope.offset = $scope.offset + 1;
            var p = $scope.offset;
            var selMerchant = $("#selMerchant").val();
            if (selMerchant == '' || selMerchant == null) {
                $("#selMerchant").val($scope.firstSid);
                return;
            } else {
                HomePage.siglOpraBoardList(selMerchant, p).then(function (data) {  // 指定门店
                    var data = data.data;
                    $scope.map = data;
                    if (data == null) {
                        $scope.tsContentShow = true;
                    }
                });
            }
        }


        // 首页 - 切换门店时进行查询
        $scope.changeStore = function (id) {
            $scope.offset = 0;
            // 查询所有
            if (id == "" || id == null) {
                return;
            }
            // 门店交易信息
            HomePage.siglOpraBoardInfo(id).then(function (data) {
                var data = data.data;
                $scope.storeTotalCount = data.totalCount;
                $scope.storeTotalPrice = data.totalPrice / 100.0;
            });
            // 门店订单列表
            HomePage.siglOpraBoardList(id, $scope.offset).then(function (data) {
                var data = data.data;
                $scope.map = data;
            });
        }

        //  获取当前账户信息
        $scope.getCurrentLogin = function () {
            $http.get("/api/merchantUser/merchantInfo").success(function (response) {
                var data = response.data;
                $scope.loginInfo = data;
            });
        }
        $scope.getCurrentLogin();

        // 页面跳转
        $scope.toOrderList = function () {
            $state.go("codeTrade");
        }
        $scope.toTradeList = function () {
            $state.go("lePlusCodeTrade");
        }
        $scope.refresh = function () {
            window.location.reload();
        }

        /**********上拉加载**********/

        var list = document.querySelector('.list-out-div');
        var pageNum = 1, pageAllNum = 3;

        list.addEventListener('scroll', function (e) {
            var outHeight = list.clientHeight;
            var inHeight = document.querySelector('.list-in-div').clientHeight;
            var scrollHeight = e.path[0].scrollTop;


            if (inHeight - outHeight - scrollHeight < 2) {
                pageNum++
                /*
                 console.log(pageNum > pageAllNum)
                 if (pageNum > pageAllNum) {
                 pageNum = pageAllNum;
                 $scope.tsContentShow = true;
                 return;
                 }
                 */

                /********根据pageNum加载相应的数据添加到list-in-div的底部*********/
            }
        }, false)

        /**********上拉加载**********/


        /***********************************************local Variables**********************************************************/
            // 语音播放

        var HINT_SPEAK = "乐加支付成功,欢迎下次光临!";
        var audioPalyUrl = "http://h5.xf-yun.com/audioStream/";

        /**
         * 初始化Session会话
         * url                 连接的服务器地址（可选）
         * reconnection        客户端是否支持断开重连
         * reconnectionDelay   重连支持的延迟时间
         */
        var session = new IFlyTtsSession({
            'url': 'ws://h5.xf-yun.com/tts.do',
            'reconnection': true,
            'reconnectionDelay': 30000
        });


        function play(content, vcn) {
            var player = document.getElementById("voicePlayer");
            var ssb_param = {
                "appid": '585b74af',
                "appkey": "ea5b9c830cea7e90",
                "synid": "12345",
                "params": "ent=aisound,aue=lame,vcn=" + vcn
            };
            session.start(ssb_param, content, function (err, obj) {
                var audio_url = audioPalyUrl + obj.audio_url;
                if (audio_url != null && audio_url != undefined) {
                    player.src = audio_url;
                    player.play();
                }
            });
        };

        function play_xiaoqi() {
            play(HINT_SPEAK, 'xiaoqi')
        };

        Tracker.receiveMerchantVoice().then(null, null, function (data) {//等待websocket发送消息
            HINT_SPEAK = data.body.split(",")[3].split(":")[1];
            play_xiaoqi();
        });

    });
