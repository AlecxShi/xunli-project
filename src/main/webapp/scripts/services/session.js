'use strict';

angular.module('app')
  .factory('Session', function($resource,$rootScope) {
    return $resource($rootScope.managerUrl + 'api/sessions/:series', {}, {});
  });
