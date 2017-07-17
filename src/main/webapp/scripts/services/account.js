'use strict';

angular.module('app')
  .factory('Account', function($rootScope,$resource) {
	  $rootScope.managerUrl = 'http://localhost:9999/';
    return $resource($rootScope.managerUrl + 'api/account', {}, {});
  })
  .factory('Token', function($rootScope,$resource) {
    return $resource($rootScope.managerUrl + 'api/token', {}, {
      'get': {
        method: 'GET',
        transformResponse: function(data, headers) {
          return {
            token: data
          };
        }
      }
    });
  });
