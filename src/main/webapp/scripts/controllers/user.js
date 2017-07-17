'use strict';

angular.module('app')
  .controller('UserListCtrl', function($rootScope,$scope, $state, $stateParams, $mdToast, User,$mdDialog,ngDialog,dialogs) {

    var bookmark;
    $scope.selected = [];
    $scope.selectedTreeNode = null;
    $scope.expandedData = [];
    $scope.node1 = undefined;

    $scope.filter = {
      options: {
        debounce: 500
      }
    };

    $scope.filters ={
		addDir : false,
		editDir: false,
		delDir : false,
		editCus: true,
		delCus: true,
		addPermission: false
    }
    
    $scope.query = {
      filter: '',
      order: '-ID',
      page: 1,
      size: 50
    };
    

    $scope.showSelected = function(node,parentNode){
    	$scope.filters.addDir = true;
    	$scope.filters.editDir = true;
    	$scope.filters.delDir = true;
    	$scope.filters.editCus = true;
    	$scope.filters.delCus = true;
    	$scope.filters.addPermission = true;
    	$scope.selectedTreeNode = node;
    	$scope.selectedTreeParentNode = parentNode;
    	$scope.query.customerid = null;
    	$scope.query.directoryid = null;
    	if(node.isSub == 0){
    		$scope.query.customerid = node.name;
        	$scope.filters.editDir = false;
        	$scope.filters.delDir = false;
        	$scope.filters.addPermission = false;
    	}else{
    		$scope.query.directoryid = node.name;
    		$scope.filters.addDir = false;
    		$scope.filters.editCus = false;
        	$scope.filters.delCus = false;
    	}
    	$scope.onChange();
    }
    
    $scope.treeOptions = {
    	    nodeChildren: "children",
    	    dirSelectable: true,
    	    /*defaultExpanded: function(node) {
    	        // your code here
    	        return true;
    	    },*/
    	    injectClasses: {
    	        ul: "a5",
    	        li: "a5",
    	        liSelected: "a7",
    	        iExpanded: "a3",
    	        iCollapsed: "a4",
    	        iLeaf: "a5",
    	        label: "a6",
    	        labelSelected: "a8"
    	    }
    }
    
    
    //刷新客户目录菜单树
    $scope.refreshTree = function(){
    	User.queryCustomerDir({
    		filter : '',
    		name: $scope.query.filter
    	},function(value, responseHeaders) {
    		$scope.dataForTheTree = [];
    		angular.copy(value,$scope.dataForTheTree);
    		for(var i in $scope.dataForTheTree){
    			$scope.expandedData.push($scope.dataForTheTree[i]);
    			if(i == 0){
    				$scope.node1 = $scope.dataForTheTree[i];
    				$scope.query.customerid = $scope.node1.name;
    			}
    		}
      }).$promise.then(function(){
    	  $scope.onChange();
      });
    }
    
    //添加客户
    $scope.addCustomer = function(){
    	$mdDialog.show({
            controller: CustomerDialogController,
            templateUrl: 'customer_add.html',
            parent: angular.element(document.body),
            resolve: {
              /*customerName: function(){
            	  var customerName = '';
            	  angular.copy(scope.$modelValue, customerName);
            	  return customerName;
              }*/
            },
            clickOutsideToClose:true
          }).then(function(result) {
        	  	$scope.refresh();
          }, function() {

          });
    }
   
    //删除客户
    $scope.deleteCustomer = function(ev){
    	if($scope.selectedTreeNode != null && $scope.selectedTreeNode.isSub == 0){
    		var customerID = $scope.selectedTreeNode.name;
    		var customerName = $scope.selectedTreeNode.desc;
    		
    		User.deleteCustomer({
				customerID: customerID
			}).$promise.then(function() {
				$mdToast.show(
						$mdToast.simple().content('客户【' + customerName + '】删除成功。').hideDelay(3000).theme('success')
				);
				$scope.refresh();
			}).catch(function(httpResponse) {
				$mdToast.show(
						$mdToast.simple().content('客户【' + customerName + '】删除失败。').hideDelay(3000).theme('error')
				);
			});
    		$scope.selectedTreeNode = null;
    	}
    }
    
    //修改客户信息
    $scope.editCustomer = function(scope){
    	if($scope.selectedTreeNode != null && $scope.selectedTreeNode.isSub == 0){
    		$mdDialog.show({
    			controller: EditCustomerDialogController,
    			templateUrl: 'customer_edit.html',
    			parent: angular.element(document.body),
    			resolve: {
    				customer: function(){
    					var customer = {};
    					angular.copy($scope.selectedTreeNode, customer);
    					return customer;
    				}
    			},
    			clickOutsideToClose:true
    		}).then(function(result) {
    			$scope.refresh();
    		}, function() {
    			
    		});
    	}
      }
    
    //添加客户目录
    $scope.addCustomerDir = function(){
    	if($scope.selectedTreeNode != null && $scope.selectedTreeNode.isSub == 0){
	    	$mdDialog.show({
	            controller: CustomerDirDialogController,
	            templateUrl: 'customer_dir_add.html',
	            parent: angular.element(document.body),
	            resolve: {
	              customer: function(){
	            	  var customer = {};
	            	  angular.copy($scope.selectedTreeNode, customer);
	            	  return customer;
	              }
	            },
	            clickOutsideToClose:true
	          }).then(function(result) {
	        	  $scope.refresh();
	          }, function() {
	
	          });
    	}
    }
    
    //删除客户目录
    $scope.deleteCustomerDir = function(ev){
    	if($scope.selectedTreeNode != null && $scope.selectedTreeNode.isSub == 1){
    		var DirectoryID = $scope.selectedTreeNode.name;
    		var DirectoryName = $scope.selectedTreeNode.desc;
    		
    		User.deleteCustomerDir({
				DirectoryID: DirectoryID
			}).$promise.then(function() {
				$mdToast.show(
						$mdToast.simple().content('客户目录【' + DirectoryName + '】删除成功。').hideDelay(3000).theme('success')
				);
				$scope.refresh();
	    		$scope.selectedTreeNode = null;
			}).catch(function(httpResponse) {
				$mdToast.show(
						$mdToast.simple().content('客户目录【' + DirectoryName + '】删除失败。').hideDelay(3000).theme('error')
				);
			});
    	}
    }
    
    //修改客户目录信息
    $scope.editCustomerDir = function(scope){
    	if($scope.selectedTreeNode != null && $scope.selectedTreeNode.isSub == 1){
	        $mdDialog.show({
	          controller: EditCustomerDirDialogController,
	          templateUrl: 'customer_dir_edit.html',
	          parent: angular.element(document.body),
	          resolve: {
	            customer_dir: function(){
	              var customer = {};
	              var directory = {};
	              angular.copy($scope.selectedTreeNode, directory);
	              angular.copy($scope.selectedTreeParentNode, customer);
	              directory.ifUse = directory.ifUse == 'Y' ? true : false;
	              return {customer:customer,directory:directory};
	            }
	          },
	          	clickOutsideToClose:true
	        }).then(function(result) {
	        	$scope.refresh();
	        }, function() {
	
	        });
    	}
      }
    
    //添加用户任务权限
    $scope.addTaskPermission = function(){
    	if($scope.selectedTreeNode != null && $scope.selectedTreeNode.isSub == 1){
    		var param = {
    				data:{
    					
    				}
    		};
            angular.copy($scope.selectedTreeNode, param.data);
            param.refresh = $scope.onChange;
	    	dialogs.create('views/user.task.permission.html',AddCustomerTaskPermission,
	    			param,
	    			{size:'lg',keyboard: true}).result.then(function(){
	    											
											},function(){
												
											});
    	}
    }
    
    //删除用户任务权限
    $scope.deleteTask = function(event){
    	var data = [];
    	for(var i in $scope.selected){
    		data[i] = $scope.selected[i]['id'];
    	}
    	User.delTaskPermission({
    		taskList: data
    	}).$promise.then(
				function(result, responseHeaders) {
			        $scope.error = null;
			        $scope.success = 'OK';
			        $mdToast.show(
			          $mdToast.simple().content('目录任务删除成功。').hideDelay(3000).theme('success')
			        );
			        $scope.onChange();
				}
	    		).catch(function(httpResponse) {
			        $scope.error = 'ERROR';
			        $scope.success = null;
			        $mdToast.show(
			          $mdToast.simple().content('目录任务删除失败。').hideDelay(3000).theme('error')
			        );
	      });
    	$scope.selected=[];//选中行重置
    }
    
    
    //批量运行选中的任务
    $scope.runBatchTask= function(event){
    	var taskids = [];
    	for(var i in $scope.selected){
    		taskids[i] = $scope.selected[i]['cfgTaskInfo']['taskID'];
    	}
    	
    	$mdDialog.show({
  	      controller: TaskRunController,
  	      templateUrl: 'views/taskRun.html',
  	      parent: angular.element(document.body),
  	      clickOutsideToClose:true,
  	      resolve : {
		  	 ids : function() {
		  		 return taskids;
		  	 }
  	      }
  	    });
    }
    
    $scope.onChange = function() {
    	console.log($scope);
	      var sort = $scope.query.order;
	      if (sort && sort.length > 0) {
	        if (sort.charAt(0) === '-') {
	          sort = sort.substring(1) + ',desc';
	        } else {
	          sort = sort + ',asc';
	        }
	      }
	      return User.pageQuery({
		    	  page: $scope.query.page - 1,
		          size: $scope.query.size,
		          sort: sort,
		          filter: $scope.query.filter,
		          customerid: $scope.query.customerid == null ? '' : $scope.query.customerid,
		          directoryid: $scope.query.directoryid == null ? '' : $scope.query.directoryid
	      },function(value, responseHeaders) {
		        $scope.data1 = value.content;
		        $scope.page = value;
	      }).$promise;
    };
    
    $scope.removeFilter = function() {
      $scope.filter.show = false;
      $scope.query.filter = '';

      if ($scope.filter.form.$dirty) {
        $scope.filter.form.$setPristine();
      }
    };

    $scope.refresh = function(page, limit) {
    	if(limit != undefined && limit !=  null)
    	{
    	  $scope.query.page = page;
    	  $scope.query.size = limit;
    	}
      $scope.promise = $scope.onChange();
      $scope.refreshTree();
    };
    
    $scope.view = function(item) {
    	$state.go('taskedit', {
    		id : item.cfgTaskInfo.taskID,
    		toWeb : 'useList',
    		query : $scope.query
    	});
      };
      
      $scope.refresh();
  });

