'use strict';
App.factory( 'PaymentService', ['$http', '$q', function( $http, $q) {
		return {
			accept: function( id) {
				return $http.put( 'rest/payment/' + id + '/accept')
							.then(
								function( response) { return response.data; },
								function( errResponse) {
									console.log( 'Error: PaymentService > accept');
									return $q.reject( errResponse);
								}
							);
			},
			reject: function( id) {
				return $http.put( 'rest/payment/' + id + '/reject')
							.then(
								function( response) { return response.data; },
								function( errResponse) {
									console.log( 'Error: PaymentService > accept');
									return $q.reject( errResponse);
								}
							);
			},
			loadAdminEAccountsForPayments: function() {
				return $http({
					method: 'GET',
					url: 'rest-api/e-accounts-for-payments',
				}).then( function( response) { return response.data }, function( response) { return $q.reject( response);});
			},
		}
	}                            
]);

