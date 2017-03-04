(function () {
  'use strict';

  angular.module('pretaApp')
      .directive('homeSlides', homeSlidesDirective);

  /** @ngInject */
  function homeSlidesDirective() {
	  return {
	      restrict: 'E',
	      templateUrl: 'angular/preta/directives/home-slides/template.html',
	      controller: [ '$scope', 'SlideService', function( $scope, SlideService) {
	    	  $scope.isLoading = true;
	    	  $scope.myInterval = 3500;
		      $scope.noWrapSlides = false;
		      $scope.active = 0;
		      var slides = $scope.slides = [];
		      var currIndex = 0;
			  
			  function loadEntities( page, pageSize) {
				  SlideService.loadEntities( page, pageSize)
				  			  .then( function( r) {
				  				  angular.forEach( r.entities, function( ent) {
				  					slides.push({
//							    		image: 'rest-api/slide/' + ent.id + '/fetch-picture',
							    		image: 'https://drive.google.com/uc?id=' + ent.googleId +'&export=download',
							    		id: currIndex++
							    	});
				  				  });
				  				  
				  				  $scope.isLoading = false;
				  			  }, function( r) {
				  				  console.error( r);
				  				  $scope.isLoading = false;
				  			  });
			  }
			  
			  loadEntities( 1, 0);
	      }]
	    };
  }
})();
