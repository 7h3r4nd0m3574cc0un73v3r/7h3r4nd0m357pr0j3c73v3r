'use strict';

App.controller( 'SlideController', [ '$state', '$scope', '$uibModal', 'SlideService', '$stateParams',
                                     function( $state, $scope, $uibModal, SlideService, $stateParams)
{
  	/* Loading Mutexes */
  	$scope.isListLoading = true;
  	$scope.isEntityLoading = true;
  	
  	/* Handle PagedListJSON ArticleOrders */
  	$scope.entities = [];
  	$scope.undisplayedEntities = [];
  	$scope.form = { file: null, entity: {}};
  	$scope.test = { file: null };
  	$scope.maxDispOrder = 0;
  	
	/* Pagination Settings */
	$scope.pagination = { currentPage: $stateParams.page == undefined ? 1 : $stateParams.page,
						  pageSize: $stateParams.pageSize == undefined ? 10 : $stateParams.pageSize,
						  orderByDispOrderAsc: $stateParams.orderByDispOrderAsc == undefined ? true : $stateParams.orderByDispOrderAsc,
					      pagesNumber: null,
					      itemsNumber: null
						};
	/* End Pagination Setting */
	
	/* Loading func */
	function loadEntities( page, pageSize, orderByDispOrderAsc) {
		$scope.isListLoading = true;
		
		SlideService.loadEntities( page, pageSize, orderByDispOrderAsc)
					.then( function( r) {
						/* Update Pagination info and Scope */
						$scope.pagination.pagesNumber = r.pagesNumber;
						$scope.pagination.itemsNumber = r.itemsNumber;
						$stateParams.page = $scope.pagination.currentPage;
						$scope.entities = [];
						$scope.undisplayedEntities = [];
						
						angular.forEach( r.entities, function( ent, key) {
							if( $scope.maxDispOrder < ent.displayOrder) {
								$scope.maxDispOrder = ent.displayOrder;
							}
							
							if( !ent.displayed) {
								$scope.undisplayedEntities.push( ent);
							} else {
								$scope.entities.push( ent);
							}
						});
						
						$scope.isListLoading = false;
					}, function( r) {
						console.error( r);
						$scope.isListLoading = false;
					});
	}
	
	/* Load Entities */
	loadEntities( $scope.pagination.currentPage, $scope.pagination.pageSize,
				  $scope.pagination.orderByDispOrderAsc);
	
	/* New Entity */
	$scope.addEntity = function() {
		$scope.isEntityLoading = true;
		
		console.log( $scope.form);
		
		SlideService.addEntity( $scope.form)
					.then( function( r) {
						$scope.resetEntity();
						$state.go( 'root.slides', {}, { reload: true});
					}, function( r) {
						console.error( r);
					});
	}
	$scope.resetEntity = function() {
		$scope.form = { entity: { displayed: true}, file: null};
	}

	/*Modal Config */
	$scope.uibModalInstance = {};
    $scope.open = function (page, id, size) {
    	$scope.entity = { id: id};
    	
        $uibModal.open({
          animation: true,
          templateUrl: page,
          controller: 'SlideController',
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
      
    /* Updates */
    $scope.moveEntity = function( id, moveUp) {
    	SlideService.moveEntity( id, moveUp)
    				.then( function( r){
    					console.log( r);
    					loadEntities( $scope.pagination.currentPage, $scope.pagination.pageSize,
    							  $scope.pagination.orderByDispOrderAsc);
    				}, function( r) {
    					console.error( r);
    				});
    }
    
    $scope.changeDisplayed = function( id) {
    	SlideService.changeDisplayed( id)
    				.then( function( r) {
    					console.log( r);
    					loadEntities( $scope.pagination.currentPage, $scope.pagination.pageSize,
    							  $scope.pagination.orderByDispOrderAsc);
    					
    				}, function( r) {
    					console.error( r);
    				});
    }

    $scope.deleteEntity = function( id) {
    	SlideService.deleteEntity( id)
    				.then( function( r) {
    					console.log( r);
    					loadEntities( $scope.pagination.currentPage, $scope.pagination.pageSize,
    							  $scope.pagination.orderByDispOrderAsc);
    					
    				}, function( r) {
    					console.error( r);
    				});
    }
    
}]);