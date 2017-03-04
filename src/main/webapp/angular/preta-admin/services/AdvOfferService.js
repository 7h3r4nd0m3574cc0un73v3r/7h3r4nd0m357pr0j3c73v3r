'use strict';
//AngularJS Service for User And EShop Registration
App.factory( 'AdvOfferService', ['$http', '$q', function( $http, $q) {
		return {
			loadEntities: function( page, pageSize, status, orderByIdAsc) {
				return $http({
					method: 'GET',
					url: 'rest-api/admin/adv-offers',
					params: {
						page: page,
						pageSize: pageSize,
						status: status,
						orderByIdAsc: orderByIdAsc
					}
				}).then( function( response) {
					return response.data;
				}, function( response) {
					return $q.reject( response);
				});
			},
			loadEntity: function( id) {
				return $http.get( 'rest-api/admin/adv-offer/' + id)
							.then( function( response) {
								return response.data;
							}, function( response) {
								return $q.reject( response);
							});
			},
			loadPayments: function( id, page, pageSize) {
				return $http.get( 'rest-api/admin/adv-offer/' + id + '/payments', { page: page, pageSize: pageSize})
							.then( function( response) {
								return response.data;
							}, function( response) {
								return $q.reject( response);
							});
			}
		}
	}                            
]);

