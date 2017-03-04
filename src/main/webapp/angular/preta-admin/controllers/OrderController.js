'use strict';

App.controller( 'OrderController', [ '$state', '$stateParams', '$scope', 'OrderService', 'PaymentService', 'StompService',
                                     function( $state, $stateParams, $scope, OrderService, PaymentService, StompService)
{
	/* Breadcrumb custom */
	if( $stateParams.status == -1)
		$scope.breadCrumbLabel = "Commandes";
	else if( $stateParams.status == 0)
		$scope.breadCrumbLabel = "Commandes \xE0 payer";
	else if( $stateParams.status == 1)
		$scope.breadCrumbLabel = "Commandes \xE0 confirmer";
	else if( $stateParams.status == 2)
		$scope.breadCrumbLabel = "Commandes pay\xE8es";
	else if( $stateParams.status == 3)
		$scope.breadCrumbLabel = "Commandes à livrer";
	else if( $stateParams.status == 4)
		$scope.breadCrumbLabel = "Commandes livr\xE8es";
	else
		$scope.breadCrumbLabel = "Commandes";
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
						  orderStatus: $stateParams.orderStatus == undefined ? 0 : $stateParams.orderStatus,
						  orderByIdAsc: $stateParams.orderByIdAsc == undefined ? true : $stateParams.orderByIdAsc,
					      pagesNumber: null,
					      itemsNumber: null
						};
	/* End Pagination Setting */
	
	/* Funcs and Tools */
	function loadEntities( page, pageSize, orderStatus, orderByIdAsc) {
		$scope.isListLoading = true;
		
		OrderService.loadEntities( page, pageSize, orderStatus, orderByIdAsc)
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
					}, function( response) {
						
						$scope.isListLoading = false;
					});
	};
	/* Retrieve necessary article order info */
	function prepareEntity( entity, level) {
		/* Load EShop */
		OrderService.loadEShop( entity.id)
					.then( function( response) {
						entity.eShop = response;
					}, function( response) {
						console.log( response);
					});
		/* Load Buyer */
		OrderService.loadBuyer( entity.id)
					.then( function( response) {
						entity.buyer = response;
					}, function( response) {
						console.log( response);
					});
		
		if( level >= 1) {
			/* Load OrderedArticles */
			OrderService.loadOrderedArticlesByOrder( entity.id)
						.then( function( orderedArticles) {
							entity.orderedArticles = orderedArticles;
							entity.total = 0;
							angular.forEach( entity.orderedArticles, function( value) {
								entity.total += ( value.article.price + value.article.deliveryFee ) * value.quantity;
							});
						}, function( r) {
							console.error( r);
						});
			/* Load Payments */
			OrderService.loadPayments( entity.id)
						.then( function( payments) {
							entity.payments = payments.entities;
						}, function( r) {
							console.error( r);
						});
		}
	}
	
	loadEntities( $scope.pagination.page, $scope.pagination.pageSize,
				  $scope.pagination.orderStatus, $scope.pagination.orderByIdAsc);
	
	/* Page Change */
	$scope.changePage = function() {
		$state.go( $state.current.name, { page: $scope.pagination.currentPage, pageSize: $scope.pagination.pageSize,
			  orderStatus: $scope.pagination.orderStatus, orderByIdAsc: $scope.pagination.orderByIdAsc }, { notify: false});
					
			loadEntities( $scope.pagination.currentPage, $scope.pagination.pageSize, $scope.pagination.orderStatus, $scope.pagination.orderByIdAsc);
	}
	
	/* Monitor new ArticleOrders */
	StompService.connect()
				.then( function( ) {
					if( $scope.pagination.orderStatus == 1) {
						StompService.subscribe( "/topic/admin/article-order-to-confirm",
												function( payload, headers, response) {
													if( $scope.entities.length < $scope.pagination.pageSize) {
														prepareEntity( payload);
														$scope.entities.push( payload);
														$scope.$apply();
													}
													else
													{
														loadEntities( $scope.pagination.currentPage, $scope.pagination.pageSize, $scope.pagination.orderStatus, $scope.pagination.orderByIdAsc);
													}
												}
											  );
					}
				});
	
	/* Show ArticleOrder */
	if( $stateParams.id != undefined) {
		$scope.isEntityLoading = true;
		
		OrderService.loadEntity( $stateParams.id)
					.then( function( response) {
						$scope.entity = response;
						
						prepareEntity( $scope.entity, 1);

						/* Websocket Monitor Entity */
						StompService.connect()
									.then( function() {
										StompService.subscribe( '/topic/article-order/' + $scope.entity.id,
																function( payload, headers, response) {
																	$scope.entity = payload;
																	prepareEntity( $scope.entity, 1);
															  });
									});
									/* TODO Unsubscribe when leaving */
						
						$scope.isEntityLoading = false;
					}, function( r) {
						console.error( r);
						$scope.isEntityLoading = false;
					});		
					
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
	}
}]);