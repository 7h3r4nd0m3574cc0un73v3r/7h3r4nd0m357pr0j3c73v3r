'use strict';

var App = angular.module( "adminPretaApp", [
                                       'ngAnimate',
                                       'ui.bootstrap',
                                       'ui.sortable',
                                       'ui.router',
                                       /*'ngTouch',*/
                                       'ngAudio',
                                       'toastr',
                                       'smart-table',
                                       "xeditable",
                                       'ui.slimscroll',
                                       'ngJsTree',
                                       'angular-progress-button-styles',
                                       'ncy-angular-breadcrumb',
                                       'ngFileUpload',                         
                                       'ngMaterial',
                                       'lfNgMdFileInput',
                                       'ngMessages',
                                       'ngStomp',
                                       'BlurAdmin',
                                      ]);
App.config( routeConfig);

//Routing for PretaApp
/** @ngInject */
function routeConfig( $stateProvider, $urlRouterProvider, baSidebarServiceProvider, $mdThemingProvider) {
	$urlRouterProvider.otherwise('');
	
	/* Theming Config */
	$mdThemingProvider.theme( 'default')
					  .primaryPalette( 'green')
					  .accentPalette( 'amber')
					  .warnPalette( 'deep-orange')
					  .backgroundPalette( 'grey');
	/* End Theming Config */
	
	$stateProvider
		.state( 'root', {
			  url: '',
			  title: 'Tableau de bord',
			  templateUrl: 'angular/preta-admin/views/dashboard.html',
			  ncyBreadcrumb: {
				    label: 'Dashboard'
			  },
			  resolve: {
				  loggedUser: [ '$http', '$q', function( $http, $q) {
					  return $http.get( 'rest-api/logged-user')
					  				.then( function( response) { return response.data;}
					  				 , function( response) { 
					  					 console.error( response);
					  				 });
				  }]
			  },
			  controller: [ '$rootScope','loggedUser', 'StompService', function( $rootScope, loggedUser, StompService) {
				  $rootScope.loggedUser = loggedUser;
				  
				  /*Load the currently logged User if exists*/
				  if( $rootScope.loggedUser != null) {
					  $rootScope.loggedUser = loggedUser;
					  StompService.connect()
					  			  .then( function() {
					  				  StompService.subscribe( '/user/topic/logged-user',
									  						function( payload, headers, response) {
								  					  		$rootScope.loggedUser = payload;
							  						});
					  			  });
				  }
			  }]
		})
		/* EMP */
		.state( 'root.emps', {
			url: '/e-money-providers',
			title: 'Fournisseurs EMoney',
			templateUrl: 'angular/preta-admin/views/emp/list.html',
			resolve: {
				entity: function() { return null; }
			},
			controller: 'EMPController',
		    ncyBreadcrumb: {
			    label: 'Fournisseurs EMoney'
		    }
		})
		.state( 'root.emps.new', {
				url: '/new',
				title: 'Fournisseurs EMoney',
				templateUrl: 'angular/preta-admin/views/emp/new.html',
				ncyBreadcrumb: {
					label: 'Nouveau'
				}
		})
		.state( 'root.emps.show', {
			url: '/show/{id:int}',
			title: 'Fournisseurs EMoney',
			resolve: {
				entity  : ['EMPService', '$stateParams', function( EMPService, $stateParams) {
					return EMPService.loadEntity( $stateParams.id)
									.then( function( response) {
										return response;
									}, function( response) {
										console.error( response);
									});
				}]
			},
			templateUrl: 'angular/preta-admin/views/emp/show.html',
			ncyBreadcrumb: {
				label: 'Afficher'
			},
			controller: 'EMPController'
		})
		.state( 'root.emps.update', {
			url: '/update/{id:int}',
			title: 'Fournisseurs EMoney',
			resolve: {
				entity  : ['EMPService', '$stateParams', function( EMPService, $stateParams) {
					return EMPService.loadEntity( $stateParams.id)
									 .then( function( response) {
										 return response;
									 }, function( response) {
										 console.error( response);
									 });
				}]
			},
			templateUrl: 'angular/preta-admin/views/emp/update.html',
			ncyBreadcrumb: {
				label: 'Modifier'
			},
			controller: 'EMPController'
		})
		/* End EMP */
		/* AdvOption */
		.state( 'root.adv-options', {
			url: '/adv-options',
			title: 'Options Publicit\xE9',
			templateUrl: 'angular/preta-admin/views/adv-option/list.html',
			resolve: {
				entity: [ function() { return null; }]
			},
			controller: 'AdvOptionController',
			ncyBreadcrumb: {
				label: 'Options Pub.',
			}
		})
		.state( 'root.adv-options.new', {
			url: '/new',
			title: 'Options Publicit\xE9',
			templateUrl: 'angular/preta-admin/views/adv-option/new.html',
			resolve: {
				entity: [ function() { return null; }]
			},
			controller: 'AdvOptionController',
			ncyBreadcrumb: {
				label: 'Nouvelle'
			}
		})
		.state( 'root.adv-options.show', {
			url: '/show/{id:int}',
			title: 'Options Publicit\xE9',
			templateUrl: 'angular/preta-admin/views/adv-option/show.html',
			resolve: {
				entity: [ 'AdvOptionService', '$stateParams', function( AdvOptionService, $stateParams) {
					return AdvOptionService.getEntity( $stateParams.id)
										   .then( function( response) {
											   return response;
										   }, function( response) {
											   console.error( response);
										   });
				}]
			},
			controller: 'AdvOptionController',
			ncyBreadcrumb: {
				label: 'Détails # {{ entity.id }}'
			}
		})
		.state( 'root.adv-options.edit', {
			url: '/edit/{id:int}',
			title: 'Options Publicit\xE9',
			templateUrl: 'angular/preta-admin/views/adv-option/edit.html',
			resolve: {
				entity: [ 'AdvOptionService', '$stateParams', function( AdvOptionService, $stateParams) {
					return AdvOptionService.getEntity( $stateParams.id)
										   .then( function( response) {
											   return response;
										   }, function( response) {
											   console.error( response);
										   });
				}]
			},
			controller: 'AdvOptionController',
			ncyBreadcrumb: {
				label: 'Modifier # {{ entity.id }}'
			}
		})
		/* End AdvOption */
		/* Shop Status */
		.state( 'root.shopStatuses', {
			url: '/shop-statuses',
			title: 'Statuts de Boutiques',
			templateUrl: 'angular/preta-admin/views/shop-status/list.html',
			resolve: {
				entity: function () { return null;}
			},
			controller: 'ShopStatusController',
		    ncyBreadcrumb: {
			    label: 'Statuts Boutique'
		    }
		})
		.state( 'root.shopStatuses.new', {
			url: '/new',
			title: 'Statuts de Boutiques',
			templateUrl: 'angular/preta-admin/views/shop-status/new.html',
			ncyBreadcrumb: {
				label: 'Nouveau'
			},
			controller: 'ShopStatusController'
		})
		.state( 'root.shopStatuses.show', {
			url: '/show/{id:int}',
			title: 'Statuts de Boutiques',
			resolve: {
				entity  : ['$http', '$state', '$stateParams', function( $http, $state, $stateParams) {
					return $http.get( 'rest/shop-status/get/' + $stateParams.id)
								.then( function( response) {
									return response.data;
								}, function( response) {
									if( response.status == 404)
										$state.go( 'root.error404');
								});
				}]
			},
			templateUrl: 'angular/preta-admin/views/shop-status/show.html',
			ncyBreadcrumb: {
				label: 'Afficher'
			},
			controller: 'ShopStatusController'
		})
		.state( 'root.shopStatuses.edit', {
			url: '/edit/{id:int}',
			title: 'Statuts de Boutiques',
			resolve: {
				entity  : ['$http', '$state', '$stateParams', function( $http, $state, $stateParams) {
					return $http.get( 'rest/shop-status/get/' + $stateParams.id)
								.then( function( response) {
									return response.data;
								}, function( response) {
									if( response.status == 404)
										$state.go( 'root.error404');
								});
				}]
			},
			templateUrl: 'angular/preta-admin/views/shop-status/edit.html',
			ncyBreadcrumb: {
				label: 'Modifier'
			},
			controller: 'ShopStatusController'
		})
		/* End Shop Status */
		/* Users => Admin */
		.state( 'root.admins', {
			url: '/admins',
			title: 'Administrateurs',
			resolve: {
				entity: function () { return null;}
			},
			templateUrl: 'angular/preta-admin/views/user/admin/list.html',
			ncyBreadcrumb: {
				label: 'Administrateurs'
			},
			controller: 'AdminController'
		})
		.state( 'root.admins.new', {
			url: '/admins/new',
			title: 'Administrateurs',
			templateUrl: 'angular/preta-admin/views/user/admin/new.html',
			ncyBreadcrumb: {
				label: 'Administrateurs'
			},
			controller: 'AdminController'
		})
		.state( 'root.admins.show', {
			url: '/admins/show/{id:int}',
			title: 'Administrateurs',
			resolve: {
				entity  : ['$http', '$stateParams', function( $http, $stateParams) {
					return $http.get( 'rest/user/admin/get/' + $stateParams.id)
								.then( function( response) {
									return response.data;
								});
				}]
			},
			templateUrl: 'angular/preta-admin/views/shop-status/show.html',
			ncyBreadcrumb: {
				label: 'Administrateurs'
			},
			controller: 'AdminController'
		})
		/* End Users => Admins */
		/* Users => Buyers */
		.state( 'root.buyers', {
			url: '/buyers',
			title: 'Acheteurs',
			templateUrl: 'angular/preta-admin/views/user/buyer/list.html',
			ncyBreadcrumb: {
				label: 'Acheteurs'
			},
			controller: 'BuyerController'
		})
		.state( 'root.buyers.show', {
			url: '/show/{id:int}',
			title: 'Détails Acheteur',
			templateUrl: 'angular/preta-admin/views/user/shared/show.html',
			ncyBreadcrumb: {
				label: '{{ entity.username }}'
			},
			controller: 'BuyerController'
		})
		/* End Users => Buyers */
		/* SubOffers */
		.state( 'root.subOffers', {
			url: '/sub-offers',
			title: 'Mod\xE8les d\'abonnements',
			resolve: {
				entity: function () { return null;},
				shopStatuses: function() { return null; }
			},
			templateUrl: 'angular/preta-admin/views/sub-offer/list.html',
			ncyBreadcrumb: {
				label: 'Mod\xE8les Abo.'
			},
			controller: 'SubOfferController'
		})
		.state( 'root.subOffers.new', {
			url: '/new',
			title: 'Mod\xE8les d\'abonnements',
			resolve: {
				shopStatuses: [ 'ShopStatusService', function( ShopStatusService) {
					//redirect to error if there are no ShopStatuses
					return ShopStatusService.loadEntities()
											.then( function( response) {
												return response.entities;
											}, function( response) {
												console.error( response);
											});
				}]
			},
			templateUrl: 'angular/preta-admin/views/sub-offer/new.html',
			ncyBreadcrumb: {
				label: 'Nouveau'
			},
			controller: 'SubOfferController'
		})
		.state( 'root.subOffers.show', {
			url: '/show/{id:int}',
			title: 'Mod\xE8les d\'abonnements',
			resolve: {
				entity  : ['$http', '$stateParams', function( $http, $stateParams) {
					return $http.get( 'rest/sub-offer/get/' + $stateParams.id)
								.then( function( response) {
									return response.data;
								}, function( response) {
									if( response.status == 404)
										$state.go( 'root.error404');
								});
				}]
			},
			templateUrl: 'angular/preta-admin/views/sub-offer/show.html',
			ncyBreadcrumb: {
				label: 'Afficher'
			},
			controller: 'SubOfferController'
		})
		.state( 'root.subOffers.edit', {
			url: '/edit/{id:int}',
			title: 'Mod\xE8les d\'abonnements',
			resolve: {
				entity  : ['$http', '$stateParams', function( $http, $stateParams) {
					return $http.get( 'rest/sub-offer/get/' + $stateParams.id)
								.then( function( response) {
									console.log( response.data);
									return response.data;
								}, function( response) {
									if( response.status == 404)
										$state.go( 'root.error404');
								});
				}],
				shopStatuses: [ 'ShopStatusService', function( ShopStatusService) {
					//redirect to erreir if there are no ShopStatuses
					return ShopStatusService.listEntities();
				}]
			},
			templateUrl: 'angular/preta-admin/views/sub-offer/edit.html',
			ncyBreadcrumb: {
				label: 'Modifier'
			},
			controller: 'SubOfferController'
		})
		/*End SubOffers*/
		/*ShopSub*/
		.state( 'root.shop-subs', {
			url: '/shop-subs/{status:int}',
			title: 'Abonnements de boutique',
			templateUrl: 'angular/preta-admin/views/shop-sub/list.html',
			ncyBreadcrumb: {
				label: '{{ breadCrumbLabel }}'
			},
			resolve: {
				entity: [ function() { return null; }]
			},
			controller: 'ShopSubController'
		})
		.state( 'root.shop-subs-all', {
			url: '/shop-subs/all',
			controller: [ '$state', function( $state) {
				$state.go( 'root.shop-subs', { status: -1});
			}]
		})
		.state( 'root.shop-subs-pending-conf', {
			url: '/shop-subs/pending-conf',
			controller: [ '$state', function( $state) {
				$state.go( 'root.shop-subs', { status: 1});
			}]
		})
		.state( 'root.shop-subs.show', {
			url: '/show/{id:int}',
			title: 'Abonnements de boutique',
			ncyBreadcrumb: {
				label: 'Abonnement {{ entity.id }}'
			},
			resolve: {
				entity  : ['$http', '$state', '$stateParams', function( $http, $state, $stateParams) {
					return $http.get( 'rest-api/admin/shop-sub/' + $stateParams.id)
								.then( function( response) {
									return response.data;
								}, function( response) {
									if( response.status == 404)
										$state.go( 'root.error404');
								});
				}]
			},
			templateUrl: 'angular/preta-admin/views/shop-sub/show.html',
			controller: 'ShopSubController'
			
		})
		/*End ShopSub*/
		/* AdvOffer */
		.state( 'root.adv-offers', {
			url: '/adv-offers?advOfferPage&advOfferPageSize&advOfferStatus&advOfferOrderByIdAsc',
			params: {
				advOfferPage: "1",
				advOfferPageSize: "10",
				advOfferStatus: "0",
				advOfferOrderByIdAsc: "true"
			},
			templateUrl: 'angular/preta-admin/views/adv-offer/list.html',
			title: 'Publicit\xE9s',
			ncyBreadcrumb: {
				label: '{{ breadCrumbLabel }}'
			},
			controller: 'AdvOfferController'
		})
		.state( 'root.adv-offers.show', {
			url: '/show/{id:int}',
			title: 'D\xE9tails Publicit\xE9',
			ncyBreadcrumb: {
				label: 'D\xE9tails #{{ entity.id}}'
			},
			templateUrl: 'angular/preta-admin/views/adv-offer/show.html',
			controller: 'AdvOfferController'
		})
		/* Shrtcts */
		.state( 'root.adv-offers-all', {
			url: '/adv-offers/all',
			controller: [ '$state', function( $state) {
				$state.go( 'root.adv-offers', { advOfferPage: 1, advOfferPageSize: 10, advOfferStatus: -1, advOfferOrderByIdAsc: 0});
			}]
		})
		.state( 'root.adv-offers-pending-conf', {
			url: '/adv-offers/pending-conf',
			controller: [ '$state', function( $state) {
				$state.go( 'root.adv-offers', { advOfferPage: 1, advOfferPageSize: 10, advOfferStatus: 1, advOfferOrderByIdAsc: 1});
			}]
		})
		/* End AdvOffer */
		/*Admin Profile*/
		.state( 'root.profile', {
			url: '/profile',
			title: 'Mon profil',
			templateUrl: 'angular/preta-admin/views/user/self/profile.html',
			ncyBreadcrumb: {
				label: 'Mon Profil'
			},
			//Controler handling profile update
			controller: 'UserController'
		})
		/*End Admin Profile*/
		/* EShop Section */
		.state( 'root.e-shops', {
			url: '/e-shops',
			title: 'Boutiques',
			templateUrl: 'angular/preta-admin/views/e-shop/list.html',
			ncyBreadcrumb: {
				label: 'Boutiques'
			},
			resolve: {
				entity: [ function() { return null; }]
			},
			controller: 'EShopController'
		})
		.state( 'root.e-shops.show', {
			url: '/show/{id}',
			title: 'Boutiques',
			templateUrl: 'angular/preta-admin/views/e-shop/show.html',
			ncyBreadcrumb: {
				label: '{{ entity.name }}'
			},
			resolve: {
				entity: [ 'EShopService', '$stateParams', function( EShopService, $stateParams) {
					return EShopService.loadEntity( $stateParams.id)
										.then( function( response) {
											return response;
										}, function( response) {
											console.error( response);
										})
				}]
			},
			controller: 'EShopController'
		})
		/* End EShop Section */
		/* Order Section */
		.state( 'root.orders', {
			url: '/orders?page&pageSize&orderStatus&orderByIdAsc',
			title: 'Commandes',
			templateUrl: 'angular/preta-admin/views/order/list.html',
			ncyBreadcrumb: {
				label: '{{ breadCrumbLabel }}'
			},
			controller: 'OrderController'
		})
		/* Parametrized Shortcuts */
		.state( 'root.orders-all', {
			url: '/orders/all',
			controller: [ '$state', function( $state) {
				$state.go( 'root.orders', { page: 1, pageSize: 10, orderByIdAsc: false, orderStatus: -1});
			}]
		})
		.state( 'root.orders-pending-pay', {
			url: '/orders/pending-pay',
			controller: [ '$state', function( $state) {
				$state.go( 'root.orders', { page: 1, pageSize: 10, orderByIdAsc: true, orderStatus: 0});
			}]
		})
		.state( 'root.orders-pending-conf', {
			url: '/orders/pending-conf',
			controller: [ '$state', function( $state) {
				$state.go( 'root.orders', { page: 1, pageSize: 10, orderByIdAsc: true, orderStatus: 1});
			}]
		})
		.state( 'root.orders-paid', {
			url: '/orders/paid',
			controller: [ '$state', function( $state) {
				$state.go( 'root.orders', { page: 1, pageSize: 10, orderByIdAsc: true, orderStatus: 2});
			}]
		})
		.state( 'root.orders-delivering', {
			url: '/orders/delivering',
			controller: [ '$state', function( $state) {
				$state.go( 'root.orders', { page: 1, pageSize: 10, orderByIdAsc: true, orderStatus: 3});
			}]
		})
		.state( 'root.orders-delivered', {
			url: '/orders/delivered',
			controller: [ '$state', function( $state) {
				$state.go( 'root.orders', { page: 1, pageSize: 10, orderByIdAsc: true, orderStatus: 4});
			}]
		})
		.state( 'root.orders-expense-pending', {
			url: '/orders/expense-pending',
			controller: [ '$state', function( $state) {
				$state.go( 'root.orders', { page: 1, pageSize: 0, orderByIdAsc: true, orderStatus: 5});
			}]
		})
		.state( 'root.orders-expended', {
			url: '/orders/expended',
			controller: [ '$state', function( $state) {
				$state.go( 'root.orders', { page: 1, pageSize: 10, orderByIdAsc: true, orderStatus: 6});
			}]
		})
		.state( 'root.orders.show', {
			url: '/show/{id}',
			title: 'Commandes',
			templateUrl: 'angular/preta-admin/views/order/show.html',
			ncyBreadcrumb: {
				label: 'Commande #{{ entity.id }}'
			},
			controller: 'OrderController'
		})
		/* End Order Section */
		/* Payments Section */
		.state( 'root.payments', {
			url: '/payments?page&pageSize&paymentStatus&orderByIdAsc',
			title: 'Paiements',
			templateUrl: 'angular/preta-admin/views/payment/list.html',
			ncyBreadcrumb: {
				label: '{{ breadCrumbLabel }}'
			},
			controller: 'PaymentController'
		})
		.state( 'root.payments.show', {
			url: '/show/{id:int}',
			title: 'Détails Paiement',
			templateUrl: 'angular/preta-admin/views/payment/show.html',
			ncyBreadcrumb: {
				label: 'Détails #{{ entity.id | number }}'
			},
			controller: 'PaymentController'
		})
		/* Parametrized Shortcuts */
		.state( 'root.payments-all', {
			url: '/payments/all',
			controller: [ '$state', function( $state) {
				$state.go( 'root.payments', { page: 1, pageSize: 10, orderByIdAsc: true, paymentStatus: 0});
			}]
		})
		.state( 'root.payments-pending', {
			url: '/payments/pending',
			controller: [ '$state', function( $state) {
				$state.go( 'root.payments', { page: 1, pageSize: 10, orderByIdAsc: true, paymentStatus: 1});
			}]
		})
		.state( 'root.payments-accepted', {
			url: '/payments/accepted',
			controller: [ '$state', function( $state) {
				$state.go( 'root.payments', { page: 1, pageSize: 10, orderByIdAsc: true, paymentStatus: 2});
			}]
		})
		.state( 'root.payments-rejected', {
			url: '/payments/rejected',
			controller: [ '$state', function( $state) {
				$state.go( 'root.payments', { page: 1, pageSize: 10, orderByIdAsc: true, paymentStatus: 3});
			}]
		})
		/* End Payements Section */
		/* Category */
		.state( 'root.categories', {
			url : '/categories',
			title: 'Cat\xE9gories',
			templateUrl: 'angular/preta-admin/views/category/list.html',
			ncyBreadcrumb: {
				label: 'Cat\xE9gories'
			},
			resolve: {
				entities: [ 'CategoryService', function( CategoryService) {
					return CategoryService.loadTreeData()
											.then( function( response) {
												return response;
											}, function( response) {
												console.error( response);
											});
				}]
			},
			controller: 'CategoryController'
		})
		/* End Category Section */
		/* Slides */
		.state( 'root.slides', {
			url: '/slides',
			title: 'Slides d\'accueil',
			templateUrl: 'angular/preta-admin/views/slide/list.html',
			ncyBreadcrumb: {
				label: 'Slides'
			},
			controller: 'SlideController'
		})
		.state( 'root.slides.new', {
			url: '/new',
			title: 'Slides d\'accueil',
			templateUrl: 'angular/preta-admin/views/slide/new.html',
			ncyBreadcrumb: {
				label: 'Nouvelle'
			},
			controller: 'SlideController'
		})
		/* End Slides */
		/* Expense */
		.state( 'root.expenses', {
			url: '/expenses?page&pageSize&orderByIdAsc',
			params: {
				page: "1",
				pageSize: "10",
				orderByIdAsc: "false"
			},
			title: 'R\xE8glements',
			templateUrl: 'angular/preta-admin/views/expense/list.html',
			ncyBreadcrumb: {
				label: 'R\xE8glements'
			},
			controller: 'ExpenseController'
		})
		.state( 'root.expenses.new', {
			url: '/new',
			title: 'R\xE8glements',
			templateUrl: 'angular/preta-admin/views/expense/new.html',
			ncyBreadcrumb: {
				label: 'Nouveau'
			},
			controller: 'ExpenseController'
		})
		.state( 'root.expenses.show', {
			url: '/show/{id:int}',
			title: 'R\xE8glements',
			templateUrl: 'angular/preta-admin/views/expense/show.html',
			ncyBreadcrumb: {
				label: 'Détails'
			},
			controller: 'ExpenseController'
		})
		/* End Expense */
		/* Upgrade Requests */
		.state( 'root.upgrade-requests', {
			url: '/upgrade-requests?page&pageSize&orderByIdAsc&status',
			title: 'Migration d\'acheteurs',
			ncyBreadcrumb: {
				label: '{{ breadcrumb }}'
			},
			templateUrl: 'angular/preta-admin/views/upgrade-request/list.html',
			controller: 'UpgradeRequestController'
		})
		.state( 'root.upgrade-requests.show', {
			url: '/show/{id:int}',
			title: 'Migration d\'acheteurs',
			ncyBreadcrumb: {
				label: 'Détails #{{ entity.id }}'
			},
			templateUrl: 'angular/preta-admin/views/upgrade-request/show.html',
			controller: 'UpgradeRequestController'
		})
		/* End Upgrade Requests */
		/* Profiles Validation */
		.state( 'root.profiles-pending-validation', {
			url: '/profiles-pending-validation?page&pageSize',
			title: 'Profils à Valider',
			ncyBreadcrumb: {
				label: 'Profils à Valider'
			},
			templateUrl: 'angular/preta-admin/views/user/shared/profile-validation-list.html',
			controller: 'ProfileValidationController'
		})
		/* End Profiles Validation */
		
		/* Local Markets */
		.state( 'root.local-markets', {
			url: '/local-markets',
			title: 'Marchés Locaux',
			templateUrl: 'angular/preta-admin/views/local-market/list.html',
			controller: 'LocalMarketController',
		    ncyBreadcrumb: {
			    label: 'Marchés Locaux'
		    }
		})
		.state( 'root.local-markets.new', {
			url: '/new',
			title: 'Marchés Locaux',
			templateUrl: 'angular/preta-admin/views/local-market/new.html',
			ncyBreadcrumb: {
				label: 'Nouveau'
			},
			controller: 'LocalMarketController'
		})
		.state( 'root.local-markets.show', {
			url: '/show/{id:int}',
			title: 'Marchés Locaux',
			templateUrl: 'angular/preta-admin/views/local-market/show.html',
			ncyBreadcrumb: {
				label: 'Détails #{{ entity.id }}'
			},
			controller: 'LocalMarketController'
		})
		.state( 'root.local-markets.update', {
			url: '/update/{id:int}',
			title: 'Marchés Locaux',
			templateUrl: 'angular/preta-admin/views/local-market/update.html',
			ncyBreadcrumb: {
				label: 'Modifier #{{ entity.id }}'
			},
			controller: 'LocalMarketController'
		})
		/* Local Markets */
		/* Errors */
		.state( 'root.error404', {
			url: '/error/404',
			title: 'Erreur 404',
			templateUrl: 'angular/preta-admin/views/error/404.html',
			ncyBreadcrumb: {
				label: 'Erreur 404'
			}
		})
		/* End Errors */
		;
	
	/* Configurations Sidebar: Ajout des liens */
    baSidebarServiceProvider.addStaticItem({
        title: 'Fournisseurs EMoney',
        icon: 'fa fa-money',
        stateRef: 'root.emps'
    });
    baSidebarServiceProvider.addStaticItem({
        title: 'Marchés locaux',
        icon: 'fa fa-map-signs',
        stateRef: 'root.local-markets'
    });
    baSidebarServiceProvider.addStaticItem({
    	  title: 'Status de Boutiques',
    	  icon: 'fa fa-flag',
    	  stateRef :'root.shopStatuses'
      });
    baSidebarServiceProvider.addStaticItem({
    	  title: 'Mod\xE8les d\'abonnement',
    	  icon: 'fa fa-calendar',
          stateRef: 'root.subOffers'
      });
    baSidebarServiceProvider.addStaticItem({
    	  title: 'Options de publicit\xE9',
    	  icon: 'fa fa-certificate',
    	  stateRef: 'root.adv-options'
      });
    baSidebarServiceProvider.addStaticItem({
    	  title: 'Cat\xE9gories',
    	  icon: 'fa fa-folder',
    	  stateRef: 'root.categories'
      });
    baSidebarServiceProvider.addStaticItem({
  	  title: 'Slides',
  	  icon: 'fa fa-file-image-o',
  	  stateRef: 'root.slides'
    });
    baSidebarServiceProvider.addStaticItem({
    	  title: 'Utilisateurs',
    	  icon: 'fa fa-users',
    	  subMenu: [{
    		  title: 'Administrateurs',
    		  stateRef: 'root.admins'
    	  }, {
    		  title: 'Acheteurs',
    		  stateRef: 'root.buyers'
    	  }, {
    		  title: 'Migration d\'acheteurs',
    		  stateRef: 'root.upgrade-requests'
    	  }, {
    		  title: 'Profils à valider',
    		  stateRef: 'root.profiles-pending-validation'
    	  }]
      });
    baSidebarServiceProvider.addStaticItem({
    	  title: 'Boutiques',
    	  icon: 'fa fa-opencart',
    	  stateRef: 'root.e-shops'
    });
    baSidebarServiceProvider.addStaticItem({
    	  title: 'Abonnements',
    	  icon: 'fa fa-calendar-check-o',
    	  subMenu: [{
    		  title: 'En attente',
    		  stateRef: 'root.shop-subs-pending-conf'
    	  }, {
    		  title: 'Toutes',
    		  stateRef: 'root.shop-subs-all'
    	  }]
    });
    baSidebarServiceProvider.addStaticItem({
    	  title: 'Publicit\xE9s',
    	  icon: 'fa fa-certificate',
    	  subMenu: [{
    		  title: 'En attente',
    		  stateRef: 'root.adv-offers-pending-conf'
    	  }, {
    		  title: 'Toutes',
    		  stateRef: 'root.adv-offers-all'
    	  }]
    });
    baSidebarServiceProvider.addStaticItem({
  	  title: 'Commandes',
  	  icon: 'fa fa-shopping-cart',
	  subMenu: [{
		  title: 'A payer',
		  stateRef: 'root.orders-pending-pay'
      },  {
		  title: 'A confirmer',
		  stateRef: 'root.orders-pending-conf'
	  }, {
		  title: 'Confirmées',
		  stateRef: 'root.orders-paid'
	  }, {
		  title: 'En cours de livraison',
		  stateRef: 'root.orders-delivering'
	  }, {
		  title: 'Livr\xE9es',
		  stateRef: 'root.orders-delivered'
	  }, {
		  title: 'A r\xE9gler',
		  stateRef: 'root.orders-expense-pending'
	  }, {
		  title: 'R\xE9gl\xE9es',
		  stateRef: 'root.orders-expended'
	  }, {
		  title: 'Toutes',
		  stateRef: 'root.orders-all'
	  }]
    });
    
    /* Payments */
    baSidebarServiceProvider.addStaticItem({
    	  title: 'Paiements',
    	  icon: 'fa fa-check',
	  	  subMenu: [{
	  		  title: 'Tous',
	  		  stateRef: 'root.payments-all'
	        }, {
	  		  title: 'A Confirmer',
	  		  stateRef: 'root.payments-pending'
	  	  }, {
	  		  title: 'Accept\xE9s',
	  		  stateRef: 'root.payments-accepted'
	  	  }, {
	  		  title: 'Rejet\xE9s',
	  		  stateRef: 'root.payments-rejected'
	  	  }]
    });
    
    /* Reglements */
    baSidebarServiceProvider.addStaticItem({
    	  title: 'R\xE8glements',
    	  icon: 'glyphicon glyphicon-transfer',
    	  stateRef: 'root.expenses'
    });
    
    /* Remboursements */
    baSidebarServiceProvider.addStaticItem({
  	  title: 'Remboursements',
  	  icon: 'fa fa-undo'
    });
}