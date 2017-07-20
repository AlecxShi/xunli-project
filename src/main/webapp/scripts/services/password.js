'use strict';

angular.module('app')
  .factory('Password', function($resource,$rootScope) {
    return $resource('api/password', {}, {});
  });
