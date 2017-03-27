'use strict';

App.controller( 'LocalMarketController', [ '$state', '$scope', '$uibModal', 'LocalMarketService', '$stateParams',
                                     function( $state, $scope, $uibModal, LocalMarketService, $stateParams)
{
  	/* Loading Mutexes */
  	$scope.isListLoading = true;
  	$scope.isEntityLoading = true;
  	
  	/* Handle PagedListJSON ArticleOrders */
  	$scope.entities = [];
  	$scope.form = { entity: { displayed: true}};
  	
	/* Pagination Settings */
	$scope.pagination = { currentPage: $stateParams.page == undefined ? 1 : $stateParams.page,
						  pageSize: $stateParams.pageSize == undefined ? 10 : $stateParams.pageSize,
						  orderByIdAsc: $stateParams.orderByIdAsc == undefined ? true : $stateParams.orderByIdAsc,
					      pagesNumber: null,
					      itemsNumber: null
						};
	/* End Pagination Setting */
	
	/* Loading func */
	function loadEntities( page, pageSize, orderByIdAsc) {
		$scope.isListLoading = true;
		
		LocalMarketService.loadEntities( page, pageSize, orderByIdAsc)
					.then( function( r) {
						/* Update Pagination info and Scope */
						$scope.pagination.pagesNumber = r.pagesNumber;
						$scope.pagination.itemsNumber = r.itemsNumber;
						$stateParams.page = $scope.pagination.currentPage;
						$scope.entities = r.entities;
						
						$scope.isListLoading = false;
					}, function( r) {
						console.error( r);
						$scope.isListLoading = false;
					});
	}
	
	/* Load Entities */
	loadEntities( $scope.pagination.currentPage, $scope.pagination.pageSize,
				  $scope.pagination.orderByIdAsc);
	
	/* New Entity */
	$scope.promise;
	$scope.addEntity = function() {
		$scope.promise = LocalMarketService.addEntity( $scope.form.entity)
					.then( function( r) {
						$scope.resetEntity();
						$state.go( 'root.local-markets', {}, { reload: true});
					}, function( r) {
						console.error( r);
					});
	}
	$scope.resetEntity = function() {
		$scope.form = { entity: { displayed: true}};
	}

	/*Modal Config */
	$scope.uibModalInstance = {};
    $scope.open = function (page, id, size) {
    	$scope.entity = { id: id};
    	
        $uibModal.open({
          animation: true,
          templateUrl: page,
          controller: 'LocalMarketController',
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

    $scope.deleteEntity = function( id) {
    	LocalMarketService.deleteEntity( id)
    				.then( function( r) {
    					console.log( r);
    					loadEntities( $scope.pagination.currentPage, $scope.pagination.pageSize,
    							  $scope.pagination.orderByDispOrderAsc);
    					
    				}, function( r) {
    					console.error( r);
    				});
    }
    
}]);