function CustomerDialogController($scope, $mdToast,$mdDialog,User) {
	  $scope.hide = function() {
	    $mdDialog.hide();
	  };
	
	  $scope.cancel = function() {
	    $mdDialog.cancel();
	  };
	
	  $scope.ok = function(result) {
	    $mdDialog.hide();
	  };
	
	  $scope.save = function(ev){
	    User.addCustomer({
	    		customerName:$scope.customer.customerName
	    }).$promise.then(
	      function(result, responseHeaders) {
	        $scope.error = null;
	        $scope.success = 'OK';
	        $mdToast.show(
	          $mdToast.simple().content('添加成功。').hideDelay(3000).theme('success')
	        );
	      }
	    ).catch(function(httpResponse) {
	        $scope.error = 'ERROR';
	        $scope.success = null;
	        $mdToast.show(
	          $mdToast.simple().content('添加失败。').hideDelay(3000).theme('error')
	        );
	      });
	  }
};

function EditCustomerDialogController($scope, $mdToast,$mdDialog,User,customer) {
	  $scope.edit_customer = customer;
	  $scope.hide = function() {
	    $mdDialog.hide();
	  };
	
	  $scope.cancel = function() {
	    $mdDialog.cancel();
	  };
	
	  $scope.ok = function(result) {
	    $mdDialog.hide();
	  };
	
	  $scope.save = function(ev){
	    User.editCustomer({
	    		customerID:$scope.edit_customer.name,
	    		customerName:$scope.edit_customer.desc
	    }).$promise.then(
	      function(result, responseHeaders) {
	        $scope.error = null;
	        $scope.success = 'OK';
	        $mdToast.show(
	          $mdToast.simple().content('修改成功。').hideDelay(3000).theme('success')
	        );
	      }
	    ).catch(function(httpResponse) {
	        $scope.error = 'ERROR';
	        $scope.success = null;
	        $mdToast.show(
	          $mdToast.simple().content('修改失败。').hideDelay(3000).theme('error')
	        );
	      });
	  }
};

