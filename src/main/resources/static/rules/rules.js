/**
 * The controller doesn't do much more than setting the initial data model
 */
angular.module("demo").controller("RulesDemoController",  ['$scope','$http','$location','sharedProperties',
		function($scope,$http,$location,sharedProperties) {


	var data= {
			"layout" : [ {
				"type" : "section",
				"id" : "4",
				"section":"section",
				"label" : "Condition Section",
				"htmlID" : "htmlID",
				 "columns": [
		                        [
		                            {
		                                "type": "header",
		                                "id": "11"
		                            }
		                            
		                            
		                        ],
		                     
		                    ]
				

			} ,
			
			 {
				"type" : "section",
				"id" : "4",
				"section":"section",
				"label" : "Decision Section",
				"htmlID" : "htmlID",
				 "columns": [
		                        [
		                            {
		                                "type": "header",
		                                "id": "11"
		                            }
		                            
		                            
		                        ],
		                     
		                    ]
				

			} 
			] 
		};
	layoutdata = data;

$scope.screenname = '';
			$scope.models = {
				screenname:sharedProperties.getProperty(),
				selected : null,
				templates : [ 
				 {
                    "type": "DecisionCondition",
                    "id": 9,
                    "section":"section",
                    "columns": [
                            [
                            {
                                "type": "gridheader",
                                "id": "11",
                                "label":"Create Decision"
                            }
                          
                            
                            
                            
                        ],
                        
                        [
                            {
        						"type": "dropdown",
        						"id": 9,
        						"htmlID": "condition.id",
        						"options": [{
        							"display": "Set Question Value",
        							"value": "SETQ"
        						}, {
        							"display": "Show Section",
        							"value": "showSection"
        						}, {
        							"display": "Hide Question",
        							"value": "hideSection"
        						}],
        						"tempdisplay": "",
        						"tempoption": ""
        					}
                       ]
                        	,
                        
                        [
                         {
						"type": "textbox",
						"id": 5,
						"length": "10",
						"htmlID": "questID",
						"placeHolder" :"QID:Value"
					}
					
                       ],
                     
                    ]
                },
                
                {
                    "type": "QuestionCondition",
                    "grid":"grid",
                    "id": 10,
                    "columns": [
                        [
                            {
                                "type": "gridheader",
                                "id": "11",
                                "label":"QuestionID"
                            },
                            {
                                "type": "gridheader",
                                "id": "11",
                                "label":"answerValue"
                            }
                            
                            
                            
                        ],
                        
                        [
                         {
                                "type": "gridheader",
                                "id": "11",
                                "label":"equals TO"
                            }, 
                            {
        						"type": "dropdown",
        						"id": 9,
        						"htmlID": "condition.id",
        						"options": [{
        							"display": "<",
        							"value": "L"
        						}, {
        							"display": ">",
        							"value": "G"
        						}, {
        							"display": "=",
        							"value": "EQ"
        						}],
        						"tempdisplay": "",
        						"tempoption": ""
        					}
                       ]
                        	,
                        
                        [
                         {
						"type": "textbox",
						"id": 5,
						"length": "5",
						"htmlID": "questID"
					},
					{
						"type": "textbox",
						"id": 5,
						"length": "5",
						"htmlID": "answerVal"
					}
					
                       ]
                        
                      
                        
                    ]
                }

				],

				dropzones : layoutdata
			};
			
			 
			$scope.$watch('models.dropzones', function(model) {
				$scope.modelAsJson = angular.toJson(model, true);
				//$scope.apply();
			}, true);
			 $scope.saveLayout = function() {
				 var url = 'rest/rulemodel/save?key='+ $scope.models.screenname; 
				 $http.post(url,$scope.modelAsJson).
				  success(function(response) {
					  $scope.categories=response;
				  });
			    };
			    
				 $scope.addOption = function() {
					 $scope.models.selected.options.push({'display': $scope.models.selected.tempdisplay,'value':$scope.models.selected.tempoption});
					 
					 $scope.models.selected.tempdisplay='';
					 $scope.models.selected.tempoption = '';
					 
				    };
				    
				    $scope.previewScreen = function() {
				    	var screen  = $scope.models.screenname
				    	sharedProperties.setScreenName(screen);
				    	
						$location.path('/preview');
						 
					    };
			$scope.operators = [ {
				value : 'Set Value',
				label : 'Sample'
			}, {
				value : 'Set Value',
				label : ' Set Value'
			} 
		]}]);