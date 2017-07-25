'use strict';

angular.module('app')
  .controller('DictInfoCtrl', function($scope, $state, $stateParams, $mdToast,$mdDialog,DictInfo) {

    var bookmark;

    $scope.selected = [];

    $scope.filter = {
      options: {
        debounce: 500
      }
    };

    $scope.query = {
      filter: '',
      order: '-dictType',
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
      
      
      return DictInfo.get({
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
		$mdDialog.show({
  	      controller: DictInfoAddOrEditController,
  	      templateUrl: 'dictinfo_add.html',
  	      parent: angular.element(document.body),
  	      clickOutsideToClose:true,
  	      resolve : {
  				dict: function() {
  					    return {};
  				      },
  	            isNew: function() {
    					return true;
    			      }
  		  }
  	    })
  	    .then(function() {
  	    	$scope.promise = $scope.refresh();
  	    }, function() {
  	    	
  	    });
    };
    
    $scope.delete = function(ev) {
    	  var ids = [];
	      $scope.selected.forEach(function(item, index) {
	    	  ids.push(item.id);
	      });
	      DictInfo.remove({
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


  });

function DictInfoAddOrEditController($scope, $mdToast,$mdDialog, dict,DictInfo,isNew) {
	  $scope.dict = dict;
	  $scope.error = "";
	  $scope.title = isNew ? "添加新字典" : "修改字典";
	  
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
}
