angular.module('App')
.controller('ReportDetailCtrl', function ($scope , $http, $stateParams, $state, $ionicLoading ,Constants) {
//	$scope.reportData={
//		report:null ,
//		reportSectionList:[]
//	}
  
	$scope.loadData = function(scope){
		$ionicLoading.show();
		
		$http.get(Constants.baseUrl+'reportStr/'+ $stateParams.reportId).success(function (reportStr) {
			var reportObj=angular.fromJson(reportStr);
			console.log("report.name:"+ reportObj.name);
			scope.reportData = reportObj;
			$ionicLoading.hide();
		}).error(function (err) {
		    $ionicLoading.show({
		        template: '无法获取数据，请稍后再试！',
		        duration: 3000
		      })
	     });
	}
	
	   /*
	   * if given group is the selected group, deselect it
	   * else, select the given group
	   */
	  $scope.toggleGroup = function(group) {
	    if ($scope.isGroupShown(group)) {
	      $scope.shownGroup = null;
	    } else {
	      $scope.shownGroup = group;
	    }
	  };
	  
	  $scope.isGroupShown = function(group) {
	    return $scope.shownGroup === group;
	  };

	$scope.$on('$ionicView.enter', function() {
		$scope.loadData($scope);
  });
	
});