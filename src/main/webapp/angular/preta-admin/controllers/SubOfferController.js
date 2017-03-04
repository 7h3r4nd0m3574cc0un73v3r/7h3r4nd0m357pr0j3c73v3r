'use strict';

App.controller( 'SubOfferController', [ '$filter', '$state', '$scope', '$uibModal', 'SubOfferService', 'shopStatuses', 'entity', function( $filter, $state, $scope, $uibModal, SubOfferService, shopStatuses, entity)
{
	/*SmartTable Config*/
	$scope.smartTablePageSize = 10;
	/*End SmartTable Config*/
	
	/*Modal Config */
	$scope.uibModalInstance = {};
    $scope.open = function (page, id, size) {
    	$scope.entity = { id: id};
    	
        $uibModal.open({
          animation: true,
          templateUrl: page,
          controller: 'SubOfferController',
          size: size,
          resolve: {
            items: function () {
              return $scope.items;
            },
            entity: function() { return { id: id};}
          }
        });
      };
	/* End Modal Config */
    
	$scope.entities = [];
	$scope.entity = entity;
	$scope.shopStatuses = shopStatuses;
	$scope.safeEntities = [];
	
	$scope.form = { isEnabled: true};
	$scope.formErrors = { conflict: false};
	
	$scope.loadEntities = function() {
		SubOfferService.loadEntities()
						.then( function( response) {
							$scope.safeEntities = response.entities;
						}, function( errResponse) {
							console.error( errResponse);
						});
	};
	
	$scope.addEntity = function() {
		$scope.form.shopStatus = angular.fromJson( $scope.form.shopStatus);
		SubOfferService.addEntity( $scope.form)
						.then( function( response) {
							$scope.loadEntities();
							$scope.entity = {};
							$state.go( 'root.subOffers', null, { reload: true} );
							
						}, function( errResponse) {
							console.error( errResponse);
						});
	};
	$scope.editEntity = function() {
		$scope.entity.shopStatus = angular.fromJson( $scope.entity.shopStatus);
		SubOfferService.editEntity( $scope.entity)
						.then( function( response) {
							$scope.loadEntities();
							$scope.entity = {};
							$state.go( 'root.subOffers', null, { reload: true} );
							
						}, function( errResponse) {
							console.error( errResponse);
						});
	};
	$scope.showDeleteModal = function( id) {
		$scope.entity = { id: id};
		
		 return $scope.uibModalInstance = 
	        $uibModal.open({
	          animation: true,
	          scope: $scope,
	          templateUrl: 'angular/common/components/modals/deleteModal.html',
	        });
	      
	      //uibModalInstance.close();
	};
	$scope.deleteEntity= function() {
		SubOfferService.deleteEntity( $scope.entity.id)
						.then( function( response) {
							$scope.loadEntities();
							$scope.entity = {};
							$scope.uibModalInstance.close();
						}, function( errResponse) {
							console.error( errResponse);
						});
	};
	$scope.resetEntity = function() {
		$scope.form = {};
		$scope.entity = entity;
	}
	
	$scope.loadEntities();
}]);