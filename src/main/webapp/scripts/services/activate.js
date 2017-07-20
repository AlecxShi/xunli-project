'use strict';

angular.module('app')
  .factory('Activate', function($rootScope,$resource) {
    return $resource('api/activate', {}, {});
  });
