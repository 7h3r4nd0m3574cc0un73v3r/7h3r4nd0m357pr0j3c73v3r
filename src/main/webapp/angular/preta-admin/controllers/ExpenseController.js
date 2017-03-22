'use strict';

App.controller( 'ExpenseController', [ '$state', '$stateParams', '$scope', 'ExpenseService', 'OrderService', 'ArticleService',
                                     function( $state, $stateParams, $scope, ExpenseService, OrderService, ArticleService)
{	
	/* Loading Mutexes */
	$scope.isListLoading = true;
	$scope.isEntityLoading = true;
	
	/* Handle PagedListJSON ArticleOrders */
	$scope.entities = [];
	$scope.artOrds = [];
	$scope.entity = { amount: 0, expenseRef: null };
	$scope.selection= { count: 0, total: 0};

	/* Pagination Settings */
	$scope.pagination = { currentPage: $stateParams.page == undefined ? 1 : $stateParams.page,
						  pageSize: $stateParams.pageSize == undefined ? 10 : $stateParams.pageSize,
						  orderByIdAsc: $stateParams.orderByIdAsc == undefined ? true : $stateParams.orderByIdAsc,
					      pagesNumber: null,
					      itemsNumber: null
						};
	/* End Pagination Setting */
	
	/* Funcs and Tools */
	function loadEntities( page, pageSize, paymentStatus, orderByIdAsc) {
		$scope.isListLoading = true;
		
		ExpenseService.loadAddressedEntities( page, pageSize, orderByIdAsc)
					.then( function( response) {
						/* Update Pagination info and Scope */
						$scope.pagination.pagesNumber = response.pagesNumber;
						$scope.pagination.itemsNumber = response.itemsNumber;
						$stateParams.page = $scope.pagination.currentPage;
						$scope.entities = response.entities;
						
						angular.forEach( $scope.entities, function( entity) {
							prepareEntity( entity);
						});
						
						$scope.isListLoading = false;
					}, function( r) {
						$scope.isListLoading = false;
						console.error( r);
					});
	};
	
	/* Retrieve necessary article order info */
	function prepareEntity( entity) {

	}
	
	function prepareNewEntity( entity) {
		$scope.isEntityLoading = true;
		
		console.warn( ExpenseService.articleOrders);
		
		/* Load ArticleOrder Expense Pending */
		OrderService.loadAddressedEntities( 1, 0, 5, true)
					.then( function( r) {
						angular.forEach( r.entities, function( artOrd) {
							prepareArticleOrder( artOrd, 1);
						});
						
						$scope.artOrds = r.entities;
					}, function( r) {
						console.error( r);
					});
		
		$scope.isEntityLoading = false;
	}
	
	function prepareArticleOrder( entity, level) {
		/* Load EShop */
		OrderService.loadEShop( entity.id)
					.then( function( response) {
						entity.eShop = response;
					}, function( response) {
						console.log( response);
					});
		/* Load Buyer */
		OrderService.loadBuyer( entity.id)
					.then( function( response) {
						entity.buyer = response;
					}, function( response) {
						console.log( response);
					});
		
		if( level >= 1) {
			/* Load OrderedArticles */
			OrderService.loadOrderedArticlesByOrder( entity.id)
						.then( function( orderedArticles) {
							/* Expenses */
							entity.selected = false;
							entity.disabled = false;
							/* End Expenses */
							entity.orderedArticles = orderedArticles;
							entity.total = 0;
							angular.forEach( entity.orderedArticles, function( value) {
								entity.total += ( value.article.price + value.article.deliveryFee ) * value.quantity;
							});
						}, function( r) {
							console.error( r);
						});
			/* Load Payments */
			OrderService.loadPayments( entity.id)
						.then( function( payments) {
							entity.payments = payments.entities;
						}, function( r) {
							console.error( r);
						});
			
			angular.forEach( ExpenseService.articleOrders, function( artOrd2) {
				 if( artOrd2.id == entity.id) {
					 entity.selected = true;
				 }
			 });
		}
	}
	
	loadEntities( $scope.pagination.page, $scope.pagination.pageSize,
				  $scope.pagination.paymentStatus, $scope.pagination.orderByIdAsc);
	
	/* Page Change */
	$scope.changePage = function() {
		$state.go( $state.current.name, { page: $scope.pagination.currentPage, pageSize: $scope.pagination.pageSize,
				    orderByIdAsc: $scope.pagination.orderByIdAsc }, { notify: false});
		
		loadEntities( $scope.pagination.currentPage, $scope.pagination.pageSize, $scope.pagination.orderByIdAsc);
	}
	
	/* TODO Add WebSocket Real TIme Update */
	/* Show Payment */
	if( $stateParams.id != undefined) {
		$scope.isEntityLoading = true;
		
		ExpenseService.loadEntity( $stateParams.id)
					.then( function( response) {
						$scope.entity = response;
						
						prepareEntity( $scope.entity);
						
						$scope.isEntityLoading = false;
					}, function( r) {
						console.error( r);
						$scope.isEntityLoading = false;
					});
	}
	
	if( $state.current.name == 'root.expenses.new') {
		prepareNewEntity( $scope.entity);
	}
	
	$scope.$watch( 'artOrds', function( newValue, oldValue) {
		if( newValue.length) {
			console.log( "Specific Change Detected");
			var reference;
			
			$scope.selection.count = 0;
			$scope.selection.total = 0;
			
			angular.forEach( newValue, function( artOrd) {
				if( artOrd.selected) {
					reference = artOrd;
					console.log( artOrd);
					$scope.selection.count++;
					$scope.selection.total += artOrd.total;
				}
			});
			
			angular.forEach( newValue, function( artOrd) {
				if( artOrd.eShop != undefined && reference != null) {
					/* The payment was received in the same account */
					
					/* Is The Same EShop */
					if( artOrd.eShop.id != reference.eShop.id)
						artOrd.disabled = true;
					else
						artOrd.disabled = false;
					
				}
				
				if( reference == null)
					artOrd.disabled = false;
			});
		}
		
		$scope.entity.amount = $scope.selection.total;
	}, true);
	
	$scope.selectOrder = function( id) {
		angular.forEach( $scope.artOrds, function( artOrd) {
			if( artOrd.id == id)
				artOrd.selected = !artOrd.selected;
		});
	}
}]);