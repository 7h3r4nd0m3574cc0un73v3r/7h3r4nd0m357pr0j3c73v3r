(function () {
  'use strict';

  angular.module('pretaApp')
      .directive('eShopCard', eShopCardDirective);

  /** @ngInject */
  function eShopCardDirective() {
	  return {
	      restrict: 'E',
	      scope: {
		        eShop: '='
		      },
	      templateUrl: 'angular/preta/directives/e-shop-card/template.html'
	    };
  }
})();