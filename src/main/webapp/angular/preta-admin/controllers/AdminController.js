'use strict';

App.controller( 'AdminController', [ '$filter', '$state', '$scope', '$uibModal', 'AdminService', 'entity', function( $filter, $state, $scope, $uibModal, AdminService, entity)
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
          controller: 'UserController',
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
	
	$scope.form = { };
	$scope.formErrors = { conflict: false, passwordMismatch: false};
	
	$scope.loadEntities = function() {
		AdminService.listEntities()
						.then( function( response) {
							$scope.safeEntities = response;
						}, function( errResponse) {
							console.error( errResponse);
						});
	};
	
	$scope.validateForm = function() {
		$scope.formErrors = {};
		if( $scope.form == {} || $scope.form == null)
			return false;
		if( $scope.form.password != $scope.form.confirmPassword) {
			$scope.formErrors.passwordMismatch = true;
			return false;
		}
		
		return true;
	};
	
	$scope.addEntity = function() {
		if( !$scope.validateForm())
			return;
		
		console.log( $scope.form);
		AdminService.addEntity( $scope.form)
						.then( function( response) {
							$scope.loadEntities();
							$scope.entity = {};
							$state.go( 'root.admins', null, { reload: true} );
							
						}, function( errResponse) {
							console.error( errResponse);
						});
	};
	$scope.resetEntity = function() {
		$scope.form = {};
		$scope.formErrors = {};
		$scope.entity = entity;
	}
	
	$scope.loadEntities();
}]);