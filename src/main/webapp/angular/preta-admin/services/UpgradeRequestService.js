'use strict';
//AngularJS Service for User And EShop Registration
App.factory( 'UpgradeRequestService', ['$http', '$q', function( $http, $q) {
		return {
			/* Status: 0 = All, 1 = Not Validated, 2 = Validated*/
			loadEntities: function( page, pageSize, orderByIdAsc, status) {
				return $http({
					method: 'GET',
					url: 'rest-api/admin/upgrade-requests',
					params: {
						page: page,
						pageSize: pageSize,
						orderByIdAsc: orderByIdAsc,
						status: status
					}
				}).then( function( response) {
					return response.data;
				}, function( response) {
					return $q.reject( response);
				});
			},
			loadEntity: function( id) {
				return $http({
					method: 'GET',
					url: 'rest-api/admin/upgrade-request/' + id,
				}).then( function( response) { return response.data;}, function( response) { return $q.reject( response);});
			},
			approveEntity: function( id) {
				return $http({
					method: 'POST',
					url: 'rest-api/admin/upgrade-request/' + id + '/approve',
				}).then( function( response) { return response.data;}, function( response) { return $q.reject( response);});
			}
			
		}
	}                            
]);