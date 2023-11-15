/**
 * The controller doesn't do much more than setting the initial data model
 */
angular
.module("FormComponent")
.controller("FormComponentController", [
	'$scope',
	'$http',
	'$location',
	'$window',
	'sharedProperties',
	function($scope, $http, $location, $window,
			sharedProperties) {
		
		$scope.showAddPanel = false;
		$scope.showDropDownValues = false;
		$scope.selectedParams = [];
		
		$http.get('rest/datamodel/components/list')
			 .success(function(response) {
				 $scope.componentList=response;
			 });
		
		$scope.addFormComponent = function() {
			 $scope.showAddPanel = true;
		};
		
		$scope.resetParams = function() {
			$scope.selectedRow = null;
			 
			$scope.baseComponentName = '';
			$scope.displayName = '';
			$scope.containerElement = '';
			$scope.hideElement = '';
			$scope.containerRowCount = null;
			$scope.containerColCount = null;
			$scope.selectedParams = [];
		};
		
		$scope.updateRow = function(index) {
			let selectedRow = $scope.componentList[index];
			$scope.showAddPanel = true;
			$scope.selectedRow = selectedRow;
			
			$scope.baseComponentName = selectedRow.componentName;
			$scope.displayName = selectedRow.displayName;
			$scope.containerElement = selectedRow.containerElement;
			$scope.hideElement = selectedRow.hideElement;
			$scope.containerRowCount = selectedRow.containerRowCount;
			$scope.containerColCount = selectedRow.containerColCount;
			$scope.selectedParams = selectedRow.componentParams;
			$scope.formDisplayTemplate = selectedRow.formDisplayTemplate; 
			
			if (!selectedRow.componentProps || selectedRow.componentProps.length == 0) {
				selectedRow.componentProps = [{
					propertyName: '',
					propertyDataType: '',
					options: []
				}];
			}
		};
		
		$scope.deleteRow = function(index) {
			let selectedRowId = $scope.componentList[index].id;
			
			if (confirm('Are you sure to delete the Component(Y/N)?')) {
				let reqBody = {
						componentId: selectedRowId
				};
				
				var url = 'rest/design/deleteComponent';
				$http.post(url, reqBody).success(
						function(response) {
							$scope.componentList.splice(index, 1);
							$scope.showAddPanel = false;
							$scope.resetParams();
							
							alert('Success!! Form Content Deleted');
						});
			}
		};
		
		$scope.saveFormComponent = function() {
			var url = 'rest/design/saveComponent';
			
			if (!$scope.baseComponentName || $scope.baseComponentName == '') {
				alert('Error !! Please enter the Component Name');
				return false;
			}
			
			if ($scope.selectedParams && $scope.selectedParams.length > 0) {
				for (let paramInd = 0; paramInd < $scope.selectedParams.length; paramInd += 1) {
					let param = $scope.selectedParams[paramInd];
					
					if (!param.paramName || param.paramName.length == 0) {
						alert('Error !! Please enter the Param Name');
						return false;
					}
					
					if (!param.paramType || param.paramType.length == 0) {
						alert('Error !! Please enter the Param Type');
						return false;
					}
				}
			}
			
			var reqBody = {
					baseComponentName: $scope.baseComponentName,
					displayName: $scope.displayName,
					containerElement: $scope.containerElement,
					hideElement: $scope.hideElement,
					rowCount: $scope.containerRowCount,
					colCount: $scope.containerColCount,
					selectedParams: $scope.selectedParams,
					formDisplayTemplate: $scope.formDisplayTemplate
				};
			
			if ($scope.selectedRow) {
				reqBody.componentId = $scope.selectedRow.id;
				reqBody.selectedProperties = $scope.selectedRow.componentProps;
			}
			
			$http.post(url, reqBody).success(
				function(response) {
					reqBody.componentName = $scope.baseComponentName;
					
					$http.get('rest/datamodel/components/list')
					 .success(function(response) {
						 $scope.componentList=response;
					 });
					
					$scope.baseComponentName = '';
					$scope.displayName = '';
					$scope.containerElement = false;
					$scope.hideElement = false;
					$scope.containerRowCount = null;
					$scope.containerColCount = null;
					$scope.formDisplayTemplate = '';
					$scope.copyTemplateFrom = '';
					$scope.selectedParams = [];
					$scope.showAddPanel = false;
					
					alert('Success!! Form Content Created');
				});
		};
		
		$scope.addParams = function() {
			$scope.selectedParams.push({
				paramName: '',
				paramType: '',
				defaultValue: ''
			});
		};
		
		$scope.deleteParam = function(index) {
			$scope.selectedParams.splice(index, 1);
		};
		
		$scope.addDataTypeOptions = function(index) {
			selectedProp = $scope.selectedRow.componentProps[index];
			$scope.selectedProperty = selectedProp;
			
			$scope.showDropDownPanel();
		};
		
		$scope.showDropDownPanel = function() {
			if ($scope.selectedProperty.propertyDataType == 'dropdown') {
				$scope.showDropDownValues = true;
				if ($scope.selectedProperty.options.length == 0) {
					$scope.selectedProperty.options.push({
						value: '',
						display: ''
					});
				}
			} else {
				$scope.showDropDownValues = false;
			}
		};
		
		$scope.addFieldProps = function() {
			$scope.selectedRow.componentProps.push({
				propertyName: '',
				propertyDataType: '',
				options: []
			});
		};
		
		$scope.deleteFieldProps = function(index) {
			c.splice(index, 1);
		};
		
		$scope.addPropOptions = function() {
			if ($scope.selectedProperty.options) {
				$scope.selectedProperty.options.push({
					value: '',
					display: ''
				});
			} else {
				$scope.selectedProperty.options = [{
					value: '',
					display: ''
				}];
			}
		};
		
		$scope.deletePropOptions = function(index) {
			if ($scope.selectedProperty && $scope.selectedProperty.options) {
				$scope.selectedProperty.options.splice(index, 1);
			}
		};
		
		$scope.selectProperty = function(index) {
			if (!$scope.selectedRow.componentProps[index].options) {
				$scope.selectedRow.componentProps[index].options = [];
			}
			$scope.selectedProperty = $scope.selectedRow.componentProps[index];
			
			$scope.showDropDownPanel();
		};
		
		$scope.copyTemplate = function() {
			let selectedTemplate = $scope.componentList.filter((c) => c.componentName == $scope.copyTemplateFrom)[0];
			
			$scope.formDisplayTemplate = selectedTemplate.formDisplayTemplate;
		};
	} 
]);
