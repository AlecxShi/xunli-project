'use strict';

angular.module('app')
  .controller('ProfileCtrl', function($rootScope, $scope, $q, $mdToast, Account, Validate, Principal) {
    $scope.success = null;
    $scope.error = null;

    Principal.identity().then(function(account) {
      $scope.user = angular.copy(account);
    });

    $scope.save = function(event) {
      event.preventDefault();

      angular.forEach($scope.form.$error.required, function(field) {
        field.$setTouched();
      });

      if ($scope.form.$valid) {
        Account.save($scope.user).$promise.then(
          function(result, responseHeaders) {
            $scope.error = null;
            $scope.success = 'OK';

            Principal.identity(true)
              .then(function(account) {
                $rootScope.account = account;
                $rootScope.isAuthenticated = Principal.isAuthenticated;
                $scope.reset();
              });

            $mdToast.show(
              $mdToast.simple()
              .content('用户信息保存成功')
              .hideDelay(3000)
              .theme('success')
            );
          }
        ).catch(function(httpResponse) {
          $scope.error = 'ERROR';
          $scope.success = null;
          $mdToast.show(
            $mdToast.simple()
            .content('用户信息保存失败，请稍候再试。')
            .hideDelay(3000)
            .theme('error')
          );
        });
      }
    };

    $scope.emailNotExist = function(value) {
      return $q(function(resolve, reject) {
        Validate.emailNotExist({
          id: $scope.user ? $scope.user.id : null,
          value: value
        }).$promise.then(
          function(result, responseHeaders) {
            if (result.valid) {
              resolve();
            } else {
              reject();
            }
          }
        );
      });
    };

    $scope.phoneNotExist = function(value) {
      return $q(function(resolve, reject) {
        Validate.emailNotExist({
          id: $scope.user ? $scope.user.id : null,
          value: value
        }).$promise.then(
          function(result, responseHeaders) {
            if (result.valid) {
              resolve();
            } else {
              reject();
            }
          }
        );
      });
    };
    $scope.reset = function(event) {

      $scope.user = angular.copy($rootScope.account);

      $scope.form.$setPristine();
      $scope.form.$setUntouched();
    };
  });
