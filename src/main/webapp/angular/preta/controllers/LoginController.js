App.controller( 'LoginController', [ '$rootScope', '$scope', '$state', 'UserService', 'ArticleService',
                                     function( $rootScope, $scope, $state, UserService, ArticleService) {
	/*Init default variables*/
	$scope.loginForm = { username: "", password: ""};
	$scope.error = { invalidCredentials: false, invalidAccount: false, unauthorized: false};
	
	/*Login function*/
	$scope.login = function() {
		$scope.error = {};
		UserService.login( $scope.loginForm)
					.then(
						function( response) {
							/*Check if Successful Login*/
							if( response.data.code) {
								/*Load the logged User in the Angular App */
								UserService.loadLoggedUser()
											.then( function( response) {
												$rootScope.loggedUser =  response;
											}, function( response) {
												console.Log( response);
											});
								
								if( ArticleService.referrer == 2) {
									$state.go( 'root.article', { id: ArticleService.articlesToOrder[0].article.id});
									return;
								}
								else if( ArticleService.referrer == 1) {
									$state.go( 'root.article', { id: ArticleService.articleToCart.article.id});
									return;
								}
								
								$state.go( 'root');
							}
						},
						function( r) {
							/*Load the errors to be shown on the form
							 * Errors come from server
							 * */ 
							if( r.data.code == -1) {
								$scope.error = {};
								$scope.error.invalidAccount = true;
							}
							if( r.data.code == -2) {
								$scope.error = {};
								$scope.error.unauthorized = true;
							}
							if( r.data.code == 0) {
								$scope.error = {};
								$scope.error.invalidCredentials = true;
							}
						}
					);
	};
}]);