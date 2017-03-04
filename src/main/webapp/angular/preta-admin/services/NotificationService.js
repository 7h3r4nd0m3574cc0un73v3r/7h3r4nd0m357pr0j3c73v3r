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
		}
	}
}]);

