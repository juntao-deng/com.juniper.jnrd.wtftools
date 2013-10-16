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
    	jquery : '../../ext-lib/jquery/jquery-1.7.2',
    	underscore: '../../ext-lib/underscore/underscore',
    	backbone: '../../ext-lib/backbone/backbone',
    	bootstrap: '../../ext-lib/bootstrap/bootstrap',
    	fontawesome: '../../ext-lib/font-awesome/css/font-awesome',
    	jqueryui: '../../ext-lib/jquery/jquery-ui',
    	flot: '../../ext-lib/flot/jquery.flot',
    	inputmask : '../../ext-lib/inputmask/jquery.inputmask.bundle'
    }
});

var requireArr = ['jquery', 'underscore', 'backbone', 'bootstrap', 'jqueryui', 'base/util', 'base/mvcbase', 'css!bootstrap.css', 'css!fontawesome.css', 'css!jqueryui.css', "css!../common/common"];
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
	FwBase.Wtf.Application.init();
	window.Jnrd = FwBase;
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