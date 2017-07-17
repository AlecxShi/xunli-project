'use strict';

angular.module('app')
  .factory('Metrics', function($resource) {
    return $resource('manage/metrics', {}, {});
  });
