'use strict';

angular.module('app')
  .factory('User', function($resource,$rootScope) {
    return $resource($rootScope.managerUrl + '/api/user/:id', {}, {
      all: {
        url: $rootScope.managerUrl + 'api/users',
        method: 'GET',
        isArray: true
      },
      pageQuery: {
          url: $rootScope.managerUrl + 'api/Customer/PageQuery',
          method: 'GET',
       },
       queryCustomerDir: {
           url: $rootScope.managerUrl + 'api/Customer/Directory',
           method: 'GET',
           isArray: true
        },
        queryCustomerDirBytaskID: {
            url: $rootScope.managerUrl + 'api/Customer/DirectoryBytaskID',
            method: 'GET',
            isArray: true
         },
        addCustomer: {
        	url: $rootScope.managerUrl + 'api/Customer/addCustomer',
            method: 'POST'
        },
        deleteCustomer: {
        	url: $rootScope.managerUrl + 'api/Customer/deleteCustomer',
            method: 'DELETE',
        },
        editCustomer: {
        	url: $rootScope.managerUrl + 'api/Customer/editCustomer',
            method: 'POST',
        },
        addCustomerDir: {
        	url: $rootScope.managerUrl + 'api/Customer/addCustomerDir',
            method: 'POST',
        },
        deleteCustomerDir: {
        	url:$rootScope.managerUrl +  'api/Customer/delCustomerDir',
            method: 'DELETE',
        },
        editCustomerDir: {
        	url: $rootScope.managerUrl + 'api/Customer/editCustomerDir',
            method: 'POST',
        },
        pageQueryTask:{
        	url: $rootScope.managerUrl + 'api/Customer/PageQueryTask',
            method: 'GET',
        },
        addDirectoryTask:{
        	url: $rootScope.managerUrl + 'api/Customer/addDirectoryTask',
            method: 'POST',
        },
        delTaskPermission:{
        	url: $rootScope.managerUrl + 'api/Customer/delDirectoryTask',
            method: 'DELETE',
        },
        runTask:{
        	url: $rootScope.managerUrl + 'api/RunTask/:taskids',
        	method: 'DELETE'
        }
    });
  });
