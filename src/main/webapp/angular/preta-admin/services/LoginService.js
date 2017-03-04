App.factory( 'LoginService', [ '$http', '$q', function( $http, $q) {
	return {
		login: function( entity) {
			return $http.post( 'rest-api/admin/login', entity)
						.then(  
							function( response) {
								return response;
							},
							function( errResponse) {
								console.log( 'Error: LoginService > login');
								return $q.reject( errResponse);
							}
						);
		}
	}
}]);