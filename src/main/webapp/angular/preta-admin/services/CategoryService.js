'use strict';
App.factory( 'CategoryService', ['$http', '$q', 'Upload', function( $http, $q, Upload) {
		return {
			loadEntities: function() {
				return $http.get( 'rest-api/admin/categories')
							.then(	function( response) { return response.data; },
									function( errResponse) {
										console.log( 'Error: CategoryService > loadEntities');
										return $q.reject( errResponse);
									});
			},
			loadTreeData: function() {
				return $http.get( 'rest-api/admin/categories/jstree-formated')
							.then(	function( response) { return response.data; },
									function( errResponse) {
										console.log( 'Error: CategoryService > loadEntities');
										return $q.reject( errResponse);
									});
			},
			loadEntity: function( id) {
				return $http.get( 'rest-api/admin/category/' + id)
							.then( function( response) {
								return response.data;
							}, function( errResponse) {
								console.log( 'Error: CategoryService > getEntity');
								return $q.reject( errResponse);
							});
			},
			addEntity: function( entity) {
				return Upload.upload({
					url: 'rest-api/admin/category/add',
					method: 'POST',
					data: {
						label: entity.label,
						description: entity.description,
						parent: angular.toJson( entity.parent)
					}
				})
				.then( function( response) {
					return response;
				}, function( response) {
					return $q.reject( response);
				});
			},
			deleteEntity: function( id) {
				return $http.put( 'rest-api/admin/category/' + id + '/delete')
							.then( function( response) {
								return response;
							}, function( response) {
								return $q.reject( response);
							});
			}
		}
	}                            
]);

