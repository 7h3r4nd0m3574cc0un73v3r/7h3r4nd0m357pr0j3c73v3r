'use strict';
//AngularJS Service for User And EShop Registration
App.factory( 'UserService', ['$http', '$q', function( $http, $q) {
		return {

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
			updateEntity: function() {
				return $http.put( 'rest-api/logged-user/update-profile')
							.then( function( response) {
								return response.data;
							}, function( errResponse) {
								console.log( 'Error: UserService > updateEntity');
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
	}                            
]);

