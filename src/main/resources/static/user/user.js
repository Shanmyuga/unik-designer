/**
 * The controller doesn't do much more than setting the initial data model
 */
angular
.module("UserComponent")
.controller("UserController", [
	'$scope',
	'$http',
	'$location',
	'$window',
	'sharedProperties',
	function($scope, $http, $location, $window,
			sharedProperties) {
		
		$scope.showUserPanel = false;
		
		$http.get('api/allUsers')
			 .success(function(response) {
				 $scope.usersList=response;
			 });
		
		$scope.addUser = function() {
			$scope.showUserPanel = true;
			$scope.selectedUserId = null;
		};
		
		$scope.resetUser = function() {
			$scope.userName = '';
			$scope.password = '';
			$scope.fullName = '';
			$scope.selectedUserId = null;
		};
		
		$scope.selectUser = function(index) {
			let selectedUser = $scope.usersList[index];
			
			$scope.selectedUserId = selectedUser.id;
			$scope.userName = selectedUser.userName;
			$scope.password = selectedUser.password;
			$scope.fullName = selectedUser.fullName;
			$scope.showUserPanel = true;
		};
		
		$scope.deleteUser = function(index) {
			var url = 'api/deleteUser';
			
			let selectedUser = $scope.usersList[index];
			url = url + '/' + selectedUser.id;
			
			$http.get(url).success(
				function(response) {
					$scope.usersList.splice(index, 1);
					$scope.resetUser();
					$scope.showUserPanel = false;
					
					alert('Success!! User Deleted');
				});
		};
		
		$scope.saveUser = function() {
			var url = 'api/addUser';
			
			if (!$scope.userName || $scope.userName == '') {
				alert('Error !! Please enter the User Name');
				return false;
			}
			
			if (!$scope.password || $scope.password == '') {
				alert('Error !! Please enter the Password');
				return false;
			}
			
			if (!$scope.fullName || $scope.fullName == '') {
				alert('Error !! Please enter the Full Name');
				return false;
			}
			
			var reqBody = {
					id: $scope.selectedUserId,
					userName: $scope.userName,
					password: $scope.password,
					fullName: $scope.fullName
				};
			
			$http.post(url, reqBody).success(
				function(response) {
					$http.get('api/allUsers')
						 .success(function(response) {
							 $scope.usersList=response;
						 });
					
					$scope.userName = '';
					$scope.password = '';
					$scope.fullName = '';
					$scope.showUserPanel = false;
					
					alert('Success!! User Created / Updated');
				});
		};
	} 
]);
