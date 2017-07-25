/**
 * 
 */
'use strict';

angular.module('app')
  .factory('DictInfo', function($resource) {
    return $resource('system/dictinfo/:id', {}, {
      all: {
        url: 'system/dictinfo',
        method: 'GET',
        isArray: true
      }
    });
  });