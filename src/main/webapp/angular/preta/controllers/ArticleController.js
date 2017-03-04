App.controller( 'ArticleController', [ '$scope', '$rootScope', '$state', '$stateParams', 'ArticleService', 'toastr', 'toastrConfig', '$mdDialog', 'SoundService',
                                       function( $scope, $rootScope, $state, $stateParams, ArticleService, toastr, toastrConfig, $mdDialog, SoundService) {
	/* Init Article Entity and Mutex*/
	$scope.entity;
	$scope.isEntityLoading = true;
	
	/* Init Similar Articles and Same EShop Siblings
	 * as well as their mutexes
	 * */
	$scope.similar = [];
	$scope.isSimilarLoading = true;
	
	$scope.siblings = [];
	$scope.isSiblingsLoading = true;

	/* Set default quantity for item*/
	$scope.quantity = 1;
	
	/* Load the Article */
	ArticleService.loadEntity( $stateParams.id)
					.then( function( response) {
						$scope.entity = response;
						
						if( response != undefined && $rootScope.loggedUser != undefined) {
							ArticleService.addVisited( response.id)
										  .then(function( response) {
											  console.log( response);
											  console.log( "added article as visited");
										  }, function( response) {
											  console.error( response);
										  });
						}
						/* Load Default Picture */
						
						/* Init Article Features */
						ArticleService.loadEShop( $scope.entity.id)
									  .then( function( response) {
										  $scope.entity.eShop = response;

											/* Load Article of the Same EShop */
											ArticleService.loadRecentArticlesByEShop( $scope.entity.eShop.id, 1, 8)
														  .then( function( response) {
															  $scope.siblings = response.entities;
																/* Load Default Picture */
																angular.forEach( $scope.siblings, function( article) {
																	ArticleService.loadDefaultPicture( article.id)
																				  .then( function( r) {
																						article.pictures = [];
																						article.pictures.push( r);
																				  }, function( r) {
																					  console.error( r);
																				  });
																});
															  $scope.isSiblingsLoading = false;
														  }, function( response) {
															  console.error( response);
															  $scope.isSiblingsLoading = false;
														  });
											
											/* Load Article Pictures */
											ArticleService.loadPictures( $scope.entity.id)
														  .then( function( response) {
															  $scope.entity.pictures = response.entities;
															  
															    /* UIB Slide Config */
															    $scope.myInterval = 5000;
															    $scope.noWrapSlides = false;
															    $scope.active = 0;
															    var slides = $scope.slides = [];
															    var currIndex = 0;
															    
															    angular.forEach( $scope.entity.pictures, function( picture) {
															    	slides.push({
															    		/*image: 'rest/article/fetch-picture/' + picture.id,*/
															    		image: 'https://drive.google.com/uc?id=' + picture.googleId +'&export=download',
															    		id: currIndex++
															    	});
															    });
															    /* ENd UIB Slide Config */
														  }, function( response) {
															  console.error( response);
														  });
											
											/* End Loading */
									  }, function( response) {
										  console.error( response);
										  $scope.isEntityLoading = false;
									  });
						
						/* Load Article's Features */
						  ArticleService.loadArticleFeatures( $scope.entity.id, 1, 0)
									  .then( function( response) {
										  $scope.entity.features = response.entities;

											/* Set all article features as not selected*/
											angular.forEach( $scope.entity.features, function( articleFeature) {
												articleFeature.selectedFeatureValue = {};
												
												angular.forEach( articleFeature.values, function( featVal) {
													featVal.isSelected = false;
												});
											});
									  }, function( response) {
										  console.error( response);
									  });
						  
						/* Load Similar articles */
						ArticleService.loadSimilarArticles( $scope.entity.id, 1, 8)
										.then( function( response) {
											$scope.similar = response.entities;
											/* Load Default Picture */
											angular.forEach( $scope.similar, function( article) {
												ArticleService.loadDefaultPicture( article.id)
															  .then( function( r) {
																	article.pictures = [];
																	article.pictures.push( r);
															  }, function( r) {
																  console.error( r);
															  });
											});
											$scope.isSimilarLoading = false;
										}, function( response) {
											console.error( response);
											$scope.isSimilarLoading = false;
										});
						
						$scope.isEntityLoading = false;
					}, function( response) {
						console.error( response);
						$scope.isEntityLoading = false;
					});
	
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
    });
	/*End Toastr Config */

    $scope.addToCart = function() {
    	
    	if( $scope.isValidForCart()) {
    		
    		ArticleService.articleToCart = { article: $scope.entity, quantity: $scope.quantity};
    		
	    	if( $rootScope.loggedUser != undefined) {
	   			directCartAdd( ArticleService.articleToCart);
	    	}
	    	else {
	    		ArticleService.referrer = 1;
	    		ArticleService.articleToCart = { article: $scope.entity, quantity: $scope.quantity};
	    		
	    		$state.go( 'root.login');
	    	}
    	} else {
   			$scope.showFeatureAlert( null);
   			ArticleService.articleToCart = {};
   		}
	};
    
	function directCartAdd( entity) {
		 /* Debug */ 
		 console.log( { article: entity.article, quantity: entity.quantity});
		 
		 ArticleService.addToCart( entity.article, entity.quantity)
						.then( function( response) {
							if( response.status == 200)
								openToast( 'info', entity.quantity + ' ' + entity.article.name, 'Ajout\xE8 au panier');
							ArticleService.articleToCart = {};
							
							/* Clear Selected ArticleFeature if any */
							angular.forEach( $scope.entity.features, function( articleFeature) {

								articleFeature.selectedFeatureValue = {};
								
								angular.forEach( articleFeature.values, function( featVal) {
									featVal.isSelected = false;
								});
							});
							/* Plays Success Sound */
							SoundService.success();
						}, function( response) {
							console.error( response);
							if( response.status == 400) {
								angular.forEach( response.data.errors, function( error) {
									if( error.code == "Full" && error.field == "cart")
										openToast( 'error', 'Votre panier est rempli. Supprimez un article ou commandez en un.', 'Panier rempli');
								});
								ArticleService.articleToCart = {};
								/* Danger SOund */
								SoundService.danger();
							}
						});
	}
	
	$scope.orderArticle = function() {
		/* TODO:
		 * Optim
		 */
		if( $scope.isValidForCart()) {
			ArticleService.articlesToOrder = [ { article: $scope.entity, quantity: $scope.quantity} ];
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
			else {
				ArticleService.referrer = 2;
				
				$state.go( 'root.login');
			}
		}
		else {
			ArticleService.articlesToOrder = [];
			$scope.showFeatureAlert( null);
		}
	};
    
    /* Quantity Widget */
    $scope.incQuantity = function() {
    	$scope.quantity++;
    };
    
    $scope.decQuantity = function() {
    	if( $scope.quantity > 1)
    		$scope.quantity--;
    };
    /* End Quantity Widget */
    
    /*
     * TODO
     * Dry that stuff
     */
    
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
    
    function DialogController( $scope, $mdDialog, ArticleService, $stateParams) {
	    $scope.entity = ArticleService.articleToCart;
    	
	    $scope.hide = function() {
	      $mdDialog.hide();
	    };
	    $scope.cancel = function() {
	      $mdDialog.cancel();
	    };
	    $scope.answer = function( response) {
	      $mdDialog.hide( response);
	    };
    };
    /* End Add To Cart Dialog Config */
    
    $scope.isValidForCart = function() {
    	/* 
    	 * Check if at least one feature value is selected for 
    	 * each feature
    	 */
    	var isValid = true;
    	
    	if( $scope.entity.features == undefined || $scope.entity.features ==null)
    		return false;
    	
    	if( $scope.entity.features != undefined) {
    		var isFeatureValidated = [];
    		
    		for( i = 0; i < $scope.entity.features.length; ++i) {
    			isFeatureValidated.push( false);
    		}
    		
    		angular.forEach( $scope.entity.features, function( articleFeature, index) {
    			var isFeatureValueSelected = false;
    			
    			angular.forEach( articleFeature.values, function( artFeatValue) {
    				if( artFeatValue.value == articleFeature.selectedFeatureValue.value) {
    					isFeatureValueSelected = true;
    					artFeatValue.isSelected = true;
    				}
    			});

    			if( isFeatureValueSelected)
    				isFeatureValidated[index] = true;
    		});
    		
    		var isFullyValidated = true;
    		for( i = 0; i < isFeatureValidated.length; ++i) {
    			if( !isFeatureValidated[i])
    				isFullyValidated = false;
    		}
    		
    		if( !isFullyValidated)
    			isValid = false;
    	}
    	
    	return isValid;
    }
    
    /* In case of redirection to this page from Login */
    if( ArticleService.referrer == 1) {
    	ArticleService.referrer = 0;
    	directCartAdd( ArticleService.articleToCart);
    }
    if( ArticleService.referrer == 2) {
    	ArticleService.referrer = 0;
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
}]);