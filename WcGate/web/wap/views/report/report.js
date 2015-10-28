angular.module('App')
.controller('ReportCtrl', function ($scope, $filter, $http, $state, $stateParams, $ionicLoading,UserCredentials ,Constants) {

  $scope.changeReport = function () {
    $state.go('tabs.report', { index: $scope.reportIndex.value });
  };

  $scope.reportList = {
    lastUpdate: null,
    list: []
  };

  $scope.loadData = function(refresh) {
	  $ionicLoading.show();
	  $http.get(Constants.baseUrl+'reports',{ timeout: 8000 }).success(function (reports) {
		$scope.reportList.list=[];
	    angular.forEach(reports, function (report, index) {
	    	console.log(report.date);
	    	$scope.reportList.list.push(report);
	    });
	    if(refresh){
	    	$scope.reportList.lastUpdate=Date.now();
	    }
	    
	    $ionicLoading.hide();
	    if(refresh){
	    	$ionicLoading.show({
	    		template: '已刷新数据 '+$filter('date')(Date.now(), "HH:mm:ss"),
	    		duration: 1800    //显示1.8s
	    	});
	    }

	    $scope.errorMsg =null;
	  }).error(function (err) {
		    $ionicLoading.show({
		        template: '无法获取数据，请稍后再试！',
		        duration: 3000
		      });
		    $scope.errorMsg = '无法获取数据，请稍后再试！';
	     }).finally(function () {
	         $scope.$broadcast('scroll.refreshComplete');
	     });
  }
  
  $scope.detail = function (reportId) {
	  $state.go('tabs.reportDetail', { id: reportId });
  }
  
  $scope.refreshed = function(){
	  return ($scope.reportList.lastUpdate != null);
  }
  
  $scope.loadData();

});