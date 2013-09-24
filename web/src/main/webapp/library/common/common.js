FwBase = {};
FwBase.Wtf = {};
FwBase.Wtf.View = {
	navigateTo : function(url, reqData, useFrame) {
		if(useFrame == null)
			useFrame = false;
		if(useFrame == false){
			$.ajax({url: url, 
				type: 'GET', 
				data: reqData, 
				dataType: 'html', 
				timeout: 1000, 
				error: function(){
					this.updateContent('page request error!');
				}, 
				success: function(result){
					this.updateContent(result);
				},
				updateContent: function(content){
					var container = $('#home_content_part');
					container.html(content);
				}
			}); 
		}
		else{
			var iframe = document.createElement("iframe");
			//todo reqData
			iframe.src = url;
			iframe.className = "contentiframe";
			iframe.style.width = (document.documentElement.clientWidth - 240 - 32) + "px";
			iframe.style.height = (document.documentElement.clientHeight - 106) + "px";
			
			var container = $('#home_content_part');
			container.empty();
			container.append(iframe);
		}
	},
	
	createControl : function(obj) {
		if(obj == null){
			alert("illegal null arguments");
			return;
		}
		var type = obj.type;
		if(type == null || type == ""){
			alert("type attribute is needed");
			return;
		}
		
		requirejs(["text!" + type + "/" + type + ".html"], function(html){
			var template = $('#sys_atom_controls_' + type);
			if(template.length == 0){
				$('body').append(html);
			}
			requirejs([type + "/" + type], function(module){
				var id = obj.id;
				var container = null;
				if(id != null && id != ""){
					container = $('#' + id);
				}
				else{
					container = $("[wtftype='" + type + "']");
				}
				var capStr = FwBase.Wtf.Lang.Utils.capitalize(type);
				FwBase.Wtf.View.Controls[capStr].create(container, obj.objMeta);
			});
		});
	}
};
FwBase.Wtf.Lang = {};
FwBase.Wtf.View.Controls = {};

FwBase.Wtf.Lang.Utils = {
	capitalize: function(str){
		if(str == null || str == "")
			return str;
		var firstChar = str.substring(0,1).toUpperCase();
		var postStr = str.substring(1);
		return firstChar + postStr;
	}
};


var Jnrd = FwBase;

//for backbone templates
_.templateSettings = {
	evaluate    : /\{\{([\s\S]+?)\}\}/g,
	interpolate : /\{\{=([\s\S]+?)\}\}/g,
	escape      : /\{\{-([\s\S]+?)\}\}/g
};


requirejs.config({
    baseUrl: 'library/controls',
    paths : {
    	text : '../../ext-lib/requirejs/text',
    	jquery : '../../ext-lib/jquery/jquery-1.7.2.min'
    }
});

function loadCss(url) {
    var link = document.createElement("link");
    link.type = "text/css";
    link.rel = "stylesheet";
    link.href = url;
    document.getElementsByTagName("head")[0].appendChild(link);
}