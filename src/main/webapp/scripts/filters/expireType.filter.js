'use strict';

angular.module('app').filter('expireType', function() {
        var enums = {
          '12': '12小时',
          '1': '一天',
          '2': '一星期',
          '3': '一个月',
          '4': '一年',
          '99': '永久'
        };
        return function(input) {
          return enums[input];
        };
  });