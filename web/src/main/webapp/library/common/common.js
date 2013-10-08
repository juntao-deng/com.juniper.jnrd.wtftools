function testClientMode() {
	if(window.location.href.indexOf('file://') == 0)
		return true;
	return false;
}
window.clientMode = testClientMode();
if(window.clientMode && window.ClientConfig == null){
	window.ClientConfig = {
		baseRequirePath : 'library/components',
		localPrefix : {
			html : "html",
			template : "templ",
			js : 'js'
		},
		prefix : function(key, ori){
			return this.localPrefix[key] ? this.localPrefix[key] : ori;
		}
	};
}

requirejs.config({
    baseUrl: window.ClientConfig ? window.ClientConfig.baseRequirePath : 'library/components',
    paths : {
    	text : '../../ext-lib/requirejs/text',
    	html : '../../ext-lib/requirejs/html',
    	css : '../../ext-lib/requirejs/css',
    	js : '../../ext-lib/requirejs/js',
    	templ : '../../ext-lib/requirejs/templ',
    	jquery : '../../ext-lib/jquery/jquery-1.7.2.min',
    	underscore: '../../ext-lib/underscore/underscore',
    	backbone: '../../ext-lib/backbone/backbone',
    	bootstrap: '../../ext-lib/bootstrap/bootstrap',
    	fontawesome: '../../ext-lib/font-awesome/css/font-awesome',
    	jqueryui: '../../ext-lib/jquery/jquery-ui'
    }
});
requirejs(['jquery', 'underscore', 'backbone', 'bootstrap', 'jqueryui', 'base/util', 'base/mvcbase', 'css!bootstrap.css', 'css!fontawesome.css', 'css!jqueryui.css'],
	function   ($, underscore, backbone, bootstrap) {
		//for backbone templates in jsp environment
		_.templateSettings = {
			evaluate    : /\{\{([\s\S]+?)\}\}/g,
			interpolate : /\{\{=([\s\S]+?)\}\}/g,
			escape      : /\{\{-([\s\S]+?)\}\}/g
		};
		FwBase.Wtf.Application.init();
		window.Jnrd = FwBase;
	}
);

window.wtfTempLoaderContainer = [];
function getWtfTempLoaderIframe(){
	for(var i = 0; i < window.wtfTempLoaderContainer.length; i ++){
		if(window.wtfTempLoaderContainer[i].inUsing == false){
			window.wtfTempLoaderContainer[i].inUsing = true;
			return window.wtfTempLoaderContainer[i];
		}
	}
	//not find
	var iframe = document.createElement("iframe");
	iframe.style.position = "absolute";
	iframe.style.height = "1px";
	iframe.style.width = "0px";
	iframe.style.left = "-100px";
	document.body.appendChild(iframe);
	iframe.inUsing = true;
	window.wtfTempLoaderContainer.push(iframe);
	return iframe;
}