/**
 * 
 */
'use strict';

angular.module('app').factory('SysInfo', function($resource,$rootScope) {
	return $resource($rootScope.managerUrl + 'api/SysInfo/:id', {}, {
		all : {
			url :$rootScope.managerUrl +  'api/SysInfo',
			method : 'GET',
			isArray : true
		}
	
	});
});