'use strict';

angular.module('app')
  .factory('Authority', function($resource,$rootScope) {
    return $resource('api/authority/:id', {}, {
      all: {
        url: 'api/authorities',
        method: 'GET',
        isArray: true
      },
    });
  });
