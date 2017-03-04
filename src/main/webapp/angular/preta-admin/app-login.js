'use strict';

var App = angular.module( "adminLoginApp", [
                                       'ui.router'
                                      ]);
App.config( routeConfig);

function routeConfig( $stateProvider, $urlRouterProvider) {
	$urlRouterProvider.otherwise('');
	
	$stateProvider
		.state('root', {
			url: '',
			controller: "LoginController"
		});
}
