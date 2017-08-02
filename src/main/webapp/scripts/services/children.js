/**
 * 
 */
'use strict';

angular.module('app')
  .factory('ChildrenInfo', function($resource) {
    return $resource('system/children/:id', {}, {
      all: {
        url: 'system/children',
        method: 'GET',
        isArray: true
      }
    });
  });