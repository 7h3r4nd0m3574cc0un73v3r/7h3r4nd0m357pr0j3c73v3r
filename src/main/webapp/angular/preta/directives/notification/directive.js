(function () {
  'use strict';

  angular.module('pretaApp')
      .directive('notification', notificationDirective);

  /** @ngInject */
  function notificationDirective() {
	  return {
	      restrict: 'E',
	      scope: {
		        entity: '='
	      },
	      templateUrl: 'angular/preta/directives/notification/template.html',
    	  controller: [ 'NotificationService', '$scope', '$state', function( NotificationService, $scope, $state) {
    		  /* Read Notif */
    		  $scope.read = function() {
    			  /* Read notification */
    			  if( !$scope.entity.isRead) 
    				  NotificationService.read( $scope.entity.id);
    			  
    			  switch ($scope.entity.nType) {
					case 'ALTERED_PAYMENT':
						switch ($scope.entity.entType) {
						case 'ORDER':
							$state.go( 'root.orders.show', { id: $scope.entity.entAbsId})
							break;
						default:
							break;
						}
						break;
					case 'APPROVED':
						$state.go( 'root.profile');
						break;
					case 'DELIVERING_ORDER':
						switch ($scope.entity.entType) {
						case 'ORDER':
							$state.go( 'root.orders.show', { id: $scope.entity.entAbsId})
							break;
						default:
							break;
						}
						
						break;
					case 'PENDING_RATING':
						$state.go( 'root.ratings');
						
						break;
					case 'APPROVED_UPGRADE_REQUEST':
						
						var homeUrl =  $location.$$protocol + '://' + $location.$$host;
						if( $location.host() == 'localhost')
							homeUrl += ':' + $location.$$port.toString + '/preta';
						else
							homeUrl += $location.$$host;
						
						console.log( homeUr);
						window.location = homeUrl + '/preta-manager';
						break;
					default:
						break;
    			  }
    		  };
    	  }]
	    };
  }
})();
