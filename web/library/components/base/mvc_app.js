define(["../uipattern/buttonmanager"], function(){
	 window.AppUtil = FwBase.Wtf.Application = function(id){
  		 this.id = id;
  		 this.viewMap = {};
  	 	 this.modelMap = {};
  	 	 this.componentsMap = {};
  	 	 this.metadataMap = {};
  	 	 this.widgetMap = {};
  	 	 this.attrs = {};
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
  	  			AppUtil.navigateTo(appid, null, {callback : callback});
  	  		 });
  	 	},
  	 	navigateTo : function(url, reqData, options) {
  	 		if(options == null)
  	 			options = {};
  	 		var container = $('#sys_home_content_part');
			if(container.length == 0){
				container = $("#sys_design_home_content_part");
			}
			
			AppUtil.doNavigateTo(url, reqData, options, container);
			var title = options.title;
			if(title == null || title == ""){
				var pathInfo = url.split("/");
				title = FwBase.Wtf.Lang.Utils.capitalize(pathInfo[pathInfo.length - 1]);
			}
			AppUtil.updateTitle({title: title});
	 	},
	 	
	 	doNavigateTo : function(url, reqData, options, container) {
	 		var pathInfo = url.split("/");
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
				if(window.DesignMode && $.trim(html) == ''){
					html = '<div wtftype="container" style="height:99%;min-height:300px">';
				}
				if(container.length == 0){
					container = $(document.body);
					container.append(html);
				}
				else
					container.html(html);
				var app = new AppUtil(url);
				app.webroot = pathInfo[0];
				if(options)
					app.parent = options.parent;
				app.dialog = options.dialog;
				app.reqData(reqData);
				AppUtil.current(app);
				window.$app = app;
				//load contents and at last execute the app logic
				function appModelCallback() {
					var modelJs = rqhtml + "model";
					requireUtil(modelJsArr, function(){
						if(arguments[0] != null)
							arguments[0].exec();
					});
				};
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
//				var oriCallback = options ? options.callback : null;
//				var callback = null;
//				if(oriCallback){
//					callback = function() {
//						appCallback();
// 						oriCallback();
//					}
//				}
//				else
//					callback = appCallback;
				var callbacks = {oriCallback: options.callback, appModelCallback: appModelCallback, appCallback: appCallback};
				AppUtil.repaint(callbacks);
			});
	 	},
  		 	
  		updateTitle : function(titleInfo){
  			var titleZone = $('#sys_page_title');
  			if(titleZone.length > 0)
  				titleZone.html(titleInfo.title);
  			var breadcrumb = $("#sys_app_breadcrumb_last");
  			if(breadcrumb.length > 0)
  				breadcrumb.html(titleInfo.title);
  		},
	 	navigateToDialog : function(url, reqData, options){
	 		requireComponent(['dialog'], function(){
		 		var dialog = FwBase.Wtf.View.Controls.Dialog.getDialog();
		 		dialog.visible(true, options);
		 		var container = dialog.bodyContainer();
		 		dialog.once("close", function(){
		 			$app.clean();
		 		});
		 		AppUtil.doNavigateTo(url, reqData, {/*callback : lazyDialog, */parent : $app, dialog: dialog}, container);
	 		});
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
			var prefix = window.ClientConfig ? ClientConfig.prefix("template", "text") : "text";
			prefix += "!";
			var rqhtml = "../widgets/";
			for(var i in groups){
				var requireUtil = requirejs;
				if(window.requirelibs){
					requireUtil = requirelibs[i];
				}
				var htmlArr = [];
				var modelJsArr = [];
				var controllerArr = [];
				for(var j = 0; j < groups[i].length; j ++){
					var group = groups[i];
					var id = groups[i][j].id;
					htmlArr.push(prefix + rqhtml + id + "/" + id + ".html");
					modelJsArr.push(rqhtml + id + "/model");
					controllerArr.push(rqhtml + id + "/controller");
				}
				requireUtil(htmlArr, function(){
					var containers = [];
					for(var j = 0; j < arguments.length; j ++){
						var html = arguments[j];
						var container = groups[i][j].container;
						container.html(html);
						var id = groups[i][j].id;
						var widget = new FwBase.Wtf.Widget(id);
						$app.widget(widget);
						container.ctx = widget;
						containers.push(container);
					}
					requireUtil(modelJsArr, function(){
						for(var j = 0; j < arguments.length; j ++){
							if(arguments[j] != null){
								var id = groups[i][j].id;
								window.$widget = $app.widget(id);
								arguments[j].exec();
							}
						}
						var widgetCallback = function(){
							requireUtil(controllerArr, function() {
								for(var j = 0; j < arguments.length; j ++){
									if(arguments[j] != null){
										var id = groups[i][j].id;
										window.$widget = $app.widget(id);
										arguments[j].exec();
									}
								}
							});
						};
//						containers.push($(document.body));
						callbacks.widgetCallback = widgetCallback;
						AppUtil.parseHtml(containers, callbacks);
					});
				});
			}
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
					var children = requireContainer[i].children();
 					if(children.length > 0)
 						children.remove();
 					requireContainer[i].html(arguments[i]);
 					var positions = requireContainer[i].find("[wtfpos]");
 					if(positions.length > 0 && children.length > 0)
 						positions.html(children);
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
	 			if(wtfType.startWith('input')){
	 				typeList.push("input_base");
	 			}
	 			if(!_.contains(typeList, wtfType))
	 				typeList.push(wtfType);
//	 			requireList.push(wtfType + "/" + wtfType);
//	 			requireHtmlList.push(prefix + "!" + wtfType + "/" + wtfType + ".html");
	 		});
 			requireComponent(typeList, function(){
				for(var i = 0; i < containers.length; i ++){
					var container = containers[i];
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
						var ctx = null;
						if(container.ctx){
							ctx = container.ctx;
							container.ctx = null;
						}
						else
							ctx = $app;
						
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
						var ctrl = new FwBase.Wtf.View.Controls[capStr]($(this), objMeta, id);
						ctx.component(ctrl);
					});
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
  		attr : function(){
	 		if(arguments.length == 1)
	 			return this.attrs[arguments[0]];
	 		else
	 			this.attrs[arguments[0]] = arguments[1];
	 	},
	 	reqData : function(){
	 		if(arguments.length == 0)
	 			return this.requestData;
	 		this.requestData = arguments[1];
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
  	 				obj = new Model(obj);
  	 			obj.ctx = this;
  	 			this.modelMap[arguments[0].id] = obj;
  	 		}
  	 		else if(arguments.length == 2){
  	 			var obj = arguments[1];
  	 			if(!(obj instanceof Model))
  	 				obj = new Model(obj);
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
  	 	
  	 	metadata : function(metadata){
  	 		if(arguments.length == 1){
  	 			if(arguments[0] == null)
  	 				return null;
  	 			if(typeof arguments[0] == "string")
  	 				return this.metadataMap[arguments[0]];
  	 			this.metadataMap[arguments[0].id] = arguments[0];
  	 		}
  	 		else if(arguments.length == 2){
  	 			var obj = arguments[1];
  	 			this.metadataMap[arguments[0]] = obj;
  	 		}
  	 	},
  	 	/**
  	 	 * will cause current dialog be closed
  	 	 */
  	 	close : function() {
  	 		if($app.dialog)
  	 			$app.dialog.close();
  	 		else
  	 			this.clean();
  	 	},
  	 	/*private*/
  	 	clean : function() {
  	 		$app.dialog = null;
  	 		if($app.parent){
  	 			$app = $app.parent;
  	 			AppUtil.current($app);
  	 		}
  	 	}
  	 });
  	 
  	 function lazyDialog(){
  		var dialog = $app.dialog;
 		dialog.visible(true);
 		dialog.once("close", function(){
 			$app.clean();
 		});
  	 }
});