'use strict';
App.factory( 'OrderService', ['$http', '$q', 'Upload', function( $http, $q, Upload) {
		return {
			loadByManagerAndStatus: function( page, pageSize, orderByIdAsc, orderStatus) {
				return $http({
					url: 'rest-api/manager/logged-user/article-orders',
					method: 'GET',
					params: {
						page: page,
						pageSize: pageSize,
						orderByIdAsc: orderByIdAsc,
						orderStatus: orderStatus
					}
				}).then( function( response) { 
						return response.data;
					},
					function( errResponse) {
						console.log( 'Error: OrderService > loadEntitiesByManagerAndStatus');
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
				return $http.get( 'rest-api/manager/article-order/' + id)
					.then(	function( response) { return response.data; },
							function( errResponse) {
								console.log( 'Error: OrderService > loadEntity');
								return $q.reject( errResponse);
							});
			},
			loadPayments: function( id, page, pageSize) {
				return $http({
								url: 'rest-api/manager/article-order/' + id + '/payments',
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
								return response.data ;
							}, function( response) {
								return $q.reject( response);
							});
			},
			deliverEntity: function( id, packageId) {
				return Upload.upload( {
							url :'rest-api/manager/article-order/deliver',
							method: 'POST',
							data: {
								id: id,
								packageId: packageId
							}
						})
						.then( function( response) {
							console.log( response);
						}, function( response) {
							return $q.reject( response);
						});
			},
			loadEShop: function( id) {
				return $http({
								method: 'GET',
								url: 'rest-api/manager/article-order/' + id + '/e-shop'
							}).then( function( response) {
								return response.data;
							}, function( response) {
								return $q.reject( response);
							});
			}
		}
	}                            
]);