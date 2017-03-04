App.controller( 'UpgradeRequestController',[ '$scope', 'UserService', function( $scope, UserService) {
	/* Loading Mutexes */
	$scope.isLoading = false;
	$scope.done = false;
	$scope.error = 0;
	
	$scope.requestUpgrade = function() {
		$scope.isLoading = true;
		
		UserService.requestUpgrade()
		.then( function( response) {
			$scope.done = true;
			
			$scope.isLoading = false;
		}, function( response) {
			console.error( response);
			if( response.status == 400) {
				angular.forEach( response.data.errors, function( error) {
					if( error.code == "NotAllowed" && error.field == "role")
						$scope.error = 1;
					if( error.code == "AlreadyManager" && error.field == "role")
						$scope.error = 2;
					if( error.code == "Incomplete" && error.field == "profile")
						$scope.error = 4;
					if( error.code == "AlreadyRequested" && error.field == "request")
						$scope.error = 3;
				});
			}
			
			$scope.isLoading = false;
		});
	}
}]);