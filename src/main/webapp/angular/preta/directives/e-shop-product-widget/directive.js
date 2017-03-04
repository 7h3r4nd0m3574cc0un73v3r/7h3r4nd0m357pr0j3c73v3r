(function () {
  'use strict';

  angular.module('pretaApp')
      .directive('eShopProductWidget', eShopProductWidgetDirective);

  /** @ngInject */
  function eShopProductWidgetDirective() {
	  return {
	      restrict: 'E',
	      scope: {
		        article: '='
		      },
	      templateUrl: 'angular/preta/directives/e-shop-product-widget/template.html',
	    };
  }
})();
