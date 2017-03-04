'use strict';
//AngularJS Service for User And EShop Registration
App.factory( 'ShopStatusService', ['$http', '$q', function( $http, $q) {
		return {
			loadEntities: function() {
				return $http({
					url: 'rest/shop-statuses',
					method: 'GET',
					params: {
						page: "1",
						pageSize: "0"
					}
				}).then( 
					function( response) {
						return response.data;
					},
					function( errResponse) {
						console.log( 'Error: ShopStatusService > listShopStatus');
						return $q.reject( errResponse);
					}
				);
			},
			addEntity: function( entity) {
				return $http.post( 'rest/shop-status/add', entity)
							.then( function( response) {
								return response.data;
							}, function( errResponse) {
								console.log( 'Error: ShopStatusService > addShopStatus');
								return $q.reject( errResponse);
							});
			},
			editEntity: function( entity) {
				return $http.put( 'rest/shop-status/edit/' + entity.id, entity)
							.then( function( response) {
								return response.data;
							}, function( errResponse) {
								console.log( 'Error: ShopStatusService > addShopStatus');
								return $q.reject( errResponse);
							});
			},
			deleteEntity: function( id) {
				return $http.put( 'rest/shop-status/delete/' + id)
							.then( function( response) {
								return response.data;
							}, function( errResponse) {
								console.log( 'Error: ShopStatusService > addShopStatus');
								return $q.reject( errResponse);
							});
			}
		}
	}                            
]);

