(function () {
  'use strict';

  angular.module('adminPretaApp')
      .directive('durationLabel', durationLabelDirective);

  /** @ngInject */
  function durationLabelDirective() {
	  return {
	      restrict: 'E',
	      scope: {
	        duration: '=',
	        durationType: '='
	      },
	      templateUrl: 'angular/preta-admin/directives/duration-label/duration-label.html'
	    };
  }
})();
