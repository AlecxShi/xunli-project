/**
 * 
 */
'use strict';

angular.module('app').factory('SysInfo', function($resource,$rootScope) {
	return $resource($rootScope.managerUrl + 'api/SysInfo/:id', {}, {
		all : {
			url :'api/SysInfo',
			method : 'GET',
			isArray : true
		}
	
	});
});