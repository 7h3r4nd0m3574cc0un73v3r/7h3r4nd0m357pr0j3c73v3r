App.controller( 'ArticleByCategoryController', [ '$scope', '$state', '$stateParams', 'ArticleService', 'CategoryService',
                                       function( $scope, $state, $stateParams, ArticleService, CategoryService) {
	/* Loading Mutex */
	$scope.isLoading = true;
	
	/* Pagination Settings */
	$scope.pagination = { currentPage: $stateParams.page == undefined ? 1 : $stateParams.page,
						  pageSize: $stateParams.pageSize == undefined ? 12 : $stateParams.pageSize,
					      pagesNumber: null,
					      itemsNumber: null
						};
	/* End Pagination Setting */
	
	/* Init */
	$scope.entities = [];
	$scope.category = {};
	
	/* Load Functions */
	function loadCategory( id) {
		return CategoryService.loadEntity( $stateParams.id)
							  .then( function( response) {
								  $scope.category = response;
								  
								  /* View Config */
								  $scope.view = { title: 'Cat\xE9gorie ' + $scope.category.label, icon: 'fa fa-cubes',
										  		  description: 'Articles appartenant \xE0 la cat\xE9gorie ' + $scope.category.label 
												}; 
								  /* End View Config */
							   }, function( r) {
								   console.error( r);
							   });
	}
	
	function loadEntities( categoryId, page, pageSize) {
		$scope.isLoading = true;
		
		ArticleService.loadByCategory( $stateParams.id, page, pageSize)
					  .then( function( response) {
							$scope.pagination.pagesNumber = response.pagesNumber;
							$scope.pagination.itemsNumber = response.itemsNumber;
							$stateParams.page = $scope.pagination.currentPage;
			
							$scope.entities = response.entities;
							
							angular.forEach( $scope.entities, function( entity) {
			  					ArticleService.loadEShop( entity.id)
			  								  .then( function( response) {
			  									  entity.eShop = response;
			  								  }, function( response) {
			  									  console.error( response);
			  								  });

								/* Load Article Pictures */
								ArticleService.loadPictures( entity.id)
											  .then( function( response) {
												  entity.pictures = response.entities;
											  }, function( response) {
												  console.error( response);
											  });
							});
							
						  $scope.isLoading = false;
					  }, function ( r) {
						  console.error( r);
						  $scope.isLoading = false;
					  });
	}
	
	/* Call to load function */
	loadCategory( $stateParams.id);
	loadEntities( $stateParams.id, $scope.pagination.page, $scope.pagination.pageSize);
	
	$scope.changePage = function() {
		$state.go( $state.current.name, { page: $scope.pagination.currentPage, pageSize: $stateParams.pageSize },{ notify: false});
		loadEntities( $stateParams.id, $scope.pagination.currentPage, $scope.pagination.pageSize);
	};
}]);