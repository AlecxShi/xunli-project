'use strict';

angular.module('app')
  .factory('Register', function($resource,$rootScope) {
    return $resource('api/register', {}, {});
  });
