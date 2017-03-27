'use strict';
//AngularJS Service for  manager
App.factory( 'EShopService', ['$http', '$q', 'Upload', function( $http, $q, Upload) {
		var activeEShop = {};
		return {
			/* Add EShop*/
			addEntity: function( form) {
				return Upload.upload({
					url: 'rest-api/manager/e-shop/add',
					method: 'POST',
					data: {
						entity: angular.toJson( form.entity),
						file : !form.file.length ? null : form.file[0].lfFile
					}})
					.then( function( response) {
						return response;
					}, function( response) {
						return $q.reject( response);
					});
			},
			/* Update EShop */
			updateEntity: function( entity, file) {
				return Upload.upload({
					url: 'rest-api/manager/e-shop/' + entity.id + '/update',
					method: 'POST',
					data: {
						'entity': angular.toJson( entity),
						'file': file
					}})
					.then( function( response) {
						return response;
					}, function( response) {
						return $q.reject( response);
					});
			},
			loadEntitiesByManager: function( userId, page, pageSize) {
				return $http({
					url: 'rest-api/manager/logged-user/e-shops',
					method: 'GET',
					params: {
						page: page,
						pageSize: pageSize
					}
				}).then( function( response) { return response.data; },
						function( errResponse) {
							console.log( 'Error: EShopService > loadEntitiesByManager');
							return $q.reject( errResponse);
						}
				);
			},
			loadEntity: function( id) {
				return $http.get( 'rest-api/manager/e-shop/' + id)
							.then(	function( response) { return response.data; },
									function( errResponse) {
										console.log( 'Error: EShopService > getEShop');
										return $q.reject( errResponse)
							;});
			},
			loadCurrentShopSub: function( id) {
				return $http({
					method: 'GET',
					url: 'rest-api/manager/e-shop/' + id +'/current-shop-sub',
				}).then( function( response) {
					return response.data;
				}, function( response) {
					console.log( 'Error: EShopService > loadCurrentShopSub');
					return $q.reject( response);
				});
			},
			loadArticlesCount: function( id) {
				return $http({
					method: 'GET',
					url: 'rest-api/manager/e-shop/' + id + '/articles-count'
				}).then( function( response) {
					return response.data;
				}, function( response) {
					return $q.reject( response);
				});
			},
			fetchLogo: function( id) {
				return $http({
					method: 'GET',
					url: 'rest-api/e-shop/' + id + '/fetch-logo'
				}).then( function( response) { return response.data},
						 function( response) { return $q.reject( response); });
			},
			loadLocalMarkets: function( ) {
				return $http({
					url: 'rest-api/manager/local-markets',
					method: 'GET',
					params: {
						page: "1",
						pageSize: "0",
						orderByNameAsc: "true"
					}
				}).then( function( r) { return r.data; },
						function( r) {
							console.log( 'Error: EShopService > loadLocalMarkets');
							return $q.reject( r);
						}
				);
			}
		}
	}                            
]);

