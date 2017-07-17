'use strict';

angular.module('app')
  .controller('HealthCtrl', function($scope, Health) {
    $scope.health = {};
    $scope.refresh = function(event) {
      $scope.loading = true;
      Health.get().$promise
        .then(function(result, headers) {
          $scope.loading = false;
          $scope.health = result;
        })
        .catch(function(response) {
          $scope.loading = false;
          $scope.health = angular.fromJson(response.data);
        });
    };

    $scope.refresh();

  });
