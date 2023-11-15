/**
 * The controller doesn't do much more than setting the initial data model
 */
angular
.module("DefaultConfig")
.controller("DefaultConfigController", [
	'$scope',
	'$http',
	'$location',
	'$window',
	'sharedProperties',
	function($scope, $http, $location, $window,
			sharedProperties) {
		
		$scope.activePage='config';
		$scope.selectedIndex = 0;
		$scope.selectedTab = 'form';
		$scope.resources = [];
		
		$http.get('rest/datamodel/defaultconfig/list')
			 .success(function(response) {
				 $scope.defaultConfigs=response;
			 });
		
		$http.get('rest/datamodel/files/list')
		 .success(function(response) {
			 $scope.filesList=response;
		 });
		
		$scope.fetchDefaultConfig = function(uiLang) {
			 $scope.defaultConfig=$scope.defaultConfigs.filter((config) => config.configLanguage == uiLang)[0];
			 
			 $scope.defFormTemplates = $scope.defaultConfig.formTemplates.filter((form) => form.templateType == 'form'); 
			 $scope.defLayoutTemplates = $scope.defaultConfig.formTemplates.filter((form) => form.templateType == 'layout');
			 
			 $scope.resources = $scope.defaultConfig.resourceTemplates;
			 
			 var selFormElement = $scope.defaultConfig.formTemplates[0];
			 $scope.selectedFormContent = selFormElement.templateCode;
			 $scope.defaultConfigJson = $scope.defaultConfig.defaultConfigJson;
			 
			 let defConfJson = JSON.parse($scope.defaultConfigJson);
			 
			 $scope.angCompPrefix = defConfJson.componentConfig.componentPrefix;
			 $scope.angCompStyleType = defConfJson.componentConfig.styleExt;
		};
		
		$scope.displayTab = function(tabName) {
			if (tabName == 'form') {
				$scope.selectedTab = 'form';
				if ($scope.defFormTemplates && $scope.defFormTemplates.length > 0) {
					$scope.selectedFormContent = $scope.defFormTemplates[0].templateCode;
					$scope.selectedIndex = 0;
				} else {
					$scope.selectedFormContent = '';
				}
			} else if (tabName == 'layout') {
				$scope.selectedTab = 'layout';
				if ($scope.defLayoutTemplates && $scope.defLayoutTemplates.length > 0) {
					$scope.selectedFormContent = $scope.defLayoutTemplates[0].templateCode;
					$scope.selectedIndex = 0;
				} else {
					$scope.selectedFormContent = '';
				}
			} else if (tabName == 'files') {
				$scope.selectedTab = 'files';
			} else if (tabName == 'config') {
				$scope.selectedTab = 'config';
			} else  {
				$scope.selectedTab = 'full';
			}
		};
		
		$scope.displayForm = function(formEleName, index) {
			$scope.selectedIndex = index;
			
			var selFormElement = $scope.defaultConfig.formTemplates.filter((form) => form.baseTemplateName == formEleName)[0];
			$scope.selectedFormContent = selFormElement.templateCode;
		};
		
		$scope.addNewFileMappingEntry = function() {
			$scope.resources.push({
				id: '',
				fileId: '',
				filePath: ''
			});
		};
		
		$scope.saveResourceFile = function(index) {
			var reqBody = {
					id: $scope.defaultConfig.id,
					fileName: $scope.resources[index].fileId,
					filePath: $scope.resources[index].filePath
				};
			
			var url = 'rest/design/mapResourceFiles';
			$http.post(url, reqBody).success(
					function(response) {
						
						alert('Success!! Files Mapping saved successfully.');
					});
		};
		
		$scope.deleteResourceFile = function(index) {
			if (confirm("Are you sure to delete the mapping (Y/N)?")) {
				var reqBody = {
						id: $scope.defaultConfig.id,
						fileName: $scope.resources[index].fileId,
						filePath: $scope.resources[index].filePath
				};
				
				var url = 'rest/design/deleteResourceFiles';
				$http.post(url, reqBody).success(
						function(response) {
							
							alert('Success!! Files Mapping deleted successfully.');
						});
			}
		};
		
		$scope.showFileContent = function() {
			for (let ind = 0; ind < $scope.filesList.length; ind += 1) {
				if ($scope.updateFileId == $scope.filesList[ind].id) {
					$scope.selectedFileResource = $scope.filesList[ind];
					$scope.updateFileContent = $scope.selectedFileResource.fileContent;
					break;
				}
			}
		};
		
		$scope.updateFiles = function() {
			if (!$scope.selectedFileResource || $scope.selectedFileResource.id.length == 0) {
				alert ('Error !! Please select the file to update.');
				return false;
			}
			
			var reqBody = {
					fileId: $scope.selectedFileResource.id,
					fileName: $scope.selectedFileResource.fileName,
					fileType: $scope.selectedFileResource.fileType,
					fileContent: $scope.updateFileContent
				};
			
			var url = 'rest/design/importFiles';
			$http.post(url, reqBody).success(
					function(response) {
						
						alert('Success!! File updated successfully.');
					});
		};
		
		$scope.deleteFiles = function() {
			if (!$scope.selectedFileResource || $scope.selectedFileResource.id.length == 0) {
				alert ('Error !! Please select the file to delete.');
				return false;
			}
			
			var reqBody = {
					fileId: $scope.selectedFileResource.id
				};
			
			var url = 'rest/design/deleteFiles';
			$http.post(url, reqBody).success(
					function(response) {
						$http.get('rest/datamodel/files/list')
							 .success(function(response) {
								 $scope.filesList=response;

								 alert('Success!! File deleted successfully.');
							 });						
					});
		};
		
		$scope.importFiles = function() {
			if (!$scope.importFileName || $scope.importFileName.length == 0) {
				alert('Error !! Please enter a file Name.');
				return false;
			}
			
			if (!$scope.importFileType || $scope.importFileType.length == 0) {
				alert('Error !! Please select a file Type.');
				return false;
			}

			if (!$scope.importFileContent || $scope.importFileContent.length == 0) {
				alert('Error !! Please enter file Content.');
				return false;
			}
			
			var reqBody = {
					fileName: $scope.importFileName,
					fileType: $scope.importFileType,
					fileContent: $scope.importFileContent
				};
			
			var url = 'rest/design/importFiles';
			$http.post(url, reqBody).success(
					function(response) {
						$scope.importFileName = '';
						$scope.importFileType = '';
						$scope.importFileContent = null;
						
						alert('Success!! Import file successfully.');
					});
		};
		
		$scope.createFormElement = function(tempType) {
			var url = 'rest/design/saveForm';
			
			if (!$scope.configUiLang || $scope.configUiLang == '') {
				alert('Please select the UI Technology');
				return false;
			}
			
			var reqBody = {
					uiLang: $scope.configUiLang,
					configId: $scope.defaultConfig.id,
					baseElementName: $scope.formElemName,
					formElementName: $scope.formElemName,
					formContent: $scope.formContent,
					templateType: tempType
				};
			
			$http.post(url, reqBody).success(
					function(response) {
						let formTemp = {
							baseTemplateName: $scope.formElemName,
							templateName: $scope.formElemName,
							templateCode: $scope.formContent
						};
						
						$scope.defaultConfig.formTemplates.push(formTemp);
						
						if (tempType == 'form') {
							$scope.defFormTemplates.push(formTemp);
						} else {
							$scope.defLayoutTemplates.push(formTemp);
						}
						
						$scope.formElemName = '';
						$scope.formContent = '';
						alert('Success!! Form Content Created');
					});
		};
		
		$scope.updateFormElement = function(tempType) {
			var url = 'rest/design/saveForm';
			
			if (!$scope.configUiLang || $scope.configUiLang == '') {
				alert('Error !! Please select the UI Technology');
				return false;
			}
			
			let reqBody = {};
			if ($scope.selectedTab == 'form') {
				reqBody = {
						uiLang: $scope.configUiLang,
						configId: $scope.defaultConfig.id,
						baseElementName: $scope.defFormTemplates[$scope.selectedIndex].baseTemplateName,
						formElementName: $scope.defFormTemplates[$scope.selectedIndex].templateName,
						formContent: $scope.selectedFormContent,
						templateType: tempType
					};
				
			} else {
				reqBody = {
						uiLang: $scope.configUiLang,
						configId: $scope.defaultConfig.id,
						baseElementName: $scope.defLayoutTemplates[$scope.selectedIndex].baseTemplateName,
						formElementName: $scope.defLayoutTemplates[$scope.selectedIndex].templateName,
						formContent: $scope.selectedFormContent,
						templateType: tempType
					};
			}
			
			$http.post(url, reqBody).success(
					function(response) {
						if ($scope.selectedTab == 'form') {
							$scope.defFormTemplates[$scope.selectedIndex].templateCode = $scope.selectedFormContent;
						} else {
							$scope.defLayoutTemplates[$scope.selectedIndex].templateCode = $scope.selectedFormContent;
						}
						
						alert('Success!! Form Content Updated');
					});
		};
		
		$scope.updateConfig = function() {
			$scope.updateFullConfig();
		};
		
		$scope.updateFullConfig = function() {
			var url = 'rest/design/saveForm';
			
			if (!$scope.configUiLang || $scope.configUiLang == '') {
				alert('Please select the UI Technology');
				return false;
			}
			
			var reqBody = {
					uiLang: $scope.configUiLang,
					configId: $scope.defaultConfig.id,
					defaultConfigJson: $scope.defaultConfigJson,
					angCompPrefix: $scope.angCompPrefix,
					angCompStyleType: $scope.angCompStyleType
				};
			
			$http.post(url, reqBody).success(
					function(response) {
						$scope.categories = response;
						
						alert('Success!! Full Config Layout Updated');
					});
		};
		
		$scope.deleteFormElement = function(tempType) {
			var url = 'rest/design/deleteForm';
			
			if (!$scope.configUiLang || $scope.configUiLang == '') {
				alert('Error !! Please select the UI Technology');
				return false;
			}
			
			let reqBody = {};
			if ($scope.selectedTab == 'form') {
				reqBody = {
						uiLang: $scope.configUiLang,
						configId: $scope.defaultConfig.id,
						baseElementName: $scope.defFormTemplates[$scope.selectedIndex].baseTemplateName
					};
				
			} else {
				reqBody = {
						uiLang: $scope.configUiLang,
						configId: $scope.defaultConfig.id,
						baseElementName: $scope.defLayoutTemplates[$scope.selectedIndex].baseTemplateName
					};
			}
			
			$http.post(url, reqBody).success(
					function(response) {
						if ($scope.selectedTab == 'form') {
							$scope.defFormTemplates.splice($scope.selectedIndex, 1);
						} else {
							$scope.defLayoutTemplates.splice($scope.selectedIndex, 1);
						}
						
						$scope.selectedIndex = 0;
						alert('Success!! Form Content Deleted');
					});
		};
		
	} 
]);
