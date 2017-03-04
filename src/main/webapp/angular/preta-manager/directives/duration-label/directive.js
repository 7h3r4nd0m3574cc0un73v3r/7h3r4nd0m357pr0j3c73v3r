'use strict';
App.directive('durationLabel', durationLabelDirective);

function durationLabelDirective() {
	  return {
	      restrict: 'E',
	      scope: {
	        duration: '=',
	        durationType: '='
	      },
	      templateUrl: 'angular/preta-manager/directives/duration-label/template.html'
	    };
}
