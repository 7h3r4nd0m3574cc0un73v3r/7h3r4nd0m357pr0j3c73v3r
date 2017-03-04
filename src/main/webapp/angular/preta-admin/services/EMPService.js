'use strict';
/*AngularJS Service for User And EShop Registration*/
App.factory( 'EMPService', ['$http', '$q', 'Upload', function( $http, $q, Upload) {
		return {
			loadEntities: function() {
				return $http({
					url: 'rest-api/admin/e-money-providers',
					method: 'GET',
					params: {
						page: "1",
						pageSize: "0"
					}
				}).then( function( response) {
							return response.data;
						}, function( errResponse) {
							console.log( 'Error: EMPService > listEMP');
							return $q.reject( errResponse);
						}
					);
			},
			loadEntity: function( id) {
				return $http.get( 'rest-api/admin/e-money-provider/' + id)
							.then( function( response) {
								return response.data;
							}, function( response) {
								return $q.reject( response);
							});
			},
			addEntity: function( entity, file) {
				return Upload.upload({
					url: 'rest-api/admin/e-money-provider/add',
					method: 'POST',
					data: { file: file, 'entity': angular.toJson( entity) }
				}).then( function( response) {
					console.log( response);
				}, function( errResponse) {
					console.log( 'Error: EMPService > addEMP');
					return $q.reject( errResponse);
				});
			},
			updateEntity: function( entity, file) {
				/* Delete the hasLogo property because it's not persistent*/
				delete entity.hasLogo;
				
				return Upload.upload({
					url: 'rest-api/admin/e-money-provider/' + entity.id + '/update',
					method: 'POST',
					data: { file: file , 'entity': angular.toJson( entity) }
				}).then( function( response) {
					console.log( response);
				}, function( errResponse) {
					console.log( 'Error: EMPService > updateEMP');
					return $q.reject( errResponse);
				});
			},
			deleteEntity: function( id) {
				return $http.put( 'rest-api/admin/e-money-provider/' + id + "/delete")
							.then( function( response) {
								return response.data;
							}, function( errResponse) {
								console.log( 'Error: EMPService > deleteEMP');
								return $q.reject( errResponse);
							});
			}
		}
	}                            
]);

