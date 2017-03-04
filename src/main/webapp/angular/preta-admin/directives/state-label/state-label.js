/**
 * @author v.lugovksy
 * created on 16.12.2015
 */
(function () {
  'use strict';

  angular.module('adminPretaApp')
      .directive('stateLabel', stateLabelDirective);

  /** @ngInject */
  function stateLabelDirective() {
	  return {
	      restrict: 'E',
	      scope: {
	        isEnabled: '='
	      },
	      templateUrl: 'angular/preta-admin/directives/state-label/state-label.html'
	    };
  }
})();
