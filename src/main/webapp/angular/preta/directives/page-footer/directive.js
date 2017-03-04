(function () {
  'use strict';

  angular.module('pretaApp')
      .directive('pageFooter', pageFooterDirective);

  /** @ngInject */
  function pageFooterDirective() {
	  return {
	      restrict: 'E',
	      templateUrl: 'angular/preta/directives/page-footer/template.html'
	    };
  }
})();
