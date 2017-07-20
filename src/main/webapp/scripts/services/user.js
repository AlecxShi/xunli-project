'use strict';

angular.module('app')
  .factory('User', function($resource,$rootScope) {
    return $resource('/api/user/:id', {}, {
      all: {
        url: 'api/users',
        method: 'GET',
        isArray: true
      }
    });
  });
