  'use strict';

  App.directive('orderStatus', orderStatusDirective);

  /** @ngInject */
  function orderStatusDirective() {
	  return {
	      restrict: 'E',
	      scope: {
	        status: '='
	      },
	      templateUrl: 'angular/common/directives/order-status/template.html'
	    };
  }
