'use strict';

angular.module('app')
  .controller('SysUserListCtrl', function($scope, $state, $stateParams, $mdToast, User) {

    var bookmark;

    $scope.selected = [];

    $scope.filter = {
      options: {
        debounce: 500
      }
    };

    $scope.query = {
      filter: '',
      order: '-id',
      page: 1,
      size: 50
    };


    $scope.onChange = function() {

      var sort = $scope.query.order;
      if (sort && sort.length > 0) {
        if (sort.charAt(0) === '-') {
          sort = sort.substring(1) + ',desc';
        } else {
          sort = sort + ',asc';
        }
      }
      return User.get({
          page: $scope.query.page - 1,
          size: $scope.query.size,
          sort: sort,
          filter: $scope.query.filter
        },
        function(value, responseHeaders) {
          $scope.data = value;
        }
      ).$promise;
    };


    $scope.removeFilter = function() {
      $scope.filter.show = false;
      $scope.query.filter = '';

      if ($scope.filter.form.$dirty) {
        $scope.filter.form.$setPristine();
      }
    };

    $scope.refresh = function() {
      $scope.promise = $scope.onChange();
      $scope.selected = [];
    };

    $scope.add = function(event) {
      $state.go('^.edit', {
        id: 'new',
        query: $scope.query
      });
    };

    $scope.edit = function(item) {
      $state.go('^.edit', {
        id: item.id,
        query: $scope.query
      });
    };

    $scope.delete = function(event) {
      var ids = [];
      $scope.selected.forEach(function(item, index) {
        ids.push(item.id);
      });
      User.remove({
        id: ids.join(',')
      }).$promise.then(function() {
        $scope.refresh();
      });
    };

    $scope.$watch('query.filter', function(newValue, oldValue) {
      if (!oldValue) {
        bookmark = $scope.query.page;
      }

      if (newValue !== oldValue) {
        $scope.query.page = 1;
      }

      if (!newValue) {
        $scope.query.page = bookmark;
      }
      $scope.refresh();
    });



  })
  .controller('SysUserEditCtrl', function($scope, $q, $state, $stateParams, $mdToast, roles, user, User,Validate) {

    $scope.roles = roles;
    //var roles = ['ROLE_USER', 'ROLE_ADMIN'];

    function createFilterFor(query) {
      //var uppercaseQuery = angular.uppercase(query);
      return function filterFn(role) {
        return role.roleName.indexOf(query) >= 0;
      };
    }

    $scope.queryRole = function(query) {
      return query ? $scope.roles.filter(createFilterFor(query)) : $scope.roles;
    };

    $scope.transformChip = function(chip) {
      return chip;
    };

    $scope.user = user;

    $scope.cancel = function() {
      $state.go('^.list', {
        query: $scope.query
      });
    };

    $scope.save = function(event) {
      event.preventDefault();
      angular.forEach($scope.form.$error.required, function(field) {
        field.$setTouched();
      });

      if ($scope.form.$valid) {
        User.save($scope.user).$promise.then(
          function(result, responseHeaders) {
            $scope.error = null;
            $scope.success = 'OK';
            $mdToast.show(
              $mdToast.simple()
              .content('用户保存成功。')
              .hideDelay(3000)
              .theme('success')
            );
            $state.go('^.list', {
              query: $scope.query
            });
          }
        ).catch(function(httpResponse) {
          $scope.error = 'ERROR';
          $scope.success = null;
          $mdToast.show(
            $mdToast.simple()
            .content('用户保存失败。')
            .hideDelay(3000)
            .theme('error')
          );
        });
      }
    };


    $scope.usernameNotExist = function(value) {
      return $q(function(resolve, reject) {
        Validate.usernameNotExist({
          id: $scope.user.id,
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

    $scope.emailNotExist = function(value) {
      return $q(function(resolve, reject) {
        Validate.emailNotExist({
          id: $scope.user.id,
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
          id: $scope.user.id,
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
  });
