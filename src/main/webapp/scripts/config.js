'use strict';

angular.module('app').config(function($stateProvider, $urlRouterProvider) {
	$urlRouterProvider.otherwise('/');
	$stateProvider.state('login', {
		url : '/login',
		data : {
			authorities : [],
			title : '登录',
		},
		templateUrl : 'views/login.html',
		controller : 'LoginCtrl'
	}).state('dashboard', {
		url : '/',
		data : {
			authorities : [ 'ROLE_USER' ],
			title : '首页',
		},
		templateUrl : 'views/dashboard.html',
		controller : 'DashboardCtrl'
	}).state('password', {
		url : '/password',
		data : {
			authorities : [ 'ROLE_USER' ],
			title : '修改密码',
		},
		templateUrl : 'views/password.html',
		controller : 'PasswordCtrl'
	}).state('profile', {
		url : '/profile',
		data : {
			authorities : [ 'ROLE_USER' ],
			title : '用户信息',
		},
		templateUrl : 'views/profile.html',
		controller : 'ProfileCtrl'
	}).state('health', {
		url : '/health',
		data : {
			authorities : [ 'ROLE_USER' ],
			title : '健康检查',
		},
		templateUrl : 'views/health.html',
		controller : 'HealthCtrl'
	}).state('metrics', {
		url : '/metrics',
		data : {
			authorities : [ 'ROLE_USER' ],
			title : '指标监控',
		},
		templateUrl : 'views/metrics.html',
		controller : 'MetricsCtrl'
	}).state('logs', {
		url : '/logs',
		data : {
			authorities : [ 'ROLE_USER' ],
			title : '日志浏览',
		},
		templateUrl : 'views/logs.html',
		controller : 'LogsCtrl'
	}).state('logger', {
		url : '/logger',
		data : {
			authorities : [ 'ROLE_ADMIN' ],
			title : '日志配置'
		},
		templateUrl : 'views/logger.html',
		controller : 'LoggerCtrl'
	})
	.state('user', {
        url: '/user',
        abstract: true,
        data: {
          authorities: ['ROLE_ADMIN'],
          title: '用户',
        },
        templateUrl: 'views/user.html'
      })
	.state('user.list', {
		url : '/user.list',
		params : {
			query : null
		},
		data : {
			authorities : [ 'ROLE_ADMIN' ],
			title : '用户管理',
		},
		templateUrl : 'views/user.list.html',
		controller : 'SysUserListCtrl'
	}).state('user.edit', {
		url : '/{id}',
		params : {
			query : null
		},
		data : {
			authorities : [ 'ROLE_ADMIN' ],
			title : '用户编辑',
		},
		templateUrl : 'views/user.edit.html',
		controller : 'SysUserEditCtrl',
		resolve : {
			roles : function(Role) {
				return Role.all().$promise;
			},
			user : function($stateParams, User) {
				if ($stateParams.id === 'new') {
					return {
						activated : true,
						enabled : true,
						roles : [],
						ifRequired : true
					};

				} else {
					return User.get({
						id : $stateParams.id,
					}).$promise;
				}
			}
		}
	}).state('role', {
		url : '/role',
		abstract : true,
		data : {
			authorities : [ 'ROLE_ADMIN' ],
			title : '角色',
		},
		templateUrl : 'views/role.html'
	}).state('role.list', {
		url : '',
		params : {
			query : null
		},
		data : {
			authorities : [ 'ROLE_ADMIN' ],
			title : '角色管理',
		},
		templateUrl : 'views/role.list.html',
		controller : 'RoleListCtrl'
	}).state('role.edit', {
		url : '/{id}',
		params : {
			query : null
		},
		data : {
			authorities : [ 'ROLE_ADMIN' ],
			title : '角色编辑',
		},
		templateUrl : 'views/role.edit.html',
		controller : 'RoleEditCtrl',
		resolve : {
			authorities : function(Authority) {
				return Authority.all().$promise;
			},
			role : function($stateParams, Role) {
				if ($stateParams.id === 'new') {
					return {
						authorities : [],
					};

				} else {
					return Role.get({
						id : $stateParams.id
					}).$promise;
				}
			}
		}
	})

	/*.state('permission', {
		url : '/permission',
		params : {
			taskID :null
		},
		data : {
			authorities : [ 'ROLE_USER' ],
			title : '查看权限目录',
		},
		templateUrl : 'views/permission.html',
		controller : 'PermissionController',
		resolve : {
			taskID : function($stateParams)
			{
				return $stateParams.taskID;
			}
		}
	})*/
	
	.state('authority', {
        url: '/authority',
        abstract: true,
        data: {
          authorities: ['ROLE_ADMIN'],
          title: '权限',
        },
        templateUrl: 'views/authority.html'
      })
      .state('authority.list', {
		url : '',
		params : {
			query : null
		},
		data : {
			authorities : [ 'ROLE_ADMIN' ],
			title : '权限管理',
		},
		templateUrl : 'views/authority.list.html',
		controller : 'AuthorityListCtrl'
	}).state('authority.edit', {
		url : '/{id}',
		params : {
			parent : null,
			query : null
		},
		data : {
			authorities : [ 'ROLE_ADMIN' ],
			title : '权限编辑',
		},
		templateUrl : 'views/authority.edit.html',
		controller : 'AuthorityEditCtrl',
		resolve : {
			authority : function($stateParams, Authority) {
				if ($stateParams.id === 'new') {
					return {

					};
				} else if ($stateParams.id === 'sub') {
					return {
						parent : $stateParams.parent
					};
				} else {
					return Authority.get({
						id : $stateParams.id
					}).$promise;
				}
			}
		}
	}).state('error', {
		url : '/error',
		data : {
			authorities : [],
			title : '错误',
		},
		templateUrl : 'views/error.html'
	}).state('denied', {
		url : '/denied',
		data : {
			authorities : [],
			title : '无权访问',
		},
		templateUrl : 'views/denied.html'
	})
	.state('userPermissions', {
		url : '/userPermissions',
		data : {
			authorities : [ 'ROLE_ADMIN' ],
			title : '用户权限管理',
		},
		templateUrl : 'views/userPermissions.html',
		controller : 'userPermissionsCtrl'
	})

	.state('apiUser', {
		url : '/apiUser',
		data : {
			authorities : [ 'ROLE_ADMIN','ROLE_USER' ],
			title : 'API用户管理',
		},
		templateUrl : 'views/apiUser.html',
		controller : 'APIUserListCtrl'
	})

	.state('apiConfig', {
		url : '/apiConfig',
		data : {
			authorities : [ 'ROLE_ADMIN' ],
			title : 'API配置界面',
		},
		templateUrl : 'views/apiGroupInfo.html',
		controller : 'APIConfigCtrl',
		params:{
			selectedGroupId:null
		},
		resolve : {
			selectedGroupId : function($stateParams,$cookies) {
				return $stateParams.selectedGroupId = $stateParams.selectedGroupId == null ? $cookies.get('currentOperateGroupId') : $stateParams.selectedGroupId;
			}
		}
	}).state('apiInfoedit', {
		url : '/apiInfoedit/{id}',
		params : {
			query :null,
			type : null,
			toWeb :null,
			data : null,
			groupId : null,
			ifApiPublic : null,
			ifApiAnnounce : null
		},
		data : {
			authorities : [ 'ROLE_USER' ],
			title : 'API信息编辑',
		},
		templateUrl : 'views/apiInfo.html',
		controller : 'APIInfoEditCtrl',
		resolve : {
			apiInfo : function($stateParams, ApiInfo) {
				if ($stateParams.id === 'new') {
					return {};
				}
				 return ApiInfo.getApiInfoByApiId({
					 apiIds : $stateParams.id
			        },
			        function(value, responseHeaders) {
			        	value.ifPublic = value.ifPublic == "Y" ? true : false;
			        }
			      ).$promise;
			},	
//			dsApiInfos : function(ApiInfo) {
//				 return ApiInfo.getDSAPIConfigInfo().$promise;
//			},	
			query : function($stateParams)
			{
				return $stateParams.query;
			},
			toWeb : function($stateParams)
				{
					return $stateParams.toWeb;
				},
			data :function($stateParams)
			{
				return $stateParams.data;
			},
			groupId :function($stateParams)
			{
				return $stateParams.groupId;
			},
			ifApiPublic:function($stateParams)
			{
				return $stateParams.ifApiPublic;
			},
			ifApiAnnounce :function($stateParams)
			{
				return $stateParams.ifApiAnnounce;
			},
		}	
	}).state('apiInfoTest', {
		url : '/apiInfoTest/{id}',
		params : {
			data : null,
			apiInfo : null
		},
		data : {
			authorities : [ 'ROLE_USER' ],
			title : 'API指标测试',
		},
		templateUrl : 'views/apiInfo.test.html',
		controller : 'APIInfoTestCtrl',
		resolve : {
			apiInfo : function($stateParams, ApiInfo) {
				 return ApiInfo.getApiInfoByApiId({
					 apiIds : $stateParams.id
			        }, function(value, responseHeaders) {
			        	
			        }
			      ).$promise;
			},	
			data :function($stateParams)
			{
				return $stateParams.data;
			}
		}	
	}).state('dsApiUpdate', {
		url : '/dsApiUpdate',
		data : {
			authorities : [ 'ROLE_ADMIN' ],
			title : 'DS指标更新',
		},
		templateUrl : 'views/dsApiInfoUpdate.html',
		controller : 'DSApiInfoUpdateCtrl'
	}).state('hotSearch', {
        url: '/hotSearch',
        data: {
          authorities: ['ROLE_ADMIN','ROLE_USER'],
          title: '热门搜索管理',
        },
        templateUrl: 'views/apiHotSearch.html',
        controller : 'ApiHotSearchCtrl'
      }).state('apiTestCaseManage', {
		url : '/apiTestCase/{id}',
		data : {
			authorities : [ 'ROLE_USER' ],
			title : 'API测试用例管理',
		},
		templateUrl : 'views/apiInfo.testCase.html',
		controller : 'APITestCaseCtrl',
		params:{
			id : null
		},
		resolve : {
			apiInfo : function($stateParams, ApiInfo) {
				 return ApiInfo.getApiInfoByApiId({
					 apiIds : $stateParams.id
			        }, function(value, responseHeaders) {
			        	
			        }
			      ).$promise;
			}
		}	
	}).state('apiSysUpgrade', {
		url : '/apiSysUpgrade',
		params : {
			query : null
		},
		data : {
			authorities : [ 'ROLE_ADMIN' ],
			title : '系统升级信息',
		},
		templateUrl : 'views/apiSysUpgrade.html',
		controller : 'ApiSysUpgradeCtrl',
		resolve : {
			query : function($stateParams)
			{
				return $stateParams.query;
			}
		  }
	})
	.state('apiSysUpgradeEdit', {
		url : '/apiSysUpgradeEdit/{id}',
		data : {
			authorities : [ 'ROLE_ADMIN' ],
			title : '系统升级信息修改',
		},
		templateUrl : 'views/apiSysUpgradeEdit.html',
		controller : 'ApiSysUpgradeEditCtrl',
		params:{
			query : null
		},
		resolve : {
			id : function($stateParams)
			{
				return $stateParams.id;
			},
			query : function($stateParams)
			{
				return $stateParams.query;
			},
			upInfo : function($stateParams,systemUpgrade) {
				
				if ($stateParams.id === 'new') {
					return {};
				}
				return systemUpgrade.get({
					 id : $stateParams.id
			        }, function(value, responseHeaders) {
			        	
			        }
			      ).$promise;
					//return $stateParams.upInfo;
			}
		
		}	
	})
	.state('apiServerInfo', {
		url : '/apiServerInfo',
		data : {
			authorities : [ 'ROLE_ADMIN' ],
			title : '服务器',
		},
		templateUrl : 'views/apiServerInfo.html',
		controller : 'ApiServerInfoCtrl'
	});
}).config(
		function($mdThemingProvider) {
			$mdThemingProvider.theme('default').primaryPalette('blue')
					.accentPalette('pink').warnPalette('red')
					.backgroundPalette('grey');
			$mdThemingProvider.theme('success').primaryPalette('green');
			$mdThemingProvider.theme('error').primaryPalette('red');
		}).config(function($mdDateLocaleProvider,$httpProvider) {
			
			$httpProvider.defaults.xsrfCookieName = 'CSRF-TOKEN';
			$httpProvider.defaults.xsrfHeaderName = 'X-CSRF-TOKEN';

			$httpProvider.interceptors.push('loadingInterceptor');
			// $httpProvider.interceptors.push('errorHandlerInterceptor');
			$httpProvider.interceptors.push('authExpiredInterceptor');
			
	$mdDateLocaleProvider.days = [ '周日', '周一', '周二', '周三', '周四', '周五', '周六' ];
	$mdDateLocaleProvider.shortDays = [ '日', '一', '二', '三', '四', '五', '六' ];
});