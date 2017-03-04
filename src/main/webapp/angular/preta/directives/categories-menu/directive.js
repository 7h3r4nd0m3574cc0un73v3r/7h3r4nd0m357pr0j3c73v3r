(function () {
  'use strict';

  angular.module('pretaApp')
      .directive('categoriesMenu', categoriesMenu);

  /** @ngInject */
  function categoriesMenu() {
	  return {
	      restrict: 'E',
	      scope: {
	        categories: '='
	      },
	      templateUrl: 'angular/preta/directives/categories-menu/template.html'
	    };
  }
})();
