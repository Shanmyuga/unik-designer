angular.module('Home', [])
    .service('sharedProperties', function () {
        var property = 'First';
        var screenName = 'First';
        var data = 'First';
        var layoutId = '';
        var params = [];
        return {
            getProperty: function () {
                return property;
            },
            setProperty: function(value) {
                property = value;
            },
            getData: function () {
                return data;
            },
            setData: function(value) {
                data = value;
            },
            getScreenName: function () {
                return screenName;
            },
            setScreenName: function(value) {
            	screenName = value;
            },
            getLayoutId: function () {
                return layoutId;
            },
            setLayoutId: function(value) {
            	layoutId = value;
            },
            getParams: function () {
                return params;
            },
            setParams: function(value) {
            	params = value;
            }
        };
    });