function CustomerDirDialogController($scope, $mdToast,$mdDialog,User,customer) {
	  $scope.customer = customer;
	 //默认启用
	  $scope.directory = {
			  ifUse : true
			  
	  }
	  $scope.hide = function() {
	    $mdDialog.hide();
	  };
	
	  $scope.cancel = function() {
	    $mdDialog.cancel();
	  };
	
	  $scope.ok = function(result) {
	    $mdDialog.hide();
	  };
	
	  $scope.save = function(ev){
		var param = {
	    		CustomerID:$scope.customer.name,
	    		DirectoryName:$scope.customer.directoryName,
	    		IfUse:$scope.directory == undefined ? 'N' :  $scope.directory.ifUse == undefined ? 'N' : !$scope.directory.ifUse ? 'N' : 'Y'
	    };
	    User.addCustomerDir({
	    		CustomerID:$scope.customer.name,
	    		DirectoryName:$scope.customer.directoryName,
	    		IfUse:$scope.directory == undefined ? 'N' :  $scope.directory.ifUse == undefined ? 'N' : !$scope.directory.ifUse ? 'N' : 'Y'
	    }).$promise.then(
	      function(result, responseHeaders) {
	        $scope.error = null;
	        $scope.success = 'OK';
	        $mdToast.show(
	          $mdToast.simple().content('添加成功。').hideDelay(3000).theme('success')
	        );
	      }
	    ).catch(function(httpResponse) {
	        $scope.error = 'ERROR';
	        $scope.success = null;
	        $mdToast.show(
	          $mdToast.simple().content('添加失败。').hideDelay(3000).theme('error')
	        );
	      });
	  }
};

