  'use strict';

  App.directive('orderStatus', orderStatusDirective);

  /** @ngInject */
  function orderStatusDirective() {
	  return {
	      restrict: 'E',
	      scope: {
	        status: '='
	      },
	      templateUrl: 'angular/preta-manager/directives/order-status/template.html'
	    };
  }
