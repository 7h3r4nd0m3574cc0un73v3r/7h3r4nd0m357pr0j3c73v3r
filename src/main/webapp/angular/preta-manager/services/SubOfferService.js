'use strict';
App.factory( 'SubOfferService', ['$http', '$q', function( $http, $q) {
		return {
			loadEntities: function( page, pageSize) {
				return $http({
							url: 'rest-api/sub-offers',
							params: {
								page: page,
								pageSize: pageSize
							}
							})
							.then( 
								function( response) {
									return response.data;
								},
								function( errResponse) {
									console.log( 'Error: SubOfferService > listSubOffer');
									return $q.reject( errResponse);
								}
							);
			}
		}
	}                            
]);

