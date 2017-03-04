App.factory( 'LoginService', [ '$http', '$q', function( $http, $q) {
	return {
		login: function( entity) {
			return $http.post( 'rest-api/login', entity)
						.then( 
							function( response) {
								return response;
							},
							function( errResponse) {
								console.log( 'Error: LoginService > login');
								return $q.reject( errResponse);
							}
						);
		},
		loadLoggedUser: function() {
			return $http.get( 'rest-api/logged-user')
						.then(
							function( response) {
								return response.data; 
							},
							function( errResponse) {
								console.log( 'Error: Service > loggedUser');
								return $q.reject( errResponse);
						});
		}
	}
}]);