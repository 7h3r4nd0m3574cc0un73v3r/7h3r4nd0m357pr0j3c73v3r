'use strict';
//AngularJS Service for User And EShop Registration
App.factory( 'ShopSubService', ['$http', '$q', function( $http, $q) {
		return {
			loadPendingEntities: function() {
				return $http.get( 'rest/shop-subs/pending')
							.then( 
								function( response) {
									return response.data;
								},
								function( errResponse) {
									console.log( 'Error: ShopSubService > listShopSub');
									return $q.reject( errResponse);
								}
							);
			},
			loadEntities: function() {
				return $http.get( 'rest/shop-subs')
							.then( 
								function( response) {
									return response.data;
								},
								function( errResponse) {
									console.log( 'Error: ShopSubService > listShopSub');
									return $q.reject( errResponse);
								}
							);
			},
			loadEntitiesByStatus: function( status) {
				return $http.get( 'rest-api/admin/shop-sub/status/' + status)
							.then(	function( response) { 
										return response.data;
									},
									function( errResponse) {
										console.log( 'Error: ShopSubService > loadEntitiesByStatus');
										return $q.reject( errResponse);
									});
			},
			getEntity: function( id) {
				return $http.get( 'rest/shop-sub/get/' + id)
				.then( 
					function( response) {
						return response.data;
					},
					function( errResponse) {
						console.log( 'Error: ShopSubService > getShopSub');
						return $q.reject( errResponse);
					}
				);
			},
		}
	}                            
]);

