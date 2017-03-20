'use strict';

App.controller( 'OrderController', [ '$state', '$stateParams', '$scope', 'OrderService', 'PaymentService', 'StompService',
                                     function( $state, $stateParams, $scope, OrderService, PaymentService, StompService)
{
	/* Breadcrumb custom */
	if( $stateParams.orderStatus == -1)
		$scope.breadCrumbLabel = "Commandes";
	else if( $stateParams.orderStatus == 0)
		$scope.breadCrumbLabel = "Commandes \xE0 payer";
	else if( $stateParams.orderStatus == 1)
		$scope.breadCrumbLabel = "Commandes \xE0 confirmer";
	else if( $stateParams.orderStatus == 2)
		$scope.breadCrumbLabel = "Commandes pay\xE8es";
	else if( $stateParams.orderStatus == 3)
		$scope.breadCrumbLabel = "Commandes à livrer";
	else if( $stateParams.orderStatus == 4)
		$scope.breadCrumbLabel = "Commandes livr\xE8es";
	else if( $stateParams.orderStatus == 5)
		$scope.breadCrumbLabel = "Commandes \xE0 r\xE9gler";
	else if( $stateParams.orderStatus == 6)
		$scope.breadCrumbLabel = "Commandes r\xE9gl\xE9es";
	else
		$scope.breadCrumbLabel = "Commandes";
	/* End Breadcrumb custom */
	
	/* Loading Mutexes */
	$scope.isListLoading = true;
	$scope.isEntityLoading = true;
	
	/* Handle PagedListJSON ArticleOrders */
	$scope.entities = [];
	$scope.entity = {};
	$scope.selection =  { count: 0, total: 0 };

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
		
		OrderService.loadAddressedEntities( page, pageSize, orderStatus, orderByIdAsc)
					.then( function( response) {
						/* Update Pagination info and Scope */
						$scope.pagination.pagesNumber = response.pagesNumber;
						$scope.pagination.itemsNumber = response.itemsNumber;
						$stateParams.page = $scope.pagination.currentPage;
						$scope.entities = response.entities;
						
						angular.forEach( $scope.entities, function( entity) {
							prepareEntity( entity, 1);
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
							/* Expenses */
							entity.selected = false;
							entity.disabled = false;
							/* End Expenses */
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
	
	$scope.selectOrder = function( id) {
		angular.forEach( $scope.entities, function( entity) {
			if( entity.id == id)
				entity.selected = !entity.selected;
		});
	}
	
	$scope.$watch( 'entities', function( newValue, oldValue) {
		if( newValue.length) {
			console.log( "Specific Change Detected");
			var reference;
			
			$scope.selection.count = 0;
			$scope.selection.total = 0;
			
			angular.forEach( newValue, function( entity) {
				if( entity.selected) {
					reference = entity;
					console.log( entity);
					$scope.selection.count++;
					$scope.selection.total += entity.total;
				}
			});
			
			angular.forEach( newValue, function( entity) {
				if( entity.eShop != undefined && reference != null) {
					if( entity.eShop.id != reference.eShop.id)
						entity.disabled = true;
					else
						entity.disabled = false;
				}
				
				if( reference == null)
					entity.disabled = false;
			});
		}
		
	}, true);
}]);