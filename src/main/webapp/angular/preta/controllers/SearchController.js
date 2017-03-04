App.controller( 'SearchController', [ '$scope', 'ArticleService', '$state', '$stateParams', 'toastr', 'toastrConfig',
                                       function( $scope, ArticleService, $state, $stateParams, toastr, toastrConfig) {
	$scope.entities = [];
	$scope.isLoading = true;
	
	/* Pagination Settings */
	$scope.pagination = { currentPage: $stateParams.page == undefined ? 1 : $stateParams.page,
						  pageSize: $stateParams.pageSize == undefined ? 16 : $stateParams.pageSize,
					      pagesNumber: null,
					      itemsNumber: null
						};
	/* End Pagination Setting */
	
	/* Price Interval */
	$scope.isLoadingPrice = [true, true];
	$scope.searchParams = { ceil: 0, floor: 0, min: 0, max: 0};

	/* Price Slide Config */
	$scope.priceSlider = {
	    minValue: 0,
	    maxValue: 0,
	    options: {
	      ceil: 0,
	      floor: 0,
	      id: 'price-slider',
	      translate: function(value, id, which) {
	        return value + ' F CFA';
	      }
	    }
	};
	
	ArticleService.loadMinPrice()
				  .then( function( response) {
					  $scope.searchParams.floor = response;
					  
					  $scope.isLoadingPrice[0] = false;
				  }, function( response) {
					  console.error( response);
					  $scope.isLoadingPrice[0] = false;
				  });
	
	ArticleService.loadMaxPrice()
					.then( function( response) {
						$scope.searchParams.ceil = response;
						$scope.priceSlider.ceil = response;
						
						$scope.isLoadingPrice[1] = false;
					}, function( response) {
						console.error( response);
						$scope.isLoadingPrice[1] = false;
					});
	/* End Price Interval */
	
	ArticleService.searchArticles( $stateParams.name, $stateParams.page,
								   $stateParams.pageSize, $stateParams.minPrice,
								   $stateParams.maxPrice)
								   
					.then( function( response) {
						$scope.pagination.pagesNumber = response.pagesNumber;
						$scope.pagination.itemsNumber = response.itemsNumber;
						$scope.entities = response.entities;
						
						angular.forEach( $scope.entities, function( article, key) {
							ArticleService.loadEShop( article.id)
										  .then( function( response) {
											  article.eShop = response;
										  }, function( response) {
											  console.error( response);
											  $scope.isLoading = false;
										  });
						});
						
						$scope.isLoading = false;
					}, function( response) {
						console.error( response);
						$scope.isLoading = false;
					});
	  
	$scope.changePage = function() {
		/* Update Route Params */
		$stateParams.minPrice = $scope.searchParams.value.min;
		$stateParams.maxPrice = $scope.searchParams.value.max;
		
		$state.go( 'root.search', { name: $stateParams.name, page: $scope.pagination.currentPage, pageSize: $stateParams.pageSize,minPrice: $stateParams.minPrice,
									maxPrice: $stateParams.maxPrice}, { notify: false});
		
		$scope.isLoading = true;
		ArticleService.searchArticles( $stateParams.name, $scope.pagination.currentPage,
									   $stateParams.pageSize, $stateParams.minPrice,
									   $stateParams.maxPrice)
										.then( function( response) {
											$scope.pagination.pagesNumber = response.pagesNumber;
											$scope.pagination.itemsNumber = response.itemsNumber;
											$stateParams.page = $scope.pagination.currentPage;
											$scope.entities = response.entities;
											
											/* Load EShop for Each Article */
											angular.forEach( $scope.entities, function( article, key) {
												ArticleService.loadEShop( article.id)
															  .then( function( response) {
																  article.eShop = response;
															  }, function( response) {
																  console.error( response);
																  $scope.isLoading = false;
															  });
											});
											
											$scope.isLoading = false;
										}, function( response) {
											console.error( response);
											$scope.isLoading = false;
										});
	};
}]);