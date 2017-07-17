'use strict';

angular.module('app')
  .directive("importSheetJs", function() {
    return {
      restrict:'A',
      scope: { opts: '=',deal: '='},
      link: function ($scope, $elm, $attrs) {
        $scope.opts = $scope.opts || {columnDefs:[],data:[]};
        $elm.on('change', function (changeEvent) {
          if(!changeEvent.target.files[0])
          {
            return;
          }

          if (changeEvent.target.files[0].name.indexOf(".xlsx") < 0 ||
                changeEvent.target.files[0].name.indexOf(".xls") < 0) {
              alert("请选择excel格式文件！");
              return;
          }
          var reader = new FileReader();

          reader.onload = function (e) {
            /* read workbook */
            var bstr = e.target.result;
            var wb = XLSX.read(bstr, {type:'binary'});

            /* grab first sheet */
            var wsname = wb.SheetNames[0];
            var ws = wb.Sheets[wsname];
            var aob = XLSX.utils.sheet_to_formulae(ws);
            /* update scope */
            $scope.$apply(function() {
              $scope.opts.data = aob;
            });
            $scope.deal();
          };

          reader.readAsBinaryString(changeEvent.target.files[0]);
        });
      }
    }
});
