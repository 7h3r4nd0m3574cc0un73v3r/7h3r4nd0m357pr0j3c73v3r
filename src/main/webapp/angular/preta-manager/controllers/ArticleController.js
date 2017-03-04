'use strict';

App.controller( 'ArticleController', [ '$state', '$stateParams', '$scope', '$rootScope',
                                       '$uibModal', 'EShopService', 'ArticleService',
                                       'categories',
                                     function( $state, $stateParams, $scope, $rootScope,
                                    		 $uibModal, EShopService, ArticleService,
                                    		 categories)
{	
	/* Load Active EShop */
	if( $stateParams.eShopId != undefined) {
		EShopService.loadEntity( $stateParams.eShopId)
					.then( function( response) {
						$scope.activeEShop = response;
						
						/* Compute Profile Completion */
						$scope.activeEShop.profileCompletion = EShopService.computeProfileCompletion( $scope.activeEShop);

						/* Load Current Shop Sub */
						EShopService.loadCurrentShopSub( $scope.activeEShop.id)
									.then( function( response) {
										$scope.activeEShop.currentShopSub = response;
									}, function( response) {
										console.error( response);
										$scope.activeEShop.currentShopSub = null;
									});
						
						EShopService.loadArticlesCount( $scope.activeEShop.id)
									.then( function( response) {
										$scope.activeEShop.articlesCount = response;
									}, function( response) {
										console.error( response);
									});
						
						/*if( $scope.activeEShop.currentShopSub == null || $scope.activeEShop.profileCompletion < 100) {
							$state.go( 'root.error-403');
						}*/
						
					}, function( response) {
						console.error( response);
					});
	}
	
	/* Loading Mutexes */
	$scope.isListLoading = true;
	$scope.isEntityLoading = true;
	
	/* Handle PagedListJSON */
	$scope.entities = [];
	$scope.entity = {};
	/* For New Articles */
	$scope.categories = categories;
	/* End Handle PagedListJSON */
	
	/* Pagination Settings */
	$scope.pagination = { currentPage: $stateParams.articlesPage == undefined ? 1 : $stateParams.articlesPage,
						  pageSize: $stateParams.articlesPagesize == undefined ? 12 : $stateParams.articlesPagesize,
					      pagesNumber: null,
					      itemsNumber: null
						};
	/* End Pagination Settings */
    
	/* Tmp Receiver for Articles Features MD Chips */
	$scope.dummy = { featureLabel: "", articleFeatures: [] };
	
	/* Load Entities */
	$scope.loadEntities = function( id, page, pageSize) {
		ArticleService.loadEntitiesByEShop( id, page, pageSize)
					  .then( function( response) {
							if( response.entities == undefined){
								$scope.isListLoading = false;
								return;
							}
							$scope.pagination.pagesNumber = response.pagesNumber;
							$scope.pagination.itemsNumber = response.itemsNumber;
							$scope.entities = response.entities;
			
							angular.forEach( $scope.entities, function( entity) {
								ArticleService.loadDefaultPicture( entity.id)
									   .then( function( response) {
										   entity.defaultPicture = response;
									   }, function( response) {
										   console.error( response);
									   });
							})	
							
							$scope.isListLoading = false;
							
					  }, function( response) {
						  $scope.isListLoading = false;
						  console.error( response);
					  });
	}
	
	$scope.loadEntities( $stateParams.eShopId, $scope.pagination.currentPage, $scope.pagination.pageSize);
	
	if( $stateParams.id != undefined ) {
		/* Load Entity */
		ArticleService.loadEntity( $stateParams.id)
					 .then( function( response) {
						 if( response == undefined){
								$scope.isLoading = false;
								return;
						 }
						 
						 $scope.entity = response;
						 
						 /* TODO
						  * Load Categories
						  */
						 
						 ArticleService.loadArticleFeatures( $scope.entity.id, 1, 0)
						 			   .then( function( response) {
						 				   $scope.entity.features = response.entities;
						 				   
						 				   /* Shape ArticleFeatures for Edition */
						 				   if( $state.current.name == 'root.e-shops.show.articles.update') {
						 					   angular.forEach( $scope.entity.features, function( articleFeature) {
						 						   var featureValues = [];
						 						   angular.forEach( articleFeature.values, function( featureValue) {
						 							  featureValues.push( featureValue.value); 
						 						   });
						 						   
							 					   $scope.dummy.articleFeatures.push( { feature: articleFeature.feature,
							 						   									unparsedValues: featureValues });
						 					   });
						 					   
						 					   /* Load categories */
						 					  ArticleService.loadMSTFormatedCategories( $stateParams.id)
															  .then( function( selectedCategories) {
																  $scope.entity.selectedCategories = selectedCategories;
															  }, function( errorResponse) {
																  console.error( errorResponse);
															  });
						 				   }
						 			   }, function( response) {
						 				   console.error( response);
						 				   $scope.isLoading = false;
						 			   });
						 
						 ArticleService.loadPictures( $scope.entity.id, 1, 0)
						 			   .then( function( response) {
						 				   $scope.entity.pictures = response.entities;
						 				   $scope.isLoading = false;
						 			   }, function( response) {
						 				   console.error( response);
						 				   $scope.isLoading = false;
						 			   });
						 
					 }, function( response) {
						 $scope.isLoading = false;
					 });
	}
	
	/* Page Change */
	$scope.changePage = function() {
			
			$state.go( $state.current.name, { articlesPage: $scope.pagination.currentPage, articlesPageSize: $scope.pagination.pageSize }, { notify: false});
			
			$scope.isListLoading = true;
			ArticleService.loadEntitiesByEShop( $stateParams.eShopId, $scope.pagination.currentPage,
					$scope.pagination.pageSize)
							.then( function( response) {
								$scope.pagination.pagesNumber = response.pagesNumber;
								$scope.pagination.itemsNumber = response.itemsNumber;
								$scope.entities = response.entities;
								$stateParams.articlesPage = $scope.pagination.currentPage;
							
								if( response.entities == undefined){
									$scope.isListLoading = false;
									return;
								}
								
								$scope.isListLoading = false;
								
							}, function( response) {
							  $scope.isListLoading = false;
							  console.error( response);
							});
	}
	
    /*Picture widget Config */
	$scope.selectPicture = function ( index) {
	  var fileInput = document.getElementById('uploadFile'+index);
	  fileInput.click();
	};
	/*End Picture widget Config */
	
	/* File Upload config */
	$scope.file0;
	$scope.file1;
	$scope.file2;
	$scope.file3;
	if( $scope.entity != undefined)  {
		angular.forEach( $scope.entity.pictures, function( picture, key) {
			var data;
			ArticleService.fetchPicture( picture.id)
						.then( function( response) {
							data = response;
							switch( key) {
								case 0: {
									$scope.file0 = data;
									break;
								}
								case 1: {
									$scope.file1 = data;
									break;
								}
								case 2: {
									$scope.file2 = data;
									break;
								}
								case 3: {
									$scope.file3 = data;
									break;
								}
							}
						}, function( response) {
							console.error( response);
						});
		});
	};
	/* End File Upload Config */
	
	$scope.form = { isDisplayed: true, isDiscounted: false, selectedCategories: categories, keywords: [] };
	
	/*
	if( $scope.entity != undefined) {
		angular.forEach( $scope.entity.features, function( arFeat) {
			var featVals = "";
			angular.forEach( arFeat.featureValues, function( featVal, index) {
				featVals += featVal.value;
				if( arFeat.featureValues[index + 1])
					featVals += ",";
			});
			$scope.dummy.articleFeatures.push( { feature: arFeat.feature, unparsedValues: featVals});
		});
	}
	*/
	
	$scope.form.features = [];
	$scope.formErrors = { conflict: false};
	
	/* Add form */
	$scope.addArticleFeature = function() {
		var isFound = false;
		angular.forEach( $scope.dummy.articleFeatures, function( value) {
			if( value.feature.label == $scope.dummy.featureLabel)
				isFound = true;
		});
		
		if( $scope.dummy.featureLabel != "" && $scope.dummy.featureLabel != null
			&& $scope.dummy.featureLabel != undefined && !isFound) {
			$scope.dummy.articleFeatures.push( { feature: { label: $scope.dummy.featureLabel }, unparsedValues: []});
			$scope.dummy.featureLabel = null;
		}
	};
	
	$scope.removeArticleFeature = function( index) {
		$scope.dummy.articleFeatures.splice( index, 1);
	};
	/* End Add Form */
	
	/* Edit form */
	$scope.removeLastFeatureEntity = function() {
		$scope.entity.features.splice( $scope.entity.articleFeatures.length - 1, 1);
	};
	
	$scope.addArticleFeatureEntity = function() {
		$scope.entity.features.push( { feature: { label: ""}, value: ""});
	};
	/* End Edit Form */
	
	if( $state.current.name == 'root.e-shops.show.articles.new') {
		$scope.isEntityLoading = false;
	}
	
	$scope.addEntity = function( file0, file1, file2, file3) {
		$scope.isEntityLoading = true;
		console.log( $scope.form);
		/* Rearrange Article Features */
		angular.forEach( $scope.dummy.articleFeatures, function( value) {
			var tmp = { feature: { label: value.feature.label }, values: []};
			
			angular.forEach( value.unparsedValues, function( featureValue) {
				tmp.values.push( { id: null, value: featureValue });
			});
			
			$scope.form.features.push( tmp);
		});
		
		ArticleService.addEntity( $scope.form, EShopService.activeEShop.id, file0, file1, file2, file3)
						.then( function( response) {
							$scope.entity = {};
							$state.go( 'root.e-shops.show.articles', null, { reload: true} );
						}, function( errResponse) {
							console.error( errResponse);
							$scope.isEntityLoading = false;
						});
	};
	
	$scope.getDefaultPic = function( id) {
		return ArticleService.getDefaultPic( id)
							.then( function( response) {
								return response;
							}, function( response) {
								console.error( response);
							});
	};
	
	/* Update Entity */
	$scope.updateEntity = function( file0, file1, file2, file3) {
		$scope.entity.eShop = EShopService.activeEShop;
		$scope.entity.features =[];
		
		/* Rearrange Article Features */
		angular.forEach( $scope.dummy.articleFeatures, function( value) {
			var tmp = { feature: { label: value.feature.label }, values: []};
			
			angular.forEach( value.unparsedValues, function( featureValue) {
				tmp.values.push( { id: null, value: featureValue });
			});
			
			$scope.entity.features.push( tmp);
		});
		
		ArticleService.updateEntity( $scope.entity, file0, file1, file2, file3)
						.then( function( response) {
							$scope.entity = {};
							$state.go( 'root.e-shops.show.articles', null, { reload: true} );
						}, function( errResponse) {
							console.error( errResponse);
						});
	};
	
	$scope.deleteEntity= function() {
		ArticleService.deleteEntity( $scope.entity.id)
						.then( function( response) {
							$scope.loadEntities( $stateParams.eShopId, $scope.pagination.currentPage, $scope.pagination.pageSize);
							
							$scope.entity = {};
							$scope.uibModalInstance.close();
						}, function( errResponse) {
							console.error( errResponse);
						});
	};
	
	$scope.resetEntity = function() {
		$scope.form = { isDisplayed: true, isDiscounted: false, selectedCategories: categories, features: []};
		$scope.formErrors = { conflict: false};
		$scope.entity = entity;
	}
	
	$scope.removePicture = function( id) {
		ArticleService.removePicture( $scope.entity.id, id)
						.then( function( response) {
							console.log( response);
							$scope.updateEntity( $scope.entity.id);
						}, function( response) {
							console.error( response);
						});
	};
	
	$scope.reloadEntity = function( id) {
		ArticleService.loadEntity( id)
								.then( function( response) {
									var tmp = response;
									ArticleService.loadMultiTreeSelectCategories( id)
												  .then( function( selectedCategories) {
													  console.log( "Selected categories");
													  tmp.selectedCategories = selectedCategories;
													  $scope.entity = tmp;
												  }, function( errorResponse) {
													  console.error( errorResponse);
												  });
								}, function( response) {
									console.error( response);
								});
	}
	
	/*Modal Config */
	$scope.uibModalInstance = {};
    $scope.open = function (page, id, size) {
    	$scope.entity = { id: id};
    	
        $uibModal.open({
          animation: true,
          templateUrl: page,
          controller: 'ArticleController',
          size: size,
          resolve: {
            items: function () {
              return $scope.items;
            },
            entity: function() { return { id: id};}
          }
        });
      };
      
  	$scope.showDeleteModal = function( id) {
		$scope.entity = { id: id};
		 return $scope.uibModalInstance = 
	        $uibModal.open({
	          animation: true,
	          scope: $scope,
	          templateUrl: 'angular/common/components/modals/deleteModal.html',
	        });
	};
	
	/* End Modal Config */
}]);