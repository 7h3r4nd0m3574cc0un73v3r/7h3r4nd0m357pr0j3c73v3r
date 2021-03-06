'use strict';

var App = angular.module( "pretaApp", [
                                       'ngAnimate',
                                       'ui.bootstrap',
                                       'ui.sortable',
                                       'ui.router',
                                       /*'ngTouch',*/
                                       'toastr',
                                       'smart-table',
                                       'ui.slimscroll',
                                       'angular-progress-button-styles',
                                       'ncy-angular-breadcrumb',
                                       'ngIntlTelInput',
                                       'wt.responsive',
                                       'ngFileUpload',
                                       'ngMaterial',
                                       'ngStomp',
                                       'ngAudio',
                                       'ngMessages',
                                       'rzModule',
                                       'vcRecaptcha',
                                       'angular-preload-image',
                                       'cgBusy'
                                      ]);
/* cgBusy Config */
App.value( 'cgBusyDefaults'), {
	message: 'Merci de patienter ...',
	templateUrl: 'angular/common/templates/custom-cg-busy.html'
}

/* Config Interceptor */
App.factory('myInterceptor', [ '$q', '$injector', function( $q, $injector) {  
    var myInterceptor = {
    	/*response: function( config) {
    		console.log( "Request Intercepted");
    		console.log( config);
    		
    		return config;
    	},
    	requestError: function( rejection) {
    		console.log( "Request Error Intercepted");
    		console.log( rejection);
    		
    		return $q.reject( rejection);
    	},*/
    	responseError: function( rejection) {
    		/* Retrieve $state from injector */
    		var $state = $injector.get( '$state');
    		/* Redirect All Unathorized to 401 Error Page */
    		if( ( rejection.status == 401 && !( rejection.config.url == 'rest-api/logged-user' || rejection.config.url == 'rest-api/login')))
    			$state.go( 'root.errors.401');
    		/* redirects All WS 500 errors to Error 500 page */
    		if( rejection.status == 500)
    			$state.go( 'root.errors.500');

    		if( rejection.status == 503)
    			$state.go( 'root.errors.503');
    		
    		if( rejection.status == 403)
    			$state.go( 'root.errors.403');
    		
    		if( rejection.status == 404)
    			$state.go( 'root.errors.404');
    		
    		return $q.reject( rejection);
    	}
    };

    return myInterceptor;
}]);

App.config( config);

