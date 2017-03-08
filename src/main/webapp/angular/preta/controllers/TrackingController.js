App.controller( 'TrackingController', [ '$rootScope', '$scope', '$state', '$http', '$mdDialog', 'UserService', 'ToastService', 'SoundService', 'StompService', '$log',
                                     function( $rootScope, $scope, $state, $http, $mdDialog, UserService, ToastService, SoundService, StompService, $log) {
	
	/* Vars */
	$scope.form = { packagedId: null };
	$scope.packageInfo = { logAgent: null, startDate: null, endDate: null, deliveryAddress: null, status: null}
	$scope.showInfo = false;
	
	$scope.loadStatus = function() {
		$scope.packageInfo = { logAgent: "EYAWU", startDate: "15 / 03 / 2017 08:30", endDate: "15 / 03 / 2017 18:30", deliveryAddress: "Quartier Jacuques Test Address", status: "Normal"};
		$scope.showInfo = true;
	}
}]);