(function () {
  'use strict';

  angular.module('pretaApp')
      .directive('category', categoryDirective);

  /** @ngInject */
  function categoryDirective() {
	  return {
	      restrict: 'E',
	      scope: {
	        category: '='
	      },
	      templateUrl: 'angular/preta/directives/categories-menu/item.template.html'
	    };
  }
})();
