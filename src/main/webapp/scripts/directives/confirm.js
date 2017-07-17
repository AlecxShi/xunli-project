'use strict';

angular.module('app')
  .directive('confirm', function($mdDialog) {
    return {
      priority: 1,
      restrict: 'A',
      scope: {
        confirmIf: '=',
        ngClick: '&',
        confirmTitle: '@',
        confirmOk: '@',
        confirmCancel: '@',
        confirm: '@'
      },
      link: function(scope, element, attrs) {

        element.unbind('click').bind('click', function($event) {

          $event.preventDefault();

          if (angular.isUndefined(scope.confirmIf) || scope.confirmIf) {

            var confirm = $mdDialog.confirm()
              .title(scope.confirmTitle || '确认操作')
              .textContent(scope.confirm || '您确认要执行该操作吗？')
              .ariaLabel(scope.confirmTitle || '确认操作')
              .targetEvent($event)
              .ok(scope.confirmOk || '确认')
              .cancel(scope.confirmCancel || '取消');
            $mdDialog.show(confirm).then(function() {
              scope.$eval(scope.ngClick);
            });
          } else {
            scope.$apply(scope.ngClick);
          }
        });

      }
    };
  });
