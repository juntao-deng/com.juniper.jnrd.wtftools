if(window.clientMode && window.ClientConfig == null){
	window.ClientConfig = {
		contextPaths : {
		},
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

if(window.contextMappings){
	window.requirelibs = {};
	window.restlibs = {};
	for(var i in window.contextMappings){
		requireUtil = require.config({
			context : 'context' + i,
			baseUrl: window.contextMappings[i],
			paths : {
				css : window.frameworkPath + "ext-lib/requirejs/css",
				templ : window.frameworkPath + "ext-lib/requirejs/templ",
				text : window.frameworkPath + "ext-lib/requirejs/text",
			}
		});
		window.requirelibs[i] = requireUtil;
		window.restlibs[i] = window.contextMappings[i] + "rest/";
	}
	
}

window.globalRequireConfig = {
    baseUrl: window.frameworkPath + 'library/components',
    paths : {
    	text : '../../ext-lib/requirejs/text',
    	html : '../../ext-lib/requirejs/html',
    	css : '../../ext-lib/requirejs/css',
    	js : '../../ext-lib/requirejs/js',
    	templ : '../../ext-lib/requirejs/templ',
    	jquery : '../../ext-lib/jquery/jquery-1.7.2',
    	jqueryjson : '../../ext-lib/jquery/jquery-json-2.4',
    	underscore: '../../ext-lib/underscore/underscore',
    	backbone: '../../ext-lib/backbone/backbone',
    	bootstrap: '../../ext-lib/bootstrap/bootstrap',
    	fontawesome: '../../ext-lib/font-awesome/css/font-awesome',
    	jqueryui: '../../ext-lib/jquery/jquery-ui',
    	jqueryuibootstrap: '../../ext-lib/jquery/jquery-ui-bootstrap',
    	flot: '../../ext-lib/flot/jquery.flot',
    	flotpie: '../../ext-lib/flot/jquery.flot.pie',
    	flotcategories: '../../ext-lib/flot/jquery.flot.categories',
    	inputmask : '../../ext-lib/inputmask/jquery.inputmask.bundle'
    }
};

requirejs.config(window.globalRequireConfig);

var requireArr = ['jquery', 'underscore', 'backbone', 'bootstrap', 'jqueryui', 'jqueryjson', 'base/util', 'base/mvcbase', 'css!bootstrap.css', 'css!fontawesome.css', 'css!jqueryuibootstrap.css', "css!../common/common"];
requirejs(requireArr,
	function   ($, underscore, backbone, bootstrap) {
		var arr = [];
		if(window.extendArr)
			arr = arr.concat(window.extendArr);
		if($.browser.msie && $.browser.version < 9)
			arr.push("../iepatch/html5", "../../ext-lib/flot/excanvas", "css!../iepatch/patchie8");
		if(arr.length > 0)
			requirejs(arr, commonCallback);
		else
			commonCallback();
	}
);

function requireComponent(typeList, func){
	if(typeList.length == 0){
		if(func)
			func();
		return;
	}
	var prefix = window.ClientConfig ? ClientConfig.prefix("html", "text") : "text";
	var requireList = [];
	var requireHtmlList = [];
	for(var i = 0; i < typeList.length; i ++){
		var wtfType = typeList[i];
		requireList.push(wtfType + "/" + wtfType);
		requireHtmlList.push(prefix + "!" + wtfType + "/" + wtfType + ".html");
	}
	if(requireList.length > 0){
		requirejs(requireHtmlList, function(){
			for(var i = 0; i < arguments.length; i ++){
				var template = $('#sys_atom_controls_' + typeList[i]);
				if(template.length == 0){
					$('body').append(arguments[i]);
				}
			}
			requirejs(requireList, function() {
				if(func)
					func();
			});
		});
	}
}

function wdefine(req, func){
	if(typeof req == "function"){
		return define(function(){
			return {
				exec : req
			};
		});
	}
	else{
		return define(req, function(){
			return {
				exec : func
			};
		});

	}
	
}

function commonCallback() {
	//for backbone templates in jsp environment
	_.templateSettings = {
		evaluate    : /\{\{([\s\S]+?)\}\}/g,
		interpolate : /\{\{=([\s\S]+?)\}\}/g,
		escape      : /\{\{-([\s\S]+?)\}\}/g
	};
	var callback = null;
	if(window.DesignMode)
		callback = FwBase.Wtf.Design.DesignSupport.designable;
	FwBase.Wtf.Application.init(callback);
	window.Jnrd = FwBase;
	//preload some componenets, TODO
	requireComponent(["button"]);
}

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