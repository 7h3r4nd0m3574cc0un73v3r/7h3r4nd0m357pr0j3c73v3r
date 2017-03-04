(function () {
  'use strict';

  angular.module('pretaApp')
      .directive('researchWidget', researchWidget);

  /** @ngInject */
  function researchWidget() {
	  return {
	      restrict: 'E',
	      templateUrl: 'angular/preta/directives/research-widget/template.html',
	      controller: [ '$scope', '$rootScope', '$stateParams', function( $scope, $rootScope, $stateParams) {
	    	  $scope.searchParams = { name: $stateParams.name, pageSize: 12, page: 1};
	    	  /* Clear search word */
	    	  $rootScope.$on( '$stateChangeSuccess', function( event, toState, toParams, fromState, fromParams) {
	    		  if( toState.name != 'root.search') {
	    			  $scope.searchParams.name = null;
	    		  }
	    	  });
	      }]
	    };
  }
})();
