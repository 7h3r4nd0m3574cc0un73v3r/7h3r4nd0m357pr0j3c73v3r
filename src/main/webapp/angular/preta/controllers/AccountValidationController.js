App.controller( 'AccountValidationController',[ '$rootScope', '$scope', 'UserService', '$stateParams', '$state', function( $rootScope, scope, UserService, $stateParams, $state) {
	if( $rootScope.loggedUser != null) {
		$state.go( 'root.errors.403');
	}
	
	/* Loading Mutexes */
	$scope.isLoading = true;
	$scope.done = false;
	if( $stateParams.token != undefined) {
		console.log( $stateParams.token);
		UserService.validateAccount( $stateParams.token)
					.then( function( response) {
						console.log( response);
						$scope.done = true;
						$scope.isLoading = false;
					}, function( response) {
						console.error( response);
						$scope.isLoading = false;
					});
	}
	else 
		$scope.isLoading = false;
}]);