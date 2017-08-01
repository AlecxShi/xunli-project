/**
 * 
 */
'use strict';

angular.module('app')
  .factory('CommonUserInfo', function($resource) {
    return $resource('system/user/:id', {}, {
      all: {
        url: 'system/user',
        method: 'GET',
        isArray: true
      },
      batchCreate: {
        url: 'system/user/quick',
        method: 'POST'
      }
    });
  });