'use strict';
App.factory( 'EShopService', ['$http', '$q', function( $http, $q) {
		return {
			loadEntity: function( id) {
				return $http.get( 'rest-api/e-shop/' + id)
							.then( function( response) {
										return response.data;
									}, function( errResponse) {
										console.log( 'Error: EShopService > loadEShop');
										return $q.reject( errResponse);
									});
			},
			loadManager: function( id) {
				return $http.get( 'rest-api/e-shop/' + id + '/manager')
							.then(	function( response) { return response.data; },
									function( errResponse) {
										console.log( 'Error: EShopService > loadManager');
										return $q.reject( errResponse);
									});
			},
			loadTopArticles: function( id, page, pageSize) {
				return $http({
							url: 'rest-api/e-shop/' + id + '/top-articles',
							method: 'GET',
							params: {
								page: page,
								pageSize: pageSize
							}
						})
						.then( function( response) {
							return response.data;
						}, function( response) {
							return $q.reject( response);
						});
			},
			loadLatestArticles: function( id, page, pageSize) {
				return $http({
							url: 'rest-api/e-shop/' + id +'/latest-articles',
							method: 'GET',
							params: {
								page: page,
								pageSize: pageSize
							}
						})
						.then( function( response) {
							return response.data;
						}, function( response) {
							return $q.reject( response);
						});
			},
			loadBestSellersArticles: function( id, page, pageSize) {
				return $http({
							url: 'rest-api/e-shop/' + id + '/best-sellers',
							method: 'GET',
							params: {
								page: page,
								pageSize: pageSize
							}
						})
						.then( function( response) {
							return response.data;
						}, function( response) {
							return $q.reject( response);
						});
			},
			/* Favorites */
			addAsFavorite: function( id) {
				return $http({
					method: 'POST',
					url: 'rest-api/logged-user/add-favorite-e-shop',
					data: $.param({
						id: id
					}),
					headers: {
						'Content-type': 'application/x-www-form-urlencoded'
					}
				}).then( function( response) {
					return response;
				}, function( response) {
					return $q.reject( response);
				});
			},
			removeFromFavorites: function( id) {
				return $http({
					method: 'POST',
					url: 'rest-api/logged-user/remove-favorite-e-shop',
					data: $.param({
						id: id
					}),
					headers: {
						'Content-type': 'application/x-www-form-urlencoded'
					}
				}).then( function( response) {
					return response;
				}, function( response) {
					return $q.reject( response);
				});
				
			}
		}
	}                            
]);

