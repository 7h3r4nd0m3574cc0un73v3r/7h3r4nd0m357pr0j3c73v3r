(function () {
  'use strict';

  angular.module('pretaApp')
      .directive('subCategory', subCategory);

  /** @ngInject */
  function subCategory() {
	  return {
	      restrict: 'E',
	      scope: {
	        category: '='
	      },
	      templateUrl: 'angular/preta/directives/categories-menu/sub-category.template.html'
	    };
  }
})();
