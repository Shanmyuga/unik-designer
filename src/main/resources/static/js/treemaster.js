(function() {
  'use strict';

  var app = angular.module('treemasterApp', ['angularTreeview']);
  app.controller('treeMasterCTL', function($scope , $http) {
  	  $http.get("http://localhost:8076/fufweb/rest/movie/test")
  	    .success(function(response) {$scope.treedata = response});
  	
  	  
  	  $scope.$watch( 'abc.currentNode', function( newObj, oldObj ) {
  		    if( $scope.abc && angular.isObject($scope.abc.currentNode) ) {
  		        console.log( 'Node Selected!!' );
  		        console.log( $scope.abc.currentNode );
  		    }
  		}, false);
  	
  });
})();