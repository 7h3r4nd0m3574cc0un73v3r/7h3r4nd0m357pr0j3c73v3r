'use strict';
App.factory( 'PaymentService', ['$http', '$q', function( $http, $q) {
		return {
			accept: function( id) {
				return $http.put( 'rest-api/admin/payment/' + id + '/accept')
							.then(
								function( response) { return response.data; },
								function( errResponse) {
									console.log( 'Error: PaymentService > accept');
									return $q.reject( errResponse);
								}
							);
			},
			reject: function( id) {
				return $http.put( 'rest-api/admin/payment/' + id + '/reject')
							.then(
								function( response) { return response.data; },
								function( errResponse) {
									console.log( 'Error: PaymentService > accept');
									return $q.reject( errResponse);
								}
							);
			},
			/* Load Payments addresses to the logged Admin */
			/* Status: 0 => All; 1 => Pending; 2 => Accepted; 3 => Rejected */
			loadAddressedEntities: function( page, pageSize, status, orderByIdAsc) {
				return $http({
					method: 'GET',
					url: 'rest-api/admin/logged-user/payments',
					params: {
						page: page,
						pageSize: pageSize,
						status: status,
						orderByIdAsc: orderByIdAsc
					}
				}).then( function( r) {
					return r.data;
				}, function( r) {
					return $q.reject( r);
				})
			},
			/* All entities, even those that are not addressed to the logged admin */
			loadEntities: function( page, pageSize, status, orderByIdAsc) {
				return $http({
					method: 'GET',
					url: 'rest-api/admin/payments',
					params: {
						page: page,
						pageSize: pageSize,
						status: status,
						orderByIdAsc: orderByIdAsc
					}
				}).then( function( r) {
					return r.data;
				}, function( r) {
					return $q.reject( r);
				})
			},
			loadArticleOrders: function( id, page, pageSize) {
				return $http({
					method: 'GET',
					url: 'rest-api/admin/payment/' + id + '/article-orders'
				}).then( function( r) {
					return r.data;
				}, function( r) {
					return $q.reject( r);
				});
			},
			loadShopSub: function( id) {
					return $http({
						method: 'GET',
						url: 'rest-api/admin/payment/' + id + '/shop-sub'
					}).then( function( r) {
						return r.data;
				}, function( r) {
					return $q.reject( r);
				});
			},
			loadAdvOffer: function( id) {
				return $http({
					method: 'GET',
					url: 'rest-api/admin/payment/' + id + '/adv-offer'
				}).then( function( r) {
					return r.data;
				}, function( r) {
					return $q.reject( r);
				});
			},
			loadEntity: function( id) {
				return $http({
					method: 'GET',
					url: 'rest-api/admin/payment/' + id,
				}).then( function( r) {
					return r.data;
				}, function( r) {
					return $q.reject( r);
				});
			}
		}
	}                            
]);

