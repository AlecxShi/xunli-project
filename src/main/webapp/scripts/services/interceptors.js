'use strict';

angular.module('app')
  .factory('authExpiredInterceptor', function($rootScope, $q, $injector, localStorageService) {
    return {
      responseError: function(response) {
        // If we have an unauthorized request we redirect to the login page
        // Don't do this check on the account API to avoid infinite loop
        if (response.status === 401 && response.data.path !== undefined && response.data.path.indexOf('/api/account') === -1 && response.data.path.indexOf('/api/authentication') === -1) {
          var Auth = $injector.get('Auth');
          var $state = $injector.get('$state');
          var to = $rootScope.toState;
          var params = $rootScope.toStateParams;
          Auth.logout();
          $rootScope.previousStateName = to.name;
          $rootScope.previousStateNameParams = params;
          $state.go('login');
        }
        return $q.reject(response);
      }
    };
  })
  .factory('errorHandlerInterceptor', function($q, $rootScope) {
    return {
      'responseError': function(response) {
        if (!(response.status === 401 && response.data.path.indexOf('/api/account') === 0)) {
          $rootScope.$emit('app.httpError', response);
        }
        return $q.reject(response);
      }
    };
  })
  .factory('loadingInterceptor', function($rootScope, $q) {
    return {
      request: function(config) {
        $rootScope.$broadcast('loading:show');
        return config;
      },
      response: function(response) {
        $rootScope.$broadcast('loading:hide');
        return response;
      },
      responseError: function(rejection) {
        $rootScope.$broadcast('loading:hide');
        return $q.reject(rejection);
      }
    };
  });
