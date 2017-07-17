'use strict';

angular.module('app')
  .factory('Role', function($resource,$rootScope) {
    return $resource($rootScope.managerUrl + 'api/role/:id', {}, {
      all: {
        url: $rootScope.managerUrl + 'api/roles',
        method: 'GET',
        isArray: true
      },
    });
  });
