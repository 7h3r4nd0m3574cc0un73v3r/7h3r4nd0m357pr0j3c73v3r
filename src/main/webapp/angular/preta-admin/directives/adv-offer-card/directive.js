/**
 * @author v.lugovksy
 * created on 16.12.2015
 */
(function () {
  'use strict';

  angular.module('adminPretaApp')
      .directive('advOfferCard', advOfferCard);

  /** @ngInject */
  function advOfferCard() {
	  return {
	      restrict: 'E',
	      scope: {
	        entity: '='
	      },
	      templateUrl: 'angular/preta-admin/directives/adv-offer-card/template.html'
	    };
  }
})();