function EditCustomerDirDialogController($scope, $mdToast,$mdDialog,User,customer_dir) {
	  $scope.edit_customer_dir = customer_dir;
	  $scope.hide = function() {
	    $mdDialog.hide();
	  };
	
	  $scope.cancel = function() {
	    $mdDialog.cancel();
	  };
	
	  $scope.ok = function(result) {
	    $mdDialog.hide();
	  };
	
	  $scope.save = function(ev){
		var param = {
	    		DirectoryID: $scope.edit_customer_dir.directory.name,
	    		CustomerID: $scope.edit_customer_dir.customer.name,
	    		DirectoryName: $scope.edit_customer_dir.directory.desc,
	    		IfUse: $scope.edit_customer_dir.directory.ifUse ? 'Y' : 'N'
	    };
	    User.editCustomerDir(param).$promise.then(
	      function(result, responseHeaders) {
	        $scope.error = null;
	        $scope.success = 'OK';
	        $mdToast.show(
	          $mdToast.simple().content('修改成功。').hideDelay(3000).theme('success')
	        );
	      }
	    ).catch(function(httpResponse) {
	        $scope.error = 'ERROR';
	        $scope.success = null;
	        $mdToast.show(
	          $mdToast.simple().content('修改失败。').hideDelay(3000).theme('error')
	        );
	      });
	  }
};

function AddCustomerTaskPermission($scope,$uibModalInstance,$mdToast,User,data){
	$scope.data = data.data;
	$scope.refresh = data.refresh;
	var bookmark;
	$scope.task = {
			selected: [],
			filter: {
						options: {
							debounce: 500
						}
			}
			
	};

    $scope.task.query = {
        filter: '',
        order: '-taskID',
        page: 1,
        size: 50
    };
	$scope.onChange = function() {
	      var sort = $scope.task.query.order;
	      if (sort && sort.length > 0) {
	        if (sort.charAt(0) === '-') {
	        	sort = sort.substring(1) + ',desc';
	        } else {
	        	sort = sort + ',asc';
	        }
	      }
	      return User.pageQueryTask({
		    	  page: $scope.task.query.page - 1,
		          size: $scope.task.query.size,
		          sort: sort,
		          filter: $scope.task.query.filter,
		          directoryid: $scope.data.name
	      },function(value, responseHeaders) {
		        $scope.task.data1 = value.content;
		        $scope.task.page = value;
	      }).$promise;
	};
  
	 $scope.task.refresh = function(page,limit) {
		 if(limit != undefined && limit !=  null)
	    	{
	    	  $scope.task.query.page = page;
	    	  $scope.task.query.size = limit;
	    	}
	    	$scope.promise = $scope.onChange();
	 };
	
    $scope.$watch('task.query.filter', function(newValue, oldValue) {
	      if (!oldValue) {
	        bookmark = $scope.task.query.page;
	      }
	
	      if (newValue !== oldValue) {
	    	  $scope.task.query.page = 1;
	      }
	
	      if (!newValue) {
	    	  $scope.task.query.page = bookmark;
	      }
	      $scope.task.refresh();
	});
	  
	$scope.removeFilter = function() {
      $scope.task.query.filter = '';

      if ($scope.filter.form.$dirty) {
    	  $scope.filter.form.$setPristine();
      }
    };

	$scope.save = function(){
		var taskList = [];
		for(var item in $scope.task.selected){
			taskList[item] = $scope.task.selected[item]['taskID'];
		}
		
		User.addDirectoryTask({
			directoryID:$scope.data.name,
			taskList:taskList
		}).$promise.then(
				function(result, responseHeaders) {
			        $scope.error = null;
			        $scope.success = 'OK';
			        $mdToast.show(
			          $mdToast.simple().content('目录任务添加成功。').hideDelay(3000).theme('success')
			        );
			        $scope.refresh();
				}
	    		).catch(function(httpResponse) {
			        $scope.error = 'ERROR';
			        $scope.success = null;
			        $mdToast.show(
			          $mdToast.simple().content('目录任务添加失败。').hideDelay(3000).theme('error')
			        );
	      });
		$uibModalInstance.close($scope.data);
	}; 
	
	$scope.cancel = function(){
		$uibModalInstance.dismiss('Canceled');
	}; // end cancel
	
}

