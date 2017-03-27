'use strict';

var App = angular.module( "managerPretaApp", [
                                              'ngAnimate',
                                              'ui.bootstrap',
                                              'ui.sortable',
                                              'ui.router',
                                              'toastr',
                                              /* 'ngTouch', */
                                              'smart-table',
                                              "xeditable",
                                              'ui.slimscroll',
                                              'ngJsTree',
                                              'angular-progress-button-styles',
                                              'ncy-angular-breadcrumb',
                                              'ngFileUpload',
                                              'multi-select-tree',
                                              'ngMaterial',
                                              'lfNgMdFileInput',
                                              'ngStomp',
                                              'ngAudio',
                                              'cgBusy',
                                              'BlurAdmin'
                                              ]);
App.config( routeConfig);

//Routing for PretaApp
/** @ngInject */
function routeConfig( $stateProvider, $urlRouterProvider, baSidebarServiceProvider) {
	$urlRouterProvider.otherwise('');

	$stateProvider
	;
}