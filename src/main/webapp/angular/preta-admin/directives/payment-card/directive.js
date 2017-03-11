/**
 * @author v.lugovksy
 * created on 16.12.2015
 */
(function () {
  'use strict';

  angular.module('adminPretaApp')
      .directive('paymentCard', paymentCard);

  /** @ngInject */
  function paymentCard() {
	  return {
	      restrict: 'E',
	      scope: {
	        entity: '='
	      },
	      templateUrl: 'angular/preta-admin/directives/payment-card/template.html'
	    };
  }
})();
