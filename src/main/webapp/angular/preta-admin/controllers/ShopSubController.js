'use strict';

App.controller( 'ShopSubController', [ '$state', '$stateParams', '$scope', '$uibModal', 'ShopSubService', 'PaymentService', 'entity',
                                       function( $state, $stateParams, $scope, $uibModal, ShopSubService, PaymentService, entity)
{
	/*SmartTable Config*/
	$scope.smartTablePageSize = 10;
	/*End SmartTable Config*/
	
	/* Breadcrumb custom */
	if( $stateParams.status == -1)
		$scope.breadCrumbLabel = "Abonnements";
	else if( $stateParams.status == 0)
		$scope.breadCrumbLabel = "Abonnements \xE0 payer";
	else if( $stateParams.status == 1)
		$scope.breadCrumbLabel = "Abonnements \xE0 confirmer";
	else if( $stateParams.status == 2)
		$scope.breadCrumbLabel = "Abonnements pay\xE8es";
	else if( $stateParams.status == 3)
		$scope.breadCrumbLabel = "Abonnements";
	else if( $stateParams.status == 4)
		$scope.breadCrumbLabel = "Abonnements";
	else
		$scope.breadCrumbLabel = "Abonnements";
	/* End Breadcrumb custom */
	
	$scope.entities = [];
	$scope.entity = entity;
	$scope.safeEntities = [];
	
	$scope.form = { isEnabled: true};
	$scope.formErrors = { conflict: false};
	
	$scope.loadEntities = function() {
		ShopSubService.loadEntitiesByStatus( $stateParams.status)
						.then( function( response) {
							$scope.safeEntities = response.entities;
						}, function( errResponse) {
							console.error( errResponse);
						});
	};
	
	$scope.loadEntities();
	
	/* Handle Payments */
	$scope.acceptPayment = function( id) {
		PaymentService.accept( id)
					  .then( function( response) {
						  console.log( response);
						  ShopSubService.getEntity( $scope.entity.id)
						  				.then( function( response) {
						  						console.log( response);
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
						  console.log( response);
						  ShopSubService.getEntity( $scope.entity.id)
						  				.then( function( response) {
					  							console.log( response);
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