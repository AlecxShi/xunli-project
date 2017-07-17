'use strict';

angular.module('app')
  .factory('Health', function($resource) {
    return $resource('manage/health', {}, {});
  });
