  'use strict';

  App.directive('genericStatus', genericStatusDirective);

  /** @ngInject */
  function genericStatusDirective() {
	  return {
	      restrict: 'E',
	      scope: {
	        status: '='
	      },
	      templateUrl: 'angular/common/directives/generic-status/template.html'
	    };
  }
