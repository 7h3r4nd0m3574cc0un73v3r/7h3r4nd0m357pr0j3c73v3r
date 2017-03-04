'use strict';

App.controller( 'UpgradeRequestController', [ '$state', '$stateParams', '$scope', 'UpgradeRequestService',
                                       function( $state, $stateParams, $scope, UpgradeRequestService)
{	
	/* Pagination Settings */
	$scope.pagination = { currentPage: $stateParams.page == undefined ? 1 : $stateParams.page,
						  pageSize: $stateParams.pageSize == undefined ? 10 : $stateParams.pageSize,
						  orderByIdAsc: $stateParams.orderByIdAsc == undefined ? true : $stateParams.orderByIdAsc,
						  status: $stateParams.status == undefined ? 1 : $stateParams.status,
					      pagesNumber: null,
					      itemsNumber: null
						};
	/* End Pagination Settings */
	
	/* Title */
	$scope.title = 'Migrations de comptes acheteurs';
	$scope.breadcrumb = 'Migrations ';
	if( $scope.pagination.status == 1) {
		$scope.title += ' en attente de validation';
		$scope.breadcrumb += ' à valider'
	}
	if( $scope.pagination.status == 2) {
		$scope.title += ' validées';
		$scope.breadcrumb += ' validées';
	}
	/* End Title */
	
	/* Loading Mutexes */
	$scope.isListLoading = true;
	$scope.isEntityLoading = true;
	
	/* Init Var */
	$scope.entities = [];
	
	/* Page Change */
	$scope.changePage = function() {
			$state.go( $state.current.name, { page: $scope.pagination.currentPage, pageSize: $scope.pagination.pageSize }, { notify: false});
			
			$scope.loadEntities( $scope.pagination.currentPage, $scope.pagination.pageSize, $scope.pagination.orderBy);
	}
	
	/* Load Entities Def and Call */
	$scope.loadEntities = function( page, pageSize, orderByIdAsc, status) {
		$scope.isListLoading = true;
		UpgradeRequestService.loadEntities( page, pageSize, orderByIdAsc, status)
							.then( function( response) {
								$scope.pagination.pagesNumber = response.pagesNumber;
								$scope.pagination.itemsNumber = response.itemsNumber;
								$scope.entities = response.entities;
								$stateParams.Page = $scope.pagination.currentPage;
							
								if( response.entities == undefined){
									$scope.isListLoading = false;
									return;
								}
								
								$scope.isListLoading = false;
							}, function( response) {
							  $scope.isListLoading = false;
							  console.error( response);
							});
	};
	
	$scope.loadEntities( $scope.pagination.currentPage, $scope.pagination.pageSize,
						 $scope.pagination.orderByIdAsc, $scope.pagination.status);
	
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
	
	/* Approve Request */
	$scope.approveRequest = function() {
		if( $stateParams.id != undefined && $scope.entity != null) {
			$scope.isEntityLoading = true;
			UpgradeRequestService.approveEntity( $scope.entity.id)
								 .then( function( response) {
									 console.log( response);
									 $scope.isEntityLoading = false;
									 $state.go( 'root.upgrade-requests', null, { reload: true});
								 }, function( response) {
									 console.error( response);
									 $scope.isEntityLoading = false;
								 });
		}
	}
	/* End Approve Request */
}]);