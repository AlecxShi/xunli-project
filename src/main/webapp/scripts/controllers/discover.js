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
      order: '-articleId',
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

    $scope.delete = function(item) {
    	  var ids = [];
    	  if(item != undefined)
    	  {
    	    ids.push(item.articleId);
    	  }
	      Discover.delete({
	    	  ids : ids.join(',')
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

    $scope.publish = function()
    {
        var ids = [];
        $scope.selected.forEach(function(item, index) {
           ids.push(item.articleId);
        });
        Discover.publish({
           ids : ids.join(',')
        }).$promise.then(function(value, responseHeaders){
           $scope.selected = [];
           $mdToast.show(
             $mdToast.simple().content("发布成功").hideDelay(3000).theme('success')
          );
        }).catch(function(httpResponse) {
           $mdToast.show(
             $mdToast.simple().content('发布失败。').hideDelay(3000).theme('error')
           );
        }).then(function() {
           $scope.refresh();
        });
    }

    $scope.edit = function(item) {
        $state.go('^.edit',{id: item.articleId});
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
.controller('DiscoverEditController',function($http,$q,$state,$scope, $mdToast,$mdDialog,DictInfo,article,Discover) {
	  $scope.server = "http://119.23.220.163:8080";
	  $scope.article = article;
	  $scope.error = "";
	  $scope.title = article.articleId === 'new' ? "添加文章" : "修改文章";
	  $scope.image = article.image ? $scope.server + article.image : article.image;
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
            readonly : article.ifPublish == 'Y' ? true : false ,
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
          //文件读取对象
          var reader = new FileReader();
          var defer = $q.defer();
          reader.onload = function () {
            $scope.$apply(function () {
               defer.resolve(reader.result);
            })
          };

          reader.onerror = function () {
            $scope.$apply(function () {
                defer.reject(reader.result);
            });
          };

          reader.readAsDataURL($scope.articleIcon);

          defer.promise.then(function(result) {
            $scope.image = result;
          });
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
             $http({
               method:'POST',
               url: '/system/article/upload',
               headers: {
                 'Content-Type': undefined
               },
               data: {
                 icon:$scope.articleIcon
               },
               transformRequest: function(data, headersGetter){
                 var formData = new FormData();
                 angular.forEach(data, function (value, key) {
                   formData.append(key, value);
                 });
                 return formData;
                }
             }).success(function(value) {
               console.log(value);
               $mdToast.show(
                 $mdToast.simple().content("图标上传成功").hideDelay(3000).theme('success')
               );
               if(value.fileName != null)
               {
                   article.iconName = value.fileName;
                   Discover.save($scope.article).$promise.then(
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
               }
             }).error(function() {
               $mdToast.show(
                   $mdToast.simple().content("图标上传失败").hideDelay(3000).theme('error')
               );
             });
          }
	  };

	  $scope.cancel = function()
	  {
	    $state.go('discover.list');
	  }
})