function DeleteCustomerTaskPermission($scope,$uibModalInstance,$mdToast,User,data){
	$scope.data = data;
	var bookmark;
	$scope.task = {
			selected: [],
			filter: {
						options: {
							debounce: 500
						}
			}
			
	};

    $scope.task.query = {
        filter: '',
        order: '-taskID',
        page: 1,
        size: 5
    };
	$scope.onChange = function() {
	      var sort = $scope.task.query.order;
	      if (sort && sort.length > 0) {
	        if (sort.charAt(0) === '-') {
	        	sort = sort.substring(1) + ',desc';
	        } else {
	        	sort = sort + ',asc';
	        }
	      }
	      return User.pageQueryTask({
		    	  page: $scope.task.query.page - 1,
		          size: $scope.task.query.size,
		          sort: sort,
		          filter: $scope.task.query.filter,
		          TaskName: $scope.task.name == null ? '' : $scope.task.name
	      },function(value, responseHeaders) {
		        $scope.task.data1 = value.content;
		        $scope.task.page = value;
	      }).$promise;
	};
  
  	$scope.onChange();
	
    $scope.$watch('task.name', function(newValue, oldValue) {
	      if (!oldValue) {
	        bookmark = $scope.task.query.page;
	      }
	
	      if (newValue !== oldValue) {
	        $scope.task.query.page = 1;
	      }
	
	      if (!newValue) {
	        $scope.task.query.page = bookmark;
	      }
	      $scope.task.refresh();
	});
	  
	$scope.removeFilter = function() {
      $scope.task.filter.show = false;
      $scope.task.query.filter = '';

      if ($scope.filter.form.$dirty) {
        $scope.filter.form.$setPristine();
      }
    };

    $scope.task.refresh = function() {
    	$scope.promise = $scope.onChange();
    };
    
	$scope.save = function(){
		var taskList = [];
		for(var item in $scope.task.selected){
			taskList[item] = $scope.task.selected[item]['taskID'];
		}
		
		User.addDirectoryTask({
			directoryID:data.name,
			taskList:taskList
		}).$promise.then(
				function(result, responseHeaders) {
			        $scope.error = null;
			        $scope.success = 'OK';
			        $mdToast.show(
			          $mdToast.simple().content('目录任务添加成功。').hideDelay(3000).theme('success')
			        );
				}
	    		).catch(function(httpResponse) {
			        $scope.error = 'ERROR';
			        $scope.success = null;
			        $mdToast.show(
			          $mdToast.simple().content('目录任务添加失败。').hideDelay(3000).theme('error')
			        );
	      });
		$scope.task.selected = [];//重置选中行
		$uibModalInstance.close($scope.data);
	}; 
	
	$scope.cancel = function(){
		$uibModalInstance.dismiss('Canceled');
	}; // end cancel
	
	$scope.hitEnter = function(evt){
		if(angular.equals(evt.keyCode,13) && !(angular.equals($scope.task.name,null) || angular.equals($scope.task.name,'')))
			$scope.onChange();
	};
}