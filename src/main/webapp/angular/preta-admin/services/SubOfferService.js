'use strict';
//AngularJS Service for User And EShop Registration
App.factory( 'SubOfferService', ['$http', '$q', function( $http, $q) {
		return {
			loadEntities: function() {
				return $http({
					url: 'rest-api/sub-offers',
					params: {
						page: "1",
						pageSize: "0"
					}
				}).then( 
						function( response) {
							return response.data;
						},
						function( errResponse) {
							console.log( 'Error: SubOfferService > listSubOffer');
							return $q.reject( errResponse);
						}
					);
			},
			addEntity: function( entity) {
				return $http.post( 'rest/sub-offer/add', entity)
							.then( function( response) {
								return response.data;
							}, function( errResponse) {
								console.log( 'Error: SubOfferService > addSubOffer');
								return $q.reject( errResponse);
							});
			},
			editEntity: function( entity) {
				return $http.put( 'rest/sub-offer/edit/' + entity.id, entity)
							.then( function( response) {
								return response.data;
							}, function( errResponse) {
								console.log( 'Error: SubOfferService > addSubOffer');
								return $q.reject( errResponse);
							});
			},
			deleteEntity: function( id) {
				return $http.put( 'rest/sub-offer/delete/' + id)
							.then( function( response) {
								return response.data;
							}, function( errResponse) {
								console.log( 'Error: SubOfferService > addSubOffer');
								return $q.reject( errResponse);
							});
			}
		}
	}                            
]);

