'use strict';

angular.module('app')
  .filter('trustAsHtml', function($sce) {
    return $sce.trustAsHtml;
  })
  .filter('health', function() {
    var enums = {
      'UP': '正常',
      'DOWN': '异常',
      'OUT_OF_SERVICE': '停止服务',
      'UNKNOWN': '未知'
    };
    return function(input) {
      return enums[input];
    };
  })
  .filter('hostType', function(){
    var enums = {
      'DD': '数据字典',
      'DATA_SYNC': '同步程序',
      'GILDATA_TERMINAL': '聚源终端',
      'REQUEST': '外部系统',
      'HUB': 'HUB系统',
    };
    return function(input) {
      return enums[input];
    };
  })
  .filter('pushType', function(){
    var enums = {
      'UPDATE_ALL': '更新全部',
      'UPDATE': '更新',
      'DELETE_ALL': '删除全部',
      'DELETE': '删除',
      'CHANGE_PASSWORD': '修改密码',
      'RESET_LICENSE': '重置许可'
    };
    return function(input) {
      return enums[input];
    };
  })
  .filter('operateType', function(){
    var enums = {
      '1': '新增',
      '2': '更新',
      '3': '删除'
    };
    return function(input) {
      return enums[input];
    };
  })
  .filter('moduleType', function(){
    var enums = {
      'ACCOUNT' : '账号',
      'CUSTOMER' : '客户',
      'PRODUCT' : '产品',
      'GROUP' : '产品组',
      'GROUP_TABLE' : '组表',
      'TABLE' : '表',
      'DB_TYPE' : '数据类型',
      'TABLE_STRUCT' : '表结构',
      'TABLE_DICT' : '表字典',
      'AGREEMENT' : '协议',
      'SYSTEM_CONFIG' : '系统配置',
      'SYSTEM_CONST' : '系统常量',
      'HOST' : '服务器',
      'USER' : '用户',
      'ROLE' : '角色',
      'AUTHORITY' : '权限',
    };
    return function(input) {
      return enums[input];
    };
  })
  ;
