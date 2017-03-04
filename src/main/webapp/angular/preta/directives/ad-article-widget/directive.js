(function () {
  'use strict';

  angular.module('pretaApp')
      .directive('adArticleWidget', adArticleWidgetDirective);

  /** @ngInject */
  function adArticleWidgetDirective() {
	  return {
	      restrict: 'E',
	      scope: {
		        article: '='
		      },
	      templateUrl: 'angular/preta/directives/ad-article-widget/template.html'
	    };
  }
})();
