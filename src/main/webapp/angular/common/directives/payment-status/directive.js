  'use strict';

  App.directive('paymentStatus', paymentStatus);

  function paymentStatus() {
	  return {
	      restrict: 'E',
	      scope: {
	        status: '='
	      },
	      templateUrl: 'angular/common/directives/payment-status/template.html'
	    };
  }
