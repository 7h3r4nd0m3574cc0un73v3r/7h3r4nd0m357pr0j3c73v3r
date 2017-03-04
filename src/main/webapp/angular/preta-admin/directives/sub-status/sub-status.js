/**
 * @author v.lugovksy
 * created on 16.12.2015
 */
(function () {
  'use strict';

  angular.module('adminPretaApp')
      .directive('subStatus', subStatusDirective);

  /** @ngInject */
  function subStatusDirective() {
	  return {
	      restrict: 'E',
	      scope: {
	        status: '='
	      },
	      templateUrl: 'angular/preta-admin/directives/sub-status/sub-status.html'
	    };
  }
})();
