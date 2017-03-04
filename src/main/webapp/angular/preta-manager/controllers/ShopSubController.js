'use strict';

App.controller( 'ShopSubController', [ '$state', '$stateParams', '$scope', 'ShopSubService', 'EShopService', 'PaymentService', 'subOffers',
                                       function( $state, $stateParams, $scope, ShopSubService, EShopService, PaymentService, subOffers)
{
	/* MD DatePicker Config */
	$scope.minDate = new Date();
	/* End MD DatePicker Config */
	
	/* Load Active EShop */
	$scope.activeEShop = EShopService.activeEShop;

	/* Pagination Settings */
	$scope.pagination = { currentPage: $stateParams.shopSubsPage == undefined ? 1 : $stateParams.shopSubsPage,
						  pageSize: $stateParams.shopSubsPageSize == undefined ? 10 : $stateParams.shopSubsPageSize,
						  orderByIdAsc: $stateParams.shopSubsOrderByIdAsc == undefined ? true : $stateParams.shopSubsOrderByIdAsc,
					      pagesNumber: null,
					      itemsNumber: null
						};
	/* End Pagination Settings */
	
	/* Loading Mutexes */
	$scope.isListLoading = true;
	$scope.isEntityLoading = true;
	
	/* Init Var */
	$scope.entities = [];
	$scope.subOffers = subOffers.entities;
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
	
	/* Page Change */
	$scope.changePage = function() {
			$state.go( $state.current.name, { shopSubsPage: $scope.pagination.currentPage, shopSubsPageSize: $scope.pagination.pageSize }, { notify: false});
			$scope.loadEntities( $scope.pagination.currentPage, $scope.pagination.pageSize);
	}
	
	/* Load Entities Def and Call */
	$scope.loadEntities = function( page, pageSize, orderByIdAsc) {
		$scope.isListLoading = true;
		ShopSubService.loadEntitiesByEShop( $stateParams.eShopId, $scope.pagination.currentPage,
											$scope.pagination.pageSize)
						.then( function( response) {
							$scope.pagination.pagesNumber = response.pagesNumber;
							$scope.pagination.itemsNumber = response.itemsNumber;
							$scope.entities = response.entities;
							$stateParams.shopSubsPage = $scope.pagination.currentPage;
						
							if( response.entities == undefined){
								$scope.isListLoading = false;
								return;
							}
							
							$scope.isListLoading = false;
						}, function( response) {
						  $scope.isListLoading = false;
						  console.error( response);
						});
	};
	
	$scope.loadEntities( $scope.pagination.currentPage, $scope.pagination.pageSize,
						 $scope.pagination.orderByIdAsc);
	
	/* Load Selected Entity */
	if( $stateParams.id != undefined ) {
		$scope.isEntityLoading = true;
		ShopSubService.loadEntity( $stateParams.id)
					  .then( function( response) {
						  $scope.entity = response;
						  $scope.isEntityLoading = false;
					  }, function( response) {
						  console.error( response);
						  $scope.isEntityLoading = false;
					  });
	}
	
	/* New Entity */
	$scope.form = {};
	$scope.formErrors = { conflict: false};
	
	$scope.addEntity = function() {
		ShopSubService.addEntity( $scope.form, $scope.selectedAdminEAccount)
						.then( function( response) {
							$scope.loadEntities();
							$scope.entity = {};
							$state.go( 'root.e-shops.show.shop-subs', null, { reload: true});
						}, function( errResponse) {
							console.error( errResponse);
						});
	};
	
	/* Actiavte ShopSub */
	$scope.activate = function( id) {
		$scope.isListLoading = true;
		ShopSubService.activate( id)
					  .then( function( response) {
						  $scope.loadEntities( $scope.pagination.currentPage, $scope.pagination.pageSize);
						  $scope.isListLoading = false;
					  }, function( response) {
						  console.error( response);
						  $scope.isListLoading = false;
					  });
	};
	
	$scope.resetEntity = function() {
		$scope.form = {};
		$scope.entity = {};
	};
}]);