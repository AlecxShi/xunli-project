'use strict';

angular.module('app')
  .factory('systemUpgrade', function($resource,$rootScope) {
    return $resource($rootScope.managerUrl + 'api/UpgradeInfo/:id', {}, {
      pageQuery: {
          url: $rootScope.managerUrl + 'api/UpgradeInfo',
          method: 'GET',
       },
       getUpgradeInfo : {
			url : $rootScope.managerUrl + 'api/getUpgradeInfo',
			method : 'GET'
		}
    });
  });
