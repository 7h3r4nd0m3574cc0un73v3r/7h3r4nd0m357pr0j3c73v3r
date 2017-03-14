App.controller( 'PWRecoveryController',[ '$rootScopr', '$scope', '$mdDialog', 'UserService', '$state', function( $rootScopr, $scope, $mdDialog, UserService, $state) {
	
	if( $rootScope.loggedUser != null) {
		$state.go( 'root.errors.401');
	}
	
	/* Loading Mutexes */
	$scope.isLoading = false;
	$scope.done = false;
	
	/* Init Var */
	$scope.form = { email: null, captcha: null};
	initFormErrors();
	
	$scope.requestPassword = function() {
		$scope.isLoading = true;
		initFormErrors();
			UserService.requestPassword( $scope.form)
						.then( function( response) {
							$scope.done = true;
							$scope.isLoading = false;
						}, function( response) {
							console.error( response);
							if( response.status == 400) {
								angular.forEach( response.data.errors, function( error) {
									if( error.code == "NotFound")
										$scope.formErrors.notFound = true;
								})
								
								if( $scope.formErrors.notFound)
									$scope.showAlertNotFound( null);
							}
							
							if( response.status == 500) {
								$scope.formErrors.generic = true;
								/* Do Wathever you do */
							}
							
							$scope.isLoading = false;
						});
	}
	
    /* Alert Dialog */
    $scope.showAlertNotFound = function(ev) {
        $mdDialog.show(
          $mdDialog.alert()
            .parent(angular.element(document.body))
            .clickOutsideToClose( false)
            .title('Adresse Email Introuvable')
            .textContent('Désolé, cette adresse e-mail n\'est pas enregistrée dans notre système. Veuillez renseigner l\'adresse e-mail utilisée lors de votre inscription.')
            .ariaLabel('E Mail Introuvable')
            .ok('Ok')
            .targetEvent(ev)
        );
      };
    
      function initFormErrors() {
    	  $scope.formErrors = { notFound: false, generic: false};
      }
    /* End Alert Dialog */
}]);