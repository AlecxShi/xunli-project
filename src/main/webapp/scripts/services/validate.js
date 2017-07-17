'use strict';

angular.module('app')
  .factory('Validate', function ($resource,$rootScope) {
    return $resource($rootScope.managerUrl + 'api/validate', {}, {
      usernameNotExist: {
        url: $rootScope.managerUrl + 'api/validate/user/username/unique',
        method: 'POST'
      },
      emailNotExist: {
        url: $rootScope.managerUrl + 'api/validate/user/email/unique',
        method: 'POST'
      },
      phoneNotExist: {
        url: $rootScope.managerUrl + 'api/validate/user/phone/unique',
        method: 'POST'
      }
    });
  });
