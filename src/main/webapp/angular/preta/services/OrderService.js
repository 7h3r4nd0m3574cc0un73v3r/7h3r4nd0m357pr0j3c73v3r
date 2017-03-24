'use strict';

App.factory( 'OrderService', ['$http', '$q', 'Upload', function( $http, $q, Upload) {
		var orderedArticles = [];
		
		return {
			addArticleOrder: function( entities, eAccountId, paymentRef, adminEAccountId,
										useDefaultDeliveryAddress, deliveryAddress) {
				return $http({
					method: 'POST',
					url: 'rest-api/article-order/add',
					data: $.param({
						'entities': angular.toJson( entities),
						'eAccountId':  eAccountId,
						'paymentRef': paymentRef,
						'adminEAccountId': adminEAccountId,
						'useDefaultDeliveryAddress': useDefaultDeliveryAddress,
						'deliveryAddress': deliveryAddress
					}),
				    headers : {
				        'Content-Type' : 'application/x-www-form-urlencoded'
				    }
				})
				.then( function( response) {
					return response;
				}, function( response) {
					console.log( 'Error: OrderService > addArticleOrder');
					return $q.reject( response);
				});
				
			},
			loadOrdersByUser: function( page, pageSize, orderStatus, orderByIdAsc) {
				return $http({
								url: 'rest-api/logged-user/article-orders',
								method: 'GET',
								params: {
									page: page,
									pageSize: pageSize,
									orderStatus: orderStatus,
									orderByIdAsc: orderByIdAsc
								}
							}).then( function( response) {
								return response.data;
							}, function( response) {
								console.log( 'Error: OrderService > OrdersByUser');
								return $q.reject( response);
							});
			},
			loadOrderedArticlesByOrder: function( articleOrderId) {
				return $http.get( 'rest-api/article-order/' + articleOrderId + '/ordered-articles')
							.then( function( response) {
								return response.data;
							}, function( response) {
								return $q.reject( response);
							});
			},
			loadPaymentsByOrder: function( articleOrderId) {
				return $http.get( 'rest/article-order/' + articleOrderId + '/payments')
							.then( function( response) {
								return response.data;
							}, function( response) {
								return $q.reject( response);
							});
			},
			confirmReception: function( id) {
				return $http.post( 'rest-api/article-order/' + id + '/confirm-delivery')
							.then( function( response) {
								return response.data;
							}, function( response) {
								return $q.reject( response);
							})
			},
			loadEntity: function( id) {
				return $http.get( 'rest-api/article-order/' + id)
							.then( function( response) {
								return response.data;
							}, function( response) {
								return $q.reject( response);
							});
			},
			loadArticleOrderEShop: function( id) {
				return $http.get( 'rest-api/article-order/' + id + '/e-shop')
							.then( function( response) {
								return response.data;
							}, function( response) {
								return $q.reject( response);
							});
				
			},
			/* Shinyusha */
			loadAdminEAccountsForPayments: function() {
				return $http({
					method: 'GET',
					url: 'rest-api/e-accounts-for-payments',
				}).then( function( response) { return response.data}, function( response) { return $q.reject( response);});
			},
			/* Grouping for orders */
			group: function( entities) {
				return $http({
					method: 'POST',
					url: 'rest-api/group-article-orders',
					data: entities,
				}).then( function( response) {
					return response.data;
				}, function( response) {
					console.error( response);
				})
			}
		}
	}                            
]);

