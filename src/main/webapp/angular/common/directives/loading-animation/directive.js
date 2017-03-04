  'use strict';

  App.directive('loadingAnimation', loadingAnimationDirective);

  /** @ngInject */
  function loadingAnimationDirective() {
	  return {
	      restrict: 'E',
	      templateUrl: 'angular/common/directives/loading-animation/template.html'
	    };
  }
