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
			paths : getSysArray(window.frameworkPath)
		});
		window.requirelibs[i] = requireUtil;
		window.restlibs[i] = window.contextMappings[i] + "rest/";
	}
	
}

window.globalRequireConfig = {
    baseUrl: window.frameworkPath + 'library/components',
    paths : getSysArray("../../")
};

function getSysArray(prefix){
	return {
    	text : prefix + 'ext-lib/requirejs/text',
    	html : prefix + 'ext-lib/requirejs/html',
    	css : prefix + 'ext-lib/requirejs/css',
    	js : prefix + 'ext-lib/requirejs/js',
    	templ : prefix + 'ext-lib/requirejs/templ',
    	jquery : prefix + 'ext-lib/jquery/jquery-1.7.2',
    	jqueryjson : prefix + 'ext-lib/jquery/jquery-json-2.4',
    	underscore: prefix + 'ext-lib/underscore/underscore',
    	backbone: prefix + 'ext-lib/backbone/backbone',
    	bootstrap: prefix + 'ext-lib/bootstrap/bootstrap',
    	fontawesome: prefix + 'ext-lib/font-awesome/css/font-awesome',
    	jqueryui: prefix + 'ext-lib/jquery/jquery-ui',
    	jqueryuibootstrap: prefix + 'ext-lib/jquery/jquery-ui-bootstrap',
    	highcharts: prefix + 'ext-lib/highcharts/js/highcharts',
    	inputmask : prefix + 'ext-lib/inputmask/jquery.inputmask.bundle',
    	storage : prefix + 'rest/storage',
    	mvc_base : prefix + 'library/components/base/mvc_base',
    	mvc_app : prefix + 'library/components/base/mvc_app',
    	mvc_view : prefix + 'library/components/base/mvc_view',
    	mvc_model : prefix + 'library/components/base/mvc_model',
    	mvc_widget : prefix + 'library/components/base/mvc_widget',
    	mvc_restapi : prefix + 'library/components/base/mvc_restapi'
    }
}

requirejs.config(window.globalRequireConfig);

requirejs(['jquery'], function(){
	var requireArr = ['underscore', 'backbone', 'bootstrap', 'jqueryui', 'jqueryjson', 'base/util', 'base/mvcbase', 'css!bootstrap.css', 'css!fontawesome.css', 'css!jqueryuibootstrap.css', "css!../common/common"];
	requirejs(requireArr, function() {
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
});


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

function getComponentDependences(type) {
	if(type.startWith('input'))
		return ['input_base'];
	else if(type.startWith('chart'))
		return ['chart_base'];
	else if(type == 'form'){
		return ['input_base', 'input', 'input_autocomplete', 'input_checkbox', 'input_checkboxgroup', 'input_date', 'input_daterange', 'input_datetime',
		        'input_dropdown', 'input_file', 'input_highlight', 'input_integer', 'input_ip', 'input_radiogroup', 'input_search', 'input_select', 
		        'input_textarea', 'input_toggle'];
	}
	return [];
}