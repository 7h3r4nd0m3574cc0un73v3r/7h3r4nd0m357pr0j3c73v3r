App.controller( 'LoginController', [ '$scope', '$state', '$window', 'LoginService', function( $scope, $state, $window, LoginService) {
	var self = this;
	
	$scope.loginForm = { "username": "", "password": ""};
	$scope.error = { invalidCredentials: false, invalidAccount: false, unauthorized: false};
	
	$scope.login = function() {
		$scope.error = {};
		LoginService.login( $scope.loginForm)
					.then(
						function( response) {
							if( response.data.code) {
								$window.location.href = "admin";
							}
						},
						function( errResponse) {
							//Forget not to add control on Roles
							if( errResponse.data.code == -1) {
								$scope.error = {};
								$scope.error.invalidAccount = true;
							}
							if( errResponse.data.code == -2) {
								$scope.error = {};
								$scope.error.unauthorized = true;
							}
							if( errResponse.data.code == 0) {
								$scope.error = {};
								$scope.error.invalidCredentials = true;
							}
							
							console.error( errResponse);
						}
					);
	};
	
}]);