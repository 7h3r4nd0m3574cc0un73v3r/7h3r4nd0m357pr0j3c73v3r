'use strict';
App.factory( 'ExpenseService', ['$http', '$q', function( $http, $q) {
		var articleOrders = [];
		return {
			/* All entities, even those that are not addressed to the logged admin */
			loadAddressedEntities: function( page, pageSize, orderByIdAsc) {
				return $http({
					method: 'GET',
					url: 'rest-api/manager/logged-user/expenses',
					params: {
						page: page,
						pageSize: pageSize,
						orderByIdAsc: orderByIdAsc
					}
				}).then( function( r) {
					return r.data;
				}, function( r) {
					return $q.reject( r);
				})
			},
			loadEntity: function( id) {
				return $http({
					method: 'GET',
					url: 'rest-api/manager/expense/' + id,
				}).then( function( r) {
					return r.data;
				}, function( r) {
					return $q.reject( r);
				});
			},
			loadArticleOrders: function( id, page, pageSize) {
				return $http({
					method: 'GET',
					url: 'rest-api/manager/expense/' + id + '/article-orders',
					params: {
						page: page,
						pageSize: pageSize,
					}
				}).then( function( r) {
					return r.data;
				}, function( r) {
					return $q.reject( r);
				});
			}
		}
	}                            
]);

