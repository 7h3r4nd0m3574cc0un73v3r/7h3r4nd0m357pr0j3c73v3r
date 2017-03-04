'use strict';

App.controller( 'OrderController', [ '$state', '$stateParams', '$scope', '$rootScope', 'OrderService', 'PaymentService', 'entity',
                                     function( $state, $stateParams, $scope, $rootScope, OrderService, PaymentService, entity)
{
	/*SmartTable Config*/
	$scope.smartTablePageSize = 10;
	/*End SmartTable Config*/
	
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
								OrderService.getBuyer( entity.id)
											.then( function( response) {
												entity.buyer = response;
											}, function( response) {
												console.error( response);
											});
								/* OrderService.loadPayments( entity.id)
											.then( function( response) {
												entity.payments = response;
											}, function( response) {
												console.error( response);
											}); */
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
								OrderService.getEShop( entity.id)
											.then( function( response) {
												entity.eShop = response;
											}, function( response) {
												console.error( response);
											});
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
		console.log( $scope.entity);
		if( $scope.isValidForm()) {
			OrderService.deliverEntity( $scope.entity.id, $scope.entity.packageId)
			.then( function( response) {
				console.log( response);
				$scope.updateEntity( $scope.entity.id);
			}, function( response) {
				console.error( response);
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
	
	
	$scope.updateEntity = function( id) {
		OrderService.loadEntity( id)
					.then( function( response) {
						var entity = response;
						OrderService.loadOrderedArticlesByOrder( id)
									.then( function( orderedArticles) {
										entity.orderedArticles = orderedArticles;
										entity.total = 0;
										angular.forEach( entity.orderedArticles, function( value) {
											entity.total += value.article.price * value.quantity;
										});
									}, function( errResponse) {
										console.error( errResponse);
									});
						OrderService.getBuyer( id)
									.then( function( buyer) {
										entity.buyer = buyer;
									}, function( errResponse) {
										console.error( errResponse);
									});
						
						$scope.entity = entity;
					}, function( response) {
						console.error( response);
					});
	};
	
	/* Error Handling */
	$scope.initFormErrors = function() {
		$scope.formErrors = { invalidPackageId: false };
	};
	
	$scope.formErrors = {};
	$scope.initFormErrors();
}]);