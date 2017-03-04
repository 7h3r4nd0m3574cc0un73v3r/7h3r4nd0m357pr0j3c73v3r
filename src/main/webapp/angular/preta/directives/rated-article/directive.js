(function () {
  'use strict';

  angular.module('pretaApp')
      .directive('ratedArticle', ratedArticleDirective);

  /** @ngInject */
  function ratedArticleDirective() {
	  return {
	      restrict: 'E',
	      scope: {
		        article: '='
		      },
	      templateUrl: 'angular/preta/directives/rated-article/template.html'
	    };
  }
})();
