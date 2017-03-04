App.factory( 'StompService', [ '$stomp', '$log', '$q', '$rootScope', function( $stomp, $log, $q, $rootScope) {
	/* Config */
	var baseUrl = '';
	var endpoint = '/websocket';
	var url = baseUrl + endpoint;
	
	$stomp.setDebug(function (args) {
      $log.debug(args)
    });
	
	var stompClient;
	$rootScope.subs = [];
    var wrappedSocket = {
        init: function () {
            return stompClient = $stomp.connect( url);
        },
        connect: function () {
	            if( !stompClient) {
	            	return this.init();
	            }
	            else {
	            	return stompClient;
	            }
        },
        disconnect: function() {
            stompClient.disconnect();
        },
/*        subscribe: function (destination) {
            var deferred = $q.defer();
            if (!stompClient) {
            	deferred.reject("STOMP client not created");
            	return deferred.promise;
            } else {
                return stompClient.subscribe(destination);
            }
            
        },*/
		subscribe: function( destination, callback, headers) {
			/*if( this.isAlreadySubscribed( destination)) {
				return this.getSub( destination);
			}
			else {*/
				var sub = $stomp.subscribe( destination, callback, headers);
				sub.destination = destination;
				$rootScope.subs.push( sub);
				return sub;	
			/*}*/
		},
		clearSubs: function() {
			$rootScope.subs = [];
		},
		loadSubs: function() {
			return $rootScope.subs;
		},
		isAlreadySubscribed: function( destination ) {
			var found = false;
			angular.forEach( $rootScope.subs, function( sub) {
				if( sub.destination == destination)
					found = true;
			});
			
			return found;
		},
		getSub: function( destination) {
			var lSub;
			angular.forEach( $rootScope.subs, function( sub) {
				if( sub.destination == destination)
					lSub = sub;
			});
			return lSub;
		}
    };
    return wrappedSocket;
}]);