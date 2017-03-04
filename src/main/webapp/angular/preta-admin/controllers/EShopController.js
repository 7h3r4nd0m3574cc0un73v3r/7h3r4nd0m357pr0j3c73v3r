'use strict';

App.controller( 'EShopController', [ '$state', '$scope','EShopService', 'entity', function( $state, $scope, EShopService, entity)
{
	/*SmartTable Config*/
	$scope.smartTablePageSize = 10;
	/*End SmartTable Config*/
	
	$scope.entities = [];
	$scope.entity = entity;
	$scope.safeEntities = [];
	
	$scope.loadEntities = function() {
		EShopService.loadEntities()
						.then( function( response) {
							var safeEntities = response.entities;
							angular.forEach( safeEntities, function( entity) {
								EShopService.loadManager( entity.id)
											.then( function( response) {
												entity.manager = response;
											}, function( response) {
												console.error( response);
											});
								EShopService.loadCurrentShopSub( entity.id)
											.then( function( response) {
												entity.currentShopSub = response;
											}, function( response) {
												console.error( response);
											});
							});
							$scope.safeEntities = safeEntities;
						}, function( errResponse) {
							console.error( errResponse);
						});
	};
	
	$scope.resetEntity = function() {
		$scope.form = {};
		$scope.formErrors = {};
		$scope.entity = entity;
	}
	
	$scope.validateEntity = function( id) {
		EShopService.validateEntity( $scope.entity.id)
					.then( function( response) {
						EShopService.loadEntity( entity.id)
									.then( function( response) {
										$scope.entity = response;
									}, function( response) {
										console.error( response);
									});
					}, function( response) {
						console.error( response);
					});
	};
	
	$scope.loadEntities();
}]);