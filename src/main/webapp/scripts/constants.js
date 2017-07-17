'use strict';

angular.module('app')
  .constant('USER_ROLES', {
    all: '*',
    admin: 'ROLE_ADMIN',
    manager: 'ROLE_MANAGER',
    user: 'ROLE_USER'
  })
  .constant('APP_SETTINGS', {
    defaultPageSize: 10
  });
