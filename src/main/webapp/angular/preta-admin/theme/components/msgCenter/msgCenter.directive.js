  'use strict';

   App.directive('msgCenter', msgCenter);

  /** @ngInject */
  function msgCenter() {
    return {
      restrict: 'E',
      templateUrl: 'angular/preta-admin/theme/components/msgCenter/msgCenter.html',
      controller: [ '$rootScope', '$scope', 'ArticleService', '$log', 'StompService', 'NotificationService',
                   function( $rootScope, $scope, ArticleService, $log, StompService, NotificationService) {
    	  /* Loading Mutexes */
    	  $scope.areNotificationsLoading = true;
    	  
    	  /* Init variables */
    	  $scope.notifications = [];
    	  $scope.notificationsCount = 0;
    	  $scope.unreadNotificationsCount = 0;
    	
    	  /* Update What Needs to be updated */
    	  $rootScope.$watch( 'loggedUser', function( newValue, oldValue) {
  	    	if( newValue != null) {
	    		/* Call to loadEntities */
	  	    	$scope.loadNotifications( 1, 10, 0, false, 0, true);
	  	    	StompService.connect()
			  				.then( function() {
				  					StompService.subscribe( '/user/topic/logged-user/admin/new-notification',
				  							function( payload, headers, response) {
				  								$scope.notifications.unshift( payload);
				  								$scope.notificationsCount++;
				  							});
			  				});
  	    	 }
    	  });
    	
    	/* Load Notifications */
    	$scope.loadNotifications = function( page, pageSize, status, orderByIdAsc, restrict, withLoading) {
    		if( withLoading)
    			$scope.areNotificationsLoading = true;
    		
    		NotificationService.loadEntities( page, pageSize, status, orderByIdAsc, restrict, withLoading)
    						.then( function( response) {
    							if( response.entities == undefined) {
    								$scope.areNotificationsLoading = false;
    								return;
    							}
    							
    							angular.forEach( response.entities, function( n) {
    								if( !n.isRead)
    									$scope.unreadNotificationsCount++;
    							});
    							
    							$scope.notifications = response.entities;
    							
    							/* Save to scope */
    							$scope.notificationsCount = response.itemsNumber;
    							
    							if( withLoading)
    								$scope.areNotificationsLoading = false;
    						}, function( r) {		
    							console.error( r);
    							if( withLoading)
    								$scope.areNotificationsLoading = false;
    						});
    	};

    	/* Read Notifications */
    	$scope.readNotifications = function() {
    		/* Cheating */
    		$scope.unreadNotificationsCount = 0;
    		
    		var ids = [];
    		angular.forEach( $scope.notifications, function( n) {
    			if( !n.isRead)
    				ids.push( n.id);
    		});
    		
    		if( ids.length > 0)
    			NotificationService.multipleRead( ids)
	    						   .then( function(r) {
	    							   /* $scope.unreadNotificationsCount = 0; */
	    							   /* Check the cheat */
	    						   }, function( r) {
	    							   console.error( response);
	    						   });
    	};
      }]
    };
  }