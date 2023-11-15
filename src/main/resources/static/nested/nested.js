/**
 * The controller doesn't do much more than setting the initial data model
 */
angular
		.module("demo")
		.controller(
				"NestedListsDemoController",
				[
						'$scope',
						'$http',
						'$location',
						'$window',
						'sharedProperties',
						function($scope, $http, $location, $window,
								sharedProperties) {
							$scope.activePage='design';
							
							var layoutdata = sharedProperties.getData();
							$scope.screenname = '';

							$scope.models = {
								screenname : sharedProperties.getProperty(),
								selected : null,
								templates : [],
								dropzones : layoutdata,
								componentParams: sharedProperties.getParams(),
								layoutId: sharedProperties.getLayoutId()
							};
							
							$scope.selectItem = function(item) {
								$scope.models.selected = item;
								
								$scope.models.selectedTemplate = 
									$scope.componentsList.filter((templ) => templ.componentName == item.type)[0];
								console.log('selectedTemplate = ' + JSON.stringify($scope.models.selectedTemplate));
							};
							
							$http.get('rest/datamodel/components/list')
								 .success(function(response) {
									 $scope.componentsList = response;
									 let childId = response.length + 1;
									 for (let tempCount = 0; tempCount < response.length; tempCount += 1) {
										 let templ = response[tempCount];
										 
										 if (templ.hideElement) {
											 continue;
										 }
										 
										 let formComponent = {
											 type: templ.componentName,
											 displayName: templ.displayName,
											 id: tempCount + 1,
										 };
										 
										 if (templ.componentParams && templ.componentParams.length > 0) {
											 for (let paramCount = 0; paramCount < templ.componentParams.length; paramCount += 1) {
												 let param = templ.componentParams[paramCount];
												 
												 if (param.paramType == 'array') {
													 formComponent[param.paramName] = [];
												 } else {
													 if (param.defaultValue && param.defaultValue.length > 0) {
														 formComponent[param.paramName] = param.defaultValue;
													 } else {
														 formComponent[param.paramName] = '';
													 }													 
												 }
											 }
										 }
										 
										 if (templ.containerElement) {
											 
											 let columns=[];
											 let rows = [];
											 for (let col = 0; col < templ.containerColCount; col += 1) {
												 for (let row = 0; row < templ.containerRowCount; row += 1) {
													 rows.push({
														 type: 'gridheader',
														 id: childId
													 });
													 
													 childId += 1;
												 }
												 
												 columns.push(rows);
												 rows = [];
											 }

											 formComponent.columns = columns;
										 }
										 
										 $scope.models.templates.push (formComponent);
									 }
								 });
							
							$http.get('rest/datamodel/components').
								success(function(response) {
									$scope.components=response;
								});

							$scope.$watch('models.dropzones', function(model) {
								$scope.modelAsJson = angular
										.toJson(model, true);
								// $scope.apply();
							}, true);
							$scope.saveLayout = function() {
								var url = 'rest/design/save?key='
										+ $scope.models.screenname;
								
								var reqBody = {
										designJson: $scope.modelAsJson,
										type: 'Layout',
										params: []
									};
								
								$http.post(url, reqBody).success(
										function(response) {
											$scope.categories = response;
											
											alert ('Success !! Layout Saved successfully.');
										});
							};

							$scope.addOption = function() {

								var count = $scope.models.selected.options.length;
								if(count == 0) {
									$scope.models.selected.options= [];
								}
								count = count + 1;
								$scope.models.selected.options
										.push({
											'display' : $scope.models.selected.tempdisplay,
											'value' : $scope.models.selected.tempoption,
											'htmlids' : $scope.models.selected.htmlID
													+ '_' + count
										});

								$scope.models.selected.tempdisplay = '';
								$scope.models.selected.tempoption = '';

							};
							
							$scope.setComponentParams = function() {
								$scope.models.selected.componentParams = [];
								
								var selectedCompList = $scope.components.filter((comp) => comp.layoutName === $scope.models.selected.componentName ); 
								
								for (param of selectedCompList[0].params) {
									$scope.models.selected.componentParams.push({
										name: param.parameterName,
										value: param.defaultValue
									});
								}
							};
							
							$scope.addColumn = function() {
								$scope.models.selected.dtcols
								.push([{
									"type" : "datatablecolumn",
									"id" : "20"
								}]);

								
								var colWidthCount = 0;
								var count=  $scope.models.selected.dtcols.length;
								var colperctage = Math.round((100/count) * 100) / 100;

								for (var i = 0; i < count; i++) {
									$scope.models.selected.dtcols[i][0].width = colperctage.toString() + "%";
								}
								
								var difference = 100 - (colperctage * count);
								difference = Math.round(difference * 100) / 100;
								if (difference != 0) {
									$scope.models.selected.dtcols[count - 1][0].width =
										(colperctage + difference).toString() + "%";
								}
							};
							
                            $scope.modifyGrid = function() {
                                $scope.models.selected.columns
                                    .push([ {
                                        "type" : "gridheader",
                                        "id" : "11"
                                    }]);
                                
                                var count=  $scope.models.selected.columns.length;
                                var colperctage = 100/count;
                                colperctage = Math.round(colperctage * 100) / 100;

                                for (var i = 1; i <= count; i++) {
									if(i ==1)
										$scope.models.selected.col1 = colperctage.toString()+ '%';
                                    if(i ==2)
                                        $scope.models.selected.col2 = colperctage.toString()+ '%';
                                    if(i ==3)
                                        $scope.models.selected.col3 = colperctage.toString()+ '%';
                                    if(i ==4)
                                        $scope.models.selected.col4 = colperctage.toString()+ '%';
                                    if(i ==5)
                                        $scope.models.selected.col5 = colperctage.toString()+ '%';
                                    if(i ==6)
                                        $scope.models.selected.col6 = colperctage.toString()+ '%';
                                    if(i ==7)
                                        $scope.models.selected.col7 = colperctage.toString()+ '%';
                                    if(i ==8)
                                        $scope.models.selected.col8 = colperctage.toString()+ '%';
                                    if(i ==9)
                                        $scope.models.selected.col9 = colperctage.toString()+ '%';
                                    if(i ==10)
                                        $scope.models.selected.col10 = colperctage.toString()+ '%';
                                    if(i ==11)
                                        $scope.models.selected.col11 = colperctage.toString()+ '%';
                                    if(i ==12)
                                        $scope.models.selected.col12 = colperctage.toString()+ '%';
								}

                                var difference = 100 - (colperctage * count);
								difference = Math.round(difference * 100) / 100;
								if (difference != 0) {
									$scope.models.selected.dtcols[count - 1].width =
										(colperctage + difference).toString() + "%";
								}
                            };

							$scope.change = function() {
								var url = 'rest/layout/rule/'
										+ $scope.models.screenname + '?key='
										+ $scope.models.selectedData;
								$http
										.get(url)
										.success(
												function(response) {
													console.log(response);
													sharedProperties
															.setRuleData(response);
													sharedProperties
															.setRuleName($scope.models.selectedData);
												});
							};

							$scope.saveComponent = function() {
								var url = 'rest/design/save?key='
									+ $scope.models.screenname;
								
								var reqBody = {
									designJson: $scope.modelAsJson,
									type: 'Component',
									params: $scope.models.componentParams
								}
								$http.post(url, reqBody).success(
									function(response) {
										$scope.categories = response;
										
										alert ('Success !! Component Saved successfully.');
									});
							};

							$scope.getTemplate = function(itemType) {
								let selectedTemplate = 
									$scope.componentsList.filter((templ) => templ.componentName == itemType)[0];
								
								return selectedTemplate.formDisplayTemplate;
							};
							
							$scope.addComponentParams = function() {
								$scope.models.showComponentParams = true;
								
								if ($scope.models.componentParams == null 
										|| $scope.models.componentParams.length == 0) {
									
									$scope.models.componentParams = [{
										parameterName: '',
										defaultValue: ''
									}];
								} 
							};
							
							$scope.addParams = function() {
								$scope.models.componentParams.push ({
									parameterName: '',
									defaultValue: ''
								});
							};
							
							$scope.removeParams = function(index) {
								$scope.models.componentParams.splice(index, 1);
							};
							
							$scope.deleteItem = function(index) {
								$scope.models.selected.options.splice(index, 1);
							}


							$scope.previewScreen = function() {
								var screen = $scope.models.screenname
								sharedProperties.setScreenName(screen);
								var url = 'author/index.html#?target=' + screen;
								$window.open(url);

							};
							$scope.operators = [ {
								value : 'Set Value',
								label : 'Sample'
							}, {
								value : 'Set Value',
								label : ' Set Value'
							} ]
						} ])
	.directive('dynamicModel', ['$compile', '$parse', function ($compile, $parse) {
	    return {
	        restrict: 'A',
	        terminal: true,
	        priority: 100000,
	        link: function (scope, elem) {
	            var name = $parse(elem.attr('dynamic-model'))(scope);
	            elem.removeAttr('dynamic-model');
	            elem.attr('ng-model', name);
	            $compile(elem)(scope);
	        }
	    };
	}])
	.directive('dynamicInclude', ['$compile', '$parse', function ($compile, $parse) {
	    return {
	        restrict: 'A',
	        terminal: true,
	        priority: 100000,
	        link: function (scope, elem) {
	            var name = $parse(elem.attr('dynamic-include'))(scope);
	            elem.removeAttr('dynamic-include');
	            elem.attr('ng-include', '\'' + name + '\'');
	            $compile(elem)(scope);
	        }
	    };
	}])
	.directive('dynamicHide', ['$compile', '$parse', function ($compile, $parse) {
	    return {
	        restrict: 'A',
	        terminal: true,
	        priority: 100000,
	        link: function (scope, elem) {
	            var name = $parse(elem.attr('dynamic-hide'))(scope);
	            elem.removeAttr('dynamic-hide');
	            elem.attr('ng-hide', name);
	            $compile(elem)(scope);
	        }
	    };
	}])
	.directive('dynamicRepeat', ['$compile', '$parse', function ($compile, $parse) {
	    return {
	        restrict: 'A',
	        terminal: true,
	        priority: 100000,
	        link: function (scope, elem) {
	            var name = $parse(elem.attr('dynamic-repeat'))(scope);
	            elem.removeAttr('dynamic-repeat');
	            elem.attr('ng-repeat', name);
	            $compile(elem)(scope);
	        }
	    };
	}])
	.directive('htmlInclude', ['$compile', '$parse', function ($compile, $parse) {
	    return {
	        restrict: 'A',
	        terminal: true,
	        priority: 100000,
	        link: function (scope, elem) {
	            var name = $parse(elem.attr('html-include'))(scope);
	            elem.removeAttr('html-include');
	            elem.html($parse(scope.getTemplate(name.type))(scope));
	        }
	    };
	}]);

/* Set the width of the side navigation to 250px and the left margin of the page content to 250px */
function openNav() {
	if (document.getElementById("mySidenav")) {
		document.getElementById("mySidenav").style.width = "280px";
		document.getElementById("nestedmain").style.marginLeft = "280px";
	}
}

/* Set the width of the side navigation to 0 and the left margin of the page content to 0 */
function closeNav() {
	if (document.getElementById("mySidenav")) {
		document.getElementById("mySidenav").style.width = "0";
	}

	if (document.getElementById("nestedmain")) {
		document.getElementById("nestedmain").style.marginLeft = "0";
	}
}

var everywhere = angular.element(window.document);
everywhere.bind('click', function(event) {
	var isButtonClick = event.target.id === 'toggleMenu';
	if (!isButtonClick) {
		closeNav();
	}
});
