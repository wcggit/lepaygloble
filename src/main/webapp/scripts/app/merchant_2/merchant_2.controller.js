 /**
 * Created by recoluan on 2016/11/16.
 */
 'use strict';


angular.module('lepayglobleApp')
    .controller('merchant_2Controller', function ($scope, $state, $rootScope, $location,Principal,Auth,$http) {
        // INFO
        Principal.identity().then(function (account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });
        $rootScope.personal=false;
        $scope.personalClick=function () {
            $rootScope.personal =!$rootScope.personal;
            $rootScope.personal=$rootScope.personal;
        };
        $http.get('api/merchant').success(function (response) {
            $scope.shopName = response.data.name;
        });
        //  LOGOUT
        $scope.logout = function () {
            Auth.logout();
            $state.go('home')
        }
        //  MENU
        $scope.menuList=[
            {
                contentState:"1",
                clickState:"1",
                name:"首页",
                state:"homePage",
                icon:"iconfont icon-2wodezhangdan18x20"
            },
            {
                contentState:"0",
            },
            {
                contentState:"1",
                clickState:"0",
                name:"交易管理",
                state:"",
                icon:"iconfont icon-2wodezhangdan18x20"
            },
            {
                contentState:"1",
                clickState:"1",
                name:"乐加账单",
                state:"lePlusBilling",
                icon:""
            },
            {
                contentState:"1",
                clickState:"1",
                name:"扫码交易",
                state:"codeTrade",
                icon:""
            },
            {
                contentState:"1",
                clickState:"1",
                name:"POS交易",
                state:"POSTrade",
                icon:""
            },
            {
                contentState:"0",
            },
            {
                contentState:"1",
                clickState:"0",
                name:"佣金收入",
                state:"commissionIncome",
                icon:"iconfont icon-2wodezhangdan18x20"
            },
            {
                contentState:"1",
                clickState:"1",
                name:"数据概览",
                state:"dataOverview",
                icon:""
            },
            {
                contentState:"1",
                clickState:"1",
                name:"佣金输入",
                state:"CommissionIncome",
                icon:""
            },
            {
                contentState:"1",
                clickState:"1",
                name:"我的会员",
                state:"myMembers",
                icon:""
            },
            {
                contentState:"1",
                clickState:"1",
                name:"会员邀请码",
                state:"InvitationCode",
                icon:""
            },
            {
                contentState:"0",
            },
            {
                contentState:"1",
                clickState:"1",
                name:"门店管理",
                state:"storeManage",
                icon:"iconfont icon-2wodezhangdan18x20"
            },
            {
                contentState:"0",
            },
            {
                contentState:"1",
                clickState:"1",
                name:"账户管理",
                state:"accountManage",
                icon:"iconfont icon-2wodezhangdan18x20"
            },
            {
                contentState:"0",
            }
        ]
        $scope.currentTab=$scope.menuList[0].state;
        $scope.onClickTab=function (tab) {
            if(tab.state!=''){
                $scope.currentTab=tab.state;
                $state.go(tab.state);
            }

        }
        $scope.isActiveTab=function (tab) {
            return tab==$scope.currentTab;
        }

    })
