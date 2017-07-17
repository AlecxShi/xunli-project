'use strict';

angular.module('app')
  .controller('LoginCtrl', function($rootScope, $scope, $state, $timeout, $mdToast, Auth) {
    $scope.user = {};
    $scope.errors = {};

    $scope.rememberMe = true;

    $scope.login = function(event) {
      event.preventDefault();

      angular.forEach($scope.form.$error.required, function(field) {
        field.$setTouched();
      });
      if ($scope.form.$valid) {
        Auth.login({
          username: $scope.username,
          password: $scope.password,
          rememberMe: $scope.rememberMe
        }).then(function() {
          $scope.authenticationError = false;
          $mdToast.show(
            $mdToast.simple()
            .content('用户登录成功。')
            .hideDelay(3000)
            .theme('success')
          );
          if ($rootScope.previousStateName === 'login') {
            $state.go('dashboard');
          } else {
            $rootScope.back();
          }
        }).catch(function(httpResponse) {
          $scope.authenticationError = true;
          $mdToast.show(
            $mdToast.simple()
            .content('用户登录失败。')
            .hideDelay(3000)
            .theme('error')
          );
        });
      }
    };
  });
