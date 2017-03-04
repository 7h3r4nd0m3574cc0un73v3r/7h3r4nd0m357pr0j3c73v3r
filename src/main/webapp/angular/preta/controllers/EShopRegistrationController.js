'use strict';

App.controller( 'EShopRegistrationController', [ '$scope', '$state', 'RegistrationService', 'ToastService', 'SoundService', function( $scope, $state, RegistrationService, ToastService, SoundService) {
				  /* Init variables */
				  $scope.entity = { eshopName: null, email: null, mobile: null, username: null, password: null,
						  			confirmPassword: null, userEmail: null, userMobile: null};
				  $scope.formErrors;
				  
				  /* Tools for Verif */
				  function initFormErrors() {
					  $scope.formErrors = {
						  userEmail: {
							  conflict: false,
						  },
						  username: {
							  conflict: false
						  },
						  password: {
							  tooShort: false,
							  mismatch: false
						  },
						  userMobile: {
							  conflict: false
						  },
						  eshopName: {
							  conflict: false
						  }
					  };
				  }
				  
				  /* Varification */
				  function isValid() {
					  initFormErrors();
					  console.log( "Vaidation called");
					  /* Level 2 Checks */
					  
					  console.log( "Attempting level 2");
					  
					  var isLevelTwoPassed = true;
					  
					  if( $scope.entity.password != $scope.entity.confirmPassword) {
						  $scope.formErrors.password.mismatch = true;
						  $scope.form.password.$setValidity("password", false);
						  $scope.form.password.$setValidity("confirmPassword", false);
						  isLevelTwoPassed = false;
					  }
						  
					  /*
					   * TODO
					   * Add EMail regexp validation
					   */
					  if( !isLevelTwoPassed)
						  return false;
					  
					  return true;
				  }
				  
				  /* Register */
				  $scope.addEntity = function() {
					  if( isValid()) {
						  RegistrationService.addEShop( $scope.entity)
			  				.then( function( response) {
			  					$state.go( 'root.confirmation');
			  				}, function( response) {
			  					initFormErrors();
			  					if( response.status == 400) {
			  						if( response.data.code == 'InvalidEntity') {
			  							angular.forEach( response.data.errors, function( error) {
				  							if( error.field == 'username' && error.code == 'Conflict')
				  								$scope.formErrors.username.conflict = true;
				  							if( error.field == 'userEmail' && error.code == 'Conflict')
				  								$scope.formErrors.userEmail.conflict = true;
				  							if( error.field == 'eshopName' && error.code == 'Conflict')
				  								$scope.formErrors.eshopName.conflict = true;
				  							if( error.field == 'userMobile') {
				  								if( error.code == 'Conflict')
				  									$scope.formErrors.mobile.conflict = true;
				  								if( error.code == 'Invalid')
				  									$scope.formErrors.mobile.invalid = true;
				  							}
				  						});
			  							ToastService.clearToasts();
				  						ToastService.openToast( 'error', 'Une erreur est survenue lors de l\'enregistrement. Merci de v\xE9rifier les informations saisies.', 'Erreur')
				  						SoundService.danger();
			  						}
			  					}
			  					console.error( response);
			  				});
					  } else {
	  						ToastService.openToast( 'error', 'Une erreur est survenue lors de l\'enregistrement. Merci de v\xE9rifier les informations saisies.', 'Erreur')
	  						SoundService.danger();
					  }
					  console.log( $scope.formErrors);
				  }
			  }]);