/**
 * @author v.lugovksy
 * created on 16.12.2015
 */
(function () {
  'use strict';

  angular.module('adminPretaApp')
      .directive('shopSubCard', shopSubCard);

  /** @ngInject */
  function shopSubCard() {
	  return {
	      restrict: 'E',
	      scope: {
	        entity: '='
	      },
	      templateUrl: 'angular/preta-admin/directives/shop-sub-card/template.html'
	    };
  }
})();
