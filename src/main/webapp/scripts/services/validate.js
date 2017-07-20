'use strict';

angular.module('app')
  .factory('Validate', function ($resource,$rootScope) {
    return $resource('api/validate', {}, {
      usernameNotExist: {
        url: 'api/validate/user/username/unique',
        method: 'POST'
      },
      emailNotExist: {
        url: 'api/validate/user/email/unique',
        method: 'POST'
      },
      phoneNotExist: {
        url: 'api/validate/user/phone/unique',
        method: 'POST'
      }
    });
  });
