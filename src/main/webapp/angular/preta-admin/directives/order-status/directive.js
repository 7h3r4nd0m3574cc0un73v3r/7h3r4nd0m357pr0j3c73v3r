/**
 * @author v.lugovksy
 * created on 16.12.2015
 */
(function () {
  'use strict';

  angular.module('adminPretaApp')
      .directive('orderStatus', orderStatusDirective);

  /** @ngInject */
  function orderStatusDirective() {
	  return {
	      restrict: 'E',
	      scope: {
	        status: '='
	      },
	      templateUrl: 'angular/preta-admin/directives/order-status/template.html'
	    };
  }
})();
