/**
 * @author v.lugovksy
 * created on 16.12.2015
 */
(function () {
  'use strict';

  angular.module('adminPretaApp')
      .directive('paymentStatus', paymentStatus);

  /** @ngInject */
  function paymentStatus() {
	  return {
	      restrict: 'E',
	      scope: {
	        status: '='
	      },
	      templateUrl: 'angular/preta-admin/directives/payment-status/payment-status.html'
	    };
  }
})();
