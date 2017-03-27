'use strict';
//AngularJS Service for User And EShop Registration
App.factory( 'LocalMarketService', ['$http', '$q', function( $http, $q) {
		return {
			loadEntities: function( page, pageSize, orderByIdAsc) {
				return $http({
					url: 'rest-api/admin/local-markets',
					method: 'GET',
					params: {
						page: page,
						pageSize: pageSize,
						orderByIdAsc: orderByIdAsc
					}
				})
				.then( 
					function( r) {
						return r.data;
					},
					function( r) {
						console.log( 'Error: LocalMarketService > listLocalMarket');
						return $q.reject( r);
					}
				);
			},
			loadEntity: function( id) {
				return $http({
					url: 'rest-api/admin/local-market/' + id,
					method: 'GET'
				}).then( 
					function( r) {
						return r.data;
					},
					function( r) {
						console.log( 'Error: LocalMarketService > listLocalMarket');
						return $q.reject( r);
					}
				);
			},
			addEntity: function( entity) {
				return $http.post( 'rest-api/admin/local-market/add', entity)
							.then( function( r) {
								return r.data;
							}, function( r) {
								console.log( 'Error: LocalMarketService > addLocalMarket');
								return $q.reject( r);
							});
			},
			updateEntity: function( entity) {
				return $http.put( 'rest-api/admin/local-market/' + entity.id + '/update', entity)
							.then( function( r) {
								return r.data;
							}, function( r) {
								console.log( 'Error: LocalMarketService > addLocalMarket');
								return $q.reject( r);
							});
			},
			deleteEntity: function( id) {
				return $http.put( 'rest-api/admin/local-market/' + id + '/delete')
							.then( function( r) {
								return r.data;
							}, function( r) {
								console.log( 'Error: LocalMarketService > addLocalMarket');
								return $q.reject( r);
							});
			}
		}
	}                            
]);

