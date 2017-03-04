'use strict';

App.factory( 'CategoryService', [ '$http', '$q', function( $http, $q) {
		return {
			loadEntities: function() {
				return $http.get( 'rest-api/categories/level/0')
							.then( function( response) {
									return response.data;
								},
								function( errResponse) {
									console.log( 'Error: CategoryService > loadEntities');
									return $q.reject( errResponse);
								});
			},
			loadEntity: function( id) {
				return $http.get( 'rest-api/category/' + id)
							.then( function( response) {
								return response.data;
							}, function( response) {
								console.log( 'Error: CategoryService > loadEntity');
								return $q.reject( response);
							});
			}
		}
	}                            
]);

