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
	}).state('dictinfo', {
        url : '/dictinfo',
        data : {
            authorities : ['ROLE_ADMIN'],
            title : '字典管理',
        },
        templateUrl : 'views/dictinfo.html',
        controller : 'DictInfoCtrl'
    }).state('commonuser', {
        url : '/commonuser',
        data : {
            authorities : ['ROLE_ADMIN'],
            title : '用户管理',
        },
        templateUrl : 'views/commonuser.list.html',
        controller : 'CommonUserInfoCtrl'
    })
}).config(function($mdThemingProvider) {
			$mdThemingProvider.theme('default').primaryPalette('blue')
                .accentPalette('pink').warnPalette('red')
                .backgroundPalette('grey');
			$mdThemingProvider.theme('success').primaryPalette('green');
			$mdThemingProvider.theme('error').primaryPalette('red');
		})
    .config(function($mdDateLocaleProvider,$httpProvider) {
			
			$httpProvider.defaults.xsrfCookieName = 'CSRF-TOKEN';
			$httpProvider.defaults.xsrfHeaderName = 'X-CSRF-TOKEN';

			$httpProvider.interceptors.push('loadingInterceptor');
			// $httpProvider.interceptors.push('errorHandlerInterceptor');
			$httpProvider.interceptors.push('authExpiredInterceptor');
			
            $mdDateLocaleProvider.days = [ '周日', '周一', '周二', '周三', '周四', '周五', '周六' ];
            $mdDateLocaleProvider.shortDays = [ '日', '一', '二', '三', '四', '五', '六' ];
});