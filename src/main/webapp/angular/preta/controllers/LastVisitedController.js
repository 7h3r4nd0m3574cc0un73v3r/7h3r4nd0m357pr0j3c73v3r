App.controller( 'LastVisitedController', [ 'ArticleService', '$scope', '$stateParams', '$state', function( ArticleService, $scope, $stateParams, $state) {
	/* Loading Mutex */
	$scope.isLoading = true;
	
	/* Pagination Settings */
	$scope.pagination = { currentPage: $stateParams.page == undefined ? 1 : $stateParams.page,
						  pageSize: $stateParams.pageSize == undefined ? 12 : $stateParams.pageSize,
						  orderByIdAsc: $stateParams.orderByIdAsc == undefined ? false : $stateParams.orderByIdAsc,
					      pagesNumber: null,
					      itemsNumber: null
						};
	/* End Pagination Setting */
	
	/* Init */
	$scope.entities = [];
	var simpleEntities = [];
	
	/* View Config */
	$scope.view = { title: 'R\xE9cemment visit\xE9s', icon: 'fa fa-tags', description: 'Articles visit\xE9s derni\xE8rement'}
	/* End View Config */
	
	/* Load Functions */
	$scope.loadEntities = function( page, pageSize, orderByIdAsc) {
		$scope.isLoading = true;
		ArticleService.loadLastVisited( page, pageSize, orderByIdAsc)
					  .then( function( response) {
							$scope.pagination.pagesNumber = response.pagesNumber;
							$scope.pagination.itemsNumber = response.itemsNumber;
							$stateParams.page = $scope.pagination.currentPage;
			
							simpleEntities = response.entities;
							
							var processedEntities = 0;
							angular.forEach( simpleEntities, function( entity) {
								ArticleService.loadEntity( entity.articleId)
								  				.then( function( response) {
								  					var article = response;
								  					
								  					ArticleService.loadEShop( article.id)
								  								  .then( function( response) {
								  									  article.eShop = response;
								  									  article.visitTime = entity.visitTime;
								  									  $scope.entities.push( article);
								  									  
								  								  }, function( response) {
								  									  console.error( response);
								  								  });

													/* Load Article Pictures */
													ArticleService.loadPictures( article.id)
																  .then( function( response) {
																	  article.pictures = response.entities;
																  }, function( response) {
																	  console.error( response);
																  });
													
								  				}, function( response) {
								  					console.error( response);
								  				});
							});
							$scope.isLoading = false;
					  }, function ( response) {
						  console.error( response);
						  $scope.isLoading = false;
					  });
	}
	
	/* Call to load function */
	$scope.loadEntities( $scope.pagination.page, $scope.pagination.pageSize, $scope.pagination.orderByIdAsc);
	
	$scope.changePage = function() {
		$state.go( $state.current.name, { page: $scope.pagination.currentPage, pageSize: $stateParams.pageSize, orderByIdAsc: $stateParams.orderByIdAsc },{ notify: false});
		$scope.loadEntities( $scope.pagination.currentPage, $scope.pagination.pageSize, $scope.pagination.orderByIdAsc);
	};
}]);