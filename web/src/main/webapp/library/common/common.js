requirejs.config({
    baseUrl: window.DesignConfig ? window.DesignConfig.baseRequirePath : 'library/components',
    paths : {
    	text : '../../ext-lib/requirejs/text',
    	html : '../../ext-lib/requirejs/html',
    	css : '../../ext-lib/requirejs/css',
    	templ : '../../ext-lib/requirejs/templ',
    	jquery : '../../ext-lib/jquery/jquery-1.7.2.min',
    	underscore: '../../ext-lib/underscore/underscore',
    	backbone: '../../ext-lib/backbone/backbone',
    	bootstrap: '../../ext-lib/bootstrap/bootstrap',
    	fontawesome: '../../ext-lib/font-awesome/css/font-awesome',
    	jqueryui: '../../ext-lib/jquery/jquery-ui'
    }
});

requirejs(['jquery', 'underscore', 'backbone', 'bootstrap', 'jqueryui', 'base/util', 'css!bootstrap.css', 'css!fontawesome.css', 'css!jqueryui.css'],
	function   ($, underscore, backbone, bootstrap) {
		//for backbone templates in jsp environment
		_.templateSettings = {
			evaluate    : /\{\{([\s\S]+?)\}\}/g,
			interpolate : /\{\{=([\s\S]+?)\}\}/g,
			escape      : /\{\{-([\s\S]+?)\}\}/g
		};
		FwBase.Wtf.View.reInit();
		window.Jnrd = FwBase;
	}
);