'use strict';

angular
  .module('app', [
    'ngAnimate',
    'ngCookies',
    'ngResource',
    'ngRoute',
    'ngMessages',
    'ngSanitize',
    'ngAria',
    'ngMaterial',
    'md.data.table',
    'ui.router',
    'ui.validate',
    'ui.tree',
    'angularMoment',
    'chart.js',
    'angular-loading-bar',
    'angular.filter',
    'LocalStorageModule',
    'ngDialog',
    'ui.bootstrap',
    'dialogs.main',
    'pascalprecht.translate',
    'dialogs.default-translations',
    'ngCookies'
  ])
  .config(['$httpProvider',function($httpProvider)
  {
    $httpProvider.defaults.withCredentials = true;
  }
  ])
.run(function($rootScope, $state, $stateParams, $window,$templateCache,$mdToast, $mdDialog, Auth, Principal) {

    $rootScope.$state = $state;
    $rootScope.$stateParams = $stateParams;
    
    $rootScope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams) {
      $rootScope.toState = toState;
      $rootScope.toStateParams = toParams;
      //if (Principal.isIdentityResolved()) {
      Auth.authorize();
      //}

    });

    $rootScope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams) {
      if (toState.name !== 'login' && $rootScope.previousStateName) {
        $rootScope.previousStateName = fromState.name;
        $rootScope.previousStateParams = fromParams;
      }
      if (toState.data && toState.data.title) {
        $window.document.title = '管理台－ ' + toState.data.title;
      }


    });

    $rootScope.logout = function() {
      Auth.logout();
      $state.go('login');
    };

    $rootScope.back = function() {
      // If previous state is 'activate' or do not exist go to 'home'
      if ($rootScope.previousStateName === 'login' || $state.get($rootScope.previousStateName) === null) {
        $state.go('dashboard');
      } else {
        $state.go($rootScope.previousStateName, $rootScope.previousStateParams);
      }
    };
    
    $templateCache.put('loading.html', '<md-dialog style="background-color:transparent;box-shadow:none;z-index:99999;">' +
      '<div layout="row" layout-sm="column" layout-align="center center" aria-label="wait">' +
      '<div loader-css="line-scale"></div>' +
      '</div>' +
      '</md-dialog>');

    /**
     * 显示遮罩
     */
    $rootScope.showLoading = function () {
      $mdDialog.show({
        templateUrl: 'loading.html',
        parent: angular.element(document.body),
        clickOutsideToClose: false,
        fullscreen: false
      });
    };
    /**
     * 隐藏遮罩
     */
    $rootScope.hideLoading = function () {
      $mdDialog.cancel();
    };

    $rootScope.showDictionary = function(key,list)
    {
        if(angular.isArray(list))
        {
            for(var i in list)
            {
                var dict = list[i];
                if(angular.isObject(dict) && dict['id'] == key)
                {
                    return dict['dictDesc'];
                }
            }
        }
    }
});
