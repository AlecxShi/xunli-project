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
      },
      getByDictType: {
        url : 'system/dictinfo/getByType',
        method: 'GET',
        isArray: true
      }
    });
  });