  'use strict';

  App.directive('genericStatus', genericStatus);

  /** @ngInject */
  function genericStatus() {
	  return {
	      restrict: 'E',
	      scope: {
	        status: '='
	      },
	      templateUrl: 'angular/preta-manager/directives/generic-status/template.html'
	    };
  }
