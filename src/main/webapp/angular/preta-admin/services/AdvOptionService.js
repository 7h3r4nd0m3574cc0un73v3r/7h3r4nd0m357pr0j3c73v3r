'use strict';
//AngularJS Service for User And EShop Registration
App.factory( 'AdvOptionService', ['$http', '$q', function( $http, $q) {
		return {
			loadEntities: function() {
				return $http.get( 'rest-api/admin/adv-options')
							.then( function( response) {
									return response.data;
								}, function( errResponse) {
									console.log( 'Error: AdvOptionService > loadAdvOptions');
									return $q.reject( errResponse);
								}
							);
			},
			getEntity: function( id) {
				return $http.get( 'rest-api/admin/adv-option/get/' + id)
							.then(	function( response) { return response.data; },
									function( errResponse) {
										console.log( 'Error: AdvOptionService > getEntity');
										return $q.reject( errResponse);
									});
			},
			addEntity: function( entity) {
				return $http.post( 'rest-api/admin/adv-option/add', entity)
							.then( function( response) {
								return response.data;
							}, function( errResponse) {
								console.log( 'Error: AdvOptionService > addAdvOption');
								return $q.reject( errResponse);
							});
			},
			editEntity: function( entity) {
				return $http.put( 'rest-api/admin/adv-option/edit/' + entity.id, entity)
							.then( function( response) {
								return response.data;
							}, function( errResponse) {
								console.log( 'Error: AdvOptionService > editAdvOption');
								return $q.reject( errResponse);
							});
			},
			deleteEntity: function( id) {
				return $http.put( 'rest-api/admin/adv-option/delete/' + id)
							.then( function( response) {
								return response.data;
							}, function( errResponse) {
								console.log( 'Error: AdvOptionService > deleteAdvOption');
								return $q.reject( errResponse);
							});
			}
		}
	}                            
]);

