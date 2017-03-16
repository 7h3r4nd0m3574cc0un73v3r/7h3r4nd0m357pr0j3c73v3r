(function () {
  'use strict';

  angular.module('pretaApp')
      .directive('visitedArticlesWidget', visitedArticlesWidgetDirective);

  /** @ngInject */
  function visitedArticlesWidgetDirective() {
	  return {
	      restrict: 'E',
	      templateUrl: 'angular/preta/directives/visited-articles-widget/template.html',
	      controller: [ '$scope', 'ArticleService', function( $scope, ArticleService) {
	    	  /* Loading Mutexes */
	    	  $scope.isLoading = true;
	    	  
	    	  /* Init vars */
	    	  $scope.entities = [];
	    	  var simpleEntities = [];
	    	  
	    	  /* Loading Entities */
	    	  $scope.loadEntities = function( page, pageSize, orderByIdAsc) {
	    		   	$scope.isLoading = true;
	    	        ArticleService.loadLastVisited( page, pageSize, orderByIdAsc)
	    	        			  .then( function( response) {
	    	        				  simpleEntities = response.entities;
	    	        				  
	    	        				  angular.forEach( simpleEntities, function( entity) {
	    	        					  ArticleService.loadEntity( entity.articleId)
	    	        					  				.then( function( response) {
	    	        					  					var article = response;
	    	        					  					
	    	        					  					ArticleService.loadEShop( article.id)
	    	        					  								  .then( function( response) {
	    	        					  									  article.eShop = response;
	    	        					  									  article.visitTime = entity.visitTime;
	    	        					  									  $scope.entities.push( article);
	    	        					  									  
	    	        					  								  }, function( response) {
	    	        					  									  console.error( response);
	    	        					  								  });
	    	        					  					
	    	        										ArticleService.loadDefaultPicture( article.id)
					  													  .then( function( r) {
					  															article.pictures = [];
					  															article.pictures.push( r);
					  													  }, function( r) {
					  														  console.error( r);
					  													  });
	    	        					  					
	    	        					  				}, function( response) {
	    	        					  					console.error( response);
	    	        					  				});
	    	        				  });
	    	        				  
	    	        				  $scope.isLoading = false;
	    	        			  }, function( response) {
	    	        				  console.error( response);
	    	        				  $scope.isLoading = false;
	    	        			  });
	    	  }
	    	  
	    	  /* Call to loading functions */
	    	  $scope.loadEntities( 1, 4, false);
	      }]
	    };
  }
})();
