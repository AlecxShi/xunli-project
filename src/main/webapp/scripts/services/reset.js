'use strict';

angular.module('app')
  .factory('Reset', function($resource,$rootScope) {
    return $resource($rootScope.managerUrl +'api/reset', {}, {});
  });
