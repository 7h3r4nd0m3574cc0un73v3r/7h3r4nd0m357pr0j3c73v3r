/**
 * @author v.lugovksy
 * created on 16.12.2015
 */
(function () {
  'use strict';

  angular.module('managerPretaApp')
      .directive('expenseCard', expenseCard);

  /** @ngInject */
  function expenseCard() {
	  return {
	      restrict: 'E',
	      scope: {
	        entity: '='
	      },
	      templateUrl: 'angular/preta-manager/directives/expense-card/template.html'
	    };
  }
})();
