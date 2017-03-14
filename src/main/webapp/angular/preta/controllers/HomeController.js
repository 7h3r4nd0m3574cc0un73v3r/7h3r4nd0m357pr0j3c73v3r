'use strict';

App.controller( 'HomeController', [ '$rootScope', '$scope', 'loggedUser', 'ArticleService', 'categories', 'UserService', 'StompService',
    function( $rootScope, $scope, loggedUser, ArticleService, categories, UserService, StompService) {
		/*Load the currently logged User if exists 
		 * and connect to websocket */
		  if( loggedUser != null) {
			  $rootScope.loggedUser = loggedUser;
			  StompService.connect()
			  			  .then( function() {
			  				  StompService.subscribe( '/user/topic/logged-user',
							  						function( payload, headers, response) {
						  					  		$rootScope.loggedUser = payload;
					  						});
			  			  });
		  }
		  
		  /* Init Variable */
		  $scope.categories = categories;
		  $scope.topArticles = [];
		  $scope.recentArticles = [];
		  $scope.latestArticles = [];
		  $scope.bestSellerArticles =[];
		  $scope.discountedArticles =[];
		  
		  $scope.isLoading = [ true, true, true, true];
		  
		  /* Loading BestSellers */
		  ArticleService.loadBestSellers()
						.then( function( response) {
								$scope.bestSellerArticles = response.entities;
								
								angular.forEach( $scope.bestSellerArticles, function( article, key) {
									ArticleService.loadEShop( article.id)
												  .then( function( response) {
													  article.eShop = response;
												  }, function( response) {
													  console.error( response);
													  $scope.isLoading[0] = false;
												  });

									/* Load Article Pictures */
									ArticleService.loadPictures( article.id)
												  .then( function( response) {
													  article.pictures = response.entities;
												  }, function( response) {
													  console.error( response);
												  });
								});
								
								$scope.isLoading[0] = false;
							}, function( response) {
								console.error( response);
								$scope.isLoading[0] = false;
							});
		  
		  /* Loading BestSellers */
		  ArticleService.loadTopArticles()
						.then( function( response) {
								$scope.topArticles = response.entities;
								
								angular.forEach( $scope.topArticles, function( article, key) {
									ArticleService.loadEShop( article.id)
												  .then( function( response) {
													  article.eShop = response;
												  }, function( response) {
													  console.error( response);
													  $scope.isLoading[1] = false;
												  });

									/* Load Article Pictures */
									ArticleService.loadPictures( article.id)
												  .then( function( response) {
													  article.pictures = response.entities;
												  }, function( response) {
													  console.error( response);
												  });
								});
								
								$scope.isLoading[1] = false;
							}, function( response) {
								console.error( response);
								$scope.isLoading[1] = false;
							});
		  
		  /* Loading Discounted Articles */
		  ArticleService.loadDiscounted()
						.then( function( response) {
								$scope.discountedArticles = response.entities;
								
								angular.forEach( $scope.discountedArticles, function( article, key) {
										ArticleService.loadEShop( article.id)
										  .then( function( response) {
											  article.eShop = response;
										  }, function( response) {
											  console.error( response);
										  });
										
										/* Load Default Article Picture */
										ArticleService.loadDefaultPicture( article.id)
													  .then( function( r) {
														  article.pictures = [ r];
													  }, function( response) {
														  console.error( response);
													  });
										
								});
								
								$scope.isLoading[3] = false;
							}, function( response) {
								console.error( response);
								$scope.isLoading[3] = false;
							});
		  
		  /* Loading Newest Articles */
		  ArticleService.loadLatest()
						.then( function( response) {
								$scope.latestArticles = response.entities;

								angular.forEach( $scope.latestArticles, function( article, key) {
										ArticleService.loadEShop( article.id)
										  .then( function( response) {
											  article.eShop = response;
										  }, function( response) {
											  console.error( response);
										  });

										/* Load Article Pictures */
										ArticleService.loadPictures( article.id)
													  .then( function( response) {
														  article.pictures = response.entities;
													  }, function( response) {
														  console.error( response);
													  });
								});
								
								$scope.isLoading[2] = false;
							}, function( response) {
								console.error( response);
								$scope.isLoading[2] = false;
							});
		  
	}
]);
