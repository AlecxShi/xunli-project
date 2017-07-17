'use strict';

angular.module('app')
  .controller('AuthorityListCtrl', function($scope, $state, $stateParams, $mdToast, Authority) {

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

    $scope.toTypeStr = function(type){
      if(type == '0'){
        return '目录';
      }else if(type == '1'){
        return '菜单';
      }else if(type == '2'){
        return '按钮';
      }else if(type == '3'){
        return '功能';
      }
    }

    $scope.onChange = function() {

      var sort = $scope.query.order;
      if (sort && sort.length > 0) {
        if (sort.charAt(0) === '-') {
          sort = sort.substring(1) + ',desc';
        } else {
          sort = sort + ',asc';
        }
      }
      return Authority.get({
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

    $scope.addSub = function(item) {
      $state.go('^.edit', {
        id: 'sub',
        parent: item,
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
      Authority.remove({
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
  .controller('AuthorityEditCtrl', function($scope, $q, $state, $stateParams, $mdToast, authority, Authority, Validate) {

    $scope.authority = authority;
    $scope.authorityTypes = [
      {
        value: 0,
        label: '目录'
      },{
        value: 1,
        label: '菜单'
      },{
        value: 2,
        label: '按钮'
      },{
        value: 3,
        label: '功能'
      },
      ];

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
        Authority.save($scope.authority).$promise.then(
          function(result, responseHeaders) {
            $scope.error = null;
            $scope.success = 'OK';
            $mdToast.show(
              $mdToast.simple()
              .content('权限保存成功。')
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
            .content('权限保存失败。')
            .hideDelay(3000)
            .theme('error')
          );
        });
      }
    };
  });
