'use strict';
//AngularJS Service for  Admins
App.factory( 'UserService', ['$http', '$q', function( $http, $q) {
		return {
			/* Profile Validation */
			loadPendingProfileValidation: function( page, pageSize) {
				return $http({
					method: 'GET',
					url: 'rest-api/admin/profiles-pending-validation',
					params: {
						page: page,
						pageSize: pageSize
					}
				}).then( function( response) {
					return response.data;
				}, function( response) {
					return $q.reject( response);
				});
			},
			/* Validate Profile */
			validateProfile: function( id) {
				return $http({
					method: 'POST',
					url: 'rest-api/admin/user/' + id + '/validate-profile',
				}).then( function( response) {
					return response;
				}, function( response) {
					return $q.reject( response);
				});
			},
			/* Buyers */
			loadBuyers: function( page, pageSize) {
				return $http({
					method: 'GET',
					url: 'rest-api/admin/buyers',
					params: {
						page: page,
						pageSize: pageSize
					}
				}).then( function( response) {
					return response.data;
				}, function( response) {
					return $q.reject( response);
				});
			},
			/* Self */
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
			/* Change Password */
			changePassword: function( entity) {
				return $http.put( 'rest-api/logged-user/change-password', entity)
							.then( function( response) {
								return response.data;
							}, function( response) {
								return $q.reject( response);
							});
			},
			loadEntity: function( id) {
				return $http.get( 'rest-api/admin/user/' + id)
							.then( function( r) {
								return r.data;
							}, function( r) {
								return $q.reject( r);
							});
			}
		}
	}                            
]);

