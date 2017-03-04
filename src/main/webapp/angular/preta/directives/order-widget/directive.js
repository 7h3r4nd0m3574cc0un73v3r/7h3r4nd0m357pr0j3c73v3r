(function () {
  'use strict';

  angular.module('pretaApp')
      .directive('orderWidget', orderWidgetDirective);

  /** @ngInject */
  function orderWidgetDirective() {
	  return {
	      restrict: 'E',
	      scope: {
		        order: '='
		      },
	      templateUrl: 'angular/preta/directives/order-widget/template.html'
	    };
  }
})();
