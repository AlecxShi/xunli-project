'use strict';

angular.module('app')
  .controller('PasswordCtrl', function($scope,$rootScope, $mdToast, Password,Auth) {
    $scope.success = null;
    $scope.error = null;

    $scope.save = function(event) {
      event.preventDefault();

      angular.forEach($scope.form.$error.required, function(field) {
        field.$setTouched();
      });

      if ($scope.form.$valid) {
        Password.save({
        	originalPassword:$scope.originalPassword,
        	password:$scope.password
        }).$promise.then(
          function(result, responseHeaders) {
            $scope.error = null;
            $scope.success = 'OK';

            $scope.originalPassword=null;
            $scope.password = null;
            $scope.confirmPassword = null;
            $scope.form.$setPristine();
            $scope.form.$setUntouched();
            $mdToast.show(
              $mdToast.simple()
              .content('密码修改成功')
              .hideDelay(3000)
              .theme('success')
            );
            $rootScope.logout();
          }
        ).catch(function(httpResponse) {
          $scope.error = 'ERROR';
          $scope.success = null;
          $mdToast.show(
            $mdToast.simple()
            .content('密码修改失败，请稍候再试。')
            .hideDelay(3000)
            .theme('error')
          );
        });
      }
    };
  });