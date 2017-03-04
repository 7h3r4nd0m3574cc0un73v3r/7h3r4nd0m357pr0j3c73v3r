'use strict';

App.controller( 'ShopStatusController', [ '$filter', '$state', '$scope', '$uibModal', 'ShopStatusService', 'entity', function( $filter, $state, $scope, $uibModal, ShopStatusService, entity)
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
          controller: 'ShopStatusController',
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
	$scope.safeEntities = [];
	
	$scope.form = { isEnabled: true};
	$scope.formErrors = { conflict: false};
	
	$scope.loadEntities = function() {
		ShopStatusService.loadEntities()
						.then( function( response) {
							$scope.safeEntities = response.entities;
						}, function( errResponse) {
							console.error( errResponse);
						});
	};
	
	$scope.addEntity = function() {		
		ShopStatusService.addEntity( $scope.form)
						.then( function( response) {
							$scope.loadEntities();
							$scope.entity = {};
							$state.go( 'root.shopStatuses', null, { reload: true} );
							
						}, function( errResponse) {
							console.error( errResponse);
						})	
	};
	$scope.editEntity = function() {
		ShopStatusService.editEntity( $scope.entity)
						.then( function( response) {
							$scope.loadEntities();
							$scope.entity = {};
							$state.go( 'root.shopStatuses', null, { reload: true} );
							
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
		ShopStatusService.deleteEntity( $scope.entity.id)
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