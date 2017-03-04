'use strict';
//AngularJS Service for  Admins
App.factory( 'AdminService', ['$http', '$q', function( $http, $q) {
		return {
			listEntities: function() {
				return $http.get( 'rest/admins')
							.then(
								function( response) { return response.data; },
								function( errResponse) {
									console.log( 'Error: Service > listAdmin');
									return $q.reject( errResponse);
								}
							);
			},
			addEntity: function( entity) {
				return $http.post( 'rest/user/admin/add', entity)
							.then( function( response) {
								return response.data;
							}, function( errResponse) {
								console.log( 'Error: AdminService > addAdmin');
								return $q.reject( errResponse);
							});
			},
			getEntity: function( id) {
				return $http.get( 'rest/user/admin/get/' + id)
							.then( function( response) {
										return response.data;
									},
									function( errResponse) {
										console.error( 'Error: AdminService > getAdmin');
										return $q.reject( errResponse);
									}
							);
			},
			deleteEntity: function( id) {
				return $http.put( 'rest/user/admin/delete/' + id)
							.then( function( response) {
								return response.data;
							}, function( errResponse) {
								console.log( 'Error: AdminService > addAdmin');
								return $q.reject( errResponse);
							});
			}
		}
	}                            
]);

