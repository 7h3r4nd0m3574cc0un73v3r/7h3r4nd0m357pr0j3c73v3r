App.controller( 'CartController', [ '$rootScope', '$scope', '$state', 'ArticleService', '$stateParams', '$mdDialog', 'toastr', 'toastrConfig', '$log',
                                    function( $rootScope, $scope, $state, ArticleService, $stateParams, $mdDialog, toastr, toastrConfig, $log) {
	/* Reject Unlogged User */
	if( $rootScope.loggedUser == null) {
		$state.go( 'root.errors.401');
	}
	
	/* Loading Mutex */
	$scope.isListLoading = true;
	
	/* Pagination Settings */
	$scope.pagination = { currentPage: $stateParams.page == undefined ? 1 : $stateParams.page,
						  pageSize: $stateParams.pageSize == undefined ? 5 : $stateParams.pageSize,
					      pagesNumber: null,
					      itemsNumber: null
						};
	/* End Pagination Setting */
	
	/* Entities */
	$scope.allEntities = [];
	$scope.cartItems = [];
	$scope.total = 0 ;
	$scope.selectedItemsCount = 0;
	
	/* Loading Entities */
	function loadEntities( page, pageSize) {
		$scope.isListLoading = true;
		/* Load Everything, pagiantion handlded with slice */
		ArticleService.loadCartContent( page, pageSize)
					  .then( function( response) {
						  $scope.pagination.pagesNumber = response.pagesNumber;
						  $scope.pagination.itemsNumber = response.itemsNumber;
						  
						  angular.forEach( response.entities, function( cartItem) {
							  cartItem.isSelected = false;
							  /* Load Article Pictures */
							  ArticleService.loadDefaultPicture( cartItem.article.id)
							  				.then( function( response) {
							  					
							  					cartItem.article.pictures = [];
							  					cartItem.article.pictures.push( response);
							  				}, function( response) {
							  					console.error( response);
							  				});
						  });
						  
						  $scope.allEntities = response.entities;
						  
						  $scope.changePage();
						  
						  $scope.isListLoading = false;
					  }, function( response) {
						  console.error( response);
						  $scope.isListLoading = false;
					  });
	}
	
    /* Change Page */
    $scope.changePage = function() {
    	$log.info( "Change Page Called");
    	$scope.isListLoading = true;
    	/* In case of articles deletion and page is no longer available */
    	if( $scope.pagination.currentPage < $scope.pagination.pagesNumber) {
    		$scope.pagination.currentPage = $scope.pagination.pagesNumber;
    	}
    	var pageIndex = ($scope.pagination.currentPage - 1) * $scope.pagination.pageSize;
    	$scope.cartItems = $scope.allEntities.slice( pageIndex, pageIndex + $scope.pagination.pageSize);
    	$log.info( "Entities changed from ChangePage: " + $scope.cartItems.length);
    	$log.info( $scope.cartItems);
    	$state.go( $state.current.name, { page: $scope.pagination.currentPage, pageSize: $scope.pagination.pageSize}, { notify: false});
    	
    	$scope.isListLoading = false;
	};
    /* End Change Page */
	
	/* Order the cart contents */
	$scope.orderArticles = function() {
		$scope.selectedEntities = [];
		angular.forEach( $scope.allEntities, function( value) {
			if( value.isSelected)
				$scope.selectedEntities.push( value);
		});
		
		if( $scope.selectedEntities.length) {
			ArticleService.articlesToOrder = $scope.selectedEntities;

			/* Checks if profile is Valid */
	    	if( $rootScope.loggedUser != undefined)
			{
				if( $rootScope.loggedUser.profileCompletion < 100 || !$rootScope.loggedUser.approved) {
					$scope.showProfileAlert();
					return;
				}
				else {
					$state.go( 'root.order');
				}
			}
		}
	};
	
	/* Remove item from cart*/
	$scope.removeFromCart = function( id, ev) {
		var confirm = $mdDialog.confirm()
						        .title('Confirmation de suppression')
						        .textContent('Supprimer cet article du panier ?')
						        .ariaLabel('Confirmation Suppression')
						        .targetEvent(ev)
						        .ok('Oui')
						        .cancel('Non');
						
					$mdDialog.show(confirm).then(function() {
						/* Yes */
						ArticleService.removeFromCart( id)
									.then( function( response) {
										loadEntities( 1, 0);
										openToast( 'info','Article supprimé du panier', 'Article Supprimé');
									}, function( response) {
										console.error( response);
									});
					}, function() {
						/* No */
						
					});
	}
    
    /* Quantity Widget */
    $scope.incQuantity = function( id) {
    	angular.forEach( $scope.allEntities, function( cartItem) {
    		if( cartItem.id == id) {
    			cartItem.quantity ++;
    		}
    	});
    };

    $scope.decQuantity = function( id) {
    	angular.forEach( $scope.allEntities, function( cartItem) {
    		if( cartItem.id == id) {
    	    	if( cartItem.quantity > 1) {
    	    		cartItem.quantity--;
    	    	}
    		}
    	});
    };
    /* End Quantity Widget */
    
    /* Suppress All & Selected*/
    $scope.clearCart = function( ) {
    	var confirm = $mdDialog.confirm()
					        .title('Confirmation de suppression')
					        .textContent('Tous les articles du panier seront supprim\xE9s. Continuer ?')
					        .ariaLabel('Confirmation Suppression')
					        .targetEvent( null)
					        .ok('Oui')
					        .cancel('Non');

		  $mdDialog.show(confirm).then(function() {
		      /* Yes */
			 ArticleService.clearCart()
			 				.then( function( response) {
			 					loadEntities( 1, 0);
			 					openToast( 'info','Votre panier a bien été vidé', 'Panier vide');
			 				}, function( response) {
			 					console.error( response);
			 				});
		  }, function() {
			/* No */
			  
		  });
    }
    /*
     * TODO
     * Doesn't clear all selection
     */
    $scope.clearSelection = function() {
    	var confirm = $mdDialog.confirm()
        .title('Confirmation de suppression')
        .textContent('Supprimer les articles sélectionnés ?')
        .ariaLabel('Confirmation Suppression')
        .targetEvent( null)
        .ok('Oui')
        .cancel('Non');

		$mdDialog.show(confirm).then(function() {
			/* Yes */
			$scope.isListLoading = true;
			
			var ids = [];
			angular.forEach( $scope.allEntities, function( cartItem, key) {
				if( cartItem.isSelected)
					ids.push( cartItem.id);
			});

			ArticleService.clearSelection( ids)
						  .then( function( response) {
							  console.log( response);
							  loadEntities( 1, 0);
							  
							  $scope.isListLoading = false;
						  }, function( response) {
							  console.error( response);
						  });
			
			loadEntities( 1, 0);
			$scope.isListLoading = false;
			openToast( 'info','Les articles sélectionnés ont été supprimés', 'Sélection supprimée');
		}, function() {
			/* No */
		
		});
    }
    
    $scope.selectAll = function() {
    	if( $scope.selectedItemsCount < $scope.allEntities.length) {
    		angular.forEach( $scope.allEntities, function( cartItem) {
    			cartItem.isSelected = true;
    		});
    	}
    	else {
    		angular.forEach( $scope.allEntities, function( cartItem) {
    			cartItem.isSelected =false;
    		});
    	}
    }
    /* End Suppress All & Selected*/

    /* Alert Dialog */
    $scope.showFeatureAlert = function(ev) {
        $mdDialog.show(
          $mdDialog.alert()
            .parent(angular.element(document.body))
            .clickOutsideToClose( false)
            .title('Choix Invalide')
            .textContent('S\xE9lectionner au moins une valeur pour chacune des caract\xE9ristiques de l\'article afin continuer')
            .ariaLabel('Action Invalide')
            .ok('Ok!')
            .targetEvent(ev)
        );
      };

      $scope.showProfileAlert = function(ev) {
          $mdDialog.show(
            $mdDialog.alert()
              .parent(angular.element(document.body))
              .clickOutsideToClose( false)
              .title('Profil Incomplet')
              .textContent('Veuillez complèter votre profil à 100 % et vous faire valider afin de pouvoir passer des commandes !')
              .ariaLabel('Profil Incomplet')
              .ok('Ok!')
              .targetEvent(ev)
          );
        };
    /* End Alert Dialog */
    
	/* Toastr Config */
	var defaultConfig = angular.copy(toastrConfig);
	var openedToasts = [];
    var options = {
		      autoDismiss: false,
		      positionClass: 'toast-top-right',
		      type: 'info',
		      timeOut: '5000',
		      extendedTimeOut: '2000',
		      allowHtml: false,
		      closeButton: true,
		      tapToDismiss: true,
		      progressBar: false,
		      newestOnTop: true,
		      maxOpened: 0,
		      preventDuplicates: false,
		      preventOpenDuplicates: false
		    };
    angular.extend(toastrConfig, options);

    function openToast( type, message, title) {
        openedToasts.push( toastr[ type]( message, title));
    }

    $scope.$on('$destroy', function iVeBeenDismissed() {
      angular.extend(toastrConfig, defaultConfig);
    })
	/*End Toastr Config */

	/* Call to loading functions - The Order is important */
	loadEntities( 1, 0);
    
	/*Updates the selection total price */
	$scope.updateTotal = function() {
		$scope.total = 0;
		$scope.selectedItemsCount = 0;
		angular.forEach( $scope.allEntities, function( value) {
			if( value.isSelected) {
				if( !value.article.isDiscounted)
					$scope.total += ( value.article.price + value.article.deliveryFee) * value.quantity;
				else
					$scope.total += ( value.article.discountPrice + value.article.deliveryFee) * value.quantity;
				$scope.selectedItemsCount++;
			}
		});
	};
	
	$scope.$watch( 'allEntities', function( newValue, oldValue) {
		console.warn( "Change detected");
		$scope.updateTotal();
	}, true);
}]);