angular.module("Authentication", []);
angular.module("Preview", []);
angular.module("Config", []);
angular.module("DefaultConfig", []);
angular.module("FormComponent", []);
angular.module("UserComponent", []);
angular.module("demo", [ "Authentication",
	"Home", "Config", "DefaultConfig", "FormComponent", "UserComponent", "ngRoute","ngCookies", "dndLists"])
	.config(function($routeProvider) {
		$routeProvider
			.when('/simple', {
				templateUrl: 'simple/simple-frame.html',
				controller: 'SimpleDemoController'
			})
			.when('/nested', {
				templateUrl: 'nested/nested-frame.html',
				controller: 'NestedListsDemoController'
			})
			.when('/types', {
				templateUrl: 'types/types-frame.html',
				controller: 'TypesDemoController'
			})
			.when('/advanced', {
				templateUrl: 'advanced/advanced-frame.html',
				controller: 'AdvancedDemoController'
			}) 
			.when('/rules', {
				templateUrl: 'rules/nested-frame.html',
				controller: 'RulesDemoController'
			}) 
			.when('/login', {
				templateUrl: 'authentication/views/login.html',
				controller: 'LoginController'
			})
			.when('/home', {
				controller: 'HomeController',
				templateUrl: 'home/views/home.html'
			})
			.when('/config', {
				controller: 'ConfigController',
				templateUrl: 'config/config.html'
			})
			.when('/defaultconfig', {
				controller: 'DefaultConfigController',
				templateUrl: 'defaultconfig/defaultconfig.html'
			})
			.when('/formcomponent', {
				controller: 'FormComponentController',
				templateUrl: 'formcomponent/formcomponent.html'
			})
			.when('/user', {
				controller: 'UserController',
				templateUrl: 'user/user.html'
			})
			.when('/userprofile', {
				controller: 'UserController',
				templateUrl: 'user/user.html'
			})
			.when('/preview', {
				controller: 'PreviewController',
				templateUrl: 'preview/views/preview.html'
			})
			.when('/modelCreator', {
				templateUrl: 'advanced/advanced-frame.html',
				controller: 'ModelController'
			})
			.when('/download', {
				templateUrl: 'download/download.html',
				controller: 'HomeController'
			})
			.otherwise({redirectTo: '/login'});
	})

	.directive('navigation', function($rootScope, $location) {
		return {
			template: '<li ng-repeat="option in options" ng-class="{active: isActive(option)}">' +
			'    <a ng-href="{{option.href}}">{{option.label}}</a>' +
			'</li>',
			link: function (scope, element, attr) {
				scope.options = [
					{label: "Nested Containers", href: "#/nested"},
					{label: "Simple Demo", href: "#/simple"},
					{label: "Item Types", href: "#/types"},
					{label: "Advanced Demo", href: "#/advanced"},
					{label: "Github", href: "https://github.com/marceljuenemann/angular-drag-and-drop-lists"}
					];

				scope.isActive = function(option) {
					return option.href.indexOf(scope.location) === 1;
				};

				$rootScope.$on("$locationChangeSuccess", function(event, next, current) {
					scope.location = $location.path();
				});
			}
		};
	})
	.run(['$rootScope', '$location', '$cookieStore', '$http',
		function ($rootScope, $location, $cookieStore, $http) {
		// keep user logged in after page refresh
		$rootScope.globals = $cookieStore.get('globals') || {};
		if ($rootScope.globals.currentUser) {
			$http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.globals.currentUser.authdata; // jshint ignore:line
		}

		$rootScope.$on('$locationChangeStart', function (event, next, current) {
			// redirect to login page if not logged in
			if ($location.path() !== '/login' && !$rootScope.globals.currentUser) {
				$location.path('/login');
			}
		});
	}])
