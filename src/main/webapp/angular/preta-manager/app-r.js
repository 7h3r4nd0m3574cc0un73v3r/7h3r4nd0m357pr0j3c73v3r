/* D055 Code - Preta Manager */
App.config( [ '$stateProvider', '$urlRouterProvider', 'baSidebarServiceProvider', '$mdThemingProvider',
              function( $stateProvider, $urlRouterProvider, baSidebarServiceProvider, $mdThemingProvider) {
	
	/* Theming Config */
	$mdThemingProvider.theme( 'default')
					  .primaryPalette( 'blue')
					  .accentPalette( 'orange')
					  .warnPalette( 'deep-orange')
					  .backgroundPalette( 'grey');
	/* End Theming Config */
	
	$urlRouterProvider.otherwise( '');
	
	$stateProvider
		/* General */
		.state( 'root', {
			url: '',
			title: 'Dashboard',
			resolve: {
				loggedUser : ['$rootScope', '$state', '$http', '$q', function ($rootScope, $state, $http, $q){
					var loggedUser;
					$http.get( 'rest-api/logged-user')
								.then(
									function( response) {
										$rootScope.loggedUser = response.data;
										loggedUser = response.data;
										
										if( loggedUser == null) {
											/* TODO: Harden. */
											$state.go( 'root.error403');
										}
									},
									function( errResponse) {
										console.log( 'Error: Service > loggedUser');
										return $q.reject( errResponse);
									});
				}]
			},
			templateUrl: 'angular/preta-manager/views/dashboard.html',
			ncyBreadcrumb: {
				label: 'Dashboard'
			},
			controller: 'DashboardController'
		})
		/* End General */
		/*EShops Base Remix*/
		.state( 'root.e-shops', {
			url: '/e-shops?page&pageSize',
			params: {
				page: "1",
				pageSize: "0"
			},
			title: 'Mes Boutiques',
			templateUrl: 'angular/preta-manager/views/e-shop/list.html',
			ncyBreadcrumb: {
				label: 'Mes Boutiques'
			},
			controller: 'EShopController'
		})
		.state( 'root.e-shops.new', {
			url: '/new',
			title: 'Mes Boutiques' ,
			ncyBreadcrumb: {
				label: 'Ajout d\'une boutique'
			},
			templateUrl: 'angular/preta-manager/views/e-shop/new.html',
			controller: 'EShopController'
		})
		.state( 'root.e-shops.update', {
			url: '/update/{eShopId:int}',
			title: 'Mes Boutiques' ,
			ncyBreadcrumb: {
				label: 'Modifier boutique #{{ entity.id }}'
			},
			templateUrl: 'angular/preta-manager/views/e-shop/update.html',
			controller: 'EShopController'
		})
		.state( 'root.e-shops.show', {
			/*Custom Breadcrumb to show name*/
			url: '/{eShopId:int}',
			title: 'Mes Boutiques',
			templateUrl: 'angular/preta-manager/views/e-shop/show.html',
			ncyBreadcrumb: {
				label: '{{ activeEShop.name }}'
			},
			resolve: {
				entity:[ 'EShopService', '$stateParams', function( EShopService, $stateParams) {
					return EShopService.loadEntity( $stateParams.eShopId)
										.then( function( response) {
											EShopService.activeEShop = response;
											return response;
										}, function( response) {
											console.log( response);
										});
				}],
				subOffers: [ function() { return []; }],
				entities: [function() { return []; }]
			},
			controller: 'EShopController'
		})
		/* End EShop Section */
		/* ShopSub Section */
		.state( 'root.e-shops.show.shop-subs', {
			url: '/shop-subs?shopSubsPage&shopSubsPageSize&orderByIdAsc',
			params: {
				shopSubsPage: "1",
				shopSubsPageSize: "10",
				orderByIdAsc: "false"
			},
			title: 'Abonnements',
			templateUrl: 'angular/preta-manager/views/shop-sub/list.html',
			ncyBreadcrumb: {
				label: 'Abonnements'
			},
			controller: 'ShopSubController'
		})
		.state( 'root.e-shops.show.shop-subs.new', {
			url: '/new',
			title: 'Abonnements',
			templateUrl: 'angular/preta-manager/views/shop-sub/new.html',
			ncyBreadcrumb: {
				label: 'Nouveau'
			},
			resolve: {
				subOffers: [ 'SubOfferService', function( SubOfferService) {
					return SubOfferService.loadEntities( 1, 0)
										  .then( function( response) {
											  return response;
										  }, function( response) {
											  console.error( response);
										  });
				}]
			},
			controller: 'ShopSubController'
		})
		.state( 'root.e-shops.show.shop-subs.show', {
			url: '/show/{id:int}',
			title: 'Abonnements',
			templateUrl: 'angular/preta-manager/views/shop-sub/show.html',
			ncyBreadcrumb: {
				label: 'D\xE8tails #{{ entity.id }}'
			},
			resolve: {
				subOffers: [ function() {  return []; }],
			},
			controller: 'ShopSubController'
		})
		/* End ShopSub Section */
		/* Article Section */
		.state( 'root.e-shops.show.articles', {
			url: '/articles?articlesPage&articlesPageSize',
			params: {
				articlesPage: "1",
				articlesPageSize: "12"
			},
			title: 'Articles',
			templateUrl: 'angular/preta-manager/views/article/list.html',
			ncyBreadcrumb: {
				label: 'Articles'
			},
			resolve: {
				categories: [ function() { return []; }],
			},
			controller: 'ArticleController'
		})
		.state( 'root.e-shops.show.articles.show', {
			url: '/show/{id:int}',
			title: 'Articles',
			templateUrl: 'angular/preta-manager/views/article/show.html',
			ncyBreadcrumb: {
				label: 'Article {{ entity.id }}'
			},
			resolve: {
				categories: [ function() { return []; }]
			},
			controller: 'ArticleController'
		})
		.state( 'root.e-shops.show.articles.update', {
			url: '/update/{id:int}',
			title: 'Articles',
			templateUrl: 'angular/preta-manager/views/article/update.html',
			ncyBreadcrumb: {
				label: 'Article {{ entity.id }}'
			},
			resolve: {
				categories: [ 'CategoryService', function( CategoryService) {
					return CategoryService.loadMSTFormatedEntities()
											.then( function( response) {
												return response;
											}, function( response) {
												console.error( response);
											});
				}]
			},
			controller: 'ArticleController'
		})
		.state( 'root.e-shops.show.articles.new', {
			url: '/new',
			title: 'Articles',
			templateUrl: 'angular/preta-manager/views/article/new.html',
			resolve: {
				categories: [ 'CategoryService', function( CategoryService) {
					return CategoryService.loadMSTFormatedEntities()
											.then( function( response) {
												return response;
											}, function( response) {
												console.error( response);
											});
				}]
			},
			ncyBreadcrumb: {
				label: 'Ajouter'
			},
			controller: 'ArticleController'
		})
		.state( 'root.e-shops.show.articles.show.adv-offers', {
			url: '/adv-offers?advOfferPage&advOfferPageSize&advOfferStatus&advOfferOrderByIdAsc',
			params: {
				advOfferPage: "1",
				advOfferPageSize: "10",
				advOfferStatus: "0",
				advOfferOrderByIAsc: "true"
			},
			title: 'Publicit\xE9s',
			templateUrl: 'angular/preta-manager/views/adv-offer/list.html',
			ncyBreadcrumb: {
				label: 'Publicit\xE9s'
			},
			controller: 'AdvOfferController'
		})
		/* AdvOffer */
		.state( 'root.adv-offers', {
			url: '/adv-offers?advOfferPage&advOfferPageSize&advOfferStatus&advOfferOrderByIdAsc',
			params: {
				advOfferPage: "1",
				advOfferPageSize: "10",
				advOfferStatus: "-1",
				advOfferOrderByIAsc: "true"
			},
			title: '',
			templateUrl: 'angular/preta-manager/views/adv-offer/list.html',
			ncyBreadcrumb: {
				label: '{{ breadCrumbLabel }}'
			},
			controller: 'AdvOfferController'
		})
		.state( 'root.adv-offers.new', {
			url: '/new',
			title: 'Nouvelle Publicit\xE9',
			templateUrl: 'angular/preta-manager/views/adv-offer/new.html',
			ncyBreadcrumb: {
				label: 'Nouvelle Publicité'
			},
			controller: 'AdvOfferController'
		})
		.state( 'root.adv-offers.show', {
			url: '/show/{id:int}',
			title: 'D\xE9tails Publicit\xE9',
			templateUrl: 'angular/preta-manager/views/adv-offer/show.html',
			ncyBreadcrumb: {
				label: 'D\xE9tail Publicit\xE9'
			},
			controller: 'AdvOfferController'
		})
		/* Shrtcts */
		.state( 'root.adv-offers-pending-pay', {
			url: '/adv-offers/pending-pay',
			controller: [ '$state', function( $state) {
				$state.go( 'root.adv-offers', { advOfferPage: 1, advOfferPageSize: 10, advOfferStatus: 0, advOfferOrderByIdAsc: true});
			}]
		})
		.state( 'root.adv-offers-pending-conf', {
			url: '/adv-offers/pending-conf',
			controller: [ '$state', function( $state) {
				$state.go( 'root.adv-offers', { advOfferPage: 1, advOfferPageSize: 10, advOfferStatus: 1, advOfferOrderByIdAsc: true});
			}]
		})
		.state( 'root.adv-offers-paid', {
			url: '/adv-offers/paid',
			controller: [ '$state', function( $state) {
				$state.go( 'root.adv-offers', { advOfferPage: 1, advOfferPageSize: 10, advOfferStatus: 2, advOfferOrderByIdAsc: true});
			}]
		})
		.state( 'root.adv-offers-ongoing', {
			url: '/adv-offers/ongoing',
			controller: [ '$state', function( $state) {
				$state.go( 'root.adv-offers', { advOfferPage: 1, advOfferPageSize: 10, advOfferStatus: 3, advOfferOrderByIdAsc: true});
			}]
		})
		.state( 'root.adv-offers-expired', {
			url: '/adv-offers/expired',
			controller: [ '$state', function( $state) {
				$state.go( 'root.adv-offers', { advOfferPage: 1, advOfferPageSize: 10, advOfferStatus: 4, advOfferOrderByIdAsc: true});
			}]
		})
		.state( 'root.adv-offers-all', {
			url: '/adv-offers/all',
			controller: [ '$state', function( $state) {
				$state.go( 'root.adv-offers', { advOfferPage: 1, advOfferPageSize: 10, advOfferStatus: -1, advOfferOrderByIdAsc: false});
			}]
		})
		/* End AdvOffer */
		/* Order Section */
		.state( 'root.orders', {
			url: '/orders?articleOrdersPage&articleOrdersPageSize&articleOrdersOrderByIdAsc&articleOrderStatus',
			title: 'Commandes',
			templateUrl: 'angular/preta-manager/views/order/list.html',
			ncyBreadcrumb: {
				label: '{{ breadCrumbLabel }}'
			},
			resolve: {
				entity: [ function() { return null; }]
			},
			controller: 'OrderController'
		})
		/* Parametrized Shortcuts */
		.state( 'root.orders-all', {
			url: '/orders/all',
			controller: [ '$state', function( $state) {
				$state.go( 'root.orders', { articleOrdersPage: 1, articleOrdersPageSize: 6, articleOrdersOrderByIdAsc: false, articleOrderStatus: -1});
			}]
		})
		.state( 'root.orders-paid', {
			url: '/orders/ready-for-delivery',
			controller: [ '$state', function( $state) {
				$state.go( 'root.orders', { articleOrdersPage: 1, articleOrdersPageSize: 6, articleOrdersOrderByIdAsc: true, articleOrderStatus: 2});
			}]
		})
		.state( 'root.orders-delivered', {
			url: '/orders/delivered',
			controller: [ '$state', function( $state) {
				$state.go( 'root.orders', { articleOrdersPage: 1, articleOrdersPageSize: 6, articleOrdersOrderByIdAsc: true, articleOrderStatus: 4});
			}]
		})
		.state( 'root.orders-delivering', {
			url: '/orders/delivering',
			controller: [ '$state', function( $state) {
				$state.go( 'root.orders', { articleOrdersPage: 1, articleOrdersPageSize: 6, articleOrdersOrderByIdAsc: true, articleOrderStatus: 3});
			}]
		})
		.state( 'root.orders.show', {
			url: '/show/{id}',
			title: 'Commandes',
			templateUrl: 'angular/preta-manager/views/order/show.html',
			ncyBreadcrumb: {
				label: 'Commande #{{ entity.id }}'
			},
			controller: 'OrderController'
		})
		/* Profile */
		.state( 'root.profile', {
				url: '/profile',
				title: 'Mon Profil',
				templateUrl: 'angular/preta-manager/views/user/profile.html',
				ncyBreadcrumb: {
					label: 'Mon profil'
				},
				controller: 'UserController'
		})
		/* Errors */
		.state( 'root.error-403', {
			url: '/error/403',
			title: 'Erreur 403',
			ncyBreadcrumb: {
				label: 'Acc\xE9s Non Authoris\xE8'
			},
			templateUrl: 'angular/preta-manager/views/errors/403.html',
		})
		/* End Errors */
	;
	/* End Order Section */
	/* Sidebar Config */
	baSidebarServiceProvider.addStaticItem({
		title: 'Mes Boutiques',
		icon: 'fa fa-opencart',
		stateRef:'root.e-shops'
	});
	baSidebarServiceProvider.addStaticItem({
		title: 'Mes Commandes',
		icon: 'fa fa-shopping-cart',
		  subMenu: [{
			  title: 'A livrer',
			  stateRef: 'root.orders-paid'
		  },{
			  title: 'Livrées',
			  stateRef: 'root.orders-delivered'
		  },{
			  title: 'Livraison en cours',
			  stateRef: 'root.orders-delivering'
		  }, {
			  title: 'Toutes',
			  stateRef: 'root.orders-all'
		  }]
	});
	baSidebarServiceProvider.addStaticItem({
		title: 'Mes Publicit\xE9s',
		icon: 'fa fa-calendar',
		  subMenu: [{
			  title: 'En cours',
			  stateRef: 'root.adv-offers-ongoing'
		  },{
			  title: 'A confirmer',
			  stateRef: 'root.adv-offers-pending-conf'
		  },{
			  title: 'Activable',
			  stateRef: 'root.adv-offers-paid'
		  },{
			  title: 'Expir\xE9es',
			  stateRef: 'root.adv-offers-expired'
		  },{
			  title: 'Toutes',
			  stateRef: 'root.adv-offers-all'
		  }]
	});
/*	baSidebarServiceProvider.addStaticItem({
		title: 'Statistiques',
		icon: 'fa fa-bar-chart'
	});*/
}]);