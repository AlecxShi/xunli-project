/**
 * 
 */
'use strict';

angular.module('app')
  .factory('EmailInfo', function($resource) {
    return $resource('api/EmailInfo/:id', {}, {
      all: {
        url: 'api/EmailInfo',
        method: 'GET',
        isArray: true
      },
    });
  });