'use strict';

angular.module('app')
  .controller('CommonUserInfoCtrl', function ($scope, $state, $filter, $mdDialog, $rootScope, $stateParams, $mdToast, CommonUserInfo,ChildrenInfo,DictInfo) {
    var bookmark;

    $scope.products = {};
    $scope.advancedSearchFlag = false;
    //用户类型
    DictInfo.getByDictType({dictType:'USER_TYPE'}).$promise.then(function (data) {
      $scope.userType = data;
    });
    //性别
    DictInfo.getByDictType({dictType:'Gender'}).$promise.then(function (data) {
      $scope.gender = data;
    });
    //教育程度
    DictInfo.getByDictType({dictType:'Education'}).$promise.then(function (data) {
      $scope.education = data;
    });
    //省
    DictInfo.getByDictType({dictType:'Province'}).$promise.then(function (data) {
      $scope.bornLocationProvince = angular.copy(data);
      $scope.currentLocationProvince = angular.copy(data);
    });
    //房产
    DictInfo.getByDictType({dictType:'House'}).$promise.then(function (data) {
      $scope.house = data;
    });

    //是否有车
    $scope.car = [
        {
            value:true,
            desc:'是'
        },
        {
            value:false,
            desc:'否'
        }
    ];

    //出生省份联动城市
    $scope.$watch('query.bornLocationProvince', function (newValue, oldValue) {
      if (newValue && newValue !== oldValue) {
        var dict = angular.fromJson(newValue);
        DictInfo.getByDictType({dictType:dict.dictValue}).$promise.then(function (data) {
          $scope.bornLocationCity = data;
        });
      }
    });
    //当前所在省份联动城市
    $scope.$watch('query.currentLocationProvince', function (newValue, oldValue) {
      if (newValue && newValue !== oldValue) {
        var dict = angular.fromJson(newValue);
        DictInfo.getByDictType({dictType:dict.dictValue}).$promise.then(function (data) {
          $scope.currentLocationCity = data;
        });
      }
    });

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

    $scope.advancedSearchToggle = function (advancedSearchEvent) {
      $scope.advancedSearchFlag = !advancedSearchEvent;
    }

    $scope.onChange = function () {
      var sort = $scope.query.order;
      if (sort && sort.length > 0) {
        if (sort.charAt(0) === '-') {
          sort = sort.substring(1) + ',desc';
        } else {
          sort = sort + ',asc';
        }
      }
      // = '';
      return CommonUserInfo.get({
          page: $scope.query.page - 1,
          size: $scope.query.size,
          sort: sort,
          filter: $scope.query.filter
        },
        function (value, responseHeaders) {
          $scope.data = value;
        }
      ).$promise;
    };

//-------------------------高级搜索-------------------------------------------------

    $scope.advancedSearchSave = function (e) {
      $scope.startDate = null;
      $scope.endDate = null;

      $rootScope.showLoading();
      if ($scope.query.advancedSearchStartTime) {
        $scope.startDate = $filter('date')($scope.query.advancedSearchStartTime, 'yyyy-MM-dd');
      }
      if ($scope.query.advancedSearchEndTime) {
        $scope.endDate = $filter('date')($scope.query.advancedSearchEndTime, 'yyyy-MM-dd');
      }
      $scope.selected = [];
      var sort = $scope.query.order;
      if (sort && sort.length > 0) {
        if (sort.charAt(0) === '-') {
          sort = sort.substring(1) + ',desc';
        } else {
          sort = sort + ',asc';
        }
      }
      var params = {
        page: $scope.query.page - 1,
        size: $scope.query.size,
        sort: sort,
        Id: $scope.query.advancedSearchId,
        systemType: $scope.query.advancedSearchSystemType,
        cpId: $scope.query.advancedSearchCpId,
        companyId: $scope.query.companyId,
        accountName: $scope.query.advancedSearchAccountName,
        status: $scope.query.advancedSearchStatus,
        nature: $scope.query.advancedSearchNature,
        isHistory: $scope.query.isHistory,
        startDate: $scope.startDate,
        endDate: $scope.endDate
      };
      return Agreement.get(params,function (value, responseHeaders) {
          $rootScope.hideLoading();
          $scope.data = value;
          for(var i=0;i<value.content.length;i++){
            if(value.content[i].remark!=null){
              var cutRemark=value.content[i].remark.substring(0,15);
              cutRemark=cutRemark+'...';
              $scope.data.content[i].cutRemark=cutRemark;
            }
            if(value.content[i].name!=null){
              var names=[];
              for(var j=0;j<$scope.data.content[i].name.length;j++){
                names.push($scope.data.content[i].name[j].name);
              }
              $scope.data.content[i].names=names.join(',');
            }
          }
        }
      ).$promise;
    }

    //清除搜索条件
    $scope.cleanAdvancedSearch = function () {
      $scope.query.userType = null;
      $scope.query.currentLocationProvince = null;
      $scope.query.currentLocationCity = null;
      $scope.query.bornLocationProvince = null;
      $scope.query.bornLocationCity = null;
      $scope.query.height = null;
      $scope.query.gender = null;
      $scope.query.car = null;
      $scope.query.house = null;
      $scope.query.bornStartTime = false;
      $scope.query.bornEndTime = null;
      $scope.promise = $scope.onChange();
    }


    $scope.removeFilter = function () {
      $scope.filter.show = false;
      $scope.query.filter = '';

      if ($scope.filter.form.$dirty) {
        $scope.filter.form.$setPristine();
      }
    };

    $scope.refresh = function () {
      //刷新时收起高级搜索，并清空条件
      $scope.advancedSearchFlag = false;
      $scope.cleanAdvancedSearch();
      $scope.selected = [];
    };

    //翻页时判断高级搜索是否收起
    $scope.pageRefresh = function () {
      if ($scope.advancedSearchFlag == false) {//收起，调取所有数据
        $scope.promise = $scope.onChange();
      } else {//未收起，根据条件查询
        $scope.promise = $scope.advancedSearchSave();
      }
      $scope.selected = [];
    }

    //添加用户和子女信息,单个操作
    $scope.add = function (event) {
      $state.go('^.edit', {
        id: 'new'
      });
    };

    //快速创建
    $scope.quickAdd = function (event) {
      $rootScope.showLoading();
      CommonUserInfo.batchCreate({},function(value){
        $rootScope.hideLoading();
      });
    };

    $scope.edit = function (itemId) {
      $state.go('^.edit', {
        id: itemId
      });
    };

    //删除选中用户信息
    $scope.delete = function (event) {
      var ids = [];
      $scope.selected.forEach(function (item, index) {
        ids.push(item.id);
      });
      $rootScope.showLoading();
      CommonUserInfo.remove({
        id: ids.join(',')
      }).$promise.then(function () {
        $rootScope.hideLoading();
        $scope.refresh();
      });
    };

    $scope.$watch('query.filter', function (newValue, oldValue) {
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
  .controller('CommonUserInfoEditCtrl', function ($rootScope, $scope, $filter, $q, $state, $stateParams, $mdToast, $mdDialog,$timeout,CommonUserInfo,ChildrenInfo,DictInfo,commonUser) {
    console.log('commonuser',commonUser);
    $scope.commonUser = commonUser || {};
    $scope.children = $scope.children || {};
    //用户类型
    DictInfo.getByDictType({dictType:'USER_TYPE'}).$promise.then(function (data) {
      $scope.userType = data;
    });
    //性别
    DictInfo.getByDictType({dictType:'Gender'}).$promise.then(function (data) {
      $scope.gender = data;
    });
    //教育程度
    DictInfo.getByDictType({dictType:'Education'}).$promise.then(function (data) {
      $scope.education = data;
    });
    //省
    DictInfo.getByDictType({dictType:'Province'}).$promise.then(function (data) {
      $scope.bornLocationProvince = angular.copy(data);
      $scope.currentLocationProvince = angular.copy(data);
    });
    //房产
    DictInfo.getByDictType({dictType:'House'}).$promise.then(function (data) {
      $scope.house = data;
    });

    //收入选择
    DictInfo.getByDictType({dictType:'Income'}).$promise.then(function (data) {
      $scope.income = data;
    });
    //是否有车
    $scope.car = [
        {
            value:true,
            desc:'是'
        },
        {
            value:false,
            desc:'否'
        }
    ];

    //出生省份联动城市
    $scope.$watch('commonUser.bornLocationProvince', function (newValue, oldValue) {
      if (newValue && newValue !== oldValue) {
        var dict = angular.fromJson(newValue);
        DictInfo.getByDictType({dictType:dict.dictValue}).$promise.then(function (data) {
          $scope.bornLocationCity = data;
        });
      }
    });

    //子女省份联动城市
    $scope.$watch('commonUser.children.bornLocationProvince', function (newValue, oldValue) {
      if (newValue && newValue !== oldValue) {
        var dict = angular.fromJson(newValue);
        DictInfo.getByDictType({dictType:dict.dictValue}).$promise.then(function (data) {
          $scope.childrenBornLocationCity = data;
        });
      }
    });
    //当前所在省份联动城市
    $scope.$watch('commonUser.children.currentLocationProvince', function (newValue, oldValue) {
      if (newValue && newValue !== oldValue) {
        var dict = angular.fromJson(newValue);
        DictInfo.getByDictType({dictType:dict.dictValue}).$promise.then(function (data) {
          $scope.currentLocationCity = data;
        });
      }
    });

    $scope.phoneNotExist = function(value) {
      return $q(function(resolve, reject) {
        ChildrenInfo.phoneNotExist({
          id: $scope.commonUser ? $scope.commonUser.id : null,
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

