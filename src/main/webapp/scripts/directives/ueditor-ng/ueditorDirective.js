'use strict';
angular.module('app')
    .directive('ueditor',function () {
         return{
             restrict: 'EA',
             require: 'ngModel',
             scope: {
                 height: '@?'
             },
             link: function (scope, element, attr, ctrl) {
                 var _self = this,
                     _initContent,
                     editor,
                     editorReady = false,
                     baseURL = "/scripts/directives/ueditor-ng/ueditor/"; //写你的ue路径
                 console.log(baseURL,UE.ui);
                 var fexUE = {
                     initEditor: function () {
                         var _self = this;
                         if (typeof UE != 'undefined') {
                             editor = new UE.ui.Editor({
                                 initialContent: _initContent,
                                 toolbars: [
                                     ['source', 'undo', 'redo', 'bold', 'italic',  'removeformat', 'formatmatch', 'autotypeset', 'blockquote',
                                         'pasteplain', '|', 'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist']
                                 ],
                                 initialFrameHeight:scope.height || 120,
                                 autoHeightEnabled:false,
                                 wordCount:false,
                                 elementPathEnabled: false
                             });


                             editor.render(element[0]);
                             editor.ready(function () {
                                 editorReady = true;
                                 _self.setContent(_initContent);

                                 editor.addListener('contentChange', function () {
                                     scope.$apply(function () {
                                         ctrl.$setViewValue(editor.getContent());
                                     });
                                 });
                             });
                         } else {

                             addScript(baseURL + 'ueditor.config.js');
                             addScript(baseURL + 'ueditor.all.min.js', function(){
                                 _self.initEditor();
                             })
                         }
                     },
                     setContent: function (content) {
                         if (editor && editorReady) {
                             editor.setContent(content);
                         }
                     }
                 };

                 /**
                  * 当Model改变值得时候赋值。
                  */
                 ctrl.$render = function () {
                     _initContent = ctrl.$isEmpty(ctrl.$viewValue) ? '' : ctrl.$viewValue;
                     fexUE.setContent(_initContent);
                 };

                 fexUE.initEditor();
             }
         }
     });