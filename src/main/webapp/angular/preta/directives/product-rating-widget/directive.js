(function () {
  'use strict';

  angular.module('pretaApp')
      .directive('productRatingWidget', productRatingWidgetDirective);

  /** @ngInject */
  function productRatingWidgetDirective() {
	  return {
	      restrict: 'E',
	      scope: {
		        orderedArticle: '=',
	      },
	      templateUrl: 'angular/preta/directives/product-rating-widget/template.html',
	    };
  }
})();
