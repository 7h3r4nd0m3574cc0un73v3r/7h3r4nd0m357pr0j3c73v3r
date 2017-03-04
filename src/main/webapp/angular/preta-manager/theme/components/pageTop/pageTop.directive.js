(function () {
'use strict';

angular.module('managerPretaApp')
.directive('pageTop', pageTop);

/** @ngInject */
function pageTop() {
	return {
		restrict: 'E',
		templateUrl: 'angular/preta-manager/theme/components/pageTop/pageTop.html',
		controller: [ '$scope', 'UserService', '$location', function( $scope, UserService, $location) {
			console.log( $location);
			console.log( $location.$$protocol);
			console.log( $location.$$port);
			/* Var Config */
			var homeUrl =  $location.$$protocol + '://' + $location.$$host;
			if( $location.host() == 'localhost')
				homeUrl += ':' + $location.$$port.toString + '/preta';
			else
				homeUrl += $location.$$host;
			
			console.log( homeUr);
			
			/* Logout Function */
			$scope.logout = function() {
				UserService.logout()
						   .then( function( r) {
							   window.location = homeUrl;
						   }, function( r) {
							   console.error( r);
						   });
			}
			
			$scope.home = function() {
				window.location = homeUrl;
			}
		}]
	}
}})();