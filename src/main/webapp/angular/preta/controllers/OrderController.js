App.controller( 'OrderController', [ '$rootScope', '$state', '$stateParams', '$scope', 'ArticleService', 'OrderService', 'StompService',
                                     function( $rootScope, $state, $stateParams, $scope, ArticleService, OrderService, StompService) {
	/*$scope.articlesToOrder = ArticleService.articlesToOrder;*/
	
	if( $rootScope.loggedUser == null) {
		$state.go( 'root.errors.401');
	}
	
	/* Loading Mutexes */
	$scope.isListLoading = true;
	$scope.isEntityLoading = true;
	
	/* Handle PagedListJSON ArticleOrders */
	$scope.orders = [];
	$scope.entity = {};
	/* End Handle PagedListJSON ArticleOrders */
	
	/* Pagination Settings */
	$scope.pagination = { currentPage: $stateParams.page == undefined ? 1 : $stateParams.page,
						  pageSize: $stateParams.pageSize == undefined ? 6 : $stateParams.pageSize,
						  orderStatus: $stateParams.orderStatus == undefined ? 0 : $stateParams.orderStatus,
						  orderByIdAsc: $stateParams.orderByIdAsc == undefined ? false : $stateParams.orderByIdAsc,
					      pagesNumber: null,
					      itemsNumber: null
						};
	/* End Pagination Setting */

	/* View Config */
	$scope.view = { title: "", description: ""};
	if( $scope.pagination.orderStatus == 0 ){
		$scope.view = { title: "Mes Commandes", description: ""}
	}
	else if( $scope.pagination.orderStatus == 4) {
		$scope.view = { title: "Mes Commandes en cours", description: "Vos commandes en cours de livraison" }
	}
	else if( $scope.pagination.orderStatus == 5) {
		$scope.view = { title: "Mes Commandes reçues", description: "Vos commandes et articles livr\xE9es" }
	}
	/* End View Config */
	
	/* Load Entity */
	if( $stateParams.id != undefined ) {
		OrderService.loadEntity( $stateParams.id)
					.then( function( response) {
						$scope.entity = response;
						prepareArticleOrder();
						
						/* Init websockets for real time update */
						StompService.connect()
									.then( function() {
										StompService.subscribe( '/user/topic/logged-user/article-order/' + response.id,
												function( payload, header, response) {
													$scope.entity = payload;
													console.log( "Order Updated");
													prepareArticleOrder();
												});
									});
						
					}, function( response) {
						console.error( response);
						$scope.entity = null;
						$scope.isEntityLoading = false;
					});
	}
	
	function prepareArticleOrder() {
		OrderService.loadArticleOrderEShop( $scope.entity.id)
					.then( function( r) {
						$scope.entity.eShop = r;
					}, function( r) {
						console.error( r);
					});
		
		OrderService.loadOrderedArticlesByOrder( $scope.entity.id)
					.then( function( response) {
						$scope.entity.orderedArticles = response;
						$scope.entity.total = 0;
						
						angular.forEach( $scope.entity.orderedArticles, function( orderedArticle) {
							orderedArticle.total = ( orderedArticle.article.price + orderedArticle.article.deliveryFee) * orderedArticle.quantity;
							$scope.entity.total += orderedArticle.total;
							
							/* Load Default Picture */
							ArticleService.loadDefaultPicture( orderedArticle.article.id)
										  .then( function( response) {
											  orderedArticle.article.pictures.push( response);
										  }, function( response) {
											  console.error( response);
										  });
						});
						
						$scope.isEntityLoading = false;
					}, function( response) {
						console.error( response);
						$scope.isEntityLoading = false;
					});
	}
	
	
	/* Load ArticleOrders */
	function loadOrdersByUser( page, pageSize, orderStatus, orderByIdAsc)
	{
		$scope.isListLoading = true;
		
		OrderService.loadOrdersByUser( page, pageSize, orderStatus, orderByIdAsc)
					.then( function( response) {
						
						$scope.pagination.pagesNumber = response.pagesNumber;
						$scope.pagination.itemsNumber = response.itemsNumber;
						$stateParams.page = $scope.pagination.currentPage;
						$scope.orders = response.entities;
						
						angular.forEach( $scope.orders, function( articleOrder) {
							OrderService.loadOrderedArticlesByOrder( articleOrder.id)
										.then( function( response) {
											articleOrder.orderedArticles = response;
											articleOrder.total = 0;
											angular.forEach( articleOrder.orderedArticles, function( orderedArticle) {
												articleOrder.total += ( orderedArticle.article.price + orderedArticle.article.deliveryFee) * orderedArticle.quantity;
											});
											
										}, function( response) {
											console.error( response);
											$scope.isListLoading = false;
										});
							
							/* Subscribe to websocket for each article order */
							StompService.connect()
										.then( function() {
											StompService.subscribe( '/user/topic/logged-user/article-order/' + articleOrder.id,
																	function( payload, headers, response) {
																		console.log( "WebSocket updated handle");
																		angular.forEach( $scope.orders, function( entitius, index) {
																			if( entitius.id == articleOrder.id) {
																				$scope.orders[index] = payload;
																				
																				OrderService.loadOrderedArticlesByOrder( $scope.orders[index].id)
																							.then( function( response) {
																								$scope.orders[index].orderedArticles = response;
																								$scope.orders[index].total = 0;
																								angular.forEach( $scope.orders[index].orderedArticles, function( orderedArticle) {
																									$scope.orders[index].total += ( orderedArticle.article.price + orderedArticle.article.deliveryFee) * orderedArticle.quantity;
																								});
																								
																							}, function( response) {
																								console.error( response);
																							});
																				
																				$scope.$apply();
																			}
																		});
																	}
											);
										});
						});
						
						$scope.isListLoading = false;
					}, function( response) {
						console.error( response);
						$scope.isListLoading = false;
					});
					
					//OrderService.loadPaymentsByOrder( articleOrder.id)
					//	.then( function( response) {
					//		articleOrder.payments = response;
					//	}, function( response) {
					//		console.error( response);
					//	});
	}
	
	/* Load Orders */
	loadOrdersByUser( $scope.pagination.currentPage, $scope.pagination.pageSize, $scope.pagination.orderStatus, $scope.pagination.orderByIdAsc);
	
	$scope.confirmReception = function( id) {
		OrderService.confirmReception( id)
					.then( function( respone) {
						/* websocket handled */
					}, function( response) {
						console.error( response);
					});
	};
	
	$scope.updateArticleOrder = function( id) {
		OrderService.loadEntity( id)
					.then( function( response) {
						$scope.entity.status = response.status;
						$scope.entity.receptionDate = response.receptionDate;
//						loadOrdersByUser( $scope.pagination.currentPage, $scope.pagination.pageSize, $scope.pagination.orderStatus, $scope.pagination.orderByIdAsc);
					}, function( response ) {
						console.error( response);
					});
	};

	$scope.changePage = function() {
		$state.go( $state.current.name, { page: $scope.pagination.currentPage, pageSize: $scope.pagination.pageSize,
										  orderByIdAsc: $scope.pagination.orderByIdAsc,
										  orderStatus: $scope.pagination.orderStatus }, { notify: false});
		
		loadOrdersByUser( $scope.pagination.currentPage, $scope.pagination.pageSize,
				          $scope.pagination.orderStatus, $scope.pagination.orderByIdAsc);
	}
}]);