App.factory( 'UserService', [ '$http', '$q', function( $http, $q) {
	return {
		/*Login against the Buyer Login WebService*/
		login: function( entity) {
			return $http.post( 'rest-api/login', entity)
						.then(
							function( response) {
								/* Handle when user is anonymous */
								return response;
							},
							function( errResponse) {
								console.log( 'Error: LoginService > login');
								return $q.reject( errResponse);
							}
						);
		},
		/*Returns an Object of the currently logged user*/
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
		},
		/*Logout the currently logged user*/
		logout: function() {
			return $http.get( 'rest-api/logout')
						.then( function( response) {
							return response;
						}, function( response) {
							console.log( 'Error: LoginService > logout');
							return $q.reject( response);
						})
		},
		/* Request password */
		requestPassword: function( form) {
			return $http({
				method: 'POST',
				url: 'rest-api/request-password',
				data: form,
			}).then( function( response) { return response.data;}, function( response) { return $q.reject( response);});
		},
		/* Reset Passwords */
		resetPassword: function( form, token) {
			return $http({
				method: 'POST',
				url: 'rest-api/reset-password',
				data:  $.param({
					pw: form.pw,
					pwConf: form.pwConf,
					token: token
				}),
			    headers : {
			        'Content-Type' : 'application/x-www-form-urlencoded'
			    }
			}).then( function( response) { return response.data;}, function( response) { return $q.reject( response);});
		},
		/* Vaidate Account */
		validateAccount: function( token) {
			return $http({
				method: 'POST',
				url: 'rest-api/validate-account',
				data: $.param({
					token: token
				}),
			    headers : {
			        'Content-Type' : 'application/x-www-form-urlencoded'
			    }
			}).then( function( response) { return response.data;}, function( response) { return $q.reject( response);});
		},
		/* Upgrade Request */
		requestUpgrade: function() {
			return $http({
				method: 'POST',
				url: 'rest-api/request-upgrade',
			}).then( function( response) { return response.data;}, function( response) { return $q.reject( response);});
		},
		/* Favorites EShops */
		loadFavoritesEShops: function( page, pageSize) {
			return $http({
				method: 'GET',
				url: 'rest-api/logged-user/favorites-e-shops',
				params: {
					page: page,
					pageSize: pageSize
				}
			}).then( function( response) { return response.data;}, function( response) { return $q.reject( response);});
		},
		/* Add EAccount */
		addEAccount: function( entity) {
			return $http({
				method: 'POST',
				url: 'rest-api/logged-user/e-account/add',
				data: entity
			})
			.then( function( response) { return response.data;}, function( response) { return $q.reject( response); });
		},
		setEAccountAsDefault: function( id) {
			return $http.post( 'rest-api/logged-user/e-account/' + id + '/set-default')
						.then( function( response) {
							return response;
						}, function( response) {
							return $q.reject( response);
						});
		},
		deleteEAccount: function( id) {
			return $http.post( 'rest-api/logged-user/e-account/' + id + '/delete')
						.then( function( response) {
							return response;
						}, function( response) {
							return $q.reject( response);
						});
		},
		/* Profile */
		updateProfile: function( entity) {
			return $http.put( 'rest-api/logged-user/update-profile', entity)
				.then( function( response) {
					return response.data;
				}, function( response) {
					return $q.reject( response);
				});
		},
		/* Change Password */
		changePassword: function( entity) {
			return $http.put( 'rest-api/logged-user/change-password', entity)
						.then( function( response) {
							return response.data;
						}, function( response) {
							return $q.reject( response);
						});
		}
	}
}]);