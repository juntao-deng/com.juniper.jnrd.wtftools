define(["../uipattern/buttonmanager"], function(){
	 window.AppUtil = FwBase.Wtf.Application = function(id){
  		 this.id = id;
  		 this.viewMap = {};
  	 	 this.modelMap = {};
  	 	 this.componentsMap = {};
  	 	 this.metadataMap = {};
  	 	 this.widgetMap = {};
  	 	 this.dataObj = {};
  	 	 this.requestData = {};
  	 };
  	 
  	 _.extend(FwBase.Wtf.Application.prototype, Backbone.Events);
  	 
  	 $.extend(FwBase.Wtf.Application, {
  		applicationMap : {},
  	 	currentApplication : null,
  	 	application : function(app) {
  	 		if(app instanceof String)
  	 			return FwBase.Wtf.Application.applicationMap[app];
  	 		AppUtil.applicationMap[app.id, app];
  	 	},
  	 	current : function(app){
  	 		if(app != null){
  	 			AppUtil.application(app);
  	 			AppUtil.currentApplication = app;
  	 		}
  	 		return AppUtil.currentApplication;
  	 	},
  	 	init : function(callback) {
  	  		 $("[wtftype='application']").each(function(){
  	  			var appid = $(this).attr("wtfmetadata");
  	  			if(appid == null || appid == ""){
  	  				alert("wtftype='application' must has an wtfmetadata for id");
  	  				return;
  	  			}
  	  			$(this).attr('wtfdone', 'done');
  	  			AppUtil.navigateTo(appid, null, {callback : callback, homeApp : true});
  	  		 });
  	 	},
  	 	popStack : function() {
  	 		AppUtil.removeTitle($app.uniqueId);
  	 		$app.close();
  	 	},
  	 	navigateToStack : function(url, reqData, options){
  	 		var stacks = 1;
  	 		var currApp = $app;
			var lastStackApp = null;
  	 		while(!currApp.baseApp){
  	 			if(currApp.stackApp){
  	 				if(lastStackApp == null)
						lastStackApp = currApp;
  	 				stacks ++;
  	 			}
  	 			currApp = currApp.parent;
  	 		}
			if(lastStackApp == null)
				lastStackApp = currApp;
  	 		var stackIndex = 100 * stacks;
  	 		var	container = $('#sys_home_content_part');
  	 		var width = container.innerWidth();
  	 		var height = container.innerHeight();
  	 		var div = $("<div></div>").css(
  	 										{"z-index": stackIndex, "background": "#FFF", 
  	 										"position": "absolute", "left": "0px", "top": "0px", 
  	 										"width":(width + "px"), "min-height": (height + "px")
  	 										}
  	 									);
  	 		container.append(div);
  	 		div.fadeIn("slow");
  	 		options.container = div;
  	 		options.stackApp = true;
  	 		options.parent = lastStackApp;
  	 		options.baseApp = false;
  	 		AppUtil.navigateTo(url, reqData, options);
  	 		div.attr('id', "stack_container_" + $app.uniqueId);
  	 		$app.stackContainer = div;
  	 	},
  	 	navigateToDialog : function(url, reqData, options){
  	 		options = options || {};
  	 		options.baseApp = false;
	 		requireComponent(['dialog'], function(){
		 		var dialog = FwBase.Wtf.View.Controls.Dialog.getDialog();
		 		dialog.visible(true, options);
		 		dialog.mask();
		 		var container = dialog.bodyContainer();
		 		dialog.once("close", function(){
		 			this.ctx.clean();
		 		});
		 		
		 		options.parent = $app;
		 		options.dialog = dialog;
		 		AppUtil.doNavigateTo(url, reqData, options, container);
		 		dialog.ctx = $app;
	 		});
	 	},
  	 	navigateTo : function(url, reqData, options) {
  	 		if(window.$app != null && !$app.homeApp)
  	 			$app.close();
  	 		if(options == null)
  	 			options = {};
  	 		var container = options.container;
  	 		if(container == null)
  	 			container = $('#sys_home_content_part');
			if(container.length == 0){
				container = $("#sys_design_home_content_part");
			}
			if(options.baseApp == null)
				options.baseApp = true;
			AppUtil.doNavigateTo(url, reqData, options, container);
			$app.homeApp = options.homeApp;
			var title = options.title;
			if(title == null || title == ""){
				var pathInfo = url.split("/");
				title = FwBase.Wtf.Lang.Utils.capitalize(pathInfo[pathInfo.length - 1]);
			}
			if(options.stackApp){
				AppUtil.appendTitle({title: title, url: url, uniqueId: $app.uniqueId});
			}
			else
				AppUtil.updateTitle({title: title, url: url, uniqueId: $app.uniqueId});
	 	},
	 	
	 	doNavigateTo : function(url, reqData, options, container) {
	 		//loadFromUrl
	 		var uniqueId = getUniqueId(url);
	 		var pathInfo = url.split("/");
	 		
	 		var app = new AppUtil(url);
			app.webroot = pathInfo[0];
			app.baseApp = options.baseApp;
			app.stackApp = options.stackApp;
			if(options)
				app.parent = options.parent;
			app.dialog = options.dialog;
			app.reqData(reqData);
			app.uniqueId = uniqueId;
			AppUtil.current(app);
			window.$app = app;
	 		
			var prefix = window.ClientConfig ? ClientConfig.prefix("template", "text") : "text";
			prefix += "!";
//			var rb = "applications/";
			var rqhtml = "";
			for(var i = 1; i < pathInfo.length; i ++){
				rqhtml += pathInfo[i] + "/";
			}
			var requireUtil = requirejs;
			if(window.requirelibs){
				requireUtil = requirelibs[pathInfo[0]];
			}
			var htmlArr = [prefix + rqhtml + pathInfo[pathInfo.length - 1] + ".html"];
			var modelJsArr = [rqhtml + "model"];
			var controllerArr = [rqhtml + "controller"];
			requireUtil(htmlArr, function(html){
				
				if(window.DesignMode && app.dialog == null){
					html = '<div wtftype="container" style="height:99%;min-height:300px">' + html + "</div>";
				}
				if(container.length == 0){
					container = $(document.body);
					container.append(html);
				}
				else{
					if(app.dialog)
						app.dialog.content(html);
					else
						container.html(html);
				}
				//load contents and at last execute the app logic
				//var modelJs = rqhtml + "model";
				requireUtil(modelJsArr, function(){
					var modelFunc = arguments[0];
					function appModelCallback() {
						if(modelFunc != null)
							modelFunc.exec();
					}
					function appCallback(){
	 					var controllerJs = rqhtml + "controller";
	 					requireUtil(controllerArr, function() {
	 						if(arguments[0] != null)
	 							arguments[0].exec();
	 						var models = $app.models();
					 		for(var i = 0; i < models.length; i ++)
					 			models[i].toInit();
					 		if(container.notifyContentChange)
								container.notifyContentChange();
					 		$app.trigger('loaded');
	 					});
					};
//					var oriCallback = options ? options.callback : null;
//					var callback = null;
//					if(oriCallback){
//						callback = function() {
//							appCallback();
//	 						oriCallback();
//						}
//					}
//					else
//						callback = appCallback;
					var callbacks = {oriCallback: options.callback, appModelCallback: appModelCallback, appCallback: appCallback};
					AppUtil.repaint(callbacks);
				});
				//};
				
			});
	 	},
  		 	
  		updateTitle : function(titleInfo){
  			var titleZone = $('#sys_page_title');
  			if(titleZone.length > 0)
  				titleZone.html(titleInfo.title);
  			$("#sys_app_breadcrumb li:gt(1)").remove();
  			var breadcrumb = $("#sys_app_breadcrumb li:eq(1)");
  			if(breadcrumb.length > 0){
	  			breadcrumb[0].originalUrl = titleInfo.url;
	  			breadcrumb[0].originalTitle = titleInfo.title;
	  			breadcrumb.attr('id', 'breadcrumb_' + titleInfo.uniqueId);
	  			breadcrumb.html(titleInfo.title);
  			}
  		},
  		
  		appendTitle : function(titleInfo){
  			var titleZone = $('#sys_page_title');
  			if(titleZone.length > 0)
  				titleZone.html(titleInfo.title);
  			var last = $("#sys_app_breadcrumb li:last");
  			last.html("<a href=\"javascript:AppUtil.lookupInStack('" + last[0].originalUrl + "')\">" + last.html() + "</a><span class=\"divider\">/</span>");
  			var newAdd = last.clone();
  			newAdd.html(titleInfo.title);
  			newAdd[0].originalUrl = titleInfo.url;
  			newAdd[0].originalTitle = titleInfo.title;
  			newAdd.attr('id', 'breadcrumb_' + titleInfo.uniqueId);
  			$("#sys_app_breadcrumb").append(newAdd);
  		},
  		removeTitle : function(uniqueid) {
  			var curr = $('#breadcrumb_' + uniqueid);
  			var prev = curr.prev();
  			prev.html(prev[0].originalTitle);
  			curr.remove();
  		},
  		
	 	closeDialog : function() {
	 		var dialog = FwBase.Wtf.View.Controls.Dialog.closeDialog();
	 	},
  		 	
	 	getControl : function(id){
	 		return AppUtil._controlMap[id];
	 	},
	 	repaint : function(callbacks) {
	 		if(callbacks == null || typeof callbacks == "function"){
	 			callbacks = {oriCallback: callbacks};
	 		}
//	 		var func = function(){
//	 			AppUtil.parseHtml(callbacks);
//	 		};
//	 		
//	 		var widgetCallback = function() {
//	 			AppUtil.parseTemplate(func);
//	 		}
//	 		
//	 		AppUtil.parseWidget(widgetCallback);
	 		AppUtil.parseTemplate(callbacks);
	 	},
	 	
	 	parseWidget : function(callbacks){
	 		var groups = [];
	 		var hasWidget = AppUtil.detectWidget(groups);
	 		if(hasWidget)
	 			AppUtil.doParseWidget(groups, callbacks);
	 		else
	 			AppUtil.parseHtml([], callbacks);
	 	},
	 	
	 	doParseWidget : function(groups, callbacks){
	 		if(callbacks == null)
	 			callbacks = {};
			for(var i in groups){
				AppUtil.doParseWidgetByGroup(i, groups[i], callbacks);
			}
	 	},
	 	
	 	doParseWidgetByGroup : function(ctx, group, callbacks){
	 		var prefix = window.ClientConfig ? ClientConfig.prefix("template", "text") : "text";
			prefix += "!";
			var rqhtml = "../widgets/";
	 		var requireUtil = requirejs;
			if(window.requirelibs){
				requireUtil = requirelibs[ctx];
			}
			var htmlArr = [];
			var modelJsArr = [];
			var controllerArr = [];
			for(var j = 0; j < group.length; j ++){
				var id = group[j].id;
				htmlArr.push(prefix + rqhtml + id + "/" + id + ".html");
				modelJsArr.push(rqhtml + id + "/model");
				controllerArr.push(rqhtml + id + "/controller");
			}
			requireUtil(htmlArr, function(){
				var containers = [];
				for(var j = 0; j < arguments.length; j ++){
					var html = arguments[j];
					var container = group[j].container;
					container.html(html);
					var id = group[j].id;
					var widget = new FwBase.Wtf.Widget(id);
					$app.widget(widget);
					container[0].ctx = widget;
					containers.push(container);
				}
				requireUtil(modelJsArr, function(){
					for(var j = 0; j < arguments.length; j ++){
						if(arguments[j] != null){
							var id = group[j].id;
							window.$widget = $app.widget(id);
							arguments[j].exec();
						}
					}
					var widgetCallback = function(){
						requireUtil(controllerArr, function() {
							for(var j = 0; j < arguments.length; j ++){
								if(arguments[j] != null){
									var id = group[j].id;
									window.$widget = $app.widget(id);
									arguments[j].exec();
									
									var models = window.$widget.models();
							 		for(var m = 0; m < models.length; m ++)
							 			models[m].toInit();
								}
							}
						});
					};
//					containers.push($(document.body));
					callbacks.widgetCallback = widgetCallback;
					AppUtil.parseHtml(containers, callbacks);
				});
			});
	 	},
	 	
	 	detectWidget : function(groups) {
	 		var has = false;
	 		var prefix = window.ClientConfig ? ClientConfig.prefix("template", "text") : "text";
	 		$("[wtftype='widget']").each(function(ele){
	 			if($(this).attr('wtfdone') != null)
	 				return;
	 			$(this).attr('wtfdone', 'done');
	 			has = true;
	 			var widgetId = $(this).attr("wtfmetadata");
	 			if(widgetId == null || widgetId == "")
	 				return;
	 			var ids = widgetId.split("/");
	 			if(ids.length == 1){
	 				ids[1] = ids[0];
	 				ids[0] = window.frameCtx;
	 			}
	 			if(groups[ids[0]] == null){
	 				groups[ids[0]] = [];
	 			}
	 			groups[ids[0]].push({id: ids[1], container: $(this)});
	 		});
	 		return has;
	 	},
	 	
	 	parseTemplate : function(callbacks){
	 		var requireList = [];
	 		var requireModelList = [];
	 		var requireControllerList = [];
	 		var requireContainer = [];
	 		AppUtil.detectTemplate(requireList, requireModelList, requireControllerList, requireContainer);
	 		AppUtil.doParseTemplate(requireList, requireModelList, requireControllerList, requireContainer, callbacks);
	 	},
	 	
	 	doParseTemplate : function(requireList, requireModelList, requireControllerList, requireContainer, callbacks){
	 		requirejs(requireList, function(){
				for(var i = 0; i < arguments.length; i ++){
					var children = requireContainer[i].children().each(function(){
						this.wtfpos = $(this).attr('for');
					});
 					if(children.length > 0)
 						children.remove();
 					requireContainer[i].html(arguments[i]);
 					var positions = requireContainer[i].find("[wtfpos]").each(function(){
 						this.wtfpos = $(this).attr('wtfpos');
 					});
 					var defaultPos = requireContainer[i].find("[wtfpos='default']");
 					if(defaultPos == null && positions.length > 0)
 						defaultPos = positions[0];
 					
 					if(positions.length > 0 && children.length > 0){
 						for(var i = 0; i < children.length; i ++){
 							var pos = children[i].wtfpos;
 							if(pos == null || pos == "default")
 								$(defaultPos).append(children[i]);
 							else{
 								for(var j = 0; j < positions.length; j ++){
 									if(pos == positions[j].wtfpos){
 										$(positions[j]).append(children[i]);
 									}
 								}
 							}
 						}
// 						for(var i = 0; i < positions.length; i ++)
// 							$(positions[i]).html(children[i]);
 					}
				}
				requirejs(requireModelList, function(){
					for(var i = 0; i < arguments.length; i ++){
						if(arguments[i] != null)
							arguments[i].exec();
					}
					if(callbacks.appModelCallback)
						callbacks.appModelCallback();
//					var requireList1 = [];
//					var requireModelList1 = [];
//					var requireControllerList1 = [];
//					var requireContainer1 = [];
//					var needCallback = false;
//					AppUtil.detectTemplate(requireList1, requireModelList1, requireControllerList1, requireContainer1);
//					if(requireList1.length == 0){
//						needCallback = true;
//					}
					var templateCallback = function(){
						requirejs(requireControllerList, function() {
							for(var i = 0; i < arguments.length; i ++){
								if(arguments[i] != null)
									arguments[i].exec();
							}
						});
					};
					
					var parseWidgetCallback = function(){
						AppUtil.parseWidget(callbacks);
					};
					callbacks.templateCallback = templateCallback;
					requireContainer.push($(document.body));
					AppUtil.parseHtml(requireContainer, {oriCallback: parseWidgetCallback});
					
				});
			});
	 	},
	 	detectTemplate : function(requireList, requireModelList, requireControllerList, requireContainer) {
	 		var prefix = window.ClientConfig ? ClientConfig.prefix("template", "text") : "text";
	 		$("[wtftype='template']").each(function(ele){
	 			if($(this).attr('wtfdone') != null)
	 				return;
	 			$(this).attr('wtfdone', 'done');
	 			var templateId = $(this).attr("wtfmetadata");
	 			if(templateId == null || templateId == "")
	 				return;
	 			var path = "../../templates/" + templateId + "/";
	 			var urlStr = path + templateId;
	 			requireList.push(prefix + "!" + urlStr + ".html");
	 			requireModelList.push(path + "model");
	 			requireControllerList.push(path + "controller");
	 			requireContainer.push($(this));
	 		});
	 	},
	 	parseHtml : function(containers, callbacks) {
	 		var typeList = [];
//	 		var requireList = [];
//	 		var requireHtmlList = [];
//	 		var prefix = window.ClientConfig ? ClientConfig.prefix("html", "text") : "text";
	 		$("[wtftype]").each(function(){
	 			if($(this).attr('wtfdone') != null)
	 				return;
	 			var wtfType = $(this).attr('wtftype');
	 			if(wtfType == 'container' || wtfType == 'widget')
	 				return;
	 			
	 			var dependency = getComponentDependences(wtfType);
	 			typeList = typeList.concat(dependency);
	 			typeList.push(wtfType);
//	 			requireList.push(wtfType + "/" + wtfType);
//	 			requireHtmlList.push(prefix + "!" + wtfType + "/" + wtfType + ".html");
	 		});
 			requireComponent(typeList, function(){
				for(var i = 0; i < containers.length; i ++){
					var container = containers[i];
					var ctx = null;
					if(container[0].ctx){
						ctx = container[0].ctx;
					}
					else
						ctx = $app;
					container.find("[wtftype]").each(function(){
						if($(this).attr('wtfdone') != null)
							return;
						var wtfType = $(this).attr('wtftype');
						if(wtfType == 'container' || wtfType == 'widget')
	 						return;
						$(this).attr('wtfdone', 'done');
						var id = $(this).attr("id");
						if(id == null){
							alert("id can not be null for element with wtftype:" + wtfType);
							return;
						}
						
						var objMeta = ctx.metadata(id);
						var attMeta = null;
						var metadataStr = $(this).attr("wtfmetadata");
	 					if(metadataStr != null && metadataStr != ""){
							try{
								eval("attMeta = " + metadataStr);
							}
							catch(error){
								alert("error while parsing wtfmeta:" + metadataStr);
							}
					    }
					    if(attMeta != null){
					    	objMeta = AppUtil.mergeMetadata(objMeta, attMeta);
					    }
					    if(objMeta == null)
	 						objMeta = window.globalEmptyObj;
						var capStr = FwBase.Wtf.Lang.Utils.capitalize(wtfType);
						var ctrl = new FwBase.Wtf.View.Controls[capStr]($(this), objMeta, id, ctx);
						ctx.component(ctrl);
					});
				}
				for(var i = 0; i < containers.length; i ++){
					var container = containers[i];
					container[0].ctx = null;
				}
				if(callbacks.widgetCallback)
					callbacks.widgetCallback();
				if(callbacks.templateCallback)
					callbacks.templateCallback();
				if(callbacks.appCallback)
					callbacks.appCallback();
				if(callbacks.oriCallback)
					callbacks.oriCallback();
 			});
	 			
  		 },
  		 mergeMetadata : function(ori, newMd){
  		 	if(ori == null)
  		 		return newMd;
  		 	for(var i in newMd){
  		 		if(typeof ori[i] == 'undefined')
  		 			ori[i] = newMd[i];
  		 	}
  		 	return ori;
  		 }
  	 });
  	 $.extend(FwBase.Wtf.Application.prototype, {
  		data : function(){
	 		if(arguments.length == 1){
	 			if(typeof arguments[0] == "string")
	 				return this.dataObj[arguments[0]];
	 			this.dataObj = arguments[0];
	 		}
	 		else
	 			this.dataObj[arguments[0]] = arguments[1];
	 	},
	 	reqData : function(){
	 		if(arguments.length == 0)
	 			return this.requestData;
	 		else if(typeof arguments[0] == "string"){
	 			return this.requestData ? this.requestData[arguments[0]] : null;
	 		}
	 		else
	 			this.requestData = arguments[0];
	 	},
//  	 	view : function(view){
//  	 		if(typeof view == "string")
//  	 			return this.viewMap[view];
//  	 		this.viewMap[view.id] = view;
//  	 	},
//  	 	removeView : function(viewId) {
//  	 		return this.viewMap[viewId];
//  	 	},
//  	 	destroyView : function(viewId){
//  	 		var view = this.removeView(viewId);
//  	 		if(view)
//  	 			view.destroy();
//  	 	},
  	 	
  	 	model : function(){
  	 		if(arguments.length == 1){
  	 			if(arguments[0] == null)
  	 				return null;
  	 			if(typeof arguments[0] == "string")
  	 				return this.modelMap[arguments[0]];
  	 			var obj = arguments[0];
  	 			if(!(obj instanceof Model))
  	 				obj = new Model(arguments[0].id, obj, arguments[0].idAttribute);
  	 			obj.ctx = this;
  	 			this.modelMap[arguments[0].id] = obj;
  	 		}
  	 		else if(arguments.length == 2){
  	 			var obj = arguments[1];
  	 			if(!(obj instanceof Model))
  	 				obj = new Model(arguments[0], obj);
  	 			obj.ctx = this;
  	 			this.modelMap[arguments[0]] = obj;
  	 		}
  	 	},
  	 	
  	 	models : function() {
  	 		return _.values(this.modelMap);
  	 	},
  	 	
  	 	removeModel : function(modelId) {
  	 		var model = this.modelMap[modelId];
  	 		if(model)
  	 			model.ctx = null;
  	 		delete this.modelMap[modelId];
  	 		return model;
  	 	},
  	 	destroyModel : function(modelId){
  	 		var model = this.removeModel(modelId);
  	 		if(model)
  	 			model.destroy();
  	 	},
  	 	component : function(){
  	 		if(arguments.length == 1){
  	 			if(arguments[0] == null)
  	 				return null;
  	 			if(typeof arguments[0] == "string")
  	 				return this.componentsMap[arguments[0]];
  	 			if(arguments[0].stateful)
  	 				this.installButtonManager();
  	 			if(this.componentsMap[arguments[0].id] != null){
  	 				alert("The component id: '" + arguments[0].id + "' already exists.");
  	 				return;
  	 			}
  	 			arguments[0].ctx = this;
  	 			this.componentsMap[arguments[0].id] = arguments[0];
  	 		}
  	 		else if(arguments.length == 2){
  	 			var obj = arguments[1];
  	 			if(obj.stateful)
  	 				this.installButtonManager();
  	 			if(this.componentsMap[arguments[0]] != null){
  	 				alert("The component id: '" + arguments[0] + "' already exists.");
  	 				return;
  	 			}
  	 			obj.ctx = this;
  	 			this.componentsMap[arguments[0]] = obj;
  	 		}
  	 	},
  	 	removeComponent : function(compId) {
  	 		var comp = this.componentsMap[compId];
  	 		delete this.componentsMap[compId];
  	 		if(comp)
  	 			comp.ctx = null;
  	 		return comp;
  	 	},
  	 	destroyComponent : function(compId){
  	 		var comp = this.removeComponent(compId);
  	 		if(comp)
  	 			comp.destroy();
  	 	},
  	 	
  	 	components : function() {
  	 		return _.values(this.componentsMap);
  	 	},
  	 	
  	 	installButtonManager: function(){
  	 		if(this.buttonManager == null){
  	 			this.buttonManager = new FwBase.Wtf.View.ButtonStateManager();
  	 		}
  	 	},
  	 	widget : function(widget){
  	 		if(arguments.length == 1){
  	 			if(arguments[0] == null)
  	 				return null;
  	 			if(typeof arguments[0] == "string")
  	 				return this.widgetMap[arguments[0]];
  	 			arguments[0].ctx = this;
  	 			this.widgetMap[arguments[0].id] = arguments[0];
  	 		}
  	 		else if(arguments.length == 2){
  	 			var obj = arguments[1];
  	 			obj.ctx = this;
  	 			this.widgetMap[arguments[0]] = obj;
  	 		}
  	 	},
  	 	
  	 	widgets : function() {
  	 		return _.values(this.widgetMap);
  	 	},
  	 	
  	 	metadata : function(){
  	 		if(arguments.length == 1){
  	 			if(arguments[0] == null)
  	 				return null;
  	 			if(typeof arguments[0] == "string")
  	 				return this.metadataMap[arguments[0]];
  	 			this.metadataMap[arguments[0].id] = this.mergeMetadata(this.metadataMap[arguments[0].id], arguments[0]);
  	 		}
  	 		else if(arguments.length >= 2){
  	 			var obj = arguments[1];
  	 			//clear
  	 			if(arguments[2]){
  	 				this.metadataMap[arguments[0]] = obj;
  	 			}
  	 			else{
  	 				this.metadataMap[arguments[0]] = this.mergeMetadata(this.metadataMap[arguments[0]], obj);
  	 			}
  	 		}
  	 	},
  	 	/**
  	 	 * will cause current dialog be closed
  	 	 */
  	 	close : function() {
  	 		var eventCtx = {};
  	 		var options = {source: this, eventCtx: eventCtx};
  	 		this.trigger("closing", options);
  	 		if(options.eventCtx.stop == true)
  	 			return;
  	 		if(this.dialog)
  	 			this.dialog.close();
  	 		else{
  	 			if(this.stackApp)
  	 				this.stackContainer.remove();
  	 			this.clean();
  	 		}
  	 		options = {source: this, eventCtx: {}};
  	 		this.trigger("closed", options);
  	 	},
  	 	/*private*/
  	 	mergeMetadata : function(orig, curr){
  	 		if(orig == null)
  	 			return curr;
  	 		return $.extend(orig, curr);
  	 	},
  	 	clean : function() {
  	 		this.dialog = null;
  	 		var components = this.components();
  	 		for(var i = 0; i < components.length; i ++)
  	 			components[i].destroy();
  	 		//TODO need further clean
//  	 		this.componentsMap = null;
//  	 		this.modelsMap = null;
//  	 		this.widgetMap = null;
  	 		
  	 		if($app === this){
	  	 		if($app.parent){
	  	 			$app = $app.parent;
	  	 			AppUtil.current($app);
	  	 		}
  	 		}
  	 	},
  	 	
  	 	eventDescs : function() {
			return [{value: 'loaded', text : 'Loaded'}, {value: 'closing', text : 'Closing'}, {value: 'closed', text : 'Closed'}];
		},
		
		/**
		 * return all public methods, for designer
		 */
		methodDescs : function() {
			var methods = FwBase.Wtf.View.Controls.BaseControl.prototype.methodDescs.call(this);
			return methods.concat([{name : 'close', desc: 'Close current app or dialog'},
			                      {name : 'data', params:[{name: 'dataObj', type: 'object'}], desc: "Set data object"},
			                      {name : 'data', params:[{name: 'key', type: 'string'}], desc: "Get data attribute"},
			                      {name : 'data', params:[{name: 'key', type: 'string'}, {name: 'value', type: 'object'}], desc: "Set data by key, value"}
			                      ]);
		}
  	 });
  	 
  	 function lazyDialog(){
  		var dialog = $app.dialog;
 		dialog.visible(true);
 		dialog.once("close", function(){
 			$app.clean();
 		});
  	 }
  	 
  	 var uniqueIdMap = {};
  	 function getUniqueId(url){
  		 var count = uniqueIdMap[url];
  		 if(count == null){
  			 count = 0;
  			 uniqueIdMap[url] = count;
  		 }
  		 uniqueIdMap[url] = count + 1;
  		 return url.replaceAll('/', '_') + count;
  	 }
});