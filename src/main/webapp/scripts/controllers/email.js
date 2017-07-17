'use strict';

angular.module('app')
  .controller('emailCtrl', function($scope, $state, $stateParams, $mdToast,$mdDialog, EmailInfo) {

    var bookmark;

    $scope.selected = [];

    $scope.filter = {
      options: {
        debounce: 500
      }
    };

    $scope.query = {
      filter: '',
      order: '-emailAddress',
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
      
      
      return EmailInfo.get({
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
	
	$scope.addemail = function(ev) {

		$mdDialog.show({
  	      controller: EmailEditController,
  	      templateUrl: 'email_edit.html',
  	      parent: angular.element(document.body),
  	      clickOutsideToClose:true,
  	      resolve : {
  				email : function() {
  					  return {};
  				},
  	            isNew:
  	            	function() {
    					  return true;
    				}
  			}
  	    })
  	    .then(function() {
  	    	$scope.promise = $scope.refresh();
  	    }, function() {
  	    	
  	    });
    };
    
    $scope.deleteemail = function(ev) {
    	  
    	  var ids = [];
	      $scope.selected.forEach(function(item, index) {
	    	  ids.push(item.emailID);
	      });
	      EmailInfo.remove({
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
    
    $scope.editemail = function(item) {

    	$mdDialog.show({
    	      controller: EmailEditController,
    	      templateUrl: 'email_edit.html',
    	      parent: angular.element(document.body),
    	      clickOutsideToClose:true,
    	      resolve : {
    	    	  email : function($stateParams) {
    					 return EmailInfo.get({
    							id : item.emailID
    						}).$promise;
    				       },
       	            isNew:
       	            	function() {
         					  return false;
         				}
    			}
    	    })
    	    .then(function() {
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


function EmailEditController($scope, $mdToast,$mdDialog, email,EmailInfo,isNew) {
	  $scope.email = email;
	  $scope.emailerror = "";
	  
	  var emailList;
	  var num;
	  EmailInfo.get({}, function(value, responseHeaders) {
		  emailList = value.content;
		  if(isNew==false)
		  for(var i = 0,vlen =  emailList.length; i < vlen; i++)
				 { 
				   if(emailList[i].emailAddress == $scope.email.emailAddress)
					 { 
						 num = i;
						 break;
				     } 
				 }  
		});

	  
	  $scope.hide = function() {
	     $mdDialog.hide();
	  };

	  $scope.cancel = function() {
	     $mdDialog.cancel();
	  };

	  $scope.save = function(ev){
		  $scope.emailerror = "";
		  event.preventDefault();
	      angular.forEach($scope.form.$error.required, function(field) {
	        field.$setTouched();
	      });

	      if ($scope.form.$valid)
	    	  {
				 if(isNew==true)
					 {
						 for(var i = 0,vlen =  emailList.length; i < vlen; i++)
							 { 
							   if(emailList[i].emailAddress == $scope.email.emailAddress)
								 { 
									 $scope.emailerror = "email重复";
							         return;
							     } 
							 } 
					 }
				 else
					 {
						 for(var i = 0,vlen =  emailList.length; i < vlen; i++)
							 { 
							   if(emailList[i].emailAddress == $scope.email.emailAddress)
								 { 
									 if(num != i)
										 {
											 $scope.emailerror = "email重复";
									         return;
										 }
							     } 
							 }
					 }
				 
				 EmailInfo.save($scope.email).$promise.then(
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
