'use strict';


  App.controller('MsgCenterCtrl', MsgCenterCtrl);

  function MsgCenterCtrl( $rootScope, $scope, $state, ArticleService, $log, StompService, NotificationService) {
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
  	    	$scope.loadNotifications( 1, 10, 0, false, 2, true);
  	    	StompService.connect()
		  				.then( function() {
			  					StompService.subscribe( '/user/topic/logged-user/manager/new-notification',
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
							$log.log( $scope.notifications);
							
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
	
	/* Notification fecth additional infos */
	function prepareNotification( entity) {
		
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
	
	$scope.read = function( key) {
		/* Retrieve specific notification */
		var entity = $scope.notifications[key];
		/* Mark Notification as read */
		NotificationService.read( entity.id); /*
						   .then( function( r) {
							   console.log( r);
						   }, function( r) {
							   console.error( r);
						   });*/
		
		/* Redirect to notification subject */
		switch (entity.nType) {
		case 'APPROVED':
			
			break;
		case 'ALTERED_PAYMENT':
			switch (entity.entType) {
			case 'ORDER':
				$state.go( 'root.orders.show', { id: entity.entAbsId });
				break;
			case 'SUB':
				
				break;
			case 'AD':
				
				break;
			default:
				break;
			}
			break;
		case 'DELIVERED_ORDER':
			$state.go( 'root.orders.show', { id: entity.entAbsId });
			break;
		case 'EXPIRED':
			switch ( entity.entType) {
			case 'AD':
				$state.go( 'root.adv-offers.show', { id: entity.entAbsId });
				break;
			case 'SUB':
				
				break;
			default:
				break;
			}
			break;
		case 'EXPIRING':
			switch ( entity.entType) {
			case 'AD':
				$state.go( 'root.adv-offers.show', { id: entity.entAbsId });
				break;
			case 'SUB':
				
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
	}
  }