'use strict';
App.factory( 'ArticleService', ['$http', '$q', 'Upload', function( $http, $q) {
		return {
			loadEShop: function( id) {
				return $http.get( 'rest-api/article/' + id + '/e-shop')
							.then(	function( response) { return response.data; },
									function( errResponse) {
										console.log( 'Error: ArticleService > loadEShop');
										return $q.reject( errResponse);
									});
			},
		}
	}                            
]);

