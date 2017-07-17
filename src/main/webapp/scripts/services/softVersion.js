/**
 * 
 */

'use strict';

angular.module('app').factory('SoftVersion', function($resource,$rootScope) {
	return $resource($rootScope.managerUrl + 'api/version', {}, {
		getversion : {
			url : $rootScope.managerUrl + 'api/version',
			method : 'GET',
			isArray : false
		}
	});
});