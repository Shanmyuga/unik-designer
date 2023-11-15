/**
 * The controller doesn't do much more than setting the initial data model
 */
angular
.module("Config")
.controller("ConfigController", [
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
		$scope.showConfig = false;
		
		$http.get('rest/datamodel/config/list')
			 .success(function(response) {
				 $scope.configList=response;
			 });
		
		$http.get('rest/datamodel/files/list')
			 .success(function(response) {
				 $scope.filesList=response;
			 });
		
		$scope.fetchConfig = function(uiLang) {
			$scope.uiConfigList = $scope.configList.filter((config) => config.configLanguage == uiLang && config.defaultConfig == false);
		};

		$scope.showAddConfig = false;
		$scope.addConfig = function() {
			if (!$scope.configUiLang || $scope.configUiLang == '') {
				alert('Please select the UI Technology');
				return false;
			}
			
			$scope.showAddConfig = true;
		};
		
		$scope.removeConfig = function() {
			$scope.showAddConfig = false;
		};
		
		$scope.deleteConfig = function() {
			if (!$scope.configUiLang || $scope.configUiLang == '') {
				alert('Error !! Please select the UI Technology');
				return false;
			}
			
			if (!$scope.selectedConfig || $scope.selectedConfig == '') {
				alert('Error !! Please select Configuration to delete');
				return false;
			}
			
			if (confirm("Are you sure to delete the Configuration (Y/N)?")) {
				var url = 'rest/design/deleteConfig';
				var reqBody = {
						uiLang: $scope.configUiLang,
						configId: $scope.selectedConfig.id
				};
				
				$http.post(url, reqBody).success(
						function(response) {
							$scope.configList=$scope.configList.filter((config) => config.id != $scope.selectedConfig.id);
							$scope.uiConfigList=$scope.uiConfigList.filter((config) => config.id != $scope.selectedConfig.id);
							alert('Success !! Config Successfully deleted.');
						});
				
				$scope.showConfig = false;
			}
		};
		
		$scope.createConfig = function(uiLang) {
			console.log('create config');
			var url = 'rest/design/saveConfig';
		
			if (!$scope.configUiLang || $scope.configUiLang == '') {
				alert('Error !! Please select the UI Technology');
				return false;
			}
			
			var reqBody = {
					uiLang: $scope.configUiLang,
					configName: $scope.configName
				};
			
			$http.post(url, reqBody).success(
					function(response) {
						$http.get('rest/datamodel/config/list')
							 .success(function(response) {
								 $scope.configList=response;
								 $scope.fetchConfig($scope.configUiLang);
								 $scope.removeConfig();
								 $scope.configName = '';
								 
								 alert('Success !! Config Successfully added.');
							 });
					});
		};
		
		$scope.configChange = function() {
			console.log($scope.selectedConfigId);
			$scope.selectedConfig = $scope.configList.filter((config) => config.id == $scope.selectedConfigId)[0];
			$scope.showConfig = true;
			$scope.resources = $scope.selectedConfig.resourceTemplates;
			
			$http.get('rest/datamodel/config/' + $scope.selectedConfig.defaultConfigId)
				 .success(function(response) {
					 $scope.defaultConfig=response;

					 $scope.defaultConfigJson = $scope.defaultConfig.defaultConfigJson;
					 let defConfJson = JSON.parse($scope.defaultConfigJson);
					 
					 if ($scope.selectedConfig.formTemplates && $scope.selectedConfig.formTemplates.length > 0) {
						 $scope.selectedDisplayName=$scope.selectedConfig.formTemplates[0].templateName;
						 $scope.selectedFormContent=$scope.selectedConfig.formTemplates[0].templateCode;

						 let selConfJson = $scope.selectedConfig.componentConfig;
						 $scope.angCompPrefix = selConfJson.componentPrefix;
						 $scope.angCompStyleType = selConfJson.styleExt;
						 
						 $scope.customizeFormList = $scope.defaultConfig.formTemplates.filter(function(defConf) {
							 return !$scope.selectedConfig.formTemplates.some(function(selConf) {
								 return defConf.baseTemplateName == selConf.baseTemplateName;
							 });
						 });
					 } else {
						 $scope.customizeFormList = $scope.defaultConfig.formTemplates;
					 }
				 });
			
		};
		
		$scope.customizeForm = function() {
			let baseTemplate = $scope.defaultConfig.formTemplates.filter((form) => form.baseTemplateName == $scope.selectedFormToCustomize)[0];
			
			if (!$scope.selectedConfig.formTemplates || $scope.selectedConfig.formTemplates.length == 0) {
				$scope.selectedConfig.formTemplates = [];
			}
			$scope.selectedConfig.formTemplates.push(baseTemplate);
			
			$scope.selectedDisplayName=baseTemplate.templateName;
			$scope.selectedFormContent=baseTemplate.templateCode;
			
			$scope.selectedIndex = $scope.selectedConfig.formTemplates.length - 1;
		};
		
		$scope.saveResourceFile = function(index) {
			var reqBody = {
					id: $scope.selectedConfig.id,
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
						id: $scope.selectedConfig.id,
						fileName: $scope.resources[index].fileId,
						filePath: $scope.resources[index].filePath
				};
				
				var url = 'rest/design/deleteResourceFiles';
				$http.post(url, reqBody).success(
						function(response) {
							$scope.resources.splice(index, 1);
							
							alert('Success!! Files Mapping deleted successfully.');
						});
			}
		};
		
		$scope.displayTab = function(tabName) {
			$scope.selectedTab = tabName;
		};
		
		$scope.displayForm = function(formEleName, index) {
			$scope.selectedIndex = index;
			
			var selFormElement = $scope.selectedConfig.formTemplates.filter((form) => form.baseTemplateName == formEleName)[0];
			$scope.selectedFormContent = selFormElement.templateCode;
			$scope.selectedDisplayName = selFormElement.templateName;
		};
		
		$scope.addNewFileMappingEntry = function() {
			$scope.resources.push({
				id: '',
				fileId: '',
				filePath: ''
			});
		};
		
		$scope.updateConfig = function() {
			var url = 'rest/design/saveForm';
			
			if (!$scope.configUiLang || $scope.configUiLang == '') {
				alert('Please select the UI Technology');
				return false;
			}
			
			var reqBody = {
					uiLang: $scope.configUiLang,
					configId: $scope.selectedConfigId,
					angCompPrefix: $scope.angCompPrefix,
					angCompStyleType: $scope.angCompStyleType
				};
			
			$http.post(url, reqBody).success(
					function(response) {
						$scope.categories = response;
						
						alert('Success!! Config Updated');
					});
		};
		
		$scope.deleteFormElement = function() {
			var url = 'rest/design/deleteForm';
			
			if (!$scope.configUiLang || $scope.configUiLang == '') {
				alert('Error !! Please select the UI Technology');
				return false;
			}
			
			if (confirm ("Are you sure to delete the element (Y/N)?")) {
				var reqBody = {
						uiLang: $scope.configUiLang,
						configId: $scope.selectedConfig.id,
						baseElementName: $scope.selectedConfig.formTemplates[$scope.selectedIndex].baseTemplateName
				};
				
				$http.post(url, reqBody).success(
						function(response) {
							$scope.selectedConfig.formTemplates.splice($scope.selectedIndex, 1);
							
							alert('Success !! Form Content Deleted successfully.');
						});
			}
		};
		
		$scope.updateFormElement = function() {
			var url = 'rest/design/saveForm';
			
			if (!$scope.configUiLang || $scope.configUiLang == '') {
				alert('Error !! Please select the UI Technology');
				return false;
			}
			
			var reqBody = {
					uiLang: $scope.configUiLang,
					configId: $scope.selectedConfig.id,
					baseElementName: $scope.selectedConfig.formTemplates[$scope.selectedIndex].baseTemplateName,
					formElementName: $scope.selectedConfig.formTemplates[$scope.selectedIndex].templateName,
					formContent: $scope.selectedFormContent
				};
			
			$http.post(url, reqBody).success(
					function(response) {
						$scope.selectedConfig.formTemplates[$scope.selectedIndex].templateCode = $scope.selectedFormContent;
						
						alert('Success !! Form Content Updated successfully.');
					});
		};
	} 
]);
