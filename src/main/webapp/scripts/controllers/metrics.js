'use strict';

angular.module('app')
  .controller('MetricsCtrl', function($scope, Metrics) {
    $scope.metrics = {};

    $scope.refresh = function() {
      $scope.loading = true;
      Metrics.get().$promise
        .then(function(result, headers) {
          $scope.loading = false;
          $scope.metrics = result;
        })
        .catch(function(response) {
          $scope.loading = false;
        });
    };
    $scope.refresh();
  });
