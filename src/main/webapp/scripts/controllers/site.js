'use strict';

angular.module('app')
  .controller('SiteCtrl', function($scope, $state, $mdSidenav, Auth, Principal) {

    $scope.menu = [{
      state: 'dashboard',
      title: '首页',
      icon: 'dashboard',
      authorities: 'ROLE_USER'
    },
	
	{
	      title: 'API配置管理',
	      authorities: 'ROLE_USER'
	    }, {
	      state: 'apiConfig',
	      title: 'API配置管理',
	      icon: 'format_list_bulleted',
	      authorities: 'ROLE_USER'
	    }, 
    {
	      title: '用户管理',
	      authorities: 'ROLE_USER'
	    },
	    {
	    	state:'apiUser',
	    	title:'API用户管理',
	    	icon:'person',
	    	authorities:'ROLE_USER'
	    },
	    {
	      state: 'userPermissions',
	      title: '用户权限管理',
	      icon: 'person',
	      authorities: 'ROLE_USER'
	    }, 
	    {
	      title: '其他管理',
	      authorities: 'ROLE_USER'
	    },
	    {
	    	state:'dsApiUpdate',
	    	title:'DS指标更新',
	    	icon:'format_list_bulleted',
	    	authorities:'ROLE_USER'
	    },
	    {
	    	state:'hotSearch',
	    	title:'热门搜索管理',
	    	icon:'format_list_bulleted',
	    	authorities:'ROLE_USER'
	    },
	    {
	      title: '系统升级信息',
	      authorities: 'ROLE_USER'
	    },
	    {
	    	state:'apiSysUpgrade',
	    	title:'系统升级管理',
	    	icon:'reorder',
	    	authorities:'ROLE_ADMIN'
	    },
	    {
	    	state:'apiServerInfo',
	    	title:'服务器',
	    	icon:'desktop_windows',
	    	authorities:'ROLE_ADMIN'
	    },
	    
/*    {
      state: 'database',
      active: 'database',
      title: '数据库管理',
      icon: 'data_usage',
      authorities: 'ROLE_USER'
    },*/
   
    	
    	{
    	      title: '系统用户设置',
    	      authorities: 'ROLE_USER'
    	    }, {
    	      state: 'password',
    	      title: '修改密码',
    	      icon: 'lock',
    	      authorities: 'ROLE_USER'
    	    }, {
    	      state: 'profile',
    	      title: '用户信息',
    	      icon: 'person',
    	      authorities: 'ROLE_USER'
    	    }, {
    	      state: 'user.list',
    	      active: 'user.list',
    	      title: '用户管理',
    	      icon: 'group',
    	      authorities: 'ROLE_ADMIN'
    	    },/* {
    	      state: 'database.list',
    	      active: 'database.list',
    	      title: '测试数据库配置',
    	      icon: 'reorder',
    	      authorities: 'ROLE_ADMIN'
    	    },{
    	      state: 'role.list',
    	      active: 'role',
    	      title: '角色管理',
    	      icon: 'assignment_ind',
    	      authorities: 'ROLE_ADMIN'
    	    }, {
    	      state: 'authority.list',
    	      active: 'authority',
    	      title: '权限管理',
    	      icon: 'format_list_bulleted',
    	      authorities: 'ROLE_ADMIN'
    	    }*/ 
    	   /* ,{
    	      title: '运维监控',
    	      authorities: 'ROLE_ADMIN'
    	    }, {
    	      state: 'metrics',
    	      title: '指标监控',
    	      icon: 'network_check',
    	      authorities: 'ROLE_ADMIN'
    	    }, {
    	      state: 'health',
    	      title: '健康检查',
    	      icon: 'track_changes',
    	      authorities: 'ROLE_ADMIN'
    	    }, {
    	      state: 'logs',
    	      title: '日志浏览',
    	      icon: 'subject',
    	      authorities: 'ROLE_ADMIN'
    	    }, {
    	      state: 'logger',
    	      title: '日志配置',
    	      icon: 'storage',
    	      authorities: 'ROLE_ADMIN'
    	    }*/
    	    
    	    ];
  });
