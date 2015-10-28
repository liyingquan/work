angular.module('App', ['ionic', 'highcharts-ng'])

.config(function ($stateProvider, $urlRouterProvider, $ionicConfigProvider) {
	
  $ionicConfigProvider.tabs.style('striped').position('bottom');
  
  $stateProvider
    .state('tabs', {
      url: '/tabs',
      abstract: true,
      templateUrl: 'views/tabs/tabs.html'
    })
    .state('tabs.home', {
      url: '/home',
      views: {
        'home-tab': {
          templateUrl: 'views/home/home.html',
          controller: 'HomeCtrl'
        }
      }
    })
    .state('tabs.report', {
      url: '/report',
      views: {
        'report-tab': {
          templateUrl: 'views/report/report.html',
          controller: 'ReportCtrl'
        }
      }
    })
    .state('tabs.reportDetail', {
      url: '/reportDetail?reportId',
      views: {
        'report-tab': {
          templateUrl: 'views/detail/detail.html',
          controller: 'ReportDetailCtrl'
        }
      }
    })
    .state('tabs.currencies', {
      url: '/currencies',
      views: {
        'currencies-tab': {
          templateUrl: 'views/currencies/currencies.html',
          controller: 'CurrenciesController'
        }
      }
    })
    ;

  $urlRouterProvider.otherwise('/tabs/home');
})

.run(function($ionicPlatform) {
  $ionicPlatform.ready(function() {
    if(window.cordova && window.cordova.plugins.Keyboard) {
      cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
    }
    if(window.StatusBar) {
      StatusBar.styleDefault();
    }
  });
})

.factory('UserCredentials', function () {
  return { userId: 'testUser1', token: 'asdfghjkl1234'};
})

.factory('Constants', function () {
  return { baseUrl: 'http://192.168.1.169:8888/merdp/rest/merdp/'};
});
