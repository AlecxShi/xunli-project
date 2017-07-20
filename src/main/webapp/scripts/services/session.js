'use strict';

angular.module('app')
  .factory('Session', function($resource,$rootScope) {
    return $resource('api/sessions/:series', {}, {});
  });
