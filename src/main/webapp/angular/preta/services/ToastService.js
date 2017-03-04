'use strict';

App.factory( 'ToastService', [ 'toastr', 'toastrConfig', function( toastr, toastrConfig) {
	/* Toastr Config */
	var defaultConfig = angular.copy( toastrConfig);
	var openedToasts = [];
    var options = {
		      autoDismiss: false,
		      positionClass: 'toast-top-right',
		      type: 'info',
		      timeOut: '5000',
		      extendedTimeOut: '2000',
		      allowHtml: false,
		      closeButton: true,
		      tapToDismiss: true,
		      progressBar: false,
		      newestOnTop: true,
		      maxOpened: 0,
		      preventDuplicates: false,
		      preventOpenDuplicates: false,
    };
    
    angular.extend(toastrConfig, options);
    
    return {
        openToast: function( type, message, title) {
            openedToasts.push( toastr[ type]( message, title));
        },
        clearToasts: function() {
        	openedToasts = [];
        }
    }
}]);

