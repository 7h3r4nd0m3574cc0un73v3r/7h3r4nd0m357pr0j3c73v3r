'use strict';

App.controller( 'DashboardController', [ '$scope', '$rootScope', 'EShopService', 'OrderService', 'StompService',
                                         function( $scope, $rootScope, EShopService, OrderService, StompService)
{
	/*Load the currently logged User if exists*/
	if( $rootScope.loggedUser != null) {
		  /* keeps Logged user updated */
		  StompService.connect()
		  			  .then( function() {
		  				  StompService.subscribe( '/user/topic/logged-user',
						  						function( payload, headers, response) {
					  					  		$rootScope.loggedUser = payload;
				  						});
		  			  });
	}
	
	/* Loading Mutexes */
	$scope.isLoading = true;
	$scope.isComputingProfComp = true;
	$scope.areEShopsLoading = true;
	$scope.arePendingOrdersLoading = true;
	
	/* Init Vars */
	$scope.profComp = 0;
	$scope.eShops = [];
	$scope.pendingOrders = [];
	
	/* Loading Functions */
	$scope.loadEShopsByManager = function() {
		$scope.areEShopsLoading = true;
		EShopService.loadEntitiesByManager( $rootScope.loggedUser.userInfo.id, 1, 4)
					.then( function( response) {
						$scope.eShops = response.entities;
						
						angular.forEach( $scope.eShops, function( value) {
//							value.profileCompletion = EShopService.computeProfileCompletion( value);
							
							/* Load Current Shop Sub */
							EShopService.loadCurrentShopSub( value.id)
										.then( function( response) {
											value.currentShopSub = response;
										}, function( response) {
											console.error( response);
											value.currentShopSub = null;
										});
							
							EShopService.loadArticlesCount( value.id)
										.then( function( response) {
											value.articlesCount = response;
										}, function( response) {
											console.error( response);
										});
						});
						
						$scope.areEShopsLoading = false;
					}, function( response) {
						console.error( response);
						$scope.areEShopsLoading = false;
					});
	}
	
	$scope.compProfComp = function() {
		$scope.isComputingProfComp = true;
		$scope.profComp = computeProfileCompletion( $rootScope.loggedUser);
		$scope.isComputingProfComp = false;
	}
	
	$scope.loadPendingOrders = function() {
		$scope.arePendingOrdersLoading = true;
		OrderService.loadByManagerAndStatus( 1, 8, true, 2)
						.then( function( response) {					
							angular.forEach( response.entities, function( entity) {
								OrderService.loadBuyer( entity.id)
											.then( function( response) {
												entity.buyer = response;
											}, function( response) {
												console.error( response);
											});
								OrderService.loadPayments( entity.id)
											.then( function( response) {
												entity.payments = response;
											}, function( response) {
												console.error( response);
											});
								OrderService.loadOrderedArticlesByOrder( entity.id)
											.then( function( response) {
												entity.articleCount = response.length;
												entity.total = 0;
												angular.forEach( response, function( orderedArticle) {
													entity.total += ( orderedArticle.article.price + orderedArticle.article.deliveryFee ) * orderedArticle.quantity;
												});
											}, function( response) {
												console.error( response);
											});
								OrderService.loadEShop( entity.id)
											.then( function( response) {
												entity.eShop = response;
											}, function( response) {
												console.error( response);
											});
							});
							
							/* Save to scope */
							$scope.pendingOrders = response.entities;
							
							$scope.arePendingOrdersLoading = false;
						}, function( errResponse) {						
							console.error( errResponse);
							$scope.arePendingOrdersLoading = false;
						});
	}
	
	/* Calls to loading functions */
	$scope.compProfComp();
	$scope.loadEShopsByManager();
	$scope.loadPendingOrders();
	/* End Calls */
	
	/* ProfileCompletion */
	function computeProfileCompletion( entity) {
		var total = 0;
		var propNum = 0;
		var nullPropNum = 0;
		
		angular.forEach( entity.userInfo, function( value, key){
			if( key != 'id' && key != 'birthDay') {
				if( value == null || value == "" || value == undefined) {
					nullPropNum++;
				}
			}
			propNum++;
		});
		
		return Math.round( (propNum - nullPropNum) / propNum * 100);
	}
}]);