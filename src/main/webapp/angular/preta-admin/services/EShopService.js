'use strict';
App.factory( 'EShopService', ['$http', '$q', 'Upload', function( $http, $q, Upload) {
		return {
			loadEntities: function() {
				return $http.get( 'rest-api/admin/e-shops')
							.then(	function( response) { return response.data; },
									function( errResponse) {
										console.log( 'Error: Service > LoadEntities');
										return $q.reject( errResponse);
									});
			},
			loadEntity: function( id) {
				return $http.get( 'rest-api/admin/e-shop/' + id)
							.then( function( response) {
										var eShop = response.data;
										return $http.get( 'rest-api/admin/e-shop/' + id + '/manager')
													.then( function( response) {
														eShop.manager = response.data;
														return eShop;
													}, function( response) {
														return $q.reject( response);
													});
									}, function( errResponse) {
										console.log( 'Error: EShopService > getEShop');
										return $q.reject( errResponse);
									});
			},
			loadManager: function( id) {
				return $http.get( 'rest-api/admin/e-shop/' + id + '/manager')
					.then(	function( response) { return response.data; },
							function( errResponse) {
								console.log( 'Error: EShopService > getmanager');
								return $q.reject( errResponse);
							});
			},
			loadCurrentShopSub: function( id) {
				return $http.get( 'rest-api/manager/e-shop/' + id + '/current-shop-sub')
				.then(	function( response) { return response.data; },
						function( errResponse) {
							console.log( 'Error: EShopService > getCurrentShopSub');
							return $q.reject( errResponse);
						});
				
			},
			/* Validate EShop */
			validateEntity: function( id) {
				return $http.post( 'rest-api/admin/e-shop/' + id + '/validate')
							.then( function( response) {
								console.log( response);
							}, function( response) {
								return $q.reject( response);
							});
			}
		}
	}                            
]);

