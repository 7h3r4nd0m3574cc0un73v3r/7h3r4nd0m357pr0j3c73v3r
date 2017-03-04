'use strict';

App.controller( 'AdvOfferController', [ '$state', '$stateParams', '$scope', '$uibModal', 'AdvOfferService', 'EShopService', 'ArticleService', 'PaymentService',
                                        function( $state, $stateParams, $scope, $uibModal, AdvOfferService, EShopService, ArticleService, PaymentService)
{
	/* SmartTable Config */
	$scope.smartTablePageSize = 10;
	/* End SmartTable Config */
	
	/* Breadcrumb custom */

	/* Breadcrumb & Box Title custom */
	if( $stateParams.advOfferStatus == -1) {
		$scope.breadCrumbLabel = "Publicit\xE9s";
		$scope.boxTitle = "Toutes les publicit\xE9s";
	}
	else if( $stateParams.advOfferStatus == 0) {
		$scope.breadCrumbLabel = "Publicit\xE9s \xE0 payer";
		$scope.boxTitle = "Publicit\xE9s en attente de paiement";
	}
	else if( $stateParams.advOfferStatus == 1) {
		$scope.breadCrumbLabel = "Publicit\xE9s \xE0 confirmer";
		$scope.boxTitle = "Publicit\xE9s en attente de validation du paiement";
	}
	else if( $stateParams.advOfferStatus == 2) {
		$scope.breadCrumbLabel = "Publicit\xE9s pay\xE8es à livrer";
		$scope.boxTitle = "Publicit\xE9s pay\xE8es - A LIVRER";
	}
	else if( $stateParams.advOfferStatus == 3) {
		$scope.breadCrumbLabel = "Publicit\xE9s en cours";
		$scope.boxTitle = "Publicit\xE9s en cours";
	}
	else if( $stateParams.advOfferStatus == 4) {
		$scope.breadCrumbLabel = "Publicit\xE9s termin\xE9es";
		$scope.boxTitle = "Publicit\xE9s termin\xE9es";
	}
	else {
		$scope.breadCrumbLabel = "Publicit\xE9s";
		$scope.boxTitle = "Publicit\xE9s";
	}
	/* End Breadcrumb custom */

	/* Pagination Settings */
	$scope.pagination = { currentPage: $stateParams.advOfferPage == undefined ? 1 : $stateParams.advOfferPage,
						  pageSize: $stateParams.advOfferPageSize == undefined ? 10 : $stateParams.advOfferPageSize,
						  orderByIdAsc: $stateParams.advOfferOrderByIdAsc == undefined ? true : $stateParams.advOfferOrderByIdAsc,
						  status: $stateParams.advOfferStatus == undefined ? -1 : $stateParams.advOfferStatus,
					      pagesNumber: null,
					      itemsNumber: null
						};
	/* End Pagination Settings */

	/* Loading Mutexes */
	$scope.isListLoading = true;
	$scope.isEntityLoading = true;
	
	/* Init Var */
	$scope.entities = [];
	$scope.entity;
	
	$scope.loadEntities = function( page, pageSize, status, orderByIdAsc) {
		AdvOfferService.loadEntities( page, pageSize, status, orderByIdAsc)
						.then( function( response) {
							/* Save to scope */
							$scope.pagination.pagesNumber = response.pagesNumber;
							$scope.pagination.itemsNumber = response.itemsNumber;
							$stateParams.advOfferPage = $scope.pagination.currentPage;
							$scope.entities = response.entities;
							
							$scope.isListLoading = false;
						}, function( errResponse) {
							console.error( errResponse);
							$scope.isListLoading = false;
						});
	};
	
	/* Loading Entities */
	$scope.loadEntities( $scope.pagination.page, $scope.pagination.pageSize, $scope.pagination.status, $scope.pagination.orderByIdAsc);
	
	/* Show Entity */
	$scope.loadEntity = function() {
		$scope.isEntityLoading = true;
		AdvOfferService.loadEntity( $stateParams.id)
					   .then( function( response) {
						   /* Save to scope */
						   $scope.entity = response;
						   /* Load Concerned EShop */
						   ArticleService.loadEShop( $scope.entity.article.id)
						   				 .then( function( response) {
						   					 $scope.entity.article.eShop = response;
						   				 }, function( response) {
						   					 console.error( response);
						   				 });
						   /* Update EShop with Manager once it's loaded */
						   $scope.$watch( 'entity.article.eShop', function( newValue, oldValue) {
							    if( newValue != undefined && newValue.id != null) {
							    	EShopService.loadManager( newValue.id)
							    				.then( function( response) {
							    					newValue.manager = response;
							    				}, function( response) {
							    					console.error( response);
							    				});
							    }
						   }, true);
						   
						   AdvOfferService.loadPayments( $scope.entity.id, 1, 0)
						   				  .then( function( response) {
						   					  $scope.entity.payments = response.entities;
						   				  }, function( response) {
						   					  console.error( response);
						   				  });
						   
						   $scope.isEntityLoading = false;
					   }, function( response) {
						   console.error( response);
						   $scope.isEntityLoading = false;
					   });
	}
	
	if( $stateParams.id != undefined) {
		$scope.loadEntity();
	}
	
	/* Handle Payments */
	$scope.acceptPayment = function( id) {
		PaymentService.accept( id)
					  .then( function( response) {
						  AdvOfferService.loadEntity( $scope.entity.id)
						  				.then( function( response) {
						  						$scope.entity = response;
						  					  }, function( response) {
						  						  console.error( response);
						  					  });
					  }, function( response) {
						  console.error( response);
					  });
	};
	$scope.rejectPayment = function( id) {
		PaymentService.reject( id)
					  .then( function( response) {
						  AdvOfferService.loadEntity( $scope.entity.id)
						  				.then( function( response) {
					  							$scope.entity = response;
						  					  }, function( response) {
						  						  console.error( response);
						  					  });
					  }, function( response) {
						  console.error( response);
					  });
	};
	/* End Payment */
	
}]);