App.controller( 'PWResetController',[ '$rootScope', '$scope', '$mdDialog', 'UserService', '$stateParams', '$state', function( $rootScope, $scope, $mdDialog, UserService, $stateParams, $state) {
	
	if( $rootScope.loggedUser != null) {
		$state.go( 'root.errors.401');
	}
	
	/* Loading Mutexes */
	$scope.isLoading = false;
	$scope.done = false;
	
	/* Init Var */
	$scope.form = { pw: null, pwConf: null};
	$scope.formErrors;
	initFormErrors();

	/* Reset PW */
	$scope.resetPassword = function() {
		if( isValid()) {
			$scope.isLoading = true;
			UserService.resetPassword( $scope.form, $stateParams.token)
						.then( function( response) {
							$scope.done = true;
							$scope.isLoading = false;
						}, function( response) {
							console.error( response);
							if( response.status == 400) {
								angular.forEach( response.data.errors, function( error) {
									if( error.code == "NotFound" && error.field == "token")
										$scope.formErrors.token = true;
									if( error.code == "Mismatch" && error.field == "password")
										$scope.formErrors.mismatch = true;
									if( error.code == "MinLength" && error.filed == "password")
										$scope.formErrors.minLength = true;
								});
								
								if( $scope.formErrors.token)
									$scope.showTokenAlert( null);
								if( $scope.formErrors.mismatch)
									$scope.showMismatchAlert( null);
							}
							
							if( response.status == 500) {
								$scope.formErrors.generic = true;
								/* Do Wathever you do */
							}
							
							$scope.isLoading = false;
						})
		}
		else {
			if( $scope.form.pw != null && $scope.form.pwConf != null) {
				if( $scope.formErrors.token) {
					showAlert( "Requête invalide", "La réinitialisation du mot de passe n'a pas été approuvée pour ce compte.",
							   "Requête invalide", null);
					initFormErrors();
				}
			}
		}
	}
	/*
	 * TODO
	 * Refactor
	 */
	
	/* Alerts */
	function showAlert( title, content, ariaLabel, ev) {
        $mdDialog.show(
          $mdDialog.alert()
            .parent(angular.element(document.body))
            .clickOutsideToClose( false)
            .title(title)
            .textContent( content)
            .ariaLabel( ariaLabel)
            .ok( "Ok")
            .targetEvent(ev)
        );
	}
	
	/* Validation */
	function isValid() {
		if( $stateParams.token == undefined) {
			$scope.formErrors.token.required = true;
			return false;
		}
		if( $scope.form.pw == null || $scope.form.pw == "") {
			$scope.formErrors.pw.required = true;
		}
		
		if( $scope.form.pw !== $scope.form.pwConf) {
			$scope.formErrors.pw.mismatch = true;
		}
		
		return true;
	}

	/* InitFormErrors */
    function initFormErrors() {
    	$scope.formErrors = {
    			token: {
    				required: false
    			},
    	    	password: {
    				mismatch: false
    			},
    			confirmPassword: {
    				mismatch: false
    			}
    	}
    }
}]);