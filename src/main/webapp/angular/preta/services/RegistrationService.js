'use strict';
//AngularJS Service for  Admins
App.factory( 'RegistrationService', ['$http', '$q', function( $http, $q) {
		return {
			/*Handles Buyer Registration*/
			addBuyer: function( entity) {
				return $http.post( 'rest-api/register/buyer', entity)
							.then( function( response) {
								return response.data;
							}, function( response) {
								return $q.reject( response);
							});
			},
			/*Handles Manager and ESHop Registration*/
			addEShop: function( entity) {
				return $http.post( 'rest-api/register/e-shop', entity)
							.then( function( response) {
								return response.data;
							}, function( errResponse) {
								console.log( 'Error: RegistrationService > addManager');
								return $q.reject( errResponse);
							});
			},
		}
	}                            
]);

