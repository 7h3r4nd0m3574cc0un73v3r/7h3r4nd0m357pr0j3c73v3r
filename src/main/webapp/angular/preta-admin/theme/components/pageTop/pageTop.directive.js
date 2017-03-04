(function () {
  'use strict';

  angular.module('adminPretaApp')
      .directive('pageTop', pageTop);

  /** @ngInject */
  function pageTop() {
    return {
      restrict: 'E',
      templateUrl: 'angular/preta-admin/theme/components/pageTop/pageTop.html'
    };
  }

})();