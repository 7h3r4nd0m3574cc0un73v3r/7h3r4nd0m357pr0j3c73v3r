(function () {
  'use strict';

  angular.module('pretaApp')
      .directive('orderStatusLabel', orderStatusLabel);

  /** @ngInject */
  function orderStatusLabel() {
	  return {
	      restrict: 'E',
	      scope: {
	        status: '='
	      },
	      templateUrl: 'angular/preta/directives/order-status-label/template.html'
	    };
  }
})();
