'use strict';
App.factory( 'OrderService', ['$http', '$q', function( $http, $q) {
		return {
			loadEntities: function( page, pageSize, orderStatus, orderByIdAsc) {
				return $http({
								url: 'rest-api/admin/article-orders',
								method: 'GET',
								params: {
									page: page,
									pageSize: pageSize,
									orderStatus: orderStatus,
									orderByIdAsc: orderByIdAsc
								}
							})
							.then(	function( response) { 
										return response.data;
									},
									function( errResponse) {
										console.log( 'Error: OrderService > loadEntities');
										return $q.reject( errResponse);
									});
			},
			loadBuyer: function( id) {
				return $http.get( 'rest/article-order/' + id +'/buyer')
							.then(	function( response) { return response.data; },
									function( errResponse) {
										console.log( 'Error: OrderService > getBuyer');
										return $q.reject( errResponse);
									});
				
			},
			loadEntity: function( id) {
				return $http.get( 'rest-api/admin/article-order/' + id)
				.then(	function( response) { return response.data; },
						function( errResponse) {
							console.log( 'Error: OrderService > loadEntity');
							return $q.reject( errResponse);
						});
			},
			loadPayments: function( id, page, pageSize) {
				return $http({
								url: 'rest-api/admin/article-order/' + id + '/payments',
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
			loadOrderedArticlesByOrder: function( articleOrderId) {
				return $http.get( 'rest/article-order/' + articleOrderId + '/ordered-articles')
							.then( function( response) {
								return response.data;
							}, function( response) {
								return $q.reject( response);
							});
			},
			loadEShop: function( id) {
				return $http.get( 'rest-api/article-order/' + id + '/e-shop')
							.then( function( response) {
								return response.data;
							}, function( response) {
								return $q.reject( response);
							});
				
			},
		}
	}                            
]);