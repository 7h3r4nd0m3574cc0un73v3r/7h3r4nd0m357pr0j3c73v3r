'use strict';
App.factory( 'CategoryService', ['$http', '$q', 'Upload', function( $http, $q, Upload) {
		return {
			loadEntities: function() {
				return $http.get( 'rest-api/categories')
							.then(	function( response) { return response.data; },
									function( errResponse) {
										console.log( 'Error: CategoryService > loadEntities');
										return $q.reject( errResponse);
									});
			},
			loadMSTFormatedEntities: function() {
				/* Special Structure for the Multi Select Tree Plugin of Angular JS,
				 * used to add Categories to an article 
				 */
				return $http.get( 'rest-api/manager/categories/mst-formated')
							.then(	function( response) { return response.data; },
									function( errResponse) {
										console.log( 'Error: CategoryService > loadMultiSelectTreeEntities');
										return $q.reject( errResponse);
									}); 
			},
			loadEntity: function( id) {
				return $http.get( 'rest-api/category/' + id)
							.then( function( response) {
								return response.data;
							}, function( errResponse) {
								console.log( 'Error: CategoryService > getEntity');
								return $q.reject( errResponse);
							});
			}
		}
	}                            
]);

