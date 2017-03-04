'use strict';

App.controller( 'EShopController', [ '$state', '$stateParams', '$rootScope', '$scope','EShopService', 'UserService', 'ArticleService',
                                     function( $state, $stateParams, $rootScope, $scope, EShopService, UserService, ArticleService)
{	
	/* Load the EShop */
	$scope.entity = {};
	$scope.isEntityLoading = true;
	
	/* Init the Best Sellers, Top Rated Articles and Last Articles */ 
	$scope.bestSellerArtiles = [];
	$scope.isBestSellerLoading = true;
	
	$scope.topArticles = [];
	$scope.isTopArticleLoading = true;
	
	$scope.latestArticles = [];
	$scope.isLatestArticlesLoading = true;
	
	/* Loading EShop */
	EShopService.loadEntity( $stateParams.id)
				.then( function( response) {
					$scope.entity = response;
					
					/* Loading Best Seller, Top rated Articles and Last Articles */
					EShopService.loadLatestArticles( $scope.entity.id, 1, 8)
								.then( function( response) {
									$scope.latestArticles = response.entities;
									/* Load Default Picture */
									angular.forEach( $scope.latestArticles, function( article) {
										ArticleService.loadDefaultPicture( article.id)
													  .then( function( r) {
															article.pictures = [];
															article.pictures.push( r);
													  }, function( r) {
														  console.error( r);
													  });
									});
									$scope.isLatestArticlesLoading = false;
								}, function( response) {
									console.error( response);
									$scope.isLatestArticlesLoading = false;
								});
					
					EShopService.loadBestSellersArticles( $scope.entity.id, 1, 8)
								.then( function( response) {
									$scope.bestSellerArtiles = response.entities;
									$scope.isBestSellerLoading = false;
									/* Load Default Picture */
									angular.forEach( $scope.bestSellerArtiles, function( article) {
										ArticleService.loadDefaultPicture( article.id)
													  .then( function( r) {
															article.pictures = [];
															article.pictures.push( r);
													  }, function( r) {
														  console.error( r);
													  });
									});
								}, function( response) {
									console.error( response);
									$scope.isBestSellerLoading = false;
								});
					
					EShopService.loadTopArticles( $scope.entity.id, 1, 8)
								.then( function( response) {
									$scope.topArticles = response.entities;
									$scope.isTopArticleLoading = false;
									/* Load Default Picture */
									angular.forEach( $scope.topArticles, function( article) {
										ArticleService.loadDefaultPicture( article.id)
													  .then( function( r) {
															article.pictures = [];
															article.pictures.push( r);
													  }, function( r) {
														  console.error( r);
													  });
									});
								}, function( response) {
									console.error( response);
									$scope.isTopArticleLoading = false;
								});
					
					if( $rootScope.loggedUser != null) {
						var isFound = false;
						UserService.loadFavoritesEShops( 1, 0)
									.then( function( response) {
										angular.forEach( response.entities, function( entity) {
											if( entity.id == $scope.entity.id)
												isFound = true;
										});
										
										$scope.entity.isFavorite = isFound;
										
										$scope.isEntityLoading = false;
									}, function( response) {
										console.error( response);
										$scope.isEntityLoading = false;
									});
					}
					else
						$scope.isEntityLoading = false;
				}, function( response) {
					console.error( response);
					$scope.isEntityLoading = false;
				});
	
	/* Favorites Mgmt */
	$scope.addAsFavorite = function() {
		EShopService.addAsFavorite( $scope.entity.id)
					.then( function( response) {
						console.log( response);
						$scope.entity.isFavorite = true;
					}, function( response) {
						$scope.entity.isFavorite = false;
						console.error( response);
					});
	}
	
	$scope.removeFromFavorites = function() {
		EShopService.removeFromFavorites( $scope.entity.id)
					.then( function( response) {
						console.log( response);
						$scope.entity.isFavorite = false;
					}, function( response) {
						$scope.entity.isFavorite = true;
						console.error( response);
					});
	}
}]);