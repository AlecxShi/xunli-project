/**
 * 
 */
'use strict';

angular.module('app')
  .factory('ChildrenExtendInfo', function($resource) {
    return $resource('children/extend/:id', {}, {
      all: {
        url: 'children/extend',
        method: 'GET',
        isArray: true
      }
    });
  });