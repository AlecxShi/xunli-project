/**
 * 
 */
'use strict';

angular.module('app')
  .factory('Discover', function($resource) {
    return $resource('system/article/:id', {}, {
      get: {
        url: 'system/article/query',
        method: 'GET'
      },
      save: {
        url: 'system/article/save',
        method: 'POST'
      },
      delete: {
        url: 'system/article/delete',
        method: 'POST'
      },
      upload: {
        url: 'system/article/upload',
        method: 'POST'
      },
      getById: {
        url: 'system/article/query',
        method: 'GET'
      }
    });
  });