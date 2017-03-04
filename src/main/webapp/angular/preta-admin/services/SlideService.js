'use strict';
/*AngularJS Service for User And EShop Registration*/
App.factory( 'SlideService', ['$http', '$q', 'Upload', function( $http, $q, Upload) {
		return {
			loadEntities: function( page, pageSize, orderByDispOrderAsc) {
				return $http({
					url: 'rest-api/admin/slides',
					method: 'GET',
					params: {
						page: page,
						pageSize: pageSize,
						orderByDispOrderAsc: orderByDispOrderAsc
					}
				}).then( function( r) {
							return r.data;
						}, function( r) {
							console.error( 'Error: SlideService > loadEntities');
							return $q.reject( r);
						}
					);
			},
			addEntity: function( form) {
				return Upload.upload({
										url: 'rest-api/admin/slide/add',
										method: 'POST',
										data: {
											entity: angular.toJson( form.entity),
											file : form.file[0].lfFile
										}
									})
									.then( function( r) {
										return r;
									}, function( r) {
										return $q.reject( r);
									});
			},
			changeDisplayed: function( id) {
				return $http({
								method: 'PUT',
								url: 'rest-api/admin/slide/' + id + '/changed-displayed',
							}).then( function( r) {
								return r;
							}, function( r) {
								return $q.reject( r);
							});
			},
			moveEntity: function( id, moveUp) {
				return $http({
							method: 'POST',
							url: 'rest-api/admin/slide/' + id + '/move',
							data: $.param({
								up: moveUp
							}),
						    headers : {
						        'Content-Type' : 'application/x-www-form-urlencoded'
						    }
						}).then( function( r) {
							return r;
						}, function( r) {
							return $q.reject( r);
						});
				
			},
			deleteEntity: function( id) {
				return $http.post( 'rest-api/admin/slide/' + id + '/delete')
							.then( function( r) {
								return r;
							}, function( r) {
								return $q.reject( r);
							});
				
			}
		}
	}                            
]);

