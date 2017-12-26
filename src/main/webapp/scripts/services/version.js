/**
 *
 */
'use strict';

angular.module('app')
  .factory('AppVersion', function($resource) {
    return $resource('api/version/:id', {}, {
      all: {
        url: 'api/version/query',
        method: 'GET'
      },
      save: {
        url : 'api/version/save',
        method: 'POST'
      },
      del: {
        url : 'api/version/delete',
        method: 'DELETE'
      }
    });
  });