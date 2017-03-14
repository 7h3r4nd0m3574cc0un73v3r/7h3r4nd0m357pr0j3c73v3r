App.controller( 'RatingController', [ '$rootScope', '$scope', '$state', '$stateParams', 'ArticleService', 'OrderService',
                                     function( $rootScope, $scope, $state, $stateParams, ArticleService, OrderService) {
	/* Reject Anonymous User */
	if( $rootScope.loggedUser != null) {
		$state.go( 'root.errors.401');
	}
	
	/* Init Data and Loading Mutex*/
	$scope.entities = [];
	$scope.isListLoading = true;
	
	/* Pagination Settings */
	$scope.pagination = { currentPage: $stateParams.page == undefined ? 1 : $stateParams.page,
						  pageSize: $stateParams.pageSize == undefined ? 8 : $stateParams.pageSize,
					      pagesNumber: null,
					      itemsNumber: null
						};
	/* End Pagination Setting */
	
	/* View Config */
	$scope.view = { title: 'Articles \xE0 noter', icon: 'fa fa-star-o', description: 'Donnez votre avis sur les articles re\xE7us'};
	/* End View Config */
	
	/* Load Articles to Rate */
	$scope.loadEntities = function() {
		$scope.isListLoading = true;
		ArticleService.loadArticleToRate( $scope.pagination.currentPage, $stateParams.pageSize)
						.then( function( response) {
							  $scope.entities = response.entities;
							  $scope.pagination.pagesNumber = response.pagesNumber;
							  $scope.pagination.itemsNumber = response.itemsNumber;
								
								/* Init Ratings */
								angular.forEach( $scope.entities, function( entity) {
									entity.rating = 0;
								});
								
							  $scope.isListLoading = false;
						}, function( response) {
							console.error( response);
							$scope.isListLoading = false;
						});
	};
	
	$scope.loadEntities();
	
	$scope.rateOrderedArticle = function( id, rating) {
		ArticleService.rateOrderedArticle( id, rating)
						.then( function( response) {
							$scope.loadEntities();
						}, function( response) {
							console.error( response);
						});
	};
	
	/* Page Changed */
	$scope.changePage = function() {
		$state.go( 'root.ratings', { page: $scope.pagination.currentPage, pageSize: $stateParams.pageSize },{ notify: false});

		$scope.isListLoading = true;
		ArticleService.loadArticleToRate( $scope.pagination.currentPage,
								   $stateParams.pageSize)
									.then( function( response) {
										$scope.loadEntities();
									}, function( response) {
										console.error( response);
										$scope.isListLoading = false;
									});
	};
}]);