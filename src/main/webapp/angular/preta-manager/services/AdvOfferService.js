'use strict';
App.factory( 'AdvOfferService', ['$http', '$q', function( $http, $q) {
		return {
			loadEntitiesByManager: function( page, pageSize, status, orderByIdAsc) {
				return $http({
					method: 'GET',
					url: 'rest-api/manager/logged-user/adv-offers',
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
				return $http.get( 'rest-api/manager/adv-offer/' + id)
							.then( function( response) {
								return response.data;
							}, function( response) {
								return $q.reject( response);
							});
			},
			loadAdvOptions: function( page, pageSize) {
				return $http({
					url: 'rest-api/manager/adv-options',
					method: 'GET',
					params: {
						page: page,
						pageSize: pageSize
					}
				}).then( function( response) {
					return response.data;
				}, function( response) {
				    return $q.reject( response);
				});
			},
			addEntity: function( entity, adminEAccount) {
				return $http({
								method: 'POST',
								url: 'rest-api/manager/adv-offer/add',
								data:  $.param({
									advOffer: angular.toJson( entity.advOffer),
									paymentRef: entity.paymentRef,
									eAccountId: entity.eAccount == undefined ? null : entity.eAccount.id,
									adminEAccountId: adminEAccount == undefined ? null : adminEAccount.id
								}),
							    headers : {
							        'Content-Type' : 'application/x-www-form-urlencoded'
							    }
							}).then( function( response) {
								console.log( response);
							}, function( response) {
								return $q.reject( response);
							});
			},
			actDeactEntity: function( id) {
				return $http.post( 'rest-api/manager/adv-offer/' + id + '/act-deact', { id: id})
							.then( function( response) {
								return response.data;
							}, function( response) {
								return $q.reject( response);
							});
			},
			loadPayments: function( id, page, pageSize) {
				return $http.get( 'rest-api/manager/adv-offer/' + id + '/payments', { page: page, pageSize: pageSize})
							.then( function( response) {
								return response.data;
							}, function( response) {
								return $q.reject( response);
							});
			}
		}
	}
]);

