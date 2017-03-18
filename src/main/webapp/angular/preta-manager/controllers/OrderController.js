'use strict';

App.controller( 'OrderController', [ '$state', '$stateParams', '$scope', '$rootScope', 'OrderService', 'PaymentService', 'entity', 'StompService',
                                     function( $state, $stateParams, $scope, $rootScope, OrderService, PaymentService, entity, StompService)
{	
	/* Breadcrumb & Box Title custom */
	if( $stateParams.articleOrderStatus == -1) {
		$scope.breadCrumbLabel = "Commandes";
		$scope.boxTitle = "Toutes les Commandes";
	}
	else if( $stateParams.articleOrderStatus == 0) {
		$scope.breadCrumbLabel = "Commandes \xE0 payer";
		$scope.boxTitle = "Commandes en attente de paiement";
	}
	else if( $stateParams.articleOrderStatus == 1) {
		$scope.breadCrumbLabel = "Commandes \xE0 confirmer";
		$scope.boxTitle = "Commandes en attente de validation du paiement";
	}
	else if( $stateParams.articleOrderStatus == 2) {
		$scope.breadCrumbLabel = "Commandes pay\xE8es à livrer";
		$scope.boxTitle = "Commandes pay\xE8es - A LIVRER";
	}
	else if( $stateParams.articleOrderStatus == 3) {
		$scope.breadCrumbLabel = "Commandes en cours de livraison";
		$scope.boxTitle = "Commandes en cours de livraison";
	}
	else if( $stateParams.articleOrderStatus == 4) {
		$scope.breadCrumbLabel = "Commandes livr\xE8es";
		$scope.boxTitle = "Commandes livr\xE8es";
	}
	else if( $stateParams.articleOrderStatus == 5) {
		$scope.breadCrumbLabel = "Commandes en attente de r\xE8glement";
		$scope.boxTitle = "Commandes en attente de r\xE8glement";
	}
	else if( $stateParams.articleOrderStatus == 6) {
		$scope.breadCrumbLabel = "Commandes r\xE9gl\xE9es";
		$scope.boxTitle = "Commandes r\xE9gl\xE9es";
	}
	else {
		$scope.breadCrumbLabel = "Commandes";
		$scope.boxTitle = "Commandes";
	}
		
	/* End Breadcrumb custom */
	
	/* Init Var */
	$scope.entities = [];
	$scope.entity = entity;
	
	/* Pagination Settings */
	$scope.pagination = { currentPage: $stateParams.articleOrdersPage == undefined ? 1 : $stateParams.articleOrdersPage,
						  pageSize: $stateParams.articleOrdersPageSize == undefined ? 10 : $stateParams.articleOrdersPageSize,
						  orderByIdAsc: $stateParams.articleOrdersOrderByIdAsc == undefined ? true : $stateParams.articleOrdersOrderByIdAsc,
						  articleOrderStatus: $stateParams.articleOrderStatus == undefined ? -1 : $stateParams.articleOrderStatus,
					      pagesNumber: null,
					      itemsNumber: null
						};
	/* End Pagination Settings */

	/* Loading Mutexes */
	$scope.isListLoading = true;
	$scope.isEntityLoading = true;
	
	$scope.loadEntities = function( page, pageSize, orderByIsAsc, articleOrderStatus) {
		$scope.isListLoading = true;
		OrderService.loadByManagerAndStatus( page, pageSize, orderByIsAsc, articleOrderStatus)
						.then( function( response) {
							
							angular.forEach( response.entities, function( entity) {
								prepareEntity( entity);
								/*
								OrderService.loadBuyer( entity.id)
											.then( function( response) {
												entity.buyer = response;
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
											}, function( r) {
												console.error( r);
											});
								OrderService.loadEShop( entity.id)
											.then( function( response) {
												entity.eShop = response;
											}, function( response) {
												console.error( response);
											});
								*/
							});
							
							/* Save to scope */
							$scope.pagination.pagesNumber = response.pagesNumber;
							$scope.pagination.itemsNumber = response.itemsNumber;
							$scope.entities = response.entities;
							$stateParams.articleOrdersPage = $scope.pagination.currentPage;
							
							$scope.isListLoading = false;
						}, function( errResponse) {						
							console.error( errResponse);
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
	
	/* Call to loadEntities */
	$scope.loadEntities( $scope.pagination.currentPage, $scope.pagination.pageSize,
						 $scope.pagination.orderByIdAsc, $scope.pagination.articleOrderStatus);
	
	/* Page Change */
	$scope.changePage = function() {
			$state.go( $state.current.name, { articleOrdersPage: $scope.pagination.currentPage, articleOrdersPageSize: $scope.pagination.pageSize,
											articleOrdersOrderByIdAsc: $scope.pagination.orderByIdAsc, articleOrderStatus: $scope.pagination.articleOrderStatus }, { notify: false});
			
			$scope.loadEntities( $scope.pagination.currentPage, $scope.pagination.pageSize, $scope.pagination.orderByIdAsc, $scope.pagination.articleOrderStatus);
	}
	
	/* Mark Entity as Delivered */
	$scope.deliverEntity = function() {
		if( $scope.isValidForm()) {
			OrderService.deliverEntity( $scope.entity.id, $scope.entity.packageId)
			.then( function( response) {
//				$scope.updateEntity( $scope.entity.id);
			}, function( r) {
				console.error( r);
			});
			
		}
			
	};
	
	$scope.isValidForm = function() {
		$scope.initFormErrors();
		if( $scope.entity.packageId == "" || $scope.entity.packageId == null) {
			$scope.formErrors = { invalidPackageId: true };
			return false;
		}
		return true;
	};
	
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
	}
	
	/* Error Handling */
	$scope.initFormErrors = function() {
		$scope.formErrors = { invalidPackageId: false };
	};
	
	$scope.formErrors = {};
	$scope.initFormErrors();
}]);