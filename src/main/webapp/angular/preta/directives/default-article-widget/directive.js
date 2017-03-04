(function () {
  'use strict';

  angular.module('pretaApp')
      .directive('defaultArticleWidget', defaultArticleWidgetDirective);

  /** @ngInject */
  function defaultArticleWidgetDirective() {
	  return {
	      restrict: 'E',
	      scope: {
		        article: '='
		      },
	      templateUrl: 'angular/preta/directives/default-article-widget/template.html'
	    };
  }
})();
