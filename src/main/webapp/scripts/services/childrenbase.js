/**
 * 
 */
'use strict';

angular.module('app')
  .factory('ChildrenBaseInfo', function($resource) {
    return $resource('children/base/:id', {}, {
      all: {
        url: 'children/base',
        method: 'GET',
        isArray: true
      }
    });
  });