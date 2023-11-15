'use strict';

angular.module('Preview')

.controller('PreviewController',
    ['$scope','$http','sharedProperties', '$location',
    function ($scope,$http,sharedProperties,$location) {
    	
    	
    	
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
    	               }
    	             ];
    	            
    	             $scope.selectTab = function(tabId) {
    	               console.log('Selected tab: ' + tabId);
    	             };
    	var url = 'rest/layout/data/' + sharedProperties.getScreenName();
    	/*$http.get(url).
		  success(function(response) {
			$scope.layoutmodel = response;
		  });*/
    	
    	 $scope.next = function() {
    		 var url = 'rest/layout/save?key='+ $scope.models.screenname; 
			 $http.post(url,$scope.modelAsJson).
			  success(function(response) {
				  $scope.categories=response;
			  });
		    };
    	
		    
    }]);