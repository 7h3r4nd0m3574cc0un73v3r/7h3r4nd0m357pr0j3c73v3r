'use strict';
App.factory( 'ArticleService', ['$http', '$q', 'Upload', function( $http, $q, Upload) {
		var activeArticle;
		return {
			addEntity: function( entity, eShopId, file0, file1, file2, file3) {				
				return Upload.upload({
					url: 'rest-api/manager/article/add',
					method: 'POST',
					data: {
						'entity': angular.toJson( entity),
						'eShopId': eShopId,
						'file0': file0,
						'file1': file1,
						'file2': file2,
						'file3': file3
					}
				}).then( function( response) {
					console.log( response);
				}, function( response) {
					console.log( 'Error: ArticleService > addArticle');
					return $q.reject( response);
				});
			},
			loadEntitiesByEShop: function( eShopId, page, pageSize) {
				return $http({
					url: 'rest-api/e-shop/' + eShopId + '/articles',
					method: 'GET',
					params: {
						page: page,
						pageSize: pageSize
					}
				}).then( function( response) {
						console.log( response);
						return response.data;
					},
					function( response) {
						console.error( response);
				  });
			},
			loadEntity: function( id) {
				return $http.get( 'rest-api/manager/article/' + id)
							.then( function( response) {
								return response.data;
							}, function( response) {
								return $q.reject( response);
							});
			},
			getDefaultPic: function( id) {
				return $http.get( 'rest/article/fetchDefaultPic/' + id)
							.then( function( response) {
								return response.data;
							}, function( response) {
								return $q.reject( response);
							});
			},
			deleteEntity: function( id) {
				return $http.put( 'rest-api/manager/article/' + id + '/delete')
					.then( function( response) {
						return response.data;
					}, function( errResponse) {
						console.log( 'Error: ArticleService > deleteEntity');
						return $q.reject( errResponse);
					});
			},
			fetchPicture: function( id) {
				return $http.get( 'rest/article/fetch-picture/' + id)
							.then( function( response) {
								return response.data;
							}, function( response) {
								return $q.reject( response);
							});
			},
			loadMSTFormatedCategories: function( id) {
				return $http.get( 'rest-api/manager/article/' + id + '/mst-formated-categories')
							.then( function( response) {
									return response.data;
								},
								function( errResponse) {
									console.log( 'Error: ArticleService > loadMSTFormatedCategories');
									return $q.reject( errResponse);
								}
							);
				
			},
			removePicture: function( articleId, pictureId) {
				return $http.post( 'rest/article/' + articleId + '/remove-picture/' + pictureId)
							.then( function( response) {
								return response;
							}, function( response) {
								return $q.reject( response);
							});
			},
			updateEntity: function( entity, file0, file1, file2, file3) {
				return Upload.upload({
									url: 'rest-api/manager/article/' + entity.id + '/update',
									method: 'POST',
									data: {
										'entity': angular.toJson( entity),
										'file0': file0,
										'file1': file1,
										'file2': file2,
										'file3': file3
									}
								}).then( function( response) {
									console.log( response);
								}, function( response) {
									console.log( 'Error: ArticleService > updateArticle');
									return $q.reject( response);
								});
				
			},
			loadArticleFeatures: function( id, page, pageSize) {
				return $http({
								url: 'rest-api/article/' + id + '/article-features',
								method: 'GET',
								params: {
									page: page,
									pageSize: pageSize
								}								
							}).then( function( response) {
								return response.data;
							}, function( response) {
								return $q.reject( response);
							})
			},
			loadPictures: function( id, page, pageSize) {
				return $http({
								url: 'rest-api/article/' + id + '/pictures',
								method: 'GET',
								params: {
									page: page,
									pageSize: pageSize
								}					
							}).then( function( response) {
								return response.data;
							}, function( response) {
								return $q.reject( response);
							})
			},
			loadDefaultPicture: function( id) {
				return $http.get( 'rest-api/article/' + id + '/default-picture')
							.then( function( response) {
								return response.data;
							}, function( response) {
								console.error( response);
							});
			},
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

