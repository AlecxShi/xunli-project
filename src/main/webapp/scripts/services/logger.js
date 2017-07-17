'use strict';

angular.module('app')
  .factory('LoggerService', function($resource) {
    return $resource('api/logs', {}, {
      'findAll': { method: 'GET', isArray: true},
      'changeLevel': { method: 'PUT'}
    });
  });
