'use strict';

App.controller( 'BuyerRegistrationController', [ '$scope', '$state', 'RegistrationService', 'ToastService', 'SoundService', function( $scope, $state, RegistrationService, ToastService, SoundService) {
				  /* Init variables */
				  $scope.entity = { userInfo: { email: null}, username: null, password: null, confirmPassword: null, mobile: null, captcha: null};
				  $scope.formErrors;
				  
				  /* Tools for Verif */
				  function initFormErrors() {
					  $scope.formErrors = {
						  email: {
							  required: false,
							  conflict: false,
							  invalid: false
						  },
						  username: {
							  required: false,
							  conflict: false
						  },
						  password: {
							  required: false,
							  tooShort: false,
							  mismatch: false
						  },
						  confirmPassword: {
							  required: false
						  },
						  mobile: {
							  required: false,
							  invalid: false,
							  conflict: false
						  },
						  captcha: {
							  invalid: false
						  }
					  };
				  }
				  
				  $scope.debugCaptcha = function() {
					  console.log( $scope.entity.captcha);
				  }
				  
				  /* Varification */
				  function isValid() {
					  initFormErrors();
					  console.log( "Vaidation called");
					  /* Lvl 1 Checks */
					  var isLevelOnePassed = true;
					  
					  if( $scope.entity.username == null) {
						  $scope.formErrors.username.required = true;
						  $scope.form.username.$setValidity("username", false);
						  isLevelOnePassed = false;
					  }
					  if( $scope.entity.password == null) {
						  $scope.formErrors.password.required = true;
						  $scope.form.password.$setValidity("password", false);
						  isLevelOnePassed = false;
					  }
					  if( $scope.entity.confirmPassword == null) {
						  $scope.formErrors.confirmPassword.required = true;
						  $scope.form.confirmPassword.$setValidity("confirmPassword", false);
						  isLevelOnePassed = false;
					  }
					  if( $scope.entity.userInfo.email == null) {
						  $scope.formErrors.email.required = true;
						  $scope.form.email.$setValidity("email", false);
						  isLevelOnePassed = false;
					  }
					  if( $scope.entity.mobile == null) {
						  $scope.formErrors.mobile.required = true;
						  $scope.form.mobile.$setValidity("mobile", false);
						  isLevelOnePassed = false;
					  }
					  
					  if( !isLevelOnePassed)
						  return false;
					  
					  console.log( "Is Level One passed");
					  console.log( isLevelOnePassed);
					  
					  /* Level 2 Checks */
					  
					  console.log( "Attempting level 2");
					  
					  var isLevelTwoPassed = true;
					  
					  if( $scope.entity.password.length < 8) {
						  $scope.formErrors.password.tooShort = true;
						  $scope.form.password.$setValidity("password", false);
						  isLevelTwoPassed = false;
					  }
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
						  RegistrationService.addBuyer( $scope.entity)
			  				.then( function( response) {
			  					$state.go( 'root.confirmation');
			  				}, function( response) {
			  					initFormErrors();
			  					if( response.status == 400) {
			  						if( response.data.code == 'InvalidEntity') {
			  							angular.forEach( response.data.errors, function( error) {
				  							if( error.field == 'username' && error.code == 'Conflict')
				  								$scope.formErrors.username.conflict = true;
				  							if( error.field == 'email' && error.code == 'Conflict')
				  								$scope.formErrors.email.conflict = true;
				  							if( error.field == 'mobile') {
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