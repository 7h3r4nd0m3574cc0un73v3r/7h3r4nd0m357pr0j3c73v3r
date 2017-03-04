'use strict';

App.factory( 'NotificationService', [ '$http', '$q', function( $http, $q) {
	return {
		loadEntities: function( page, pageSize, status, orderByIdAsc, restrict) {
			return $http({
				method: 'GET',
				url: 'rest-api/logged-user/notifications',
				params: {
					page: page,
					pageSize: pageSize,
					status: status,
					orderByIdAsc: orderByIdAsc,
					restrict: restrict
				}
			}).then( function( r) {
				return r.data;
			}, function( r) {
				return $q.reject( r);
			});
		},
		read: function( id) {
			return $http({
				method: 'PUT',
				url: 'rest-api/logged-user/notification/' + id + '/read'
			}).then( function( r) {
				return r;
			}, function( r) {
				return $q.reject( response);
			});
		},
		multipleRead: function( ids) {
			return $http({
				method: 'PUT',
				url: 'rest-api/logged-user/read-notifications',
				data: ids
			}).then( function( r) {
				return r;
			}, function( r) {
				return $q.reject( response);
			});
		}
	}
}]);

