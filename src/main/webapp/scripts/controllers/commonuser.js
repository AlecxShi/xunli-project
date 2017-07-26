'use strict';

angular.module('app')
  .controller('CommonUserInfoCtrl', function ($scope, $state, $filter, $mdDialog, $rootScope, $stateParams, $mdToast, CommonUserInfo,ChildrenBaseInfo,ChildrenExtendInfo,DictInfo) {
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
      size: 10,
      status: 'NORMAL'
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

    $scope.add = function (event) {
      $state.go('commonuser.edit', {
        id: 'new',
        query: $scope.query
      });
    };

    $scope.quickAdd = function (event) {
      $mdDialog.show({
        templateUrl: 'quickAdd.html',
        controller: quickAddController,
        parent: angular.element(document.body),
        clickOutsideToClose: true
      })
    };

    $scope.edit = function (itemId) {
      $state.go('agreement.edit', {
        id: itemId,
        query: $scope.query
      });
    };

    $scope.delete = function (event) {
      var ids = [];
      $scope.selected.forEach(function (item, index) {
        ids.push(item.id);
      });
      Agreement.remove({
        id: ids.join(',')
      }).$promise.then(function () {
        $scope.refresh();
      });
    };

    $scope.exportPermission = function (type) {
      var ids = [];
      var onlyOne=true;//暂时只可以同时下载一个用户的权限，这个用来判断是否只选中一个用户
      for(var i=0;i<$scope.selected.length;i++){
        ids.push($scope.selected[i].id);
        for(var j=0;j<$scope.selected.length;j++){
          if($scope.selected[i].userAccountName!=$scope.selected[j].userAccountName){
            onlyOne=false;
            break;
          }
        }
        if(onlyOne==false){
          break;
        }
      }
      if(onlyOne==true){
        $rootScope.showLoading();
        Agreement.exportPermission({
          ids: ids.join(','),type:type,userAccountId:$scope.selected[0].userAccountId
        }).$promise.then(function (data) {
          if (data.filename != null) {
            location.href = '/api/export/exportFile/' + data.filename;// 传filename，后台根据filename找文件
          } else {
            console.log("下载失败！");
          }
          $rootScope.hideLoading();
        }).catch(function () {
          $rootScope.showResult("下载excel失败",null, false);
        });
      }
      else{
        $mdDialog.show(
          $mdDialog.alert()
            .clickOutsideToClose(true)
            .title('警告')
            .textContent('只允许导出相同账号的协议！')
            .ok('确定')
            .targetEvent(event)
        );
      }
    };
    $scope.updateEndDate = function () {
      var ids = [];
      for(var i=0;i<$scope.selected.length;i++){
        ids.push($scope.selected[i].id);
      }
      $mdDialog.show({
        controller:UpdateEndDateController,
        templateUrl:'update_endDate.html',
        parent: angular.element(document.body),
        clickOutsideToClose: true,
      }).then(function (endDate) {
        Agreement.updateEndDate({ids:ids,endDate:endDate}).$promise.then(function(data){
          if (data.code == '0') {
            $rootScope.showResult('保存成功',null, true);
            for(var j=0;j<$scope.selected.length;j++){
              $rootScope.pushToAllHost('PERMISSION', $scope.selected[j].userAccountId,$scope.selected[j].systemType);//后台直接推送
            }
            $scope.refresh();
          }
          else{
            $rootScope.showResult("延期失败",null, false);
          }
        }).catch(function () {
          $rootScope.showResult("延期失败",null, false);
        });
      });
    };

    $scope.updateNature = function () {
      var ids = [];
      var userAccountIds = [];
      for(var i=0;i<$scope.selected.length;i++){
        ids.push($scope.selected[i].id);
      }
      $mdDialog.show({
        controller:UpdateNatureController,
        templateUrl:'update_nature.html',
        parent: angular.element(document.body),
        clickOutsideToClose: true,
      }).then(function (nature) {
        Agreement.updateNature({ids:ids,nature:nature}).$promise.then(function(data){
          if (data.code == '0') {
            $rootScope.showResult('保存成功',null, true);
            $scope.refresh();
          }
          else{
            $rootScope.showResult("修改失败",null, false);
          }
        }).catch(function () {
          $rootScope.showResult("修改失败",null, false);
        });
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
    $scope.queryCard = function (remark,ev) {
      $mdDialog.show(
        $mdDialog.alert()
          .parent(angular.element(document.querySelector('#popupContainer')))
          .clickOutsideToClose(true)
          .title('备注:')
          .textContent(remark)
          .ok('关闭')
          .targetEvent(ev)
      );
    }
  })
  .controller('AgreementEditCtrl', function ($rootScope, $mdMedia, $scope, $filter, $q, $state, $stateParams, $mdToast, $mdDialog,
                                             $timeout, IntegralUITreeViewService, agreement, Agreement, SystemConstCache,
                                             products, ProductGroup, ProductPackage, UserCompany, Contract, SystemConst,
                                             UserAccount, Validate, Push) {
    $scope.agreement = agreement;//协议
    if (agreement && agreement.packageId && agreement.packageId.length > 0) {
      $scope.agreement.packageId = agreement.packageId.substring(1, agreement.packageId.length - 1);
    }
    $scope.products = products;//所有产品
    SystemConstCache.get('AGREEMENT_STATUS').then(function (data) {
      $scope.statusTypes = data;
    });
    $scope.New = $state.params.id;
    $scope.ctrl = {};
    $scope.ctrl.isPush = true;
    $scope.ctrl.signDate = null;
    $scope.ctrl.startDate = null;
    $scope.ctrl.endDate = null;
    $scope.selectedCompany = null;//所选机构
    $scope.searchCompany = null;//显示机构
    $scope.selectedUserAccount = null;//所选账号
    $scope.searchUserAccount = null;//显示账号
    $scope.userPers = [];//资源指定的权限
    $scope.items = [];//权限项
    $scope.packages = [];//产品包
    $scope.userAccounts = [];//账号
    $scope.contracts = [];//账号的合同
    $scope.forms = {};
    $scope.isSelectProduct = false;

    if ($scope.agreement.packageId) {
      $scope.agreement.packageId = $scope.agreement.packageId.split(",");
    }
    if ($stateParams.quickAgreement) {//快速添加带来的数据
      $scope.agreement.userAccountId = $stateParams.quickAgreement.userAccountId;
      $scope.agreement.startDate = new Date();
      $scope.agreement.companyId = $stateParams.quickAgreement.companyId;
      $scope.agreement.endDate = $stateParams.quickAgreement.endDate;
    }
    if ($stateParams.userAccount) {  //处理直接从账号那边带数据过来的情况
      $scope.agreement.userAccountId = $stateParams.userAccount.id;
      $scope.agreement.companyId = $stateParams.userAccount.companyId;
      $scope.agreement.systemType = $stateParams.userAccount.openSystem.split(',')[0];//如果有多个系统，第一个生效
    }
    if ($scope.agreement.createdBy) {
      $scope.createdBy = $scope.agreement.createdBy;
    }
    if ($scope.agreement.lastModifiedBy) {
      $scope.lastModifiedBy = $scope.agreement.lastModifiedBy;
    }
    if ($scope.agreement.createdDate) {
      $scope.createdDate = $scope.agreement.createdDate.substring(0, 10);
    }
    if ($scope.agreement.lastModifiedDate) {
      $scope.lastModifiedDate = $scope.agreement.lastModifiedDate.substring(0, 10);
    }

    $scope.treeName = 'packageTree';
    $scope.treeEvents = {
      itemDblClick: function (e) {
        return $scope.onItemDblClick(e);
      }
    };
    $scope.checkBoxSettings = {
      autoCheck: true,
      threeState: true
    };

    $scope.expandAll = function () {
      IntegralUITreeViewService.expand($scope.treeName);
    };

    $scope.collapseAll = function () {
      IntegralUITreeViewService.collapse($scope.treeName);
    };

    $scope.onLoadComplete = function () {
      IntegralUITreeViewService.endLoad($scope.treeName);
    };

    $scope.itemCheckStateChanging = function (e) {
      if (e.value === 'unchecked') {
        e.item.checkState = 'checked';
      }
    };

    SystemConstCache.get('SYSTEM_TYPE').then(function (data) {
      $scope.systemTypes = data;
    });
    //SystemConstCache.get('AGREEMENT_STATUS').then(function(data){
    //  $scope.statusTypes = data;
    //});

    SystemConstCache.get('AGREEMENT_NATURE').then(function (data) {
      $scope.natureTypes = data;
    });

    SystemConstCache.get('HISTORY_VALUE_TYPE').then(function (data) {
      $scope.historyValueTypes = data;
    });


    SystemConstCache.get('SALE_PRODUCT').then(function (data) {
      $scope.saleProductTypes = data;
    });

    $scope.edit = function (itemId) {
      $state.go('agreement.edit', {
        id: itemId,
        query: $scope.query
      });
    };

    //过滤机构
    $scope.querySearchCompany = function (query) {
      var query = {
        filter: query,
        sort: 'id,ASC',
        page: 0,
        size: 10,
        customerStatus: "活跃"
      };
      return UserCompany.toQuery(query).$promise.then(function (data) {
        return data;
      }).catch(function () {
        return null;
      });
    }
    $scope.searchCompanyTextChange = function (text) {
      $scope.agreement.companyId = null;
      $scope.selectedCompany = null;
      $scope.agreement.userAccountId = null;
      $scope.selectedUserAccount = null;
      $scope.searchUserAccount = null;
    }
    $scope.selectedCompanyChange = function (company) {
      if (company && company.id) {
        $scope.agreement.companyId = company.id;
        $scope.loadUserAccount(company.id);
        $scope.loadContract(company.id);
      } else {
        $scope.agreement.companyId = null;
        $scope.userAccounts = [];
        $scope.contracts = [];
        $scope.agreement.userAccountId = null;
        $scope.agreement.contractId = null;
      }
    }
    // 根据 companyId找到账号
    $scope.loadUserAccount = function (companyId) {
      console.log('根据companyId查找userAccounts');
      UserAccount.userAccountByCompany({companyId: companyId}, function (data) { // 根据机构id查找外部账号，因为此处内部账号无需授权
        $scope.userAccounts = data;
        //回显
        $scope.searchUserAccount = null;
        $scope.selectedUserAccount = [];
        angular.forEach($scope.userAccounts, function (obj) {
          if (obj.id == $scope.agreement.userAccountId) {
            $scope.searchUserAccount = obj.accountName;
            $scope.selectedUserAccount = obj;
            return;
          }
        });
      });
    }
    //跳转
    $scope.newUserAccount = function () {
      //console.debug("new user contact");
    }
    $scope.querySearchUserAccount = function (query) {
      var results = query ? $scope.userAccounts.filter($scope.createFilterForUser(query)) : $scope.userAccounts;
      return results;
    }
    $scope.createFilterForUser = function (query) {
      return function filterFn(user) {
        return (user.accountName.indexOf(query) >= 0);
      };
    }
    $scope.searchUserAccountTextChange = function (text) {
      $scope.agreement.userAccountId = null;
      $scope.selectedUserAccount = null;
    }
    $scope.selectedUserAccountChange = function (userAccount) {
      if (userAccount && userAccount.id) {
        $scope.agreement.userAccountId = userAccount.id;
      }
    }
    //加载合同
    $scope.loadContract = function (userCompanyId) {
      Contract.cAll({id: userCompanyId}).$promise.then(function (data) {
        for (var i = 0; i < data.length; i++) {
          if (data[i].endDate) {  //判断合同结束日期是否为空
            if ($scope.compareTime(data[i].endDate))
              $scope.contracts.push(data[i]);
          }
        }
      }).catch(function () {
        $rootScope.showResult("加载失败", "加载账号合同信息失败。", false);
      });
    }
    $scope.compareTime = function (endDate) {
      var currentDate = new Date();
      currentDate = $filter('date')(currentDate, 'yyyy-MM-dd');
      var arr = endDate.split("-");
      var starttime = new Date(arr[0], arr[1], arr[2]);
      var starttimes = starttime.getTime();

      var arrs = currentDate.split("-");
      var lktime = new Date(arrs[0], arrs[1], arrs[2]);
      var lktimes = lktime.getTime();

      if (endDate >= currentDate) {
        return true;
      }
      else
        return false;

    }
    $scope.detailIndex = 0;


    //双击显示表授权明细
    $scope.onItemDblClick = function (e) {
      if ($scope.agreement.systemType == 1 && e.item && e.item.isTable && e.item.isTable == 1) {
        $scope.setTableDetail(e.item);
      }
    }


    //加载产品资源树
    $scope.loadItems = function () {
      $scope.selectedResources = [];
      $scope.userPers = [];
      $scope.detailIndex = 0;
      ProductGroup.productResourceTree({"cpId": $scope.agreement.cpId}).$promise.then(function (data) {
        $scope.items = [data];
        $scope.loadTree($scope.items);
        if ($scope.agreement.details) {
          angular.forEach($scope.agreement.details, function (value, index) {
            var item = IntegralUITreeViewService.findItemById($scope.treeName, 'R.' + value.resourceId);
            item.detailIndex = $scope.detailIndex;
            $scope.userPers[item.detailIndex] = value;
            console.debug("put into userPers,index=" + item.detailIndex + ",obj=" + $scope.userPers[item.detailIndex]);
            $scope.detailIndex = $scope.detailIndex + 1;
            item.text = item.text + "*";
            IntegralUITreeViewService.updateCheckValues($scope.treeName, item);
          });
          $scope.agreement.details = [];
        }
        $scope.loadPackageByCpId();
        $scope.isSelectProduct = true;
      }).catch(function (httpResponse) {
        $rootScope.showResult("加载失败", "加载产品目录失败。", false);
        $scope.isLoading = false;
        $scope.isSelectProduct = false;
      });
    };

    $scope.loadTree = function (data) {
      IntegralUITreeViewService.beginLoad($scope.treeName, null, {type: 'linear', speed: 'veryslow', opacity: 0.25});
      IntegralUITreeViewService.clearItems($scope.treeName);
      IntegralUITreeViewService.suspendLayout($scope.treeName);
      $scope.items.forEach(function (t) {
        addItem(null, t);
      });
        IntegralUITreeViewService.resumeLayout($scope.treeName);
    };

  //添加tree的item
    var addItem = function (parent, data) {
      var item = {};
      if ($scope.agreement.selectedResources && $scope.agreement.selectedResources.indexOf(data.resourceId) >= 0) {
        data.checkState = "checked";
      }// else {
      //  data.checkState = "unchecked";
      //}
      if (data.isGroup == 1) {
        item = {
          id: 'G.' + data.resourceId,
          text: data.resourceName,
          expanded: true
        };
      } else {
        if (data.isTable == 1) {
          item = {
            id: 'R.' + data.resourceId,
            resourceId: data.resourceId,
            resourceName: data.resourceName,
            tableName: data.tableName,
            isTable: 1,
            text: data.resourceName + '(' + data.tableName + ')',
            value: data.resourceId,
            checkState: data.checkState
          };
        } else {
          item = {
            id: 'R.' + data.resourceId,
            resourceId: data.resourceId,
            resourceName: data.resourceName,
            isTable: 2,
            text: data.resourceName,
            value: data.resourceId,
            checkState: data.checkState
          };
        }
      }
      IntegralUITreeViewService.addItem($scope.treeName, item, parent);
      if (data.nodes) {
        data.nodes.forEach(function (t) {
          addItem(item, t);
        });
      }
    };

    //添加tree的item
    var addItem = function (parent, data) {
      var item = {};
      if ($scope.agreement.selectedResources && $scope.agreement.selectedResources.indexOf(data.resourceId) >= 0) {
        data.checkState = "checked";
      }// else {
      //  data.checkState = "unchecked";
      //}
      if (data.isGroup == 1) {
        item = {
          id: 'G.' + data.resourceId,
          text: data.resourceName,
          expanded: true
        };
      } else {
        if (data.isTable == 1) {
          item = {
            id: 'R.' + data.resourceId,
            resourceId: data.resourceId,
            resourceName: data.resourceName,
            tableName: data.tableName,
            isTable: 1,
            text: data.resourceName + '(' + data.tableName + ')',
            value: data.resourceId,
            checkState: data.checkState
          };
        } else {
          item = {
            id: 'R.' + data.resourceId,
            resourceId: data.resourceId,
            resourceName: data.resourceName,
            isTable: 2,
            text: data.resourceName,
            value: data.resourceId,
            checkState: data.checkState
          };
        }
      }
      IntegralUITreeViewService.addItem($scope.treeName, item, parent);
      if (data.nodes) {
        data.nodes.forEach(function (t) {
          addItem(item, t);
        });
      }
    };

    //根据产品ID加载产品包列表
    $scope.loadPackageByCpId = function () {
      ProductPackage.productPackages({"cpId": $scope.agreement.cpId}).$promise.then(function (data) {
        $scope.packages = data;
      }).catch(function (httpResponse) {
        $rootScope.showResult("加载失败", "加载产品目录失败。", false);
      });
    }

    //加载产品包对应的资源项
    $scope.loadPackageItems = function () {
      ProductPackage.queryProductPackageNodesById({id: $scope.agreement.packageId}).$promise.then(function (data) {
        $scope.userPers = [];
        $scope.detailIndex = 0;
        if (data) {
          IntegralUITreeViewService.clearSelection($scope.treeName);
          angular.forEach(data.resourceIds, function (id) {
            var item = IntegralUITreeViewService.findItemById($scope.treeName, 'R.' + id);
            if (item) {
              item.checkState = "checked";
              IntegralUITreeViewService.updateCheckValues($scope.treeName, item);
            }
          });
        }
      }).catch(function (httpResponse) {
        $rootScope.showResult("加载失败", "加载产品包资源失败。", false);
      });
    }

    Array.prototype.diff = function (a) {
      return this.filter(function (i) {
        return a.indexOf(i) < 0;
      });
    };

//加载产品包对应的资源项
    $scope.loadPerPackageItems = function () {

      console.log($scope.agreement.packageId);
      if ($scope.agreement.packageId.length > 0) {
        console.log("loadPackageItems clearSelection");
        $scope.userPers = [];
        $scope.detailIndex = 0;
        IntegralUITreeViewService.clearSelection($scope.treeName);//TODO 目前还没有找到可以一下清除checked为false的方法。
        for (var i = 0; i < $scope.agreement.packageId.length; i++) {
          ProductPackage.queryProductPackageNodesById({id: $scope.agreement.packageId[i]}).$promise.then(function (data) {
            if (data) {
              angular.forEach(data.resourceIds, function (id) {
                var item = IntegralUITreeViewService.findItemById($scope.treeName, 'R.' + id);
                if (item) {
                  item.checkState = "checked";
                  IntegralUITreeViewService.updateCheckValues($scope.treeName, item);
                }
              });
            }
          }).catch(function (httpResponse) {
            $rootScope.showResult("加载失败", "加载产品包资源失败。", false);
          });
        }
      }
    }

    $scope.cancel = function () {
      $state.go('agreement.list', {
        query: $scope.query
      });
    };

//保存
    $scope.save = function (event) {
      event.preventDefault();
      angular.forEach($scope.forms.updateForm.$error.required, function (field) {
        field.$setTouched();
      });
      if ($scope.userPers.length > 0) {
        console.debug("save===>size=" + $scope.userPers.length + ",userPers=" + $scope.userPers);
        $scope.agreement.details = $scope.userPers;
      }
      if ($scope.forms.updateForm.$valid) {
        if ($scope.ctrl.signDate) {
          $scope.agreement.signDate = $filter('date')($scope.ctrl.signDate, 'yyyy-MM-dd');
        }
        if ($scope.ctrl.startDate) {
          $scope.agreement.startDate = $filter('date')($scope.ctrl.startDate, 'yyyy-MM-dd');
        }
        if ($scope.ctrl.endDate) {
          $scope.agreement.endDate = $filter('date')($scope.ctrl.endDate, 'yyyy-MM-dd');
          if ($scope.ctrl.startDate && $scope.ctrl.endDate <= $scope.ctrl.startDate) {
            $rootScope.showResult('错误', '截止日期要大于开始日期，否则权限无法保存', false, false);
            $scope.ctrl.endDate = null;
            return;
          }
        }
        if ($scope.agreement.startDate && $scope.agreement.endDate && $scope.agreement.endDate < $scope.agreement.startDate) {
          $scope.agreement.endDate = angular.copy($scope.agreement.startDate);
        }
        //console.debug("save===>selectedResources="+$scope.selectedResources);
        var checkList = IntegralUITreeViewService.getCheckList($scope.treeName, 'checked');
        var ids = [];
        checkList.forEach(function (item) {
          if (item.value) {
            ids.push(item.value);
          }
        });
        $scope.agreement.selectedResources = $filter('unique')(ids);
        $rootScope.showLoading();
        console.log($scope.agreement);
        if ($scope.agreement.packageId && $scope.agreement.packageId.length > 0)
          $scope.agreement.packageId = ',' + $scope.agreement.packageId.join(",") + ',';
        Agreement.save($scope.agreement).$promise.then(
          function (result) {
            $rootScope.hideLoading();
            if (result.code == '0') {
              $rootScope.showResult('保存成功');
              if ($scope.ctrl.isPush) {
                $rootScope.pushToAllHost('PERMISSION', result.userAccountId, $scope.agreement.systemType);//后台直接推送
                //$rootScope.push(1, false, 'PERMISSION', null, result.userAccountId, '是否推送所选账号的授权信息？')
              }
              $state.go('agreement.list', {
                query: $scope.query
              });
            } else {
              $rootScope.showResult('保存失败', result.message, false, false);
            }
          }
        ).catch(function (httpResponse) {
          $rootScope.showResult('保存失败', null, false, false, httpResponse);
        })
      } else {
        //console.debug("unvalid");
      }
    };
//---------------------------------设置表权限--------------------------------
    $scope.setTableDetail = function (node) {
      $mdDialog.show({
        controller: SetTableDetailController,
        templateUrl: 'table_detail.html',
        parent: angular.element(document.body),
        resolve: {
          userPer: function () {
            var userPer = $scope.findPer(node);
            if (userPer == null) {
              userPer = $scope.createPer(node);
            }
            return userPer;
          },
          conditions: function (SystemConst) {
            return SystemConst.listByTableCondition({tableId: node.resourceId}).$promise;
          }
        },
        clickOutsideToClose: true
      }).then(function (userPer) {
        $scope.updatePer(userPer);
      }, function () {

      });
    };
    $scope.findPer = function (node) {
      var item = IntegralUITreeViewService.findItemById($scope.treeName, 'R.' + node.resourceId);
      console.debug("to load per detail,index=" + item.detailIndex);
      var result = $scope.userPers[item.detailIndex];
      if (result) {
        console.debug("to load per detail,index=" + item.detailIndex + ", obj=" + result);
        result.resourceName = item.resourceName;
        if (item.isTable == 1) {
          result.tableName = item.tableName;
        }
        return result;
      } else {
        return null;
      }
    }
    $scope.createPer = function (node) {
      console.debug("create new per detail to=>" + node.resourceId);
      var userPer = {};
      userPer.resourceId = node.resourceId;
      userPer.resourceName = node.resourceName;
      userPer.tableName = node.tableName;
      userPer.isUpdate = 1;
      if ($scope.ctrl.startDate) {
        userPer.startDate = $filter('date')($scope.ctrl.startDate, 'yyyy-MM-dd');
      }
      if ($scope.ctrl.endDate) {
        userPer.endDate = $filter('date')($scope.ctrl.endDate, 'yyyy-MM-dd');
      }
      userPer.customSql = null;
      userPer.historyValue = null;
      userPer.jsidValue = null;
      //console.debug("create per==>" + userPer);
      return userPer;
    }
    $scope.updatePer = function (userPer) {
      var item = IntegralUITreeViewService.findItemById($scope.treeName, 'R.' + userPer.resourceId);
      var oldPer = $scope.userPers[item.detailIndex];
      if (oldPer) {
        oldPer.isUpdate = userPer.isUpdate;
        oldPer.signedVersion = userPer.signedVersion;
        oldPer.startDate = userPer.startDate;
        oldPer.endDate = userPer.endDate;
        oldPer.conditionValue = userPer.conditionValue;
        oldPer.historyValue = userPer.historyValue;
        oldPer.jsidValue = userPer.jsidValue;
        oldPer.customSql = userPer.customSql;
      } else {
        item.detailIndex = $scope.detailIndex;
        console.debug("create new put into=>" + item.detailIndex);
        $scope.userPers[item.detailIndex] = userPer;
        $scope.detailIndex = $scope.detailIndex + 1;
        item.text = item.text + "*";
        IntegralUITreeViewService.updateCheckValues($scope.treeName, item);
      }
      //console.debug("update pers==>" + $scope.userPers);
    }


    $scope.importSelect = function () {
      $rootScope.showWatchers($scope);
      $mdDialog.show({
        controller: AgreementBatchTableImportCtrl,
        templateUrl: 'agreement_batch_table_import.html',
        parent: angular.element(document.body),
        clickOutsideToClose: true,
        fullscreen: $mdMedia('xs'),
        locals: {
          treeName: $scope.treeName,
          cpId: $scope.agreement.cpId
        },
      }).then(function (result) {

      }, function () {
        $mdDialog.hide();
      });
    }
    $scope.loadImportSelect = function (node) {
      if (node.nodes && node.nodes.length > 0) {
        angular.forEach(node.nodes, function (value, index) {
          $scope.loadImportSelect(value);
        });
      } else {
        if ($scope.importSelectNames.indexOf(node.tableName) >= 0
          || $scope.importSelectNames.indexOf(node.resourceId) >= 0) {
          node.checkState = "checked";
        }
      }
    };

//协议立即终止
    $scope.stop = function () {
      $rootScope.showLoading();
      Agreement.stop({"id": $scope.agreement.id}).$promise.then(
        function (result) {
          $rootScope.hideLoading();
          $rootScope.showResult('操作成功');
          if ($scope.ctrl.isPush) {
            var p = {
              pushType: 'PERMISSION',
              ids: result.userAccount.id
            };
            p.hostIds = '1';
            Push.push(p).$promise.then(function () {  //先推送到hub
              if ($scope.agreement.systemType == 1) {
                p.hostIds = 1;//这里没有实际意思，用于指代是同步程序
              } else {
                p.hostIds = 0;//不是同步程序
              }
              Push.stopPush(p).$promise.then(function (value, responseHeaders) {
                console.log("push successful.");
              }).catch(function (httpResponse) {
                $rootScope.showResult('失败', '推送失败', false, false, httpResponse);
                console.log("push error.");
              });
            }).catch(function (httpResponse) {
              $rootScope.showResult('失败', '推送hub失败', false, false, httpResponse);
            })
            //$rootScope.push(1, false, 'PERMISSION', null, result.userAccount.id, '是否推送所选账号的授权信息？')
          }
          $state.go('agreement.list', {
            query: $scope.query
          });
        }
      ).catch(function (httpResponse) {
        $rootScope.showResult('操作失败', null, false, false);
      })
    };

//------------回显-------------------
    if ($scope.agreement.signDate) {
      $scope.ctrl.signDate = new Date($scope.agreement.signDate);
    } else {
      $scope.ctrl.signDate = new Date();
    }
    if ($scope.agreement.startDate) {
      $scope.ctrl.startDate = new Date($scope.agreement.startDate);
    }
    if ($scope.agreement.endDate) {
      $scope.ctrl.endDate = new Date($scope.agreement.endDate);
    }
    if ($scope.agreement.companyId) {
      UserCompany.get({id: $scope.agreement.companyId}).$promise.then(function (data) {
        $scope.searchCompany = data.companyName;
        $scope.selectedCompany = data;
        $scope.loadUserAccount($scope.agreement.companyId);
      });
    }
    if ($scope.agreement.cpId) {
      $scope.loadItems();
    }
    /*  if ($scope.agreement.userAccountId) {
     $scope.loadContract($scope.agreement.userAccountId);
     }*/
//------------回显 End-------------------

    var initTimer = $timeout(function () {
      IntegralUITreeViewService.updateCheckValues($scope.treeName);
      $timeout.cancel(initTimer);
    }, 1);
  })
;

function ImportSelectController($scope, $mdDialog) {
  $scope.selectNames = "";

  $scope.hide = function () {
    $mdDialog.hide();
  };

  $scope.cancel = function () {
    $mdDialog.cancel();
  };

  $scope.saveSelect = function (ev) {
    $mdDialog.hide($scope.selectNames);
  }
}

function SetTableDetailController($filter, $scope, $mdDialog, userPer, conditions) {
  console.debug(userPer);
  $scope.userPer = userPer;
  $scope.startDate = null;
  $scope.endDate = null;
  $scope.historyValue = null;
  $scope.isUpdate = null;
  $scope.selectedConditions = [];

  if ($scope.userPer.historyValue) {
    $scope.historyValue = new Date($scope.userPer.historyValue);
  }
  if ($scope.userPer.startDate) {
    $scope.startDate = new Date($scope.userPer.startDate);
  }
  if ($scope.userPer.endDate) {
    $scope.endDate = new Date($scope.userPer.endDate);
  }
  if ($scope.userPer.isUpdate == 1) {
    $scope.isUpdate = true;
  } else {
    $scope.isUpdate = false;
  }
  if ($scope.userPer.conditionValue) {
    $scope.selectedConditions = $scope.userPer.conditionValue.split(",");
  }
  //console.debug(conditions);
  $scope.conditions = conditions;

  $scope.hide = function () {
    $mdDialog.hide();
  };

  $scope.cancel = function () {
    $mdDialog.cancel();
  };

  $scope.saveDetail = function (ev) {
    if ($scope.historyValue) {
      $scope.userPer.historyValue = $filter('date')($scope.historyValue, 'yyyy-MM-dd');
    }
    if ($scope.startDate) {
      $scope.userPer.startDate = $filter('date')($scope.startDate, 'yyyy-MM-dd');
    }
    if ($scope.endDate) {
      $scope.userPer.endDate = $filter('date')($scope.endDate, 'yyyy-MM-dd');
    }
    if ($scope.userPer.startDate && $scope.userPer.endDate && $scope.userPer.endDate < $scope.userPer.startDate) {
      $scope.userPer.endDate = angular.copy($scope.userPer.startDate);
    }
    if ($scope.isUpdate) {
      $scope.userPer.isUpdate = 1;
    } else {
      $scope.userPer.isUpdate = 0;
    }
    if ($scope.selectedConditions.length > 0) {
      $scope.userPer.conditionValue = $scope.selectedConditions.join(",");
    }
    $mdDialog.hide($scope.userPer);
  }

}

function AgreementBatchTableImportCtrl($scope, $rootScope, Upload, IntegralUITreeViewService, $mdToast, $mdDialog, $state, treeName, cpId, ProductGroup) {
  $scope.files = [];
  $scope.treeName = treeName;
  $scope.cpId = cpId;

  $scope.selectXls = function (files) {
    $scope.files = [];
    if (files && files.length) {
      $scope.files.push(files[0])
    }
  };

  $scope.showImportResult = function (title, content, isSuccess, isPopup, response) {
    $rootScope.hideLoading();
    if (isSuccess == undefined) {
      isSuccess = true;
    }
    if (isPopup == undefined) {
      isPopup = true;
    }
    if (content == null || content == undefined) {
      content = title
    }
    var theme = isSuccess ? 'success' : 'error';
    if (response != undefined) {
      var result = response.headers('X-App-Params');
      if (result && result != null && result != '') {
        result = decodeURIComponent(result);
        content = result;
      }
    }
    if (isPopup) {
      $mdToast.show(
        $mdToast.simple()
          .content(title).hideDelay(3000).theme(theme)
      );
    } else {
      $mdDialog.show(
        $mdDialog.alert()
          .clickOutsideToClose(false)
          .title(title)
          .htmlContent(content)
          .ariaLabel(title)
          .ok('确定')
        // .theme(theme)
        //.targetEvent(event)
      );
    }
  };

  $scope.uploadXlsx = function () {
    var files = $scope.files;
    var url = 'api/dealBatchTableImport/xlsx';
    if (files && files.length) {
      for (var i = 0; i < files.length; i++) {
        Upload.upload({
          url: url,
          data: {file: files[i], cpId: $scope.cpId}
        }).then(function (resp) {
            //对未找到的表，页面给出明确提示------------------------------------------------
            if (resp.data[2].length > 0) {
              var tableNames = "";
              for (var i = 0; i < resp.data[2].length; i++) {
                if (i == 0) {
                  tableNames += resp.data[2][i];
                } else {
                  tableNames += ('<br>' + resp.data[2][i]);
                }
              }
            }
            //----------------------------------------------------------------------------
            IntegralUITreeViewService.clearSelection($scope.treeName);
            // angular.forEach(resp.data[1], function (id) {
            //   var item = IntegralUITreeViewService.findItemById($scope.treeName, 'R.' + id);
            //   if (item) {
            //     item.checkState = "checked";
            //     IntegralUITreeViewService.updateCheckValues($scope.treeName, item);
            //   }
            // });
            $scope.resourceIdList = resp.data[1];
            $scope.loadItems();
          $rootScope.showResult('上传文件成功。', null, true, true);
          }, function (resp) {

          console.log('Error status: ' + resp.status);
          $rootScope.showResult('失败', '上传文件失败,请检查表是否已经存在。', false, false, resp);
        }, function (evt) {
          $mdDialog.hide();
        })
          .then(function () {
          });
      }
    }
  };

  //----------------------------------------------------------------------------------
  //加载产品资源树
  $scope.loadItems = function () {
    $scope.selectedResources = [];
    $scope.userPers = [];
    $scope.detailIndex = 0;
    ProductGroup.productResourceTree({"cpId": $scope.cpId}).$promise.then(function (data) {
      $scope.items = [data];
      $scope.loadTree($scope.items);
      $scope.isSelectProduct = true;
    }).catch(function (httpResponse) {
      $rootScope.showResult("加载失败", "加载产品目录失败。", false);
      $scope.isLoading = false;
      $scope.isSelectProduct = false;
    });
  };

  $scope.loadTree = function (data) {
    IntegralUITreeViewService.beginLoad($scope.treeName, null, {type: 'linear', speed: 'fast', opacity: 0.25});
    IntegralUITreeViewService.clearItems($scope.treeName);
    IntegralUITreeViewService.suspendLayout($scope.treeName);
    $scope.items.forEach(function (t) {
      addItem(null, t);
    });
    IntegralUITreeViewService.resumeLayout($scope.treeName);
  };

  //添加tree的item
  var addItem = function (parent, data) {
    var item = {};
    if (data.isGroup == 1) {
      item = {
        id: 'G.' + data.resourceId,
        text: data.resourceName,
        expanded: true
      };
    } else {
      if (data.isTable == 1) {
        $scope.addCheckState(data);
        item = {
          id: 'R.' + data.resourceId,
          resourceId: data.resourceId,
          resourceName: data.resourceName,
          tableName: data.tableName,
          isTable: 1,
          text: data.resourceName + '(' + data.tableName + ')',
          value: data.resourceId,
          checkState: data.checkState
        };
      } else {
        $scope.addCheckState(data);
        item = {
          id: 'R.' + data.resourceId,
          resourceId: data.resourceId,
          resourceName: data.resourceName,
          isTable: 2,
          text: data.resourceName,
          value: data.resourceId,
          checkState: data.checkState
        };
      }
    }
    IntegralUITreeViewService.addItem($scope.treeName, item, parent);
    if (data.nodes) {
      data.nodes.forEach(function (t) {
        addItem(item, t);
      });
    }
  };
  //添加checkState值
  $scope.addCheckState = function (data) {
    $scope.resourceIdList.forEach(function (id) {
      if (data.resourceId == id) {
        data.checkState = 'checked';
      }
    });
  };
  //----------------------------------------------------------------------------------

  // $scope.backCheck = function(item){
  //   var pItem = IntegralUITreeViewService.getItemParent($scope.treeName, item);
  //   if (pItem) {
  //     console.log("code-------------------------------------------->");
  //     console.log(pItem);
  //     var checkNum = 0;//组下为"checked"的节点数量
  //     var indeterminateNum = 0;//组下为"indeterminate"的节点数量
  //     var unCheckNum = 0;//组下为"unchecked"或该状态为空的节点数量
  //     angular.forEach(pItem.items, function (items) {
  //       console.log("items is : ");
  //       console.log(items);
  //       if (items.checkState == 'checked')
  //         checkNum++;
  //       else if (items.checkState == 'indeterminate')
  //         indeterminateNum++;
  //       else
  //         unCheckNum++;
  //     });
  //     if (checkNum != 0 && indeterminateNum == 0 && unCheckNum == 0) {//全选状态
  //       pItem.checkState = "checked";
  //     }else if (checkNum == 0 && indeterminateNum == 0 && unCheckNum != 0) {//空选状态
  //     } else {//半选状态
  //       pItem.checkState = "indeterminate";
  //     }
  //     IntegralUITreeViewService.updateCheckValues($scope.treeName, pItem);
  //     $scope.backCheck(pItem)
  //   } else {
  //     console.log("over============================================>");
  //   }
  // }

};
function quickAddController($scope, $mdDialog, $state, $rootScope, CommonUserInfo, ChildrenBaseInfo,ChildrenExtendInfo,DictInfo) {

  $scope.cancel = function () {
    $mdDialog.hide();
  }
//---------------------------账号下拉框-------------------------------------

  $scope.querySearchUserAccount = function (query) {
    var query = {
      accountName: query,
      sort: 'id,ASC',
      page: 0,
      size: 10,
    };
    return UserAccount.toQuery(query).$promise.then(function (data) {
      return data.content;
    }).catch(function () {
      return null;
    });
  }
//--------------------------------------------------------------------
  $scope.search = function (event) {
    event.preventDefault();
    angular.forEach($scope.form.$error.required, function (field) {
      field.$setTouched();
    });
    if ($scope.form.$valid) {
      $mdDialog.hide();
      if ($scope.selectedUserAccount != null) {
        //--------------搜索该账号是否有协议，如果有协议，就取出最大到期时间--------------
        Agreement.quickAdd({
          userAccountId: $scope.selectedUserAccount.id,
          companyId: $scope.selectedUserAccount.companyId
        }).$promise.then(function (data) {
          $state.go('agreement.edit', {
            id: 'new',
            query: $scope.query,
            quickAgreement: data
          });
        });
      }
      else {
        $mdDialog.show(
          $mdDialog.alert()
            .clickOutsideToClose(true)
            .title('警告')
            .textContent('您输入的账户不存在。')
            .ok('确定')
            .targetEvent(event)
        );
      }
    }
    ;
  }
}
function	UpdateEndDateController($scope,$filter, $mdDialog){

  $scope.cancel = function(){
    $mdDialog.cancel();
  }
  $scope.save = function(event){
    event.preventDefault();
    angular.forEach($scope.form.$error.required, function (field) {
      field.$setTouched();
    });
    if($scope.form.$valid){
      if ($scope.endDate) {
        var endDate = $filter('date')($scope.endDate, 'yyyy-MM-dd');
      }
      $mdDialog.hide(endDate);
    }
  }
}
function UpdateNatureController($scope,$filter, $mdDialog,SystemConstCache){

  SystemConstCache.get('AGREEMENT_NATURE').then(function (data) {
    $scope.natureTypes = data;
  });

  $scope.cancel = function(){
    $mdDialog.cancel();
  }
  $scope.save = function(event){
    event.preventDefault();
    angular.forEach($scope.form.$error.required, function (field) {
      field.$setTouched();
    });
    if($scope.form.$valid){
      $mdDialog.hide($scope.nature);
    }
  }
}
