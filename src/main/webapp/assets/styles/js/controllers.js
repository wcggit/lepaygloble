//// 头部
//angular.module("myseekApp").controller('headerCtrl', function ($scope,$state ) {
//    $scope.headers = [
//        {
//            ttl: "我的乐付",
//            state: "wdlf"
//        },
//        {
//            ttl: "交易管理",
//            state: "jygl"
//        },
//        {
//            ttl: "我的乐店",
//            state: "wdld"
//        },
//        {
//            ttl: "会员变现",
//            state: "hybx"
//        },
//        {
//            ttl: "账户设置",
//            state: "zhsz"
//        }
//    ];
//    $scope.currentTab = $scope.headers[0].state;
//    $scope.onClickTab = function (tab) {
//        $scope.currentTab = tab.state;
//        $state.go(tab.state);
//    };
//    $scope.isActiveTab = function (tabState) {
//        return tabState == $scope.currentTab;
//    };
//
//});
//
//// 内容
//
//// 账户设置
//angular.module("myApp").controller('zhszInfoCtrl', function ($scope) {
//    $scope.basicInfo={
//        name:"棉花糖",
//        tel:"11111111111",
//        nickname:"faaefaf",
//        passWord:"adadaxxxxxxadad"
//    };
//    $scope.billInfo={
//        payee:"王小二",
//        bankCardNum:"1234567890",
//        bank:"建行支行",
//        billCycle:"T+2（提现申请后两个工作日内到账）"
//    };
//})
//
//// 交易管理
//// 交易管理-左边
//angular.module("myApp").controller('jyglLeftCtrl', function ($scope,$state) {
//    $scope.lefts = [
//        {
//            pic: 'left-menu-icon iconfont icon-fcstubiao06',
//            name: "每日账单",
//            state: "mrtz"
//        },
//        {
//            pic: "left-menu-icon iconfont icon-jiaoyijilu",
//            name: "交易记录",
//            state: "jyjl"
//        },
//        {
//            pic: "left-menu-icon iconfont icon-tixianjilu",
//            name: "提现记录",
//            state: "txjl"
//        },
//        {
//            pic: "left-menu-icon iconfont icon-erweima",
//            name: "我的乐付码",
//            state: "wdlfm"
//        }
//    ];
//    $scope.currentTab = $scope.lefts[0].state;
//    $scope.onClickTab = function (tab) {
//        $scope.currentTab = tab.state;
//        $state.go(tab.state);
//    };
//    $scope.isActiveTab = function (tabState) {
//        return tabState == $scope.currentTab;
//    };
//})
//
//// 交易管理-每日提账
//angular.module("myApp").controller("mrzdPullCtrl",function ($scope) {
//    $scope.pulls=[
//        {
//            endTime:"2016-05-05",
//            state:"转账中",
//            arrivalMoney:"61.25",
//            billMethod:"支付宝",
//            bankNum:"xxxxxxxxxxxxxxxxxxx12345",
//            operate:"查看详情"
//        },
//        {
//            endTime:"2016-05-05",
//            state:"转账中",
//            arrivalMoney:"61.25",
//            billMethod:"支付宝",
//            bankNum:"xxxxxxxxxxxxxxxxxxx12345",
//            operate:"查看详情"
//        }
//    ];
//});
//
//// 交易管理-提现记录
//angular.module("myApp").controller("txjlPullCtrl",function ($scope) {
//    $scope.pulls=[
//        {
//            endTime:"2016-05-0512:00:00",
//            withdrawMoney:"61.25",
//            billMethod:"支付宝",
//            bankNum:"xxxxxxxxxxxxxxxxxxx12345",
//            operate:"查看详情"
//        },
//        {
//            endTime:"2016-05-05 12:00:00",
//            withdrawMoney:"61.25",
//            billMethod:"支付宝",
//            bankNum:"xxxxxxxxxxxxxxxxxxx12345",
//            operate:"查看详情"
//        }
//    ];
//});
//
//// 交易管理-交易记录
//angular.module("myApp").controller("jyjlPullCtrl",function ($scope) {
//    $scope.pulls=[
//        {
//            endTime:"2016-05-05 12:00:00",
//            tradingRecord:"52632548912345678",
//            consumer:"积分客会员",
//            money:"815.12",
//            paymentMethod:"微信支付",
//            fee:"8.00",
//            incomeArrival:"512.00"
//        },
//        {
//            endTime:"2016-05-05 12:00:00",
//            tradingRecord:"52632548912345678",
//            consumer:"积分客会员",
//            money:"815.12",
//            paymentMethod:"微信支付",
//            fee:"8.00",
//            incomeArrival:"512.00"
//        }
//    ];
//    $scope.options=["菜单1","菜单2","菜单3","菜单4","菜单5"];
//});
//
//// 交易管理-我的乐付码
//angular.module("myApp").controller("wdlfmCtrl",function ($scope) {
//    $scope.shopName="功夫鸡排（传媒大学店）";
//});
//
//// 会员变现
//// 会员变现-左边
//angular.module("myApp").controller('hybxLeftCtrl', function ($scope,$state) {
//    $scope.lefts = [
//        {
//            pic: 'left-menu-icon iconfont icon-fcstubiao06',
//            name: "数据概览",
//            state: "sjgk"
//        },
//        {
//            pic: "left-menu-icon iconfont icon-jiaoyijilu",
//            name: "佣金输入",
//            state: "yjsr"
//        },
//        {
//            pic: "left-menu-icon iconfont icon-tixianjilu",
//            name: "我的会员",
//            state: "wdhy"
//        }
//    ];
//    $scope.currentTab = $scope.lefts[0].state;
//    $scope.onClickTab = function (tab) {
//        $scope.currentTab = tab.state;
//        $state.go(tab.state);
//    };
//    $scope.isActiveTab = function (tabState) {
//        return tabState == $scope.currentTab;
//    };
//})
//
//// 会员变现-佣金输入
//angular.module("myApp").controller("yjsrTabCtrl",function ($scope) {
//    $scope.currentTab0 = true;
//    $scope.currentTab1 = false;
//    $scope.onClickTab = function () {
//        $scope.currentTab0=!$scope.currentTab0;
//        $scope.currentTab1=!$scope.currentTab1;
//    };
//});
//angular.module("myApp").controller("yjsrOnlineCtrl",function ($scope) {
//    $scope.pulls=[
//        {
//            consumeTime:"2016-07-09 12:33:44",
//            tradeVipPic:"images/left/icon1.png",
//            tradeVipName:"name",
//            SalesOutlets:"紫气东来大酒店",
//            salesAmount:"100",
//            myMoney:"100"
//        },
//        {
//            consumeTime:"2016-07-09 12:33:44",
//            tradeVipPic:"images/left/icon1.png",
//            tradeVipName:"name",
//            SalesOutlets:"紫气东来大酒店",
//            salesAmount:"100",
//            myMoney:"100"
//        }
//    ]
//})
//angular.module("myApp").controller("yjsrDownlineCtrl",function ($scope) {
//    $scope.pulls=[
//        {
//            consumeTime:"2016-07-09 12:33:44",
//            tradeVipPic:"images/left/icon1.png",
//            tradeVipName:"name",
//            SalesOutlets:"紫气东来大酒店",
//            salesAmount:"100",
//            myMoney:"100"
//        },
//        {
//            consumeTime:"2016-07-09 12:33:44",
//            tradeVipPic:"images/left/icon1.png",
//            tradeVipName:"name",
//            SalesOutlets:"紫气东来大酒店",
//            salesAmount:"100",
//            myMoney:"100"
//        }
//    ]
//})
//
//// 会员变现-我的会员
//angular.module("myApp").controller("wdhyPullCtrl",function ($scope) {
//    $scope.pulls=[
//        {
//            lockTime:"2016-07-09 12:33:44",
//            wxInfoPic:"images/left/icon1.png",
//            wxInfoName:"name",
//            tel:"紫气东来大酒店",
//            downlineMoney:"100",
//            onlineMoney:"100"
//        },
//        {
//            lockTime:"2016-07-09 12:33:44",
//            wxInfoPic:"images/left/icon1.png",
//            wxInfoName:"name",
//            tel:"紫气东来大酒店",
//            downlineMoney:"100",
//            onlineMoney:"100"
//        }
//    ]
//});
