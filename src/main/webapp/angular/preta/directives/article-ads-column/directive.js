(function () {
  'use strict';

  angular.module('pretaApp')
      .directive('articleAdsColumn', articleAdsColumnDirective);

  /** @ngInject */
  function articleAdsColumnDirective() {
	  return {
	      restrict: 'E',
	      templateUrl: 'angular/preta/directives/article-ads-column/template.html',
	      controller: [ '$scope', 'ArticleService', function( $scope, ArticleService) {
	    	  /* Loading Mutexes */
	    	  $scope.isLoading = true;
	    	  
	    	  /* Init Vars */
	    	  $scope.entities = [];
	    	  
	    	  /* Loading Functions */
	    	  $scope.loadEntities = function( page, pageSize, code) {
	    		  $scope.isLoading = true;
	    		  ArticleService.loadAdvertised( page, pageSize, code)
	    		  				.then( function( response) {
	    		  					$scope.entities = response.entities;
	    		  					angular.forEach( $scope.entities, function( article) {
										ArticleService.loadDefaultPicture( article.id)
													  .then( function( r) {
															article.pictures = [];
															article.pictures.push( r);
													  }, function( r) {
														  console.error( r);
													  });
	    		  					});
	    		  					$scope.isLoading = false;
	    		  				}, function( response) {
	    		  					console.error( response);
	    		  					$scope.isLoading = false;
	    		  				});
	    	  }

	    	  /* Call to loading function */
	    	  /* GG Task Force 141 */
	    	  $scope.loadEntities( 1, 3, 1);
	      }]
	    };
  }
})();
