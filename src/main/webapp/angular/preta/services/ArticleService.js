'use strict';

App.factory( 'ArticleService', ['$http', '$q', 'Upload', function( $http, $q, Upload) {
		var articlesToOrder = [];
		var articleToCart = {};
		var referrer = 0;
		
		return {
			loadLatest: function( page, pageSize) {
				return $http({
							url: 'rest-api/articles/latest',
							method: 'GET',
							params: {
								page: page,
								pageSize: pageSize
							}
						})
						.then( function( response) {
							return response.data;
						}, function( response) {
							return $q.reject( response);
						});
			},
			loadTopArticles: function( page, pageSize) {
				return $http({
							url: 'rest-api/articles/top-rated',
							method: 'GET',
							params: {
								page: page,
								pageSize: pageSize
							}
						})
						.then( function( response) {
							return response.data;
						}, function( response) {
							return $q.reject( response);
						});
			},
			loadBestSellers: function( page, pageSize) {
				return $http({
							url: 'rest-api/articles/best-sellers',
							method: 'GET',
							params: {
								page: page,
								pageSize: pageSize
							}
						})
						.then( function( response) {
							return response.data;
						}, function( response) {
							return $q.reject( response);
						});
			},
			loadDiscounted: function( page, pageSize) {
				return $http({
							url: 'rest-api/articles/discounted',
							method: 'GET',
							params: {
								page: page,
								pageSize: pageSize
							}
						})
						.then( function( response) {
							return response.data;
						}, function( response) {
							return $q.reject( response);
						});
			},
			loadEntity: function( id) {
				return $http.get( 'rest-api/article/' + id)
							.then( function( response) {
								return response.data;
							}, function( response) {
								return $q.reject( response);
							});
			},
			loadArticleToRate: function( page, pageSize) {
				return $http({
								url: 'rest-api/ordered-article/pending-rating',
								method: 'GET',
								params: {
									page: page,
									pageSize: pageSize,
								}
							})
							.then( function( response) {
								return response.data;
							}, function( response) {
								return $q.reject( response);
							});
			},
			rateOrderedArticle: function( id, rating) {
				return $http({
								url : 'rest-api/ordered-article/' + id + '/rate',
								method: 'POST',
								data: rating
							}).then( function( response) {
								console.log( response);
							}, function( response) {
								return $q.reject( response);
							});
			},
			searchArticles: function( name, page, pageSize, minPrice, maxPrice) {
				return $http({
								url: 'rest-api/articles/search',
								method: 'GET',
								params: {
									name: name,
									page: page,
									pageSize: pageSize,
									minPrice: minPrice,
									maxPrice: maxPrice
								},
							})
							.then( function( response) {
								return response.data;
							}, function( response) {
								return $q.reject( response);
							});
			},
			loadByCategory: function( id, page, pageSize) {
				return $http({
							url: 'rest-api/category/' + id + '/articles',
							method: 'GET',
							params: {
								page: page,
								pageSize: pageSize,
							}
						})
						.then( function( response) {
							return response.data;
						}, function( response) {
							return $q.reject( response);
						});
			},
			loadSimilarArticles: function( id, page, pageSize) {
				return $http({
							url: 'rest-api/article/' + id + '/similar',
							method: 'GET',
							params: {
								page: page,
								pageSize: pageSize,
							}
						})
						.then( function( response) {
							return response.data;
						}, function( response) {
							return $q.reject( response);
						});
			},
			loadRecentArticlesByEShop: function( id, page, pageSize) {
				return $http({
							url: 'rest-api/e-shop/' + id + '/latest-articles',
							method: 'GET',
							params: {
								page: page,
								pageSize: pageSize
							}
						})
						.then( function( response) {
							return response.data;
						}, function( response) {
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
								return $q.reject( response);
							});
			},
			loadEShop: function( id) {
				return $http({
								url: 'rest-api/article/' + id + '/e-shop',
								method: 'GET'						
							}).then( function( response) {
								return response.data;
							}, function( response) {
								return $q.reject( response);
							})
			},
			loadMinPrice: function() {
				return $http({
								url: 'rest-api/article/min-price',
								method: 'GET'						
							}).then( function( response) {
								return response.data;
							}, function( response) {
								return $q.reject( response);
							});
			},
			loadMaxPrice: function() {
				return $http({
					url: 'rest-api/article/max-price',
					method: 'GET'
							}).then( function( response) {
								return response.data;
							}, function( response) {
								return $q.reject( response);
							});
			},
			/* Cart */
			addToCart: function( entity, quantity) {
				return Upload.upload({
								url: 'rest-api/logged-user/cart/add-article',
								method: 'POST',
								data: {
									'entity': angular.toJson( entity),
									'quantity': angular.toJson( quantity)
								}
							}).then( function( response) {
								return response;
							}, function( response) {
								return $q.reject( response);
							});
			},
			removeFromCart: function( id) {
				return $http.post( 'rest-api/logged-user/cart/remove/' + id)
							.then( function( response) {
								return response;
							}, function( response) {
								console.log( 'Error: ArticleService > removeArticleFromCart');
								return $q.reject( response);
							})
			},
			loadCartContent: function( page, pageSize) {
				return $http({
								url: 'rest-api/logged-user/cart-items',
								method: 'GET',
								params: {
									page: page,
									pageSize: pageSize
								}
							}).then( function( response) {
								return response.data;
							},
							function( errResponse) {
								console.log( 'Error: ArticleService > loadCartContent');
								return $q.reject( errResponse);
							});
			},
			clearCart: function() {
				return $http({
					url: 'rest-api/logged-user/clear-cart-items',
					method: 'POST',
				}).then( function( response) { return response.data;}, function( response) { return $q.reject( response);});
			},
			/* End Cart */
			/* Last Visited */
			addVisited: function( id) {
				return $http({
					method: 'POST',
					url: 'rest-api/logged-user/add-visited-article',
					data: $.param({
						id: id
					}),
					headers: {
						'Content-type': 'application/x-www-form-urlencoded'
					}
				})
				.then( function( response) {
					return response;
				}, function( response) {
					return $q.reject( response);
				});
			},
			loadLastVisited: function( page, pageSize, orderByIdAsc) {
				return $http({
								url: 'rest-api/logged-user/visited-articles',
								method: 'GET',
								params: {
									page: page,
									pageSize: pageSize,
									orderByIdAsc: orderByIdAsc
								}
							})
							.then( function( response) {
								return response.data;
							},
							function( errResponse) {
								console.log( 'Error: ArticleService > loadLastVisited');
								return $q.reject( errResponse);
							});
			},
			clearSelection: function( ids) {
				return $http({
					method: 'POST',
					url: 'rest-api/logged-user/clear-cart-selection',
					data: ids
				}).then( function( response) {
					return response;
				}, function( response) {
					return $q.reject( response);
				});
			},
			/* End Last Visited */
			/* Advertised */
			loadAdvertised: function( page, pageSize, code) {
				return $http({
								url: 'rest-api/articles/advertised',
								method: 'GET',
								params: {
									page: page,
									pageSize: pageSize,
									code: code
								}
							}).then( function( response) {
								return response.data;
							},
							function( response) {
								console.log( 'Error: ArticleService > loadAdvertised');
								return $q.reject( response);
							});
			},
		}
	}                            
]);

