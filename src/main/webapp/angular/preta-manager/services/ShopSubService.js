'use strict';
//AngularJS Service for User And EShop Registration
App.factory( 'ShopSubService', ['$http', '$q','EShopService', function( $http, $q, EShopService) {
		return {
			loadPendingEntities: function() {
				return $http.get( 'rest/shop-subs/pending')
							.then( 
								function( response) {
									return response.data;
								},
								function( errResponse) {
									console.log( 'Error: ShopSubService > listShopSub');
									return $q.reject( errResponse);
								}
							);
			},
			addEntity: function( entity, adminEAccount) {
				return $http({
								method: 'POST',
								url: 'rest-api/manager/shop-sub/add',
								data:  $.param({
									shopSub: angular.toJson( entity.shopSub),
									paymentRef: entity.paymentRef,
									eShopId: EShopService.activeEShop.id,
									eAccountId: entity.eAccount == undefined ? null : entity.eAccount.id,
									adminEAccountId: adminEAccount == undefined ? null : adminEAccount.id
								}),
							    headers : {
							        'Content-Type' : 'application/x-www-form-urlencoded'
							    }
							}).then( function( response) {
								console.log( response);
							}, function( response) {
								return $q.reject( response);
							});
			
			},
			loadEntity: function( id) {
				return $http.get( 'rest-api/admin/shop-sub/' + id)
							.then( 
								function( response) {
									return response.data;
								},
								function( errResponse) {
									console.log( 'Error: ShopSubService > getEntity');
									return $q.reject( errResponse);
								}
							);
			},
			loadEntitiesByEShop: function( id, page, pageSize, orderByIdAsc) {
				return $http({
					url: 'rest-api/manager/e-shop/' + id + '/shop-subs',
					method: 'GET',
					params: {
						page: page,
						pageSize: pageSize,
						orderByIdAsc: orderByIdAsc
					}
				}).then(
						function( response) {
							return response.data;
						},
						function( errResponse) {
							console.log( 'Error: ShopSubService > loadEntitiesByEShop');
							return $q.reject( errResponse);
						}
					);
			},
			activate: function( id) {
				return $http({
					method: 'POST',
					url: 'rest-api/manager/shop-sub/' + id + '/activate',
				}).then( function( response) {
					return response;
				}, function( response) {
					console.error( response);
				});
			},
		}
	}                            
]);

