'use strict';

App.controller( 'AdvOfferController', [ '$state', '$stateParams', '$scope', '$rootScope', 'AdvOfferService', 'EShopService', 'ArticleService', 'PaymentService',
                                     function( $state, $stateParams, $scope, $rootScope, AdvOfferService, EShopService, ArticleService, PaymentService)
{
	/* MD DatePicker Config */
	$scope.minDate = new Date();
	/* End MD DatePicker Config */
	
	/*SmartTable Config*/
	$scope.smartTablePageSize = 10;
	/*End SmartTable Config*/
	
	/* Breadcrumb & Box Title custom */
	if( $stateParams.advOfferStatus == -1) {
		$scope.breadCrumbLabel = "Toutes les publicit\xE9s";
		$scope.boxTitle = "Toutes les publicit\xE9s";
	}
	else if( $stateParams.advOfferStatus == 0) {
		$scope.breadCrumbLabel = "Pubs. En attente du paiement";
		$scope.boxTitle = "Pubs. En attente du paiement";
	}
	else if( $stateParams.advOfferStatus == 1) {
		$scope.breadCrumbLabel = "Pubs. En attente de validation";
		$scope.boxTitle = "Pubs. En attente de validation";
	}
	else if( $stateParams.advOfferStatus == 2) {
		$scope.breadCrumbLabel = "Publicit\xE9s activables";
		$scope.boxTitle = "Publicit\xE9s activables";
	}
	else if( $stateParams.advOfferStatus == 3) {
		$scope.breadCrumbLabel = "Publicit\xE9s en cours";
		$scope.boxTitle = "Publicit\xE9s en cours";
	}
	else if( $stateParams.advOfferStatus == 4) {
		$scope.breadCrumbLabel = "Publicit\xE9s expir\xE9es";
		$scope.boxTitle = "Publicit\xE9s expir\xE9es";
	}
	else {
		$scope.breadCrumbLabel = "Publicit\xE9s";
		$scope.boxTitle = "Publicit\xE9s";
	}
		
	/* End Breadcrumb custom */
	
	/* Init Var */
	$scope.entities = [];
	$scope.entity = {};
	
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
	
	$scope.loadEntities = function( page, pageSize, status, orderByIdAsc) {
		$scope.isListLoading = true;
		AdvOfferService.loadEntitiesByManager( page, pageSize, status, orderByIdAsc)
				.then( function( response) {
							
							/* Save to scope */
							$scope.pagination.pagesNumber = response.pagesNumber;
							$scope.pagination.itemsNumber = response.itemsNumber;
							$stateParams.advOfferPage = $scope.pagination.currentPage;
							$scope.entities = response.entities;
							
							$scope.isListLoading = false;
						}, function( response) {		
							console.error( response);
							$scope.isListLoading = false;
						});
	};
	
	/* Call to loadEntities */
	$scope.loadEntities( $scope.pagination.currentPage, $scope.pagination.pageSize, $scope.pagination.status, 
						 $scope.pagination.orderByIdAsc);
	
	/* Page Change */
	$scope.changePage = function() {
			$state.go( $state.current.name, { advOfferPage: $scope.pagination.currentPage, advOfferPageSize: $scope.pagination.pageSize,
											  advOfferOrderByIdAsc: $scope.pagination.orderByIdAsc, advOfferStatus: $scope.pagination.advOfferStatus }, { notify: false});
			
			$scope.loadEntities( $scope.pagination.currentPage, $scope.pagination.pageSize, $scope.pagination.status, 
					 $scope.pagination.orderByIdAsc);
	}
	
	/* New Entity */
	$scope.form = { selectedEShop: null, advOffer: null};
	$scope.formErrors = {};
	
	$scope.advOptions = [];
	$scope.eShops = [];
	$scope.articles = [];

	$scope.selectedAdminEAccount = undefined;
	$scope.adminEAccounts = [];
	
	/* Load  AdminEAccount */
	PaymentService.loadAdminEAccountsForPayments()
				  .then( function( response) {
					  $scope.adminEAccounts = response.entities;
					  
					  $scope.$watch( 'form.eAccount', function( newValue, oldValue) {
							if( $scope.form.eAccount != undefined) {
								angular.forEach( $scope.adminEAccounts, function( adminEAccount) {
									
									if( adminEAccount.emp.id == newValue.emp.id) {
										$scope.selectedAdminEAccount = adminEAccount;
									}
								});
							}
							else {
								$scope.selectedAdminEAccount = null;
							}
						});
					  
				  }, function( response) {
					  console.error( response);
				  });
	
	$scope.loadAdvOptions = function() {
		AdvOfferService.loadAdvOptions( 1, 0)
					   .then( function( response) {
						   $scope.advOptions = response.entities;
					   }, function( response) {
						   console.error( response);
					   });
	}
	
	$scope.loadEShops = function() {
		EShopService.loadEntitiesByManager( $rootScope.loggedUser.userInfo.id, 1, 0)
					.then( function( response) {
						$scope.eShops = response.entities;
					}, function( response) {
						console.error( response);
					});
	}
	
	$scope.loadArticles = function() {
		ArticleService.loadEntitiesByEShop( $scope.form.selectedEShop.id, 1, 0)
					  .then( function( response) {
						  $scope.articles = response.entities;
					  }, function( response) {
						  console.error( response);
					  });
	}
	
	$scope.addEntity = function() {
		AdvOfferService.addEntity( $scope.form, $scope.selectedAdminEAccount)
						.then( function( response) {
							$scope.loadEntities();
							$scope.entity = {};
							$state.go( 'root.adv-offers', null, { reload: true});
						}, function( response) {
							console.error( response);
						});
	}
	
	$scope.actDeactEntity = function( id) {
		AdvOfferService.actDeactEntity( id)
				.then( function( response) {
					$scope.loadEntities( $scope.pagination.currentPage, $scope.pagination.pageSize, $scope.pagination.status, 
										 $scope.pagination.orderByIdAsc);
				}, function( response) {
					console.error( response);
				});
	}
	
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
}]);