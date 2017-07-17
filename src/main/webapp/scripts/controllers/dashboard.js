'use strict';

angular.module('app')
  .controller('DashboardCtrl', function($scope, $mdDialog, Token) {
    $scope.showToken = function(event) {
      Token.get().$promise
        .then(function(result, headers) {
          $mdDialog.show(
            $mdDialog.alert()
            .clickOutsideToClose(true)
            .title('同步令牌')
            .textContent(result.token)
            .ariaLabel('同步令牌')
            .ok('确定')
            .targetEvent(event)
          );
        })
        .catch(function(response) {
          $mdDialog.show(
            $mdDialog.alert()
            .clickOutsideToClose(true)
            .title('同步令牌')
            .textContent('无法生成同步令牌，请稍后再试')
            .ariaLabel('同步令牌')
            .ok('确定')
            .targetEvent(event)
          );

        });

    }

  });
