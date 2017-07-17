'use strict';

angular.module('app')
  .factory('Authority', function($resource,$rootScope) {
    return $resource($rootScope.managerUrl + 'api/authority/:id', {}, {
      all: {
        url: $rootScope.managerUrl +  'api/authorities',
        method: 'GET',
        isArray: true
      },
    });
  });
