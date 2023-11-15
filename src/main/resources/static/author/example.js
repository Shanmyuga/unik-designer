angular.module('ui.bootstrap.demo', ['ui.bootstrap']);
angular.module('ui.bootstrap.demo').controller('TabsDemoCtrl',
		  ['$scope','$http', '$location',
		    function ($scope,$http,$location) {
			  
			 var screenname = JSON.stringify($location.search().target);
			 screenname = screenname.replace(/^"(.*)"$/, '$1');
			 console.log("Hi " +screenname);
			  var url = '/fufweb/rest/layout/data/'+ screenname.toString();
			
		    	$http.get(url).
				  success(function(response) {
					$scope.layoutmodel = response;
					$scope.screenname = screenname.toString();
				  });
		    	
		    	 $scope.myappform = {};
  $scope.tabs = [
    {
        id: 1,
        title:'Dynamic Title 1',
        content:'Dynamic content 1'
    },
    {
        id: 2,
        title:'Dynamic Title 2',
        content:'Dynamic content 2',
        disabled: false
    },
    
   
  ];

  $scope.selectTab = function(tabId) {
	
	  var url = '/fufweb/rest/layout/data/'+ screenname.toString()+'/'+tabId;
		
  	$http.get(url).
		  success(function(response) {
			  console.log(response)
			$scope.sectcontent = response;
		  });
  };
  
  
  $scope.SubmitForm = function() {
	 
	  var url = '/fufweb/rest/layout/saveanswers?key=ANS_'+ screenname.toString();
	  console.log($scope.myappform);
	  
		 $http.post(url,$scope.myappform).
		  success(function(response) {
			  var url = '/fufweb/rest/layout/data/'+ screenname.toString();
			  var url1 = '/fufweb/rest/layout/answerData/'+ screenname.toString();
		    	$http.get(url).
				  success(function(response) {
					  console.log(response);
					$scope.layoutmodel = response;
					$scope.screenname = screenname.toString();
				  });
		    	
		    	
		    	$http.get(url1).
				  success(function(response) {
					  console.log(response);
					$scope.myappform = response;
					$scope.screenname = screenname.toString();
				  });
		  });
		 
		
  };
  
  $scope.allInfo = function (data) {
      return "myappform" + ' . ' + data;
  }
  $scope.selectItemChange = function() {
		 
	 console.log($scope.myappform);
		/* $http.post(url,$scope.myform).
		  success(function(response) {
			  $scope.categories=response;
		  });*/
  };
}]);
