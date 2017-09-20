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
.controller('DiscoverEditController',function($q,$state,$scope, $mdToast,$mdDialog,DictInfo,article,Discover) {
	  $scope.article = article || {};
	  $scope.error = "";
	  $scope.article.content = "shihuijiang";
	  $scope.title = article.id === 'new' ? "添加新字典" : "修改字典";
	  $scope.config =
	  {
            //是否聚焦 focus默认为false
            focus : true,
            //首行缩进距离,默认是2em
            indentValue:'2em',
            //初始化编辑器宽度,默认100%
            initialFrameWidth:'100%',
            //初始化编辑器高度,默认320
            initialFrameHeight:550,
            //编辑器初始化结束后,编辑区域是否是只读的，默认是false
            readonly : false ,
            //启用自动保存
            enableAutoSave: false,
            //自动保存间隔时间， 单位ms
            saveInterval: 3000,
            //是否开启初始化时即全屏，默认关闭
            fullscreen : false,
            //图片操作的浮层开关，默认打开
            imagePopup:true,
            //提交到后台的数据是否包含整个html字符串
            allHtmlEnabled:false,
            //额外功能添加
            functions :['map','insertimage','insertvideo','attachment','insertcode','webapp','template','background','wordimage']
      };
	  $scope.hide = function() {
	     $mdDialog.hide();
	  };

      $scope.getFile = function () {
          $scope.readAsDataURL($scope.articleIcon, $scope)
            .then(function(result) {
                $scope.imageSrc = result;
            });
      };

      $scope.onLoad = function(reader, deferred, scope) {
         return function () {
             scope.$apply(function () {
                 deferred.resolve(reader.result);
             });
         };
      };

      $scope.onError = function (reader, deferred, scope) {
         return function () {
             scope.$apply(function () {
                 deferred.reject(reader.result);
             });
         };
      };

      $scope.getReader = function(deferred, scope) {
          var reader = new FileReader();
          reader.onload = $scope.onLoad(reader, deferred, scope);
          reader.onerror = $scope.onError(reader, deferred, scope);
          return reader;
      };

      $scope.readAsDataURL = function (file, scope) {
          var deferred = $q.defer();
          var reader = $scope.getReader(deferred, scope);
          reader.readAsDataURL(file);
          return deferred.promise;
      };

	  $scope.cancel = function() {
	     $mdDialog.cancel();
	  };

	  $scope.save = function(ev){
	      console.log($scope.article);
		  $scope.error = "";
		  event.preventDefault();
	      angular.forEach($scope.form.$error.required, function(field) {
	        field.$setTouched();
	      });

	      if ($scope.form.$valid)
          {
             Discover.save($scope.dict).$promise.then(
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

	  $scope.cancel = function()
	  {
	    $state.go('discover.list');
	  }
})
