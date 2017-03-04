App.factory( 'ConfigService', [ '$state', function( $state) {
	
	var ConfigService = {
		'loggedUser': {},
		'isAuthenticated': false
	};
	
	return ConfigService;
}]);