'use strict';

angular.module('app')
  .factory('Account', function($rootScope,$resource) {
    return $resource('api/account', {}, {});
  })
  .factory('Token', function($rootScope,$resource) {
    return $resource('api/token', {}, {
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
