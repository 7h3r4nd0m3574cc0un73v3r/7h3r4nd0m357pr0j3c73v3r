'use strict';

App.controller( 'CategoryController', [ '$state', '$scope', '$uibModal', 'CategoryService', 'entities',function( $state, $scope, $uibModal, CategoryService, entities)
{	
	/*Modal Config */
	$scope.uibModalInstance = {};
    $scope.open = function (page, id, size) {
    	$scope.entity = { id: id};
    	
        $uibModal.open({
          animation: true,
          templateUrl: page,
          controller: 'CategoryController',
          size: size,
          resolve: {
            items: function () {
              return $scope.items;
            },
            entity: function() { return { id: id};}
          }
        });
      };
	/* End Modal Config */
	
    /* JS Tree Config */
      $scope.ignoreChanges = false;
      $scope.basicConfig = {
      core: {
    	multiple: false,
    	check_callback: true,
    	worker: true
      },
      'types': {
    	  'default': {
    		  'icon': 'ion-ios-folder'
    	 }
      },
      'plugins': ['types'],
      'version': 1
    };
    /* End JS Tree Config */
    
    /* Entities loading */
	$scope.entities = entities;
	angular.forEach( $scope.entities, function( entity) {
		entity.state = { opened: true };
	});
	
	$scope.entities.push({
		id: '0',
		parent: '#',
		text: 'Cat\xE9gories',
		state: {
			opened: true
		}
	});
	
	$scope.form = { };
	
	$scope.loadTreeData = function() {
		CategoryService.loadTreeData()
						.then( function( response) {
							$scope.entities = response;
							angular.forEach( $scope.entities, function( entity) {
								entity.state = { opened: true };
							});
							$scope.entities.push({
								id: '0',
								parent: '#',
								text: 'Cat\xE8gories',
								type: 'folder',
								state: {
									opened: true
								}
							});
							$scope.basicConfig.version++;
						}, function( errResponse) {
							console.error( errResponse);
						});
	};
	
	/* Add Category */
    $scope.addEntity = function () {
        var selected = this.basicTree.jstree(true).get_selected()[0];
        if (selected) {
      	  CategoryService.addEntity( { label: $scope.form.label, parent: selected, description: $scope.form.description })
      	  				.then( function( response) {
      	  					console.log( response);
      	  					$scope.loadTreeData();
      	  					$scope.basicConfig.version++;
      	  				}, function( response) {
      	  					console.error( response);
      	  				});
        }
        else {
      	  	CategoryService.addEntity( { label: $scope.form.label, description: $scope.form.description })
      	  				.then( function( response) {
      	  					console.log( response);
      	  					$scope.loadTreeData();
      	  					/* Update the tree */
      	  					$scope.basicConfig.version++;
      	  				}, function( response) {
      	  					console.error( response);
      	  				});
        }
        $scope.form.label = "";
        $scope.form.description = "";
    };
  	
  	$scope.removeEntity = function() {
  		var selected = this.basicTree.jstree(true).get_selected()[0];
  		console.log( selected);
  		CategoryService.deleteEntity( selected)
  					   .then( function( response) {
  						   console.log( response);
  						   $scope.loadTreeData();
  						   $scope.basicConfig.version++;
  					   }, function( response) {
  						   console.error( response);
  					   });
  	}
  	
  	$scope.changedCB = function( ev, obj) {
  		
  	};
}]);