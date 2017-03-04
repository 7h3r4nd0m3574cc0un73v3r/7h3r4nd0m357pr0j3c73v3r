'use strict';

App.controller( 'AdvOptionController', ['$state', '$scope', '$uibModal', 'AdvOptionService', 'entity',
                                        function( $state, $scope, $uibModal, AdvOptionService, entity)
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
          controller: 'AdvOptionController',
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
	
	$scope.loadEntities = function() {
		AdvOptionService.loadEntities()
						.then( function( response) {
							$scope.safeEntities = response.entities;
						}, function( errResponse) {
							console.error( errResponse);
						});
	};
	
	$scope.addEntity = function() {		
		AdvOptionService.addEntity( $scope.form)
						.then( function( response) {
							$scope.loadEntities();
							$scope.entity = {};
							$state.go( 'root.adv-options', null, { reload: true});
						}, function( response) {
							initFormErrors();
							processErrors( response.data.errors);
							console.error( response);
						});
	};
	
	$scope.editEntity = function() {
		AdvOptionService.editEntity( $scope.entity)
						.then( function( response) {
							$scope.loadEntities();
							$scope.entity = {};
							$state.go( 'root.adv-options', null, { reload: true} );
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
		AdvOptionService.deleteEntity( $scope.entity.id)
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
		initFormErrors();
	}
	
	$scope.loadEntities();
	
	function initFormErrors() {
		$scope.formErrors = { conflict: false,
				  title: { notNull: false, conflict: false },
				  description: { notNull: false, conflict: false},
				  price: { notNull: false, conflict: false},
				  duration: { notNull: false, conflict: false},
				  durationType: { notNull: false, conflict: false}
				 };
	};
	
	function processErrors( errors) {
		angular.forEach( errors, function( error) {
			if( error.field == "title") {
				if( error.code == "NotNull")
					$scope.formErrors.title.notNull = true;
			}
			if( error.field == "price") {
				if( error.code == "NotNull")
					$scope.formErrors.price.notNull = true;
			}
			if( error.field == "description") {
				if( error.code == "NotNull")
					$scope.formErrors.description.notNull = true;
			}
			if( error.field == "duration") {
				if( error.code == "NotNull")
					$scope.formErrors.duration.notNull = true;
			}
			if( error.field == "durationType") {
				if( error.code == "NotNull")
					$scope.formErrors.durationType.notNull = true;
			}
		});
	};
}]);