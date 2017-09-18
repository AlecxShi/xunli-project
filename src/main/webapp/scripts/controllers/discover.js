'use strict';

angular.module('app')
  .controller('DiscoverController', function($scope, $rootScope,$state, $stateParams, $mdToast,$mdDialog,DictInfo,Discover) {

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
      $rootScope.showLoading();
      var sort = $scope.query.order;
      if (sort && sort.length > 0) {
        if (sort.charAt(0) === '-') {
          sort = sort.substring(1) + ',desc';
        } else {
          sort = sort + ',asc';
        }
      }


      return Discover.get({
          page: $scope.query.page - 1,
          size: $scope.query.size,
          sort: sort,
          filter: $scope.query.filter
        },
        function(value, responseHeaders) {
          $rootScope.hideLoading();
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

    $scope.refresh = function(page, limit)
    {
		if(limit != undefined && limit !=  null)
        {
          $scope.query.page = page;
          $scope.query.size = limit;
        }
		$scope.promise = $scope.onChange();
	};

	$scope.add = function(ev) {
        $state.go('^.edit',{id: 'new'});
    };

    $scope.delete = function(ev) {
    	  var ids = [];
	      $scope.selected.forEach(function(item, index) {
	    	  ids.push(item.id);
	      });
	      Discover.remove({
	    	  id: ids.join(',')
	      }).$promise.then(function(value, responseHeaders){
	    	  $scope.selected = [];
	          $mdToast.show(
	            $mdToast.simple().content("刪除成功").hideDelay(3000).theme('success')
	          );
          }).catch(function(httpResponse) {
              $mdToast.show(
                $mdToast.simple().content('刪除失败。').hideDelay(3000).theme('error')
              );
          }).then(function() {
              $scope.refresh();
	      });


    };

    $scope.edit = function(item) {
    	$mdDialog.show({
    	      controller: DictInfoAddOrEditController,
    	      templateUrl: 'dictinfo_add.html',
    	      parent: angular.element(document.body),
    	      clickOutsideToClose:true,
    	      resolve : {
    	    	  dict : function($stateParams) {
    					    return angular.copy(item);
    				     },
       	          isNew: function() {
         					  return false;
         				 }
    		  }
    	    }).then(function() {
    	    	$scope.promise = $scope.refresh();
    	    }, function() {

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
.controller('DiscoverEditController',function($scope, $mdToast,$mdDialog,DictInfo,article,UM) {

	  $scope.dict = article;
	  $scope.error = "";
	  $scope.title = article.id === 'new' ? "添加新字典" : "修改字典";

      window.um = UM.getEditor('container', {
        	/* 传入配置参数,可配参数列表看umeditor.config.js */
            toolbar: ['undo redo | bold italic underline']
      });
	  $scope.hide = function() {
	     $mdDialog.hide();
	  };

	  $scope.cancel = function() {
	     $mdDialog.cancel();
	  };

	  $scope.save = function(ev){
		  $scope.error = "";
		  event.preventDefault();
	      angular.forEach($scope.form.$error.required, function(field) {
	        field.$setTouched();
	      });

	      if ($scope.form.$valid)
          {
             DictInfo.save($scope.dict).$promise.then(
                    function(result, responseHeaders) {
                        $scope.error = null;
                        $scope.success = 'OK';
                        $mdToast.show(
                          $mdToast.simple()
                          .content('保存成功。')
                          .hideDelay(3000)
                          .theme('success')
                        );
                        $mdDialog.hide();
                      }
                    ).catch(function(httpResponse) {
                      $scope.error = 'ERROR';
                      $scope.success = null;
                      $mdToast.show(
                        $mdToast.simple()
                        .content('保存失败。')
                        .hideDelay(3000)
                        .theme('error')
                      );
                    });
             $mdDialog.hide();
          }
	  };
})
