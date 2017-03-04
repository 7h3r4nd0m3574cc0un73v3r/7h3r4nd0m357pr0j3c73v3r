'use strict';

App.controller( 'EMPController', [ '$state', '$scope', '$uibModal', 'EMPService', 'entity', function( $state, $scope, $uibModal, EMPService, entity)
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
          controller: 'EMPController',
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
	
	$scope.form = {};
	
	$scope.formErrors = { conflict: false};
	
	$scope.refresh = function() {
		$scope.loadEntities();
	};
	
	$scope.loadEntities = function() {
		EMPService.loadEntities()
					.then ( function( response) {
							console.log( response);
							$scope.safeEntities = response.entities;
						},
						function( errResponse) {
							console.error( 'Error: EMPController > listEMP');
						});
	};
	
	$scope.resetEntity = function() {
		$scope.form = {};
		$scope.error = {};
	};
    
	$scope.addEntity = function( file) {
		EMPService.addEntity( $scope.form, file)
					.then( function( response) {
							$scope.loadEntities();
							$scope.resetEntity();
							$state.go( 'root.emps', null, { reload: true} );
						},
						function( errResponse) {
							if( errResponse.status == 409) {
								$scope.formErrors.conflict = true;
							}
							console.error( 'Error: EMPController > addEMP');
						});
	};
	
	$scope.updateEntity = function( file) {
		EMPService.updateEntity( $scope.entity, file)
					.then( function( response) {
							$scope.loadEntities();
							$scope.resetEntity();
							$state.go( 'root.emps', null, { reload: true} );
						},
						function( errResponse) {
							if( errResponse.status == 409) {
								$scope.formErrors.conflict = true;
							}
							console.error( 'Error: EMPController > editEMP');
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
		EMPService.deleteEntity( $scope.entity.id)
						.then( function( response) {
							$scope.loadEntities();
							$scope.entity = {};
							$scope.uibModalInstance.close();
						}, function( errResponse) {
							console.error( errResponse);
						});
	};
	
	$scope.loadEntities();
}]);