/* PretaApp Config */
function config( $stateProvider, $urlRouterProvider, $mdThemingProvider, $httpProvider) {	
	/* Theming Config */
	$mdThemingProvider.theme( 'default')
					  .primaryPalette( 'deep-orange')
					  .accentPalette( 'blue')
					  .warnPalette( 'red')
					  .backgroundPalette( 'grey');
	/* End Theming Config */
	
	/* mdDateProvider */
	
	/* End mdDateProvider */
	$httpProvider.interceptors.push('myInterceptor');
	
	/* Route Config */
	$urlRouterProvider.otherwise('');
	
	$stateProvider
		/*Home Page - Heavy stuff involved*/
		.state( 'root', {
			  url: '',
			  title: 'Accueil',
			  ncyBreadcrumb: {
				    label: 'Accueil'
			  },
			  resolve: {
				  categories: [ 'CategoryService', function( CategoryService) {
					  return CategoryService.loadEntities()
					  						.then( function( response) {
					  							return response.entities;
					  						}, function( response) {
					  							console.error( response);
					  						});
				  }],
				  loggedUser: [ 'UserService', function( UserService) {
					  return UserService.loadLoggedUser()
					  					.then( function( response) {
					  						return response;
					  					}, function( response) {
					  						console.error( response);
					  					});
				  }]
			  },
			  templateUrl: 'angular/preta/views/home.html',
			  controller: 'HomeController'
		})
		/* Articles */
		.state( 'root.latest-articles-full', {
			url: '/latest-articles?page&pageSize',
			params: {
				page: '1',
				pageSize: '12'
			},
			templateUrl: 'angular/preta/views/article/generic-list.html',
			title: 'Nouveaux Articles',
			ncyBreadcrumb: {
				label: 'Nouveaux Articles'
			},
			controller: [ 'ArticleService', '$scope', '$stateParams', '$state', function( ArticleService, $scope, $stateParams, $state) {
				/* Loading Mutex */
				$scope.isLoading = true;
				
				/* Pagination Settings */
				$scope.pagination = { currentPage: $stateParams.page == undefined ? 1 : $stateParams.page,
									  pageSize: $stateParams.pageSize == undefined ? 16 : $stateParams.pageSize,
								      pagesNumber: null,
								      itemsNumber: null
									};
				/* End Pagination Setting */
				
				/* Init */
				$scope.entities = [];
				
				/* View Config */
				$scope.view = { title: 'Nouveaux Articles', icon: 'fa fa-tags', description: 'Les produits r\xE9cents'}
				/* End View Config */
				
				ArticleService.loadLatest( 1, 16)
							  .then( function( response) {
								  console.log( response);
								  $scope.entities = response.entities;

									angular.forEach( $scope.entities, function( article, key) {
										ArticleService.loadEShop( article.id)
													  .then( function( response) {
														  article.eShop = response;
													  }, function( response) {
														  console.error( response);
														  $scope.isLoading = false;
													  });

										/* Load Article Pictures */
										ArticleService.loadPictures( article.id)
													  .then( function( response) {
														  article.pictures = response.entities;
													  }, function( response) {
														  console.error( response);
														  $scope.isLoading = false;
													  });
									});
									
								  $scope.pagination.pagesNumber = response.pagesNumber;
								  $scope.pagination.itemsNumber = response.itemsNumber;
								  
								  $scope.isLoading = false;
							  }, function ( response) {
								  console.error( response);
								  $scope.isLoading = false;
							  });
				
				$scope.changePage = function() {
					$state.go( 'root.latest-articles-full', { page: $scope.pagination.currentPage, pageSize: $stateParams.pageSize },{ notify: false});

					$scope.isLoading = true;
					ArticleService.loadLatest( $scope.pagination.currentPage,
											   $stateParams.pageSize)
												.then( function( response) {
													$scope.pagination.pagesNumber = response.pagesNumber;
													$scope.pagination.itemsNumber = response.itemsNumber;
													$stateParams.page = $scope.pagination.currentPage;
													$scope.entities = response.entities;
													
													/* Load EShop for Each Article */
													angular.forEach( $scope.entities, function( article, key) {
														ArticleService.loadEShop( article.id)
																	  .then( function( response) {
																		  article.eShop = response;
																	  }, function( response) {
																		  console.error( response);
																		  $scope.isLoading = false;
																	  });

														/* Load Article Pictures */
														ArticleService.loadPictures( article.id)
																	  .then( function( response) {
																		  article.pictures = response.entities;
																		  console.log( article.pictures);
																	  }, function( response) {
																		  console.error( response);
																	  });
													});
													
													$scope.isLoading = false;
												}, function( response) {
													console.error( response);
													$scope.isLoading = false;
												});
				};
			}]
			
		})
		.state( 'root.best-sellers-full', {
			url: '/best-sellers?page&pageSize',
			params: {
				page: '1',
				pageSize: '12'
			},
			templateUrl: 'angular/preta/views/article/generic-list.html',
			title: 'Meilleurs Ventes',
			ncyBreadcrumb: {
				label: 'Meilleurs Ventes'
			},
			controller: [ 'ArticleService', '$scope', '$stateParams', '$state', function( ArticleService, $scope, $stateParams, $state) {
				/* Loading Mutex */
				$scope.isLoading = true;
				
				/* Pagination Settings */
				$scope.pagination = { currentPage: $stateParams.page == undefined ? 1 : $stateParams.page,
									  pageSize: $stateParams.pageSize == undefined ? 16 : $stateParams.pageSize,
								      pagesNumber: null,
								      itemsNumber: null
									};
				/* End Pagination Setting */
				
				/* Init */
				$scope.entities = [];
				
				/* View Config */
				$scope.view = { title: 'Meilleurs Ventes', icon: 'fa fa-tags', description: 'Les produits les plus vendus'}
				/* End View Config */
				
				ArticleService.loadBestSellers( 1, 16)
							  .then( function( response) {
								  console.log( response);
								  $scope.entities = response.entities;
								  
								  /* Load EShop for Each Article */
									angular.forEach( $scope.entities, function( article, key) {
										ArticleService.loadEShop( article.id)
													  .then( function( response) {
														  article.eShop = response;
													  }, function( response) {
														  console.error( response);
														  $scope.isLoading = false;
													  });

										/* Load Article Pictures */
										ArticleService.loadPictures( article.id)
													  .then( function( response) {
														  article.pictures = response.entities;
														  console.log( article.pictures);
													  }, function( response) {
														  console.error( response);
													  });
									});
									
								  $scope.pagination.pagesNumber = response.pagesNumber;
								  $scope.pagination.itemsNumber = response.itemsNumber;
								  
								  $scope.isLoading = false;
							  }, function ( response) {
								  console.error( response);
								  $scope.isLoading = false;
							  });
				
				$scope.changePage = function() {
					$state.go( 'root.best-sellers-full', { page: $scope.pagination.currentPage, pageSize: $stateParams.pageSize },{ notify: false});

					$scope.isLoading = true;
					ArticleService.loadLatest( $scope.pagination.currentPage,
											   $stateParams.pageSize)
												.then( function( response) {
													$scope.pagination.pagesNumber = response.pagesNumber;
													$scope.pagination.itemsNumber = response.itemsNumber;
													$stateParams.page = $scope.pagination.currentPage;
													$scope.entities = response.entities;
													
													/* Load EShop for Each Article */
													angular.forEach( $scope.entities, function( article, key) {
														ArticleService.loadEShop( article.id)
																	  .then( function( response) {
																		  article.eShop = response;
																	  }, function( response) {
																		  console.error( response);
																		  $scope.isLoading = false;
																	  });

														/* Load Article Pictures */
														ArticleService.loadPictures( article.id)
																	  .then( function( response) {
																		  article.pictures = response.entities;
																		  console.log( article.pictures);
																	  }, function( response) {
																		  console.error( response);
																	  });
													});
													
													$scope.isLoading = false;
												}, function( response) {
													console.error( response);
													$scope.isLoading = false;
												});
				};
			}]
			
		}).state( 'root.top-rated-full', {
			url: '/top-rated?page&pageSize',
			params: {
				page: '1',
				pageSize: '12'
			},
			templateUrl: 'angular/preta/views/article/generic-list.html',
			title: 'Meilleurs Ventes',
			ncyBreadcrumb: {
				label: 'Meilleurs Ventes'
			},
			controller: [ 'ArticleService', '$scope', '$stateParams', '$state', function( ArticleService, $scope, $stateParams, $state) {
				/* Loading Mutex */
				$scope.isLoading = true;
				
				/* Pagination Settings */
				$scope.pagination = { currentPage: $stateParams.page == undefined ? 1 : $stateParams.page,
									  pageSize: $stateParams.pageSize == undefined ? 16 : $stateParams.pageSize,
								      pagesNumber: null,
								      itemsNumber: null
									};
				/* End Pagination Setting */
				
				/* Init */
				$scope.entities = [];
				
				/* View Config */
				$scope.view = { title: 'Meilleurs Produits', icon: 'fa fa-star', description: 'Les produits les mieux notés'}
				/* End View Config */
				
				ArticleService.loadBestSellers( 1, 16)
							  .then( function( response) {
								  console.log( response);
								  $scope.entities = response.entities;
								  $scope.pagination.pagesNumber = response.pagesNumber;
								  $scope.pagination.itemsNumber = response.itemsNumber;
									
									/* Load EShop for Each Article */
									angular.forEach( $scope.entities, function( article, key) {
										ArticleService.loadEShop( article.id)
													  .then( function( response) {
														  article.eShop = response;
													  }, function( response) {
														  console.error( response);
														  $scope.isLoading = false;
													  });

										/* Load Article Pictures */
										ArticleService.loadPictures( article.id)
													  .then( function( response) {
														  article.pictures = response.entities;
														  console.log( article.pictures);
													  }, function( response) {
														  console.error( response);
													  });
									});
									
								  $scope.isLoading = false;
							  }, function ( response) {
								  console.error( response);
								  $scope.isLoading = false;
							  });
				
				$scope.changePage = function() {
					$state.go( 'root.top-rated-full', { page: $scope.pagination.currentPage, pageSize: $stateParams.pageSize },{ notify: false});

					$scope.isLoading = true;
					ArticleService.loadTopArticles( $scope.pagination.currentPage,
											   $stateParams.pageSize)
												.then( function( response) {
													$scope.pagination.pagesNumber = response.pagesNumber;
													$scope.pagination.itemsNumber = response.itemsNumber;
													$stateParams.page = $scope.pagination.currentPage;
													
													$scope.entities = response.entities;
													
													/* Load EShop for Each Article */
													angular.forEach( $scope.entities, function( article, key) {
														ArticleService.loadEShop( article.id)
																	  .then( function( response) {
																		  article.eShop = response;
																	  }, function( response) {
																		  console.error( response);
																		  $scope.isLoading = false;
																	  });

														/* Load Article Pictures */
														ArticleService.loadPictures( article.id)
																	  .then( function( response) {
																		  article.pictures = response.entities;
																		  console.log( article.pictures);
																	  }, function( response) {
																		  console.error( response);
																	  });
													});
													
													$scope.isLoading = false;
												}, function( response) {
													console.error( response);
													$scope.isLoading = false;
												});
				};
			}]
			
		})
		.state( 'root.article', {
			url: '/article/{id}',
			templateUrl: 'angular/preta/views/article/show.html',
			title: 'Article : {{ entity.name }}',
			ncyBreadcrumb: {
				label: 'Article #{{ entity.id }}'
			},
			controller: 'ArticleController'
		})
		/* Articles By Categories */
		.state( 'root.articles-by-categories', {
			url: '/articles-by-category?id&page&pageSize',
			params:{
				page: "1",
				pageSoze: "16"
			},
			templateUrl: 'angular/preta/views/article/generic-list.html',
			title: 'Articles',
			ncyBreadcrumb: {
				label: 'Articles'
			},
			controller: 'ArticleByCategoryController'
		})
		/* EShop */
		.state( 'root.e-shop', {
			url: '/e-shop/{id}',
			templateUrl: 'angular/preta/views/e-shop/show.html',
			title: 'Boutique : {{ entity.name }}',
			ncyBreadcrumb: {
				label: '{{ entity.name }}'
			},
			controller: 'EShopController'
		})
		.state( 'root.e-shop.latest-articles', {
			url: '/latest-articles?page&pageSize',
			params: {
				page: '1',
				pageSize: '16'
			},
			templateUrl: 'angular/preta/views/e-shop/generic-article-list.html',
			title: 'Nos Nouveaux Articles',
			ncyBreadcrumb: {
				label: 'Nos Nouveaux Articles'
			},
			controller: [ 'EShopService', '$scope', '$stateParams', '$state', function( EShopService, $scope, $stateParams, $state) {
				/* Loading Mutex */
				$scope.isLoading = true;
				
				/* Pagination Settings */
				$scope.pagination = { currentPage: $stateParams.page == undefined ? 1 : $stateParams.page,
									  pageSize: $stateParams.pageSize == undefined ? 16 : $stateParams.pageSize,
								      pagesNumber: null,
								      itemsNumber: null
									};
				/* End Pagination Setting */
				
				/* Init */
				$scope.entities = [];
				
				/* View Config */
				$scope.view = { title: 'Nos Nouveaux Produits', icon: 'fa fa-tags', description: 'Nos produits r\xE9cents'}
				/* End View Config */
				
				EShopService.loadLatestArticles( $stateParams.id, 1, 16)
							  .then( function( response) {
								  console.log( response);
								  $scope.entities = response.entities;
								  $scope.pagination.pagesNumber = response.pagesNumber;
								  $scope.pagination.itemsNumber = response.itemsNumber;
								  
								  $scope.isLoading = false;
							  }, function ( response) {
								  console.error( response);
								  $scope.isLoading = false;
							  });
				
				$scope.changePage = function() {
					$state.go( 'root.e-shop.latest-articles', { page: $scope.pagination.currentPage, pageSize: $stateParams.pageSize },{ notify: false});

					$scope.isLoading = true;
					EShopService.loadLatestArticles( $stateParams.id, $scope.pagination.currentPage,
											   $stateParams.pageSize)
												.then( function( response) {
													$scope.pagination.pagesNumber = response.pagesNumber;
													$scope.pagination.itemsNumber = response.itemsNumber;
													$scope.entities = response.entities;
													$stateParams.page = $scope.pagination.currentPage;
													$scope.isLoading = false;
												}, function( response) {
													console.error( response);
													$scope.isLoading = false;
												});
				};
			}]
		})
		.state( 'root.e-shop.best-sellers', {
			url: '/best-sellers?page&pageSize',
			params: {
				page: '1',
				pageSize: '16'
			},
			templateUrl: 'angular/preta/views/e-shop/generic-article-list.html',
			title: 'Nos Meilleures Ventes',
			ncyBreadcrumb: {
				label: 'Nos Meilleures Ventes'
			},
			controller: [ 'EShopService', '$scope', '$stateParams', '$state', function( EShopService, $scope, $stateParams, $state) {
				/* Loading Mutex */
				$scope.isLoading = true;
				
				/* Pagination Settings */
				$scope.pagination = { currentPage: $stateParams.page == undefined ? 1 : $stateParams.page,
									  pageSize: $stateParams.pageSize == undefined ? 16 : $stateParams.pageSize,
								      pagesNumber: null,
								      itemsNumber: null
									};
				/* End Pagination Setting */
				
				/* Init */
				$scope.entities = [];
				
				/* View Config */
				$scope.view = { title: 'Nos Meilleures Ventes', icon: 'fa fa-shopping-cart', description: 'Nos produits les plus vendus'}
				/* End View Config */
				
				EShopService.loadBestSellersArticles( $stateParams.id, 1, 16)
							  .then( function( response) {
								  console.log( response);
								  $scope.entities = response.entities;
								  $scope.pagination.pagesNumber = response.pagesNumber;
								  $scope.pagination.itemsNumber = response.itemsNumber;
								  
								  $scope.isLoading = false;
							  }, function ( response) {
								  console.error( response);
								  $scope.isLoading = false;
							  });
				
				$scope.changePage = function() {
					$state.go( 'root.e-shop.best-sellers', { page: $scope.pagination.currentPage, pageSize: $stateParams.pageSize },{ notify: false});

					$scope.isLoading = true;
					EShopService.loadBestSellersArticles( $stateParams.id, $scope.pagination.currentPage,
											   $stateParams.pageSize)
												.then( function( response) {
													$scope.pagination.pagesNumber = response.pagesNumber;
													$scope.pagination.itemsNumber = response.itemsNumber;
													$scope.entities = response.entities;
													$stateParams.page = $scope.pagination.currentPage;
													$scope.isLoading = false;
												}, function( response) {
													console.error( response);
													$scope.isLoading = false;
												});
				};
			}]
		})
		.state( 'root.e-shop.top-rated', {
			url: '/top-rated?page&pageSize',
			params: {
				page: '1',
				pageSize: '16'
			},
			templateUrl: 'angular/preta/views/e-shop/generic-article-list.html',
			title: 'Nos Meilleurs Produits',
			ncyBreadcrumb: {
				label: 'Nos Meilleurs Produits'
			},
			controller: [ 'EShopService', '$scope', '$stateParams', '$state', function( EShopService, $scope, $stateParams, $state) {
				/* Loading Mutex */
				$scope.isLoading = true;
				
				/* Pagination Settings */
				$scope.pagination = { currentPage: $stateParams.page == undefined ? 1 : $stateParams.page,
									  pageSize: $stateParams.pageSize == undefined ? 16 : $stateParams.pageSize,
								      pagesNumber: null,
								      itemsNumber: null
									};
				/* End Pagination Setting */
				
				/* Init */
				$scope.entities = [];
				
				/* View Config */
				$scope.view = { title: 'Nos Meilleurs Produits', icon: 'fa fa-star', description: 'Nos produits les mieux notés'}
				/* End View Config */
				
				EShopService.loadTopArticles( $stateParams.id, 1, 16)
							  .then( function( response) {
								  $scope.entities = response.entities;
								  $scope.pagination.pagesNumber = response.pagesNumber;
								  $scope.pagination.itemsNumber = response.itemsNumber;
								  
								  $scope.isLoading = false;
							  }, function ( response) {
								  console.error( response);
								  $scope.isLoading = false;
							  });
				
				$scope.changePage = function() {
					$state.go( 'root.e-shop.top-rated', { page: $scope.pagination.currentPage, pageSize: $stateParams.pageSize },{ notify: false});

					$scope.isLoading = true;
					EShopService.loadTopArticles( $stateParams.id, $scope.pagination.currentPage,
											   $stateParams.pageSize)
												.then( function( response) {
													$scope.pagination.pagesNumber = response.pagesNumber;
													$scope.pagination.itemsNumber = response.itemsNumber;
													$scope.entities = response.entities;
													$stateParams.page = $scope.pagination.currentPage;
													$scope.isLoading = false;
												}, function( response) {
													console.error( response);
													$scope.isLoading = false;
												});
				};
			}]
		})
		/* End EShop */
		.state( 'root.cart', {
			url: '/cart?page&pageSize',
			title: 'Mon Panier',
			params: {
				page: '1',
				pageSize: '5'
			},
			ncyBreadcrumb: {
				label: 'Panier',
			},
			templateUrl: 'angular/preta/views/user/cart.html',
			controller: 'CartController'
		})
		/* Orders */
		.state( 'root.order', {
			url: '/order',
			title: 'Commmande',
			ncyBreadcrumb: {
				label: 'Commander'
			},
			templateUrl: 'angular/preta/views/order/checkout.html',
			controller: 'CheckoutController'
		})
		/* Order */
		.state( 'root.orders', {
			url: '/orders?page&pageSize&orderByIdAsc&orderStatus',
			title: 'Mes Commandes',
			params: {
				page: '1',
				pageSize: '6',
				orderStatus: '0',
				orderByIdAsc: 'false'
			},
			ncyBreadcrumb: {
				label: 'Mes Commandes'
			},
			templateUrl: 'angular/preta/views/order/list.html',
			controller: 'OrderController'
		})
		.state( 'root.orders.show', {
			url: '/show/{id}',
			title: 'Détails Commande',
			ncyBreadcrumb: {
				label: 'Détails Commande'
			},
			templateUrl: 'angular/preta/views/order/show.html',
			controller: 'OrderController'
		})
		/* Shrtcts */
		.state( 'root.orders-delivering', {
			controller: [ '$state', function( $state) {
				$state.go( 'root.orders', { orderStatus: 4});
			}]
		})
		.state( 'root.orders-all', {
			controller: [ '$state', function( $state) {
				$state.go( 'root.orders', { orderStatus: 0});
			}]
		})
		.state( 'root.orders-delivered', {
			controller: [ '$state', function( $state) {
				$state.go( 'root.orders', { orderStatus: 5});
			}]
		})
		/* End Orders */
		/* Order checkout confirmation */
		.state( 'root.order-confirmation', {
			  url: '/order/confirmation',
			  title: 'Commande enregistr\xE9',
			  ncyBreadcrumb: {
				    label: 'Confirmation Enreg. Commande'
			  },
			  templateUrl: 'angular/preta/views/order/confirmation.html',
		})
		/* Article Rating by User */
		.state( 'root.ratings', {
			url: '/ordered-article/rating?page&pageSize',
			params: {
				page: '1',
				pageSize: '8'
			},
			title: 'Noter les articles commandés',
			ncyBreadcrumb: {
				label: 'Articles à Noter'
			},
			templateUrl: 'angular/preta/views/article/list-rating.html',
			controller: 'RatingController'
		})
		/* Login Route */
		.state( 'root.login', {
			url: '/login',
			title: 'Login',
			templateUrl: 'angular/preta/views/login.html',
			controller: 'LoginController'
		})
		/*Logout*/
		.state( 'root.logout', {
			url: '/logout',
			controller: [ '$rootScope', '$state', 'UserService', function( $rootScope, $state, UserService) {
				$rootScope.loggedUser = null;
				UserService.logout();
				$state.go( 'root');
			}]
		})
		/* Password Recovery */
		.state( 'root.pw-reset', {
			url: '/password-reset?token',
			templateUrl: 'angular/preta/views/user/pw-reset.html',
			controller: 'PWResetController'
		})
		.state( 'root.pw-recovery', {
			url: '/password-recovery',
			templateUrl: 'angular/preta/views/user/pw-recovery.html',
			controller: 'PWRecoveryController'
		})
		/* Validation */
		.state( 'root.validate-account', {
			url: '/account-validation?token',
			templateUrl: 'angular/preta/views/user/account-validation.html',
			controller: 'AccountValidationController'
		})
		/* Request Upgrade */
		.state( 'root.upgrade-request', {
			url: '/upgrade-to-manager',
			templateUrl: 'angular/preta/views/user/upgrade-request.html',
			controller: 'UpgradeRequestController'
		})
		/*User registration*/
		.state( 'root.registration', {
			  url: '/registration',
			  title: 'Inscription',
			  ncyBreadcrumb: {
				    label: 'Inscription'
			  },
			  templateUrl: 'angular/preta/views/user/register.html',
			  controller: 'BuyerRegistrationController' 
		})
		/*Registration confirmation User && EShop */
		.state( 'root.confirmation', {
			  url: '/registration/confirmation',
			  title: 'Confirmation d\'inscription',
			  ncyBreadcrumb: {
				    label: 'Confirmation d\'inscription'
			  },
			  templateUrl: 'angular/preta/views/shared/registration-conf.html',
		})
		/*EShop Registration*/
		.state( 'root.e-shop-reg', {
			  url: '/e-shop-registration',
			  title: 'Enregistrement d\'une boutique' ,
			  ncyBreadcrumb: {
				    label: 'Enregistrement d\'une boutique'
			  },
			  templateUrl: 'angular/preta/views/e-shop/register.html',
			  controller: 'EShopRegistrationController'
		})
		/* Search */
		.state( 'root.search', {
			url: '/search?name&page&pageSize&minPrice&maxPrice',
			title: 'R\xE9sultats de la recherche',
			templateUrl: 'angular/preta/views/article/search.html',
			controller: 'SearchController'
		})
		/* Tracking */
		.state( 'root.tracking', {
			url: '/tracking',
			title: 'Tracking',
			templateUrl: 'angular/preta/views/shared/tracking.html',
			controller: 'TrackingController'
		})
		/*Logged User Stuff*/
		.state( 'root.profile', {
			url: '/profile',
			title: 'Profil',
			templateUrl: 'angular/preta/views/user/profile.html',
			controller: 'UserController'
			
		})
		/* last Visisted */
		.state( 'root.last-visited', {
			url: '/last-visited?page&pageSize&orderByIdAsc',
			params: {
				page: '1',
				pageSize: '12',
				orderByIdAsc: 'false'
			},
			templateUrl: 'angular/preta/views/article/generic-list.html',
			title: 'RxE9cemment visit\xE9s',
			ncyBreadcrumb: {
				label: 'RxE8cemment visit\xE9s'
			},
			controller: 'LastVisitedController'
			
		})
		/* Favorites EShops */
		.state( 'root.favorites-e-shops', {
			url: '/favorites-e-shops?page&pageSize',
			params: {
				page: '1',
				pageSize: '12'
			},
			templateUrl: 'angular/preta/views/user/favorites-e-shops.html',
			title: 'Boutiques Favorites',
			ncyBreadcrumb: {
				label: 'Boutiques Favorites'
			},
			controller: [ 'UserService', 'EShopService', '$scope', '$stateParams', '$state', function( UserService, EShopService, $scope, $stateParams, $state) {
				/* Loading Mutex */
				$scope.isLoading = true;
				
				/* Pagination Settings */
				$scope.pagination = { currentPage: $stateParams.page == undefined ? 1 : $stateParams.page,
									  pageSize: $stateParams.pageSize == undefined ? 10 : $stateParams.pageSize,
								      pagesNumber: null,
								      itemsNumber: null
									};
				/* End Pagination Setting */
				
				/* Init */
				$scope.favEShops = [];
				
				/* View Config */
				$scope.view = { title: 'Boutiques favorites', icon: 'fa fa-star', description: 'Retrouvez vos boutiques pr\xE9f\xE9r\xE9es'}
				/* End View Config */
				
				/* Load Functions */
				$scope.loadEntities = function( page, pageSize) {
					$scope.isLoading = true;
					UserService.loadFavoritesEShops( page, pageSize)
					  .then( function( response) {
							$scope.pagination.pagesNumber = response.pagesNumber;
							$scope.pagination.itemsNumber = response.itemsNumber;
							$stateParams.page = $scope.pagination.currentPage;

							$scope.favEShops = response.entities;
						  
						    $scope.isLoading = false;
					  }, function ( response) {
						  console.error( response);
						  $scope.isLoading = false;
					  });
				}
				
				/* Call to load function */
				$scope.loadEntities( $scope.pagination.page, $scope.pagination.pageSize);
				
				$scope.changePage = function() {
					$state.go( $state.current.name, { page: $scope.pagination.currentPage, pageSize: $stateParams.pageSize },{ notify: false});
					$scope.loadEntities( $scope.pagination.currentPage, $scope.pagination.pageSize);
				};
				
				/* Remove from Favorites */
				$scope.removeFromFavorites = function( id) {
					EShopService.removeFromFavorites( id)
							   .then( function( response) {
								   console.log( response);
								   $scope.loadEntities( $scope.pagination.currentPage, $scope.pagination.pageSize);
							   }, function( response) {
								   console.error( response);
							   });
				}
			}]
		})
		/* Error Section */
		.state( 'root.errors', {
			url: '/errors',
			template: '<div ui-view></div>'
		})
		.state( 'root.errors.401', {
			url: '/401',
			templateUrl: 'angular/preta/views/error/401.html'
		})
		.state( 'root.errors.403', {
			url: '/403',
			templateUrl: 'angular/preta/views/error/403.html'
		})
		.state( 'root.errors.404', {
			url: '/404',
			templateUrl: 'angular/preta/views/error/404.html'
		})
		.state( 'root.errors.500', {
			url: '/500',
			templateUrl: 'angular/preta/views/error/500.html'
		})
		.state( 'root.errors.503', {
			url: '/503',
			templateUrl: 'angular/preta/views/error/503.html'
		})
		/* End Error Section */
		/* Debug */
		.state( 'root.fa', {
			url: '/fa',
			templateUrl: 'angular/preta/views/dev/fa.html'
		})
		.state( "root.sandbox", {
			url: '/sandbox',
			title: 'Sandbox',
			ncyBreadcrumb: {
				label: 'Sandbox'
			},
			templateUrl: 'angular/preta/views/sandbox.html',
			controller: [ '$scope', '$http', function( $scope, $http) {
				/* https://drive.google.com/open?id=0B0W0ktANhIydb2pwMDdHY3VYcVk */
				$scope.picture = { url: 'https://drive.google.com/uc?id=0B0W0ktANhIydb2pwMDdHY3VYcVk&export=download', data: null };
				$scope.isLoading = false;
				
				$scope.load = function() {
					$scope.isLoading = true;
					
					$http({ 
							url: $scope.picture.url,
							method: 'GET',
							headers: {
								'Access-Control-Allow-Origin': '*'
							}
						})
						.then( function( r) {
							 $scope.picture.data = r.data;
							 
							 $scope.isLoading = false;
						 }, function( r) {
							 console.error( r);
							 $scope.isLoading = false;
						 });
				};
				
				$scope.reset = function() {
					$scope.picture.data = null;
					$scope.isLoading = false;
				};
			}]
		})
};


function _arrayBufferToBase64(buffer) {
    var binary = '';
    var bytes = new Uint8Array(buffer);
    var len = bytes.byteLength;
    for (var i = 0; i < len; i++) {
      binary += String.fromCharCode(bytes[i]);
    }
    return window.btoa(binary);
};