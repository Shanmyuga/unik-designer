'use strict';

angular.module('Home')
.controller('HomeController',
		['$scope','$rootScope', '$http','sharedProperties', '$location',
			function ($scope,$rootScope,$http,sharedProperties,$location) {

			$scope.activePage='generate';
			
			$http.get('rest/layout/env').then(function (response) {
				if (response.data == 'cloud') {
					$scope.isCloud = true;
				} else {
					$scope.isCloud = false;
				}
			});
			
			$http.get('rest/datamodel/config/list')
				 .success(function(response) {
					 $scope.configList=response;
					 $scope.reactConfigList=response.filter((config) => config.configLanguage == 'React');
					 $scope.angularConfigList=response.filter((config) => config.configLanguage == 'Angular');;
				 });
			
			$scope.layoutType = 'layout';
			
			$http.get('rest/datamodel/layouts').
			success(function(response) {
				$scope.datamodels=response;
			});
			
			$http.get('rest/datamodel/compLists').
			success(function(response) {
				$scope.componentmodels=response;
			});

			$scope.change = function() {
				var key = '';
				if ($scope.layoutType == 'layout') {
					key = $scope.selectedId;
				} else {
					key = $scope.selectedCompId;
				}
				
				var url = 'rest/datamodel/get/' +  key;
				//var url = 'rest/datamodel/data/SpecialAuthorizationEntry';
				$http.get(url).
				success(function(response) {
					var layoutJson = JSON.stringify(response.layout);
					layoutJson = layoutJson.replace(/\\n/g, '');
					layoutJson = layoutJson.replace(/\\/g, '');
					
					$scope.categories=layoutJson;
					sharedProperties.setData(JSON.parse(response.layout));
					sharedProperties.setProperty(response.layoutName);
					sharedProperties.setParams(response.params);
					sharedProperties.setLayoutId(response.id);
				});
			};

			$scope.downloadFile = function() {
				var fileName = $scope.downloadFileName;
					
				if (fileName) {
					$http.post('/download1', fileName, {
						responseType: 'arraybuffer'
					})
					.success(function(response, status, xhr) {
						// check for a filename
						var filename = "";
						var disposition = xhr('Content-Disposition');
						if (disposition && disposition.indexOf('attachment') !== -1) {
							var filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
							var matches = filenameRegex.exec(disposition);
							if (matches != null && matches[1]) filename = matches[1].replace(/['"]/g, '');
						}

						var type = xhr('Content-Type');
						var blob = new Blob([response], { type: type });

						if (typeof window.navigator.msSaveBlob !== 'undefined') {
							// IE workaround for "HTML7007: One or more blob URLs were revoked by closing the blob 
							// for which they were created. These URLs will no longer resolve as the data 
							// backing the URL has been freed."
							window.navigator.msSaveBlob(blob, filename);
						} else {
							var URL = window.URL || window.webkitURL;
							var downloadUrl = URL.createObjectURL(blob);
							
							if (filename) {
								// use HTML5 a[download] attribute to specify filename
								var a = document.createElement("a");
								// safari doesn't support this yet
								if (typeof a.download === 'undefined') {
									window.location = downloadUrl;
								} else {
									a.href = downloadUrl;
									a.download = filename;
									document.body.appendChild(a);
									a.click();
								}
							} else {
								window.location = downloadUrl;
							}
						}
					});
				} 
			};     

			$scope.selectOption = function() {
				let valid = true;
				if ($scope.layoutType == 'layout') {
					if (!$scope.selectedId || $scope.selectedId == '') {
						alert ('Error !! Please select a Layout to update.');
						valid = false;
					}
				} else {
					if (!$scope.selectedCompId || $scope.selectedCompId == '') {
						alert ('Error !! Please select a Component to update.');
						valid = false;
					}
				}
				
				if (valid) {
					$location.path('/nested');
				} 
			};
			
			$scope.deleteOption = function() {
				let valid = true;
				if ($scope.layoutType == 'layout') {
					if (!$scope.selectedId || $scope.selectedId == '') {
						alert ('Error !! Please select a Layout to delete.');
						valid = false;
					}
				} else {
					if (!$scope.selectedCompId || $scope.selectedCompId == '') {
						alert ('Error !! Please select a Component to delete.');
						valid = false;
					}
				}
				
				if (valid) {
					if (confirm("Are you sure to delete the Layout (Y/N)")) {
						let selectedLayoutId = null;
						if ($scope.layoutType == 'layout') {
							selectedLayoutId = $scope.selectedId;
						} else {
							selectedLayoutId = $scope.selectedCompId;
						}
						
						let reqBody = {
								layoutId: selectedLayoutId
						};
						
						var url = 'rest/design/delete';
						$http.post(url, reqBody).success(
								function(response) {
									if ($scope.layoutType == 'layout') {
										$scope.datamodels.options=$scope.datamodels.options.filter((layout) => layout.value != $scope.selectedId);
									} else {
										$scope.componentmodels.options=$scope.componentmodels.options.filter((layout) => layout.value != $scope.selectedCompId);
									}
									
									alert('Success !! Layout / Component Successfully deleted.');
								});
					}
				} 
			};

			$scope.callAngularCodeGen = function() {
                $scope.codeGenerating = true;
                $scope.isError = false;

				var url = "";
				if($scope.uiTechnology=="AngularJs" 
					&& ($scope.isCloud || ($scope.isCloud == false && $scope.codeGenPath)) 
					&& $scope.moduleName 
					&& $scope.componentName 
					&& $scope.projectName 
					&& $scope.selectedId 
					&& $scope.configName 
					&& $scope.uiTechnology) {
					
					url = {	uiTechnology:$scope.uiTechnology, 
							codeGenPath:$scope.codeGenPath,
							screenName: $scope.selectedId,
							projectName:$scope.projectName,
							moduleName:$scope.moduleName,
							componentName:$scope.componentName, 
							configName: $scope.configName, 
							userDefinedPath:$scope.userDefinedPath, 
							userId: $rootScope.globals.currentUser.username };
					
					$scope.isDisplay = false;
					$scope.successMessage = "";		  
					
					$http.post('/rest/layout/callAngCodeGenTool',url,
								{
									headers: {
										'Content-Type': 'application/json'
									}
								})
						.success(function(response) {
							$scope.isDisplay = true;
							$scope.successMessage = response.message;
							$scope.codeGenerating = false;
							$scope.downloadFileName = response.path;
						})
						.error(function(error, status) {
							$scope.isError = true;
							$scope.isDisplay = true;
							$scope.successMessage = "Error occured !!!";
							$scope.codeGenerating = false;
						});
				} else if($scope.uiTechnology=="React" 
					&& ($scope.isCloud || ($scope.isCloud == false && $scope.codeGenPath)) 
					&& $scope.selectedId 
					&& $scope.configName 
					&& $scope.uiTechnology
					&& $scope.genLoginScreen
					&& $scope.projectName) {
					
					url = {	uiTechnology:$scope.uiTechnology, 
							projectName:$scope.projectName, 
							codeGenPath:$scope.codeGenPath,
							screenName: $scope.selectedId, 
							configName: $scope.configName, 
							genLoginScreen: $scope.genLoginScreen,
							userId: $rootScope.userId};
					
					$scope.isDisplay = false;
					$scope.successMessage = "";		  
					$http.post('/rest/layout/callAngCodeGenTool',url,
							{
								headers: {
									'Content-Type': 'application/json'
								}
							}).
					success(function(response) {
						$scope.isDisplay = true;
						$scope.successMessage = response.message;
						$scope.codeGenerating = false;
						$scope.downloadFileName = response.path;
					})
					.error(function(error, status) {
						$scope.isError = true;
						$scope.isDisplay = true;
						$scope.successMessage = "Error occured !!!";
						$scope.codeGenerating = false;
					});
				}
				else{
					$scope.isDisplay = true;
					$scope.isError = true;
					$scope.successMessage = "Mandatory fields are required";
					$scope.codeGenerating = false;
				}
			};

			$scope.addOption = function() {
				var data= {
						"layout" : [ {
							"type" : "section",
							"id" : "4",
							"section":"section",
							"label" : "First Section",
							"htmlID" : "htmlID",
							"columns": [
								[{
									"type": "header",
									"id": "11"
								}]
							]


					    }]
				};

				sharedProperties.setData(data);
				$location.path('/nested');
			};
		}])

.directive('unik-nav-bar', function() {
	return ({scope: {
		
	},
	controller: function() {
		
	},
	controllerAs: 'ctrl',
	replace: true,
	restrict: 'EA',
	bindToController: true,
	template: `test header`
	});
});