(function () {
  'use strict';

  angular.module('pretaApp')
      .directive('navbarTop', navbarTop);

  /** @ngInject */
  function navbarTop() {
	  return {
	      restrict: 'E',
	      templateUrl: 'angular/preta/directives/navbar-top/template.html',
	      controller: [ '$rootScope', '$scope', 'ArticleService', '$log', 'StompService', 'NotificationService',
	                    function( $rootScope, $scope, ArticleService, $log, StompService, NotificationService) {
	    	  /* Loading Mutexes */
	    	  $scope.areCartItemsLoading = true;
	    	  $scope.areNotificationsLoading = true;
	    	  
	    	  /* Init variables */
	    	  $scope.cartItems = [];
	    	  $scope.notifications = [];
	    	  $scope.lastCartItems = [];
	    	  $scope.cartItemsCount = 0;
	    	  $scope.notificationsCount = 0;
	    	  $scope.unreadNotificationsCount = 0;
	    	
	    	  /* Update What Needs to be updated */
	    	  $rootScope.$watch( 'loggedUser', function( newValue, oldValue) {
	  	    	if( newValue != null) {
		    		/* Call to loadEntities */
		  	    	$scope.loadCartItems( 1, 0, true);
		  	    	$scope.loadNotifications( 1, 5, 0, false, 1, true);
		  	    	
		  	    	/* Sub to WebSocket */
		  	    	StompService.connect()
		  	    				.then( function() {
			  	  					StompService.subscribe( '/user/topic/logged-user/cart-items',
			  	  							function( payload, headers, response) {
			  	  								$scope.lastCartItems = payload.entities.splice( 0, 5);
			  	  								$scope.cartItemsCount = payload.itemsNumber;
			  	  							});
		  	    				});
		  	    	
		  	    	StompService.connect()
				  				.then( function() {
					  					StompService.subscribe( '/user/topic/logged-user/buyer/new-notification',
					  							function( payload, headers, response) {
					  								console.log( "Handling Notification");
					  								$scope.notifications.unshift( payload);
					  								
					  								$scope.unreadNotificationsCount++;
					  								
					  								$scope.notificationsCount++;
					  								
					  								if( $scope.notifications.length == 6)
					  									$scope.notifications.splice( 5, 1);
					  								
					  								$scope.$apply();
					  							});
				  				});
	  	    	 }
	    	  });
	    	  
	    	/* cartItems Loading function */
	    	$scope.loadCartItems = function( page, pageSize, withLoading) {
	    		if( withLoading)
	    			$scope.areCartItemsLoading = true;
	    		
	    		ArticleService.loadCartContent( page, pageSize)
	    						.then( function( response) {
	    							if( response.entities == undefined) {
	    								$scope.areCartItemsLoading = false;
	    								return;
	    							}
	    							
	    							angular.forEach( response.entities, function( entity) {

    									/* Load Article pictures, containing the googleId */
    									ArticleService.loadPictures( entity.article.id, 1, 0)
    												  .then( function( r) {
    													  entity.article.pictures = r.entities;
    												  }, function( r) {
    													  console.error( r);
    												  });
    									
	    								var isFound = false;
	    								angular.forEach( $scope.cartItems, function( cartItem) {
	    									if( cartItem.id === entity.id) {
	    										isFound = true;
	    										return;
	    									}
	    								});
	    								
	    								if( !isFound) {
	    									$scope.cartItems.push( entity);
	    								}
	    							});
	    							
	    							$scope.lastCartItems = $scope.cartItems.splice( 0, 5); 
	    							/* Save to scope */
	    							$scope.cartItemsCount = response.itemsNumber;
	    							
	    							if( withLoading)
	    								$scope.areCartItemsLoading = false;
	    						}, function( errResponse) {						
	    							console.error( errResponse);
	    							if( withLoading)
	    								$scope.areCartItemsLoading = false;
	    						});
	    	};
	    	
	    	/* Load Notifications */
	    	$scope.loadNotifications = function( page, pageSize, status, orderByIdAsc, restrict, withLoading) {
	    		
	    		$log.debug( "Notificatios loaded");
	    		
	    		if( withLoading)
	    			$scope.areNotificationsLoading = true;
	    		
	    		NotificationService.loadEntities( page, pageSize, status, orderByIdAsc, restrict, withLoading)
	    						.then( function( response) {
	    							if( response.entities == undefined) {
	    								$scope.areNotificationsLoading = false;
	    								return;
	    							}
	    							
	    							$scope.notifications = response.entities;
	    							$scope.notificationsCount = $scope.notifications.length; 							
	    							
	    							/* Save to scope */
	    							angular.forEach( $scope.notifications, function( n) {
	    								if( !n.isRead)
	    									$scope.unreadNotificationsCount++;
	    							});
	    							
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
	    		var ids = [];
	    		angular.forEach( $scope.notifications, function( n) {
	    			if( !n.isRead)
	    				ids.push( n.id);
	    		});
	    		
	    		if( ids.length > 0)
	    			NotificationService.multipleRead( ids)
		    						   .then( function(r) {
		    							   $scope.unreadNotificationsCount = 0;
		    						   }, function( r) {
		    							   console.error( response);
		    						   });
	    	};
	    	
	      }]
	    };
  }
})();
