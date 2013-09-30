define(function(){
	window.globalEmptyObj = {};
	String.prototype.endWith = function(s){
		if(s==null || s=="" || this.length == 0 || s.length>this.length)
			return false;
		if(this.substring(this.length-s.length) == s)
			return true;
		else
			return false;
		return true;
	};

	String.prototype.startWith = function(s){
		if(s==null||s==""||this.length==0||s.length>this.length)
			return false;
		if(this.substr(0,s.length)==s)
			return true;
		else
			return false;
		return true;
	 };
	
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
	 	
	 	showDialog : function(url, reqData, options){
	 		$.ajax({url: url, 
	 			type: 'GET', 
	 			data: reqData, 
	 			dataType: 'html', 
	 			timeout: 1000, 
	 			error: function(){
	 				this.updateContent('page request error!');
	 			}, 
	 			success: function(result){
	 				this.popDialog(result);
	 			},
	 			updateContent: function(content){
	 				var container = $('#home_content_part');
	 				container.html(content);
	 			},
	 			popDialog : function(result){
	 				$('#sys_modal_dialog > .modal-body > p').html(result);
	 				var title = "Title";
	 				if(options != null && options.title != null)
	 					title = options.title;
	 				$('#sys_modal_dialog_label').html(title);
	 				$('#sys_modal_dialog').modal();
	 			}
	 		}); 
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
	 					alert("object meta must contain id attribute");
	 					return;
	 				}
	 				if(!container[0]){
	 					alert("can not find element by id:" + id);
	 					return;
	 				}
	 				var capStr = FwBase.Wtf.Lang.Utils.capitalize(type);
	 				var ctrl = new FwBase.Wtf.View.Controls[capStr](container, obj.objMeta, obj.id);
	 				FwBase.Wtf.View._controlMap[ctrl.id, ctrl];
	 			});
	 		});
	 	},
	 	
	 	getControl : function(id){
	 		return FwBase.Wtf.View._controlMap[id];
	 	},
	 	reInit : function() {
	 		FwBase.Wtf.View.parseTemplate(FwBase.Wtf.View.parseHtml);
	 	},
	 	
	 	parseTemplate : function(callback){
	 		var requireList = [];
	 		var requireCssList = [];
	 		var requireContainer = [];
	 		FwBase.Wtf.View.detectTemplate(requireList, requireCssList, requireContainer);
	 		if(requireList.length > 0)
	 			FwBase.Wtf.View.doParseTemplate(requireList, requireCssList, requireContainer, callback);
	 		else
	 			callback();
	 	},
	 	
	 	doParseTemplate : function(requireList, requireCssList, requireContainer, callback){
	 		requirejs(requireCssList, function(){
	 			requirejs(requireList, function(){
	 				for(var i = 0; i < arguments.length; i ++){
	 					requireContainer[i].html(arguments[i]);
	 				}
	 				requireList = [];
	 				requireCssList = [];
	 				requireContainer = [];
	 				FwBase.Wtf.View.detectTemplate(requireList, requireCssList, requireContainer);
	 				if(requireList.length > 0)
	 					FwBase.Wtf.View.doParseTemplate(requireList, requireCssList, requireContainer, callback);
	 				else
	 					callback();
	 			});
	 		});
	 	},
	 	detectTemplate : function(requireList, requireCssList, requireContainer) {
	 		var prefix = window.DesignMode ? DesignConfig.prefix("template", "text") : "text";
	 		$("[wtftype='template']").each(function(ele){
	 			if($(this).attr('wtfdone') != null)
	 				return;
	 			$(this).attr('wtfdone', 'done');
	 			var templateId = $(this).attr("wtfmetadata");
	 			if(templateId == null || templateId == "")
	 				return;
	 			var urlStr = "../../templates/" + templateId + "/" + templateId;
	 			requireList.push(prefix + "!" + urlStr + ".html");
	 			requireCssList.push("css!" + urlStr);
	 			requireContainer.push($(this));
	 		});
	 	},
	 	parseHtml : function() {
	 		var typeList = [];
	 		var requireList = [];
	 		var requireHtmlList = [];
	 		var prefix = window.DesignMode ? DesignConfig.prefix("html", "text") : "text";
	 		$("[wtftype]").each(function(){
	 			if($(this).attr('wtfdone') != null)
	 				return;
	 			var wtfType = $(this).attr('wtftype');
	 			typeList.push(wtfType);
	 			requireList.push(wtfType + "/" + wtfType);
	 			requireHtmlList.push(prefix + "!" + wtfType + "/" + wtfType + ".html");
	 		});
	 		if(requireList.length > 0){
	 			requirejs(requireHtmlList, function(){
	 				for(var i = 0; i < arguments.length; i ++){
	 					var template = $('#sys_atom_controls_' + typeList[i]);
	 					if(template.length == 0){
	 						$('body').append(arguments[i]);
	 					}
	 				}
	 				requirejs(requireList, function() {
	 					$("[wtftype]").each(function(){
	 						if($(this).attr('wtfdone') != null)
	 							return;
	 						$(this).attr('wtfdone', 'done');
	 						var wtfType = $(this).attr('wtftype');
	 						var id = $(this).attr("id");
	 						if(id == null){
	 							alert("id can not be null for element with wtftype:" + wtfType);
	 							return;
	 						}
	 						var objMeta = null;
	 						var metadataStr = $(this).attr("wtfmeta");
	 						if(metadataStr == null || metadataStr == "")
	 							objMeta = window.globalEmptyObj;
	 						else
	 							objMeta = $.paraseJSON(metadataStr);
	 						var capStr = FwBase.Wtf.Lang.Utils.capitalize(wtfType);
	 						var ctrl = new FwBase.Wtf.View.Controls[capStr]($(this), objMeta, id);
	 						FwBase.Wtf.View._controlMap[ctrl.id, ctrl];
	 					});
	 				});
	 			});
	 			
	 		}
	 	}
	 };

	 FwBase.Wtf.View._controlMap = {};
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
});