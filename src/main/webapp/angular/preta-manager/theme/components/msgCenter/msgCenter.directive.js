  'use strict';

   App.directive('msgCenter', msgCenter);

  /** @ngInject */
  function msgCenter() {
    return {
      restrict: 'E',
      templateUrl: 'angular/preta-manager/theme/components/msgCenter/msgCenter.html',
      controller: 'MsgCenterCtrl'
    };
  }