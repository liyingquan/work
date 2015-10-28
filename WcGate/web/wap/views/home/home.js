angular.module('App')
.controller('HomeCtrl', function ($scope, $http, $ionicPopover) {
  
  $ionicPopover.fromTemplateUrl('views/home/help-popover.html', {
    scope: $scope,
  }).then(function (popover) {
    $scope.popover = popover;
  });
  $scope.openHelp = function($event) {
    $scope.popover.show($event);
  };
  $scope.$on('$destroy', function() {
    $scope.popover.remove();
  });

  $scope.load = function () {
	  console.log('home.load');
	  
	  $scope.homeImage="views/home/home-tj.jpg"; 
	  
//    $http.get('https://api.bitcoinaverage.com/ticker/all').success(function (tickers) {
//      angular.forEach($scope.currencies, function (currency) {
//        currency.ticker = tickers[currency.code];
//        currency.ticker.timestamp = new Date(currency.ticker.timestamp);
//      });
//    }).finally(function () {
//      $scope.$broadcast('scroll.refreshComplete');
//    });
  };

  $scope.load();
});
