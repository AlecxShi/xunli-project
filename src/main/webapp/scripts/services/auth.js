'use strict';

angular.module('app')
  .factory('AuthServerProvider', function ($rootScope,$http, localStorageService, $window) {
    return {
      login: function(credentials) {
        var data = 'j_username=' + encodeURIComponent(credentials.username) +
          '&j_password=' + encodeURIComponent(credentials.password) +
          '&remember-me=' + credentials.rememberMe + '&submit=Login';
        return $http.post($rootScope.managerUrl + 'api/authentication', data, {
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
          },
          ignoreLoadingBar: true
        }).success(function(response) {
          return response;
        });
      },
      logout: function() {
        // logout from the server
        $http.post($rootScope.managerUrl + 'api/logout').success(function(response) {
          localStorageService.clearAll();
          // to get a new csrf token call the api
          $http.get($rootScope.managerUrl + 'api/account', {
            ignoreLoadingBar: true
          });
          return response;
        });
      },
      getToken: function() {
        var token = localStorageService.get('token');
        return token;
      },
      hasValidToken: function() {
        var token = this.getToken();
        return !!token;
      }
    };
  })
  .factory('Auth', function ($rootScope, $state, $q, Principal, AuthServerProvider, Account, Register, Activate, Password, Reset) {
    return {
      login: function(credentials, callback) {
        var cb = callback || angular.noop;
        var deferred = $q.defer();

        AuthServerProvider.login(credentials).then(function(data) {
          // retrieve the logged account information
          Principal.identity(true).then(function(account) {
            deferred.resolve(data);
          });
          return cb();
        }).catch(function(err) {
          this.logout();
          deferred.reject(err);
          return cb(err);
        }.bind(this));
        return deferred.promise;
      },

      logout: function() {
        AuthServerProvider.logout();
        Principal.authenticate(null);
        // Reset state memory
        $rootScope.previousStateName = undefined;
        $rootScope.previousStateNameParams = undefined;
      },

      authorize: function(force) {
        return Principal.identity(force)
          .then(function(account) {
            $rootScope.account = account;
            $rootScope.isAuthenticated = Principal.isAuthenticated;
            var isAuthenticated = Principal.isAuthenticated();

            // an authenticated user can't access to login and register pages
            if (isAuthenticated && $rootScope.toState.name === 'login') {
              $state.go('dashboard');
            }

            if ($rootScope.toState.data.authorities && $rootScope.toState.data.authorities.length > 0 && !Principal.hasAnyAuthority($rootScope.toState.data.authorities)) {
              if (isAuthenticated) {
                // user is signed in but not authorized for desired state
                $state.go('denied');
              } else {
                // user is not authenticated. stow the state they wanted before you
                // send them to the signin state, so you can return them when you're done
                $rootScope.previousStateName = $rootScope.toState.name;
                $rootScope.previousStateNameParams = $rootScope.toStateParams;

                // now, send them to the signin state so they can log in
                $state.go('login');
              }
            }
          });
      },
      createAccount: function(account, callback) {
        var cb = callback || angular.noop;

        return Register.save(account,
          function() {
            return cb(account);
          },
          function(err) {
            this.logout();
            return cb(err);
          }.bind(this)).$promise;
      },

      updateAccount: function(account, callback) {
        var cb = callback || angular.noop;

        return Account.save(account,
          function() {
            return cb(account);
          },
          function(err) {
            return cb(err);
          }.bind(this)).$promise;
      },

      activateAccount: function(key, callback) {
        var cb = callback || angular.noop;

        return Activate.get(key,
          function(response) {
            return cb(response);
          },
          function(err) {
            return cb(err);
          }.bind(this)).$promise;
      },

      changePassword: function(newPassword, callback) {
        var cb = callback || angular.noop;

        return Password.save(newPassword, function() {
          return cb();
        }, function(err) {
          return cb(err);
        }).$promise;
      },

      resetPasswordInit: function(mail, callback) {
        var cb = callback || angular.noop;

        return Reset.get({
          mail: mail
        }, function() {
          return cb();
        }, function(err) {
          return cb(err);
        }).$promise;
      },

      resetPasswordFinish: function(key, password, callback) {
        var cb = callback || angular.noop;

        return Reset.save({
            key: key,
            password: password
          }, {},
          function() {
            return cb();
          },
          function(err) {
            return cb(err);
          }).$promise;
      }
    };
  })
   .factory('UserProvider', function($resource,$rootScope) {
    return $resource($rootScope.managerUrl + '/api/userInfos/:id', {}, {
      all: {
        url: $rootScope.managerUrl + '/api/userInfos',
        method: 'GET',
        isArray: true
      }
    });
  });
