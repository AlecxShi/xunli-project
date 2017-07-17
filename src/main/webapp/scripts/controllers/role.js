'use strict';

angular.module('app')
  .controller('RoleListCtrl', function($scope, $state, $stateParams, $mdToast, Role) {

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
      size: 10
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
      return Role.get({
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
      Role.remove({
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
  .controller('RoleEditCtrl', function($scope, $filter, $q, $state, $stateParams, $mdToast, authorities, role, Role, Validate) {

    $scope.authorities = authorities;

    $scope.role = role;

    if (!$scope.role.authorities) {
      $scope.role.authorities = [];
    }

    $scope.availables = $filter('filter')($scope.authorities, function(value, index, array) {
      return $filter('filter')($scope.role.authorities, {
        id: value.id
      }).length === 0;
    });

    $scope.add = function(event) {

      var selected = [];
      for (var i = $scope.availables.length - 1; i >= 0; i--) {
        var value = $scope.availables[i]
        if (value.selected) {
          $scope.availables.splice(i, 1);
          value.selected = false;
          selected.unshift(value);
        }
      }
      $scope.role.authorities = $scope.role.authorities.concat(selected);
    };

    $scope.addAll = function(event) {

      $scope.role.authorities = $scope.role.authorities.concat($scope.availables);
      $scope.availables = [];

    };

    $scope.remove = function(event) {
      var selected = [];
      for (var i = $scope.role.authorities.length - 1; i >= 0; i--) {
        var value = $scope.role.authorities[i]
        if (value.selected) {
          $scope.role.authorities.splice(i, 1);
          value.selected = false;
          selected.unshift(value);
        }
      }
      $scope.availables = $scope.availables.concat(selected);

    };

    $scope.removeAll = function(event) {
      $scope.availables = $scope.availables.concat($scope.product.items);
      $scope.role.authorities = [];

    };

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
        Role.save($scope.role).$promise.then(
          function(result, responseHeaders) {
            $scope.error = null;
            $scope.success = 'OK';
            $mdToast.show(
              $mdToast.simple()
              .content('角色保存成功。')
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
            .content('角色保存失败。')
            .hideDelay(3000)
            .theme('error')
          );
        });
      }
    };

  });
