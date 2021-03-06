'use strict';
App.factory( 'ExpenseService', ['$http', '$q', function( $http, $q) {
		var articleOrders = [];
		return {
			/* All entities, even those that are not addressed to the logged admin */
			loadAddressedEntities: function( page, pageSize, orderByIdAsc) {
				return $http({
					method: 'GET',
					url: 'rest-api/admin/logged-user/expenses',
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
					url: 'rest-api/admin/expense/' + id,
				}).then( function( r) {
					return r.data;
				}, function( r) {
					return $q.reject( r);
				});
			},
			addEntity: function( entity) {
				return $http({
					method: 'POST',
					url: 'rest-api/admin/expense/add',
					data: entity
				}).then( function( r) {
					return r;
				}, function( r) {
					return $q.reject( r);
				});
			},
			loadEntities: function( page, pageSize, orderByIdAsc) {
				return $http({
					method: 'GET',
					url: 'rest-api/admin/logged-user/expenses',
					params: {
						page: page,
						pageSize: pageSize,
						orderByIdAsc: orderByIdAsc
					}
				}).then( function( r) {
					return r.data;
				}, function( r) {
					return $q.reject( r);
				});
			},
			loadManagerEAccounts: function( ids) {
				return $http({
					method: 'POST',
					url: 'rest-api/admin/manager-e-accounts',
					data: ids
				}).then( function( r) {
					return r.data;
				}, function( r) {
					return $q.reject( r);
				});
			},
			loadArticleOrders: function( id, page, pageSize) {
				return $http({
					method: 'GET',
					url: 'rest-api/admin/expense/' + id + '/article-orders',
					params: {
						page: page,
						pageSize: pageSize,
					}
				}).then( function( r) {
					return r.data;
				}, function( r) {
					return $q.reject( r);
				});
			},
			loadManager: function( id) {
				return $http({
					method: 'GET',
					url: 'rest-api/admin/expense/' + id + '/manager',
				}).then( function( r) {
					return r.data;
				}, function( r) {
					return $q.reject( r);
				});
				
			}
		}
	}                            
]);

