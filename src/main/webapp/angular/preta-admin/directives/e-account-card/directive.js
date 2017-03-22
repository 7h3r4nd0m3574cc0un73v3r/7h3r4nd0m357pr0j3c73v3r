/**
 * @author v.lugovksy
 * created on 16.12.2015
 */
(function () {
  'use strict';

  angular.module('adminPretaApp')
      .directive('eAccountCard', eAccountCard);

  /** @ngInject */
  function eAccountCard() {
	  return {
	      restrict: 'E',
	      scope: {
	        entity: '='
	      },
	      templateUrl: 'angular/preta-admin/directives/e-account-card/template.html'
	    };
  }
})();
