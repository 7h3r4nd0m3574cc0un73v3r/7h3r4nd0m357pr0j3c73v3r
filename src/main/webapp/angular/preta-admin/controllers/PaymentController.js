'use strict';

App.controller( 'PaymentController', [ '$state', '$stateParams', '$scope', 'PaymentService', 'StompService', 'OrderService', 'ArticleService',
                                     function( $state, $stateParams, $scope, PaymentService, StompService, OrderService, ArticleService)
{
	/* Breadcrumb custom */
	if( $stateParams.status == 0)
		$scope.breadCrumbLabel = "Paiements";
	else if( $stateParams.status == 1)
		$scope.breadCrumbLabel = "Paiements accept\xE9s";
	else if( $stateParams.status == 2)
		$scope.breadCrumbLabel = "Paiements rejet\xE9s";
	else if( $stateParams.status == 3)
		$scope.breadCrumbLabel = "Paiements";
	/* End Breadcrumb custom */
	
	/* Loading Mutexes */
	$scope.isListLoading = true;
	$scope.isEntityLoading = true;
	
	/* Handle PagedListJSON ArticleOrders */
	$scope.entities = [];
	$scope.entity = {};

	/* Pagination Settings */
	$scope.pagination = { currentPage: $stateParams.page == undefined ? 1 : $stateParams.page,
						  pageSize: $stateParams.pageSize == undefined ? 10 : $stateParams.pageSize,
						  paymentStatus: $stateParams.paymentStatus == undefined ? 0 : $stateParams.paymentStatus,
						  orderByIdAsc: $stateParams.orderByIdAsc == undefined ? true : $stateParams.orderByIdAsc,
					      pagesNumber: null,
					      itemsNumber: null
						};
	/* End Pagination Setting */
	
	/* Funcs and Tools */
	function loadEntities( page, pageSize, paymentStatus, orderByIdAsc) {
		$scope.isListLoading = true;
		
		PaymentService.loadAddressedEntities( page, pageSize, paymentStatus, orderByIdAsc)
					.then( function( response) {
						/* Update Pagination info and Scope */
						$scope.pagination.pagesNumber = response.pagesNumber;
						$scope.pagination.itemsNumber = response.itemsNumber;
						$stateParams.page = $scope.pagination.currentPage;
						$scope.entities = response.entities;
						
						angular.forEach( $scope.entities, function( entity) {
							prepareEntity( entity);
						});
						
						$scope.isListLoading = false;
					}, function( r) {
						$scope.isListLoading = false;
						console.error( r);
					});
	};
	/* Retrieve necessary article order info */
	function prepareEntity( entity) {
		$scope.isEntityLoading = true;
		/* Prepare variable for total amount */
		entity.expectedAmount = 0;
		/* Load Articles Orders */
		PaymentService.loadArticleOrders( entity.id, 1, 0)
					  .then( function( r) {
						  /* Populate Article Order */
						  angular.forEach( r.entities, function( articleOrder) {
								/* Load EShop */
								OrderService.loadEShop( articleOrder.id)
											.then( function( response) {
												articleOrder.eShop = response;
											}, function( r) {
												console.error( r);
											});
								/* Load Buyer */
								OrderService.loadBuyer( articleOrder.id)
											.then( function( response) {
												articleOrder.buyer = response;
											}, function( r) {
												console.log( r);
											});
								/* Load OrderedArticles */
								OrderService.loadOrderedArticlesByOrder( articleOrder.id)
											.then( function( orderedArticles) {
												articleOrder.orderedArticles = orderedArticles;
												articleOrder.total = 0;
												angular.forEach( articleOrder.orderedArticles, function( value) {
													articleOrder.total += ( value.article.price + value.article.deliveryFee ) * value.quantity;
													entity.expectedAmount += ( value.article.price + value.article.deliveryFee ) * value.quantity;
												});
											}, function( r) {
												console.error( r);
											});
								/* Compute the expected Amount */
								;
						  });
						  
						  console.log( entity);
						  entity.articleOrders = r.entities;
					  }, function( r) {
						  console.error( r);
					  });
		/* Load ShopSub if any */
		PaymentService.loadShopSub( entity.id)
					  .then( function( r) {
						  entity.shopSub = r;
					  }, function( r) {
						  console.error( r);
					  });
		/* Load AdvOffer if any */
		PaymentService.loadAdvOffer( entity.id)
					  .then( function( r) {
						  entity.advOffer = r;
					  }, function( r) {
						  console.error( r);
					  });
		
		$scope.isEntityLoading = false;
	}
	
	loadEntities( $scope.pagination.page, $scope.pagination.pageSize,
				  $scope.pagination.paymentStatus, $scope.pagination.orderByIdAsc);
	
	/* Page Change */
	$scope.changePage = function() {
		$state.go( $state.current.name, { page: $scope.pagination.currentPage, pageSize: $scope.pagination.pageSize,
			  paymentStatus: $scope.pagination.paymentStatus, orderByIdAsc: $scope.pagination.orderByIdAsc }, { notify: false});
					
			loadEntities( $scope.pagination.currentPage, $scope.pagination.pageSize, $scope.pagination.paymentStatus, $scope.pagination.orderByIdAsc);
	}
	
	/* Monitor new Payment */
//	StompService.connect()
//				.then( function( ) {
//					if( $scope.pagination.paymentStatus == 1) {
//						StompService.subscribe( "/topic/admin/article-order-to-confirm",
//												function( payload, headers, response) {
//													if( $scope.entities.length < $scope.pagination.pageSize) {
//														prepareEntity( payload);
//														$scope.entities.push( payload);
//														$scope.$apply();
//													}
//													else
//													{
//														loadEntities( $scope.pagination.currentPage, $scope.pagination.pageSize, $scope.pagination.paymentStatus, $scope.pagination.orderByIdAsc);
//													}
//												}
//											  );
//					}
//				});
	
	/* TODO Add WebSocket Real TIme Update */
	/* Show Payment */
	if( $stateParams.id != undefined) {
		$scope.isEntityLoading = true;
		
		PaymentService.loadEntity( $stateParams.id)
					.then( function( response) {
						$scope.entity = response;
						
						prepareEntity( $scope.entity);
						
						/* Register Stomp Sub to Real Time Updates */
						StompService.connect()
									.then( function() {
										StompService.subscribe( "/topic/admin/payment/" + $stateParams.id 
														,function( p, h, r) {
														/* Callback function Payload, Headers and Response as Params */
														prepareEntity( p);
														$scope.entity = p;
														$scope.$apply();
													});
									});
						
						$scope.isEntityLoading = false;
					}, function( r) {
						console.error( r);
						$scope.isEntityLoading = false;
					});	
	}
	
	/* Payment Related */
	$scope.acceptPayment = function( id) {
		PaymentService.accept( id)
						.then( function( response) {
							/* WebSocket handled */
						}, function( response) {
							console.error( response);
						});
	};
	
	$scope.rejectPayment = function( id) {
		PaymentService.reject( id)
						.then( function( response) {
							/* WebSocket handled */
						}, function( response) {
							console.error( response);
						});
	};
}]);