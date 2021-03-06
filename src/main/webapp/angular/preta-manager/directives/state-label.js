/**
 * @author v.lugovksy
 * created on 16.12.2015
 */
(function () {
  'use strict';

  angular.module('managerPretaApp')
      .directive('stateLabel', stateLabelDirective);

  /** @ngInject */
  function stateLabelDirective() {
	  return {
	      restrict: 'E',
	      scope: {
	        isEnabled: '='
	      },
	      template: function() {
	        return '<span>{{ isEnabled ? "Oui" : "Non"}}</span>';
	      }
	    };
  }
})();
