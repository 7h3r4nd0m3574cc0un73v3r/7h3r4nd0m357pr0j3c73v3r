'use strict';

App.factory( 'SlideService', [ '$http', '$q', function( $http, $q ) {
	return {
		loadEntities: function( page, pageSize) {
			return $http({
				method: 'GET',
				url: 'rest-api/slides',
				params: {
					page: page,
					pageSize: pageSize
				}
			})
			.then( function( r) {
				return r.data;
			}, function( r) {
				return $q.reject( r);
			});
		}
	}
}]);

