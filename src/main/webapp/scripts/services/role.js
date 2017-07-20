'use strict';

angular.module('app')
  .factory('Role', function($resource,$rootScope) {
    return $resource($rootScope.managerUrl + 'api/role/:id', {}, {
      all: {
        url: 'api/roles',
        method: 'GET',
        isArray: true
      },
    });
  });
