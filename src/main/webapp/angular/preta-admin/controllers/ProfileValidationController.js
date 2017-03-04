'use strict';

App.controller( 'ProfileValidationController', [ '$state', '$stateParams', '$scope', 'UserService', 'StompService', '$log',
                                       function( $state, $stateParams, $scope, UserService, StompService, $log)
{	
	/* Pagination Settings */
	$scope.pagination = { currentPage: $stateParams.page == undefined ? 1 : $stateParams.page,
						  pageSize: $stateParams.pageSize == undefined ? 10 : $stateParams.pageSize,
					      pagesNumber: null,
					      itemsNumber: null
						};
	/* End Pagination Settings */
	
	/* Loading Mutexes */
	$scope.isListLoading = true;
	$scope.isEntityLoading = true;
	
	/* Init Var */
	$scope.entities = [];
	
	/* Page Change */
	$scope.changePage = function() {
			$state.go( $state.current.name, { page: $scope.pagination.currentPage, pageSize: $scope.pagination.pageSize }, { notify: false});
			
			$scope.loadEntities( $scope.pagination.currentPage, $scope.pagination.pageSize);
	}
	
	/* Load Entities Def and Call */
	$scope.loadEntities = function( page, pageSize) {
		$scope.isListLoading = true;
		UserService.loadPendingProfileValidation( page, pageSize)
							.then( function( response) {
								$scope.pagination.pagesNumber = response.pagesNumber;
								$scope.pagination.itemsNumber = response.itemsNumber;
								$scope.entities = response.entities;
								$stateParams.Page = $scope.pagination.currentPage;
							
								if( response.entities == undefined) {
									$scope.isListLoading = false;
									return;
								}
								
								$scope.isListLoading = false;
							}, function( response) {
							  $scope.isListLoading = false;
							  console.error( response);
							});
	};
	
	$scope.loadEntities( $scope.pagination.currentPage, $scope.pagination.pageSize);
	/* WebSockets */
	StompService.connect()
				.then( function( response) {
					StompService.subscribe( '/topic/admin/profiles-pending-conf',
											function( payload, headers, response) {
										    	var pageIndex = ($scope.pagination.currentPage - 1) * $scope.pagination.pageSize;
										    	$scope.entities = payload.entities.slice( pageIndex, pageIndex + $scope.pagination.pageSize);
										    	$scope.$apply();
											});
				});
	/* End WebSockets */
	
	/* Load Selected Entity */
	if( $stateParams.id != undefined ) {
		$scope.isEntityLoading = true;
		UpgradeRequestService.loadEntity( $stateParams.id)
					  .then( function( response) {
						  console.log( response);
						  $scope.entity = response;
						  $scope.isEntityLoading = false;
					  }, function( response) {
						  console.error( response);
						  $scope.isEntityLoading = false;
					  });
	} else {
		$scope.entity = {};
	}
	
	/* Validate Profile */
	$scope.validateProfile = function( id) {
		UserService.validateProfile( id)
				   .then( function( response) {
					   console.log( response);
					   $scope.loadEntities( $scope.pagination.currentPage, $scope.pagination.pageSize);
				   }, function( response) {
					   console.error( response);
				   });
	}
}]);