'use strict';

angular.module('app')
  .factory('Password', function($resource,$rootScope) {
    return $resource($rootScope.managerUrl + 'api/password', {}, {});
  });
