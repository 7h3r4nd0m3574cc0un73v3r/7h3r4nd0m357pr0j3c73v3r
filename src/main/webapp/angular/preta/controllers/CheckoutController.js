App.controller( 'CheckoutController', ['$state', '$scope', 'ArticleService', 'OrderService', 'ToastService', 'SoundService', '$rootScope',
                                     function( $state,  $scope, ArticleService, OrderService, ToastService, SoundService, $rootScope) {
	
	if( $rootScope.loggedUser == null) {
		$state.go( 'root.errors.401');
	}
	
	/* Loading Mutexes */
	$scope.isLoading = true;
	
	/* Init vars */
	$scope.adminEAccounts = [];
	$scope.grouped = [];
	$scope.selectedAdminEAccount = undefined;
	$scope.form = {};
	$scope.total = 0;
	
	/* Get Grouped Article Orders */
	$scope.loadNGroup = function( entities) {
		OrderService.group( entities)
					.then( function( response) {
						$scope.grouped = response;
						
						angular.forEach( $scope.grouped, function( entity) {
							entity.total = 0;
							angular.forEach( entity.orderedArticles, function( orderedArticle) {
								/* Compute ArticleOrder's Total */
								entity.total += orderedArticle.quantity * ( orderedArticle.article.price + orderedArticle.article.deliveryFee);
								/* Computes Overall Total */
								$scope.total += entity.total;
								
								ArticleService.loadDefaultPicture( orderedArticle.article.id)
									   .then( function( response) {
										   orderedArticle.article.defaultPicture = response;
									   }, function( response) {
										   console.error( response);
									   });
							});
						});
						
						OrderService.loadAdminEAccountsForPayments()
									.then( function( response) {
										if( response.entities == undefined) {
											$scope.isLoading = false;
											return;
										}
										
										$scope.adminEAccounts = response.entities;
										
										$scope.$watch( 'form.eAccount', function( newValue, oldValue) {
											if( $scope.form.eAccount != undefined) {
												angular.forEach( $scope.adminEAccounts, function( adminEAccount) {
													
													if( adminEAccount.emp.id == newValue.emp.id) {
														$scope.selectedAdminEAccount = adminEAccount;
													}
												});
											}
											else {
												$scope.selectedAdminEAccount = null;
											}
										});
										
										$scope.isLoading = false;
									}, function( response) {
										console.error( response);
										$scope.isLoading = false;
									});
						
						$scope.isLoading = false;
					}, function( response) {
						console.error( response);
						$scope.isLoading = false;
					});
	}
	
	$scope.loadNGroup( ArticleService.articlesToOrder);
	
	/* Init ArticleService referrer */
	if( ArticleService.referrer == 2) 
		ArticleService.referrer = 0;
	
	$scope.placeOrder = function() {
		OrderService.addArticleOrder( $scope.grouped, $scope.form.eAccount.id, $scope.form.paymentRef, $scope.selectedAdminEAccount.id,
									  $scope.form.useDefaultDeliveryAddress, $scope.form.deliveryAddress)
									  
					.then( function( response) {
						console.log( response);
						if( $scope.grouped.length > 1) {
							
							ToastService.openToast( 'info', 'Vos commandes ont \xE9t\xE9 enregistr\xE9es avec succ\xE8s.', 'Commandes enregistr\xE9es !')
						}
						else {
							ToastService.openToast( 'info', 'Votre commande a \xE9t\xE9 enregistr\xE9e avec succ\xE8s.', 'Commande enregistr\xE9e !')
						}
						SoundService.success();
						$state.go( 'root.order-confirmation');
					}, function( response) {
						ToastService.openToast( 'error', 'Une erreur est survenue lors de l\'enregistrement de votre commande. Veuillez vérifier les informations fournies.', 'Erreur lors de l\'enregistrement')
						SoundService.danger();
					});
	};
}]);