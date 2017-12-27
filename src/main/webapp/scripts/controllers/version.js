'use strict';

angular.module('app')
  .controller('AppVersionCtrl', function($scope, $rootScope,$state, $stateParams, $mdToast,$mdDialog,AppVersion,DictInfo) {

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

    DictInfo.getByDictType({dictType:'UpdateLevel'}).$promise.then(function (data) {
        $scope.allUpdateLevels = data;
    });

    $scope.getUpdateLevel = function(level)
    {
        var desc = '';
        angular.forEach($scope.allUpdateLevels,function(item){
            if(item.dictValue == level)
            {
                desc =  item.dictDesc;
            }
        });

        return desc;
    }

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


      return AppVersion.all({
          page: $scope.query.page - 1,
          size: $scope.query.size,
          sort: sort,
          filter: $scope.query.filter
        },
        function(value, responseHeaders) {
          $rootScope.hideLoading();
          $scope.data = value;
        }
      ).$promise.then(function(){
        $rootScope.hideLoading();
      });
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
  	      controller: AppVersionAddOrEditController,
  	      templateUrl: 'version_add.html',
  	      parent: angular.element(document.body),
  	      clickOutsideToClose:true,
  	      resolve : {
  				version: function() {
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
	      AppVersion.del({
	    	  ids: ids.join(',')
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
    	      controller: AppVersionAddOrEditController,
    	      templateUrl: 'version_add.html',
    	      parent: angular.element(document.body),
    	      clickOutsideToClose:true,
    	      resolve : {
    	    	  version : function($stateParams) {
    	    	            var v = angular.copy(item);
    	    	            v.upload = v.fileName == undefined || v.fileName == '' ? false : true;
    	    	            v.ifUse = v.ifUse == 'Y' ? true : false;
    	    	            console.log(v);
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

function AppVersionAddOrEditController($http,$q,$scope, $mdToast,$mdDialog, version,AppVersion,DictInfo,isNew) {
	  $scope.version = version;
	  $scope.error = "";
	  $scope.title = isNew ? "添加新版本信息" : "修改版本信息";

	  $scope.hide = function() {
	     $mdDialog.hide();
	  };

      DictInfo.getByDictType({dictType:'UpdateLevel'}).$promise.then(function (data) {
         $scope.allUpdateLevels = data;
      });

	  $scope.cancel = function() {
	     $mdDialog.cancel();
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
        };

	  $scope.save = function(ev)
	  {
          $scope.error = "";
          event.preventDefault();
          angular.forEach($scope.form.$error.required, function(field) {
            field.$setTouched();
          });

          if ($scope.form.$valid)
          {
               if($scope.version.upload == true)
               {
                    console.log($scope.version.file,version.file);
                    $http({
                         method:'POST',
                         url: 'http://localhost:9527/quxiangqin/version/upload',
                         headers: {
                           'Content-Type': undefined
                         },
                         data: {
                           file:$scope.version.file
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
                           $mdToast.simple().content("安装包上传成功").hideDelay(3000).theme('success')
                         );
                         if(value.fileName != null)
                         {
                             $scope.version.fileName = value.fileName;
                             $scope.version.ifUse = $scope.version.ifUse == true ? 'Y' : 'N';
                             console.log($scope.version);
                             AppVersion.save($scope.version).$promise.then(
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
                             $mdToast.simple().content("安装包上传失败").hideDelay(3000).theme('error')
                         );
                       });
               }
               else
               {
                    $scope.version.fileName = $scope.version.fileName ? $scope.version.fileName : "";
                    $scope.version.ifUse = $scope.version.ifUse == true ? 'Y' : 'N';
                    AppVersion.save($scope.version).$promise.then(
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
          }
      }
}
