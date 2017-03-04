(function () {
  'use strict';

  angular.module('pretaApp')
      .directive('defaultProductsContainer', defaultProductsContainerDirective);

  /** @ngInject */
  function defaultProductsContainerDirective() {
	  return {
	      restrict: 'E',
	      scope: {
		        isLoading: '=',
		        title: '=',
		        icon: '=',
		        entities: '=',
		        summary: '=',
		        moreSref: '='
		      },
	      templateUrl: 'angular/preta/directives/default-products-container/template.html'
	    };
  }
})();
