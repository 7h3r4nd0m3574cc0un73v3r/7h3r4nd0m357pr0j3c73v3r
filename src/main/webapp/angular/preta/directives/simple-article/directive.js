(function () {
  'use strict';

  angular.module('pretaApp')
      .directive('simpleArticle', simpleArticle);

  /** @ngInject */
  function simpleArticle() {
	  return {
	      restrict: 'E',
	      scope: {
		        article: '='
		      },
	      templateUrl: 'angular/preta/directives/simple-article/template.html'
	    };
  }
})();
