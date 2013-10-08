define(function(){
	 FwBase = {};
	 FwBase.Wtf = {};

	 FwBase.Wtf.View = {};
	 FwBase.Wtf.View.Controls = {};
	 
	 
	 FwBase.Wtf.Model = function(id, metadata){
	 	if(id == null){
	 		alert("id is required");
	 		return;
	 	}
	 	this.id = id;
	 	this.metadata = this.makeDefault(metadata);
	 	this.currentKey = FwBase.Wtf.Model.DEFAULT_KEY;
	 	this.stores = {};
	 	this.observers = {};
	 };
	 FwBase.Wtf.Model.DEFAULT_KEY = "SYS_DEFAULT_KEY";
	 FwBase.Wtf.Model.defaults = {
	 	pageSize : 10,
	 	resturlbase: 'rest'
	 };
	 
	//use backbones extend method to add events function
	 _.extend(FwBase.Wtf.Model.prototype, Backbone.Events);
	 
	 
	 $.extend(FwBase.Wtf.Model.prototype, {
		makeDefault : function(metadata){
			if(!metadata.pageSize)
				metadata.pageSize = FwBase.Wtf.Model.defaults.pageSize;
			if(metadata.lazyInit == null)
				metadata.lazyInit = false;
			return metadata;
		},
		init : function(key){
			if(key)
				this.currentKey = key;
			if(!this.stores[this.currentKey]){
				this.stores[this.currentKey] = new FwBase.Wtf.Model.Store(this, 0);
			}
		},
		toInit : function(){
			if(!this.metadata.lazyInit)
	 		this.init();
		},
		bind : function(ctrl) {
			this.observers[ctrl.id] = ctrl;
		},
		unbind : function(ctrl) {
			this.observers[ctrl.id] = null;
		},
		
		fireAddRow : function(row){
	 		for(var i in this.observers){
	 			this.observers[i].onModelAdd(row);
	 		}
	 		this.trigger("add", row);
	 	},
	 	
	 	page : function(key) {
	 		var tmpKey = key ? key : this.currentKey;
	 		var store = this.stores[tmpKey];
	 		if(store == null)
	 			return null;
	 		return store.page();
	 	}
	 });
	 
	 FwBase.Wtf.Model.Store = function(model, currentPage) {
	 	this.model = model;
	 	this.pages = {};
	 	this.currentPage = currentPage;
	 	this.reload();
	 };
	 _.extend(FwBase.Wtf.Model.Store.prototype, Backbone.Events);
	 
	 $.extend(FwBase.Wtf.Model.Store.prototype, {
	 	reload : function() {
	 		var rowList = new FwBase.Wtf.Model.RowList();
	 		this.pages[this.currentPage] = rowList;
	 		this.listenTo(rowList, 'add', this.addOne);
//      		rowList.on('reset', rowList, this.addAll);
//      		rowList.on('all', rowList, this.render);
      		if(this.model.metadata.url){
	 			rowList.url = FwBase.Wtf.Model.defaults.resturlbase + "/" + this.model.metadata.url;
	 			rowList.fetch();
      		}
	 	},
	 	addOne : function(item) {
	 		this.model.fireAddRow(item);
	 	},
	 	page : function(index){
	 		if(index)
	 			return this.pages[i];
	 		return this.pages[this.currentPage];;
	 	}
	 });
	 
	 FwBase.Wtf.Model.Row = Backbone.Model.extend({
	 });
	 
	 var methodMap = {
	    'create': 'POST',
	    'update': 'PUT',
	    'patch':  'PATCH',
	    'delete': 'DELETE',
	    'read':   'GET'
	 };
  
	 FwBase.Wtf.Model.RowList = Backbone.Collection.extend({
	 	model : FwBase.Wtf.Model.Row,
	 	sync : function() {
	 		if(window.clientMode){
	 			this.syncFromClient.apply(this, arguments);
	 		}
	 		else{
	 			Backbone.sync.apply(this, arguments);
	 		}
	 	},
	 	syncFromClient : function(method, model, options) {
	 		var type = methodMap[method];
   			// Default JSON-request options.
    		var params = {type: type, dataType: 'json'};

		    // Ensure that we have a URL.
		    if (!options.url) {
		      params.url = _.result(model, 'url') || urlError();
		    }

    		// Ensure that we have the appropriate request data.
    		if (options.data == null && model && (method === 'create' || method === 'update' || method === 'patch')) {
      			params.contentType = 'application/json';
      			params.data = JSON.stringify(options.attrs || model.toJSON(options));
    		}

		    // Don't process data on a non-GET request.
		    if (params.type !== 'GET' && !options.emulateJSON) {
		      params.processData = false;
		    }


    		// Make the request, allowing the user to override any Ajax options.
    		var xhr = options.xhr = FwBase.Wtf.Client.mockAjax(_.extend(params, options));
    		model.trigger('request', model, xhr, options);
    		return xhr;
	 	}
	 });

	 FwBase.Wtf.Client = {};
	 FwBase.Wtf.Client.restServices = {};
	 FwBase.Wtf.Client.mockAjax = function(options){
	 	var url = options.url;
		var tempUrl = url.substring((FwBase.Wtf.Model.defaults.resturlbase + "/").length);
	 	var segs = tempUrl.split("/");
	 	var jsUrl = "../../rest/" + segs[0];
	 	var oThis = this;
	 	requirejs([jsUrl], function(rest){
	 		var service = FwBase.Wtf.Client.restServices[segs[0]];
	 		if(service == null){
	 			alert("can't find rest service with url:" + url + ",name:" + segs[0]);
	 			return;
	 		}
	 		var methodName = options.type + "_" + segs[1];
	 		var result = service[methodName].apply(oThis, segs.splice(2, segs.length));
	 		options.success(result);
	 	});
	 };
	 
  	 FwBase.Wtf.Application = function(id){
  		 this.id = id;
  	 };
  	 
  	 FwBase.Wtf.Application = $.extend(FwBase.Wtf.Application, {
  		applicationMap : {},
  	 	currentApplication : null,
  	 	application : function(app) {
  	 		if(app instanceof String)
  	 			return FwBase.Wtf.Application.applicationMap[app];
  	 		FwBase.Wtf.Application.applicationMap[app.id, app];
  	 	},
  	 	current : function(app){
  	 		if(app != null){
  	 			FwBase.Wtf.Application.application(app);
  	 			FwBase.Wtf.Application.currentApplication = app;
  	 		}
  	 		return FwBase.Wtf.Application.currentApplication;
  	 	},
  	 	init : function() {
  	  		 $("[wtftype='application']").each(function(){
  	  			var appid = $(this).attr("wtfmetadata");
  	  			if(appid == null || appid == ""){
  	  				alert("wtftype='application' must has an wtfmetadata for id");
  	  				return;
  	  			}
  	  			$(this).attr('wtfdone', 'done');
  	  			FwBase.Wtf.Application.navigateTo(appid);
  	  		 });
  	 	},
  	 	navigateTo : function(url, reqData, useFrame) {
	 		if(useFrame == null)
	 			useFrame = false;
	 		if(useFrame == false){
	 			if(true){
	 				var pathInfo = url.split("/");
	 				var prefix = window.ClientConfig ? ClientConfig.prefix("template", "text") : "text";
	 				prefix += "!";
	 				var rb = "../../applications/";
	 				var rqhtml = rb;
	 				for(var i = 0; i < pathInfo.length; i ++){
	 					rqhtml += pathInfo[i] + "/";
	 				}
	 				requirejs([prefix + rqhtml + pathInfo[pathInfo.length - 1] + ".html"], function(html){
 						var container = $('#sys_home_content_part');
 						if(container.length == 0){
 							container = $(document.body);
 							container.append(html);
 						}
 						else
 							container.html(html);
 						var app = new FwBase.Wtf.Application(url);
 						FwBase.Wtf.Application.current(app);
 						
// 						var jsPrefix = window.ClientConfig ? ClientConfig.prefix("js", "js") : null;
// 						if(jsPrefix != null)
// 							jsPrefix += "!";
// 						else
// 							jsPrefix = "";
 						var modelJs = rqhtml + "model";
 						requirejs([modelJs], function(){
		 					FwBase.Wtf.Application.repaint();
		 					app.tempCallback = function(){
			 					var controllerJs = rqhtml + "controller";
			 					requirejs([controllerJs], function() {
			 						var models = FwBase.Wtf.Application.current().models();
							 		for(var i = 0; i < models.length; i ++)
							 			models[i].toInit();
			 					});
		 					};
 						});
 					});
 					var title = FwBase.Wtf.Lang.Utils.capitalize(pathInfo[pathInfo.length - 1]);
 					FwBase.Wtf.Application.updateTitle({title: title});
	 			}
	 			else{
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
  		 	
  		updateTitle : function(titleInfo){
  			var titleZone = $('#sys_page_title');
  			if(titleZone.length > 0)
  				titleZone.html(titleInfo.title);
  			var breadcrumb = $("#sys_app_breadcrumb_last");
  			if(breadcrumb.length > 0)
  				breadcrumb.html(titleInfo.title);
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
	 				FwBase.Wtf.Application._controlMap[ctrl.id, ctrl];
	 			});
	 		});
	 	},
  		 	
	 	getControl : function(id){
	 		return FwBase.Wtf.Application._controlMap[id];
	 	},
	 	repaint : function() {
	 		FwBase.Wtf.Application.parseTemplate(FwBase.Wtf.Application.parseHtml);
	 	},
	 	
	 	parseTemplate : function(callback){
	 		var requireList = [];
	 		var requireCssList = [];
	 		var requireContainer = [];
	 		FwBase.Wtf.Application.detectTemplate(requireList, requireCssList, requireContainer);
	 		if(requireList.length > 0)
	 			FwBase.Wtf.Application.doParseTemplate(requireList, requireCssList, requireContainer, callback);
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
	 				FwBase.Wtf.Application.detectTemplate(requireList, requireCssList, requireContainer);
	 				if(requireList.length > 0)
	 					FwBase.Wtf.Application.doParseTemplate(requireList, requireCssList, requireContainer, callback);
	 				else
	 					callback();
	 			});
	 		});
	 	},
	 	detectTemplate : function(requireList, requireCssList, requireContainer) {
	 		var prefix = window.ClientConfig ? ClientConfig.prefix("template", "text") : "text";
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
	 		var prefix = window.ClientConfig ? ClientConfig.prefix("html", "text") : "text";
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
	 						var objMeta = FwBase.Wtf.Application.current().metadata(id);
	 						if(objMeta == null){
		 						var metadataStr = $(this).attr("wtfmeta");
		 						if(metadataStr == null || metadataStr == ""){
		 							objMeta = window.globalEmptyObj;
		 						}
		 						else{
		 							objMeta = $.paraseJSON(metadataStr);
		 						}
	 						}
	 						var capStr = FwBase.Wtf.Lang.Utils.capitalize(wtfType);
	 						var ctrl = new FwBase.Wtf.View.Controls[capStr]($(this), objMeta, id);
	 						var app = FwBase.Wtf.Application.current();
	 						app.component(ctrl);
	 						var tempCallback = app.tempCallback;
	 						if(tempCallback != null){
	 							app.tempCallback = null;
	 							tempCallback.apply(app);
	 						}
	 					});
	 				});
	 			});
	 			
	 		}
  		 }
  	 });
  	 $.extend(FwBase.Wtf.Application.prototype, {
  	 	viewMap : {},
  	 	modelMap : {},
  	 	componentsMap : {},
  	 	metadataMap: {},
  	 	view : function(view){
  	 		if(typeof view == "string")
  	 			return this.viewMap[view];
  	 		this.viewMap[view.id] = view;
  	 	},
  	 	removeView : function(viewId) {
  	 		return this.viewMap[viewId];
  	 	},
  	 	destroyView : function(viewId){
  	 		var view = this.removeView(viewId);
  	 		if(view)
  	 			view.destroy();
  	 	},
  	 	
  	 	model : function(model){
  	 		if(typeof model == "string")
  	 			return this.modelMap[model];
  	 		this.modelMap[model.id] = model;
  	 	},
  	 	
  	 	models : function() {
  	 		return _.values(this.modelMap);
  	 	},
  	 	
  	 	removeModel : function(modelId) {
  	 		return this.modelMap[modelId];
  	 	},
  	 	destroyModel : function(modelId){
  	 		var model = this.removeModel(modelId);
  	 		if(model)
  	 			model.destroy();
  	 	},
  	 	component : function(component){
  	 		if(typeof component == "string")
  	 			return this.componentsMap[component];
  	 		this.componentsMap[component.id] = component;
  	 	},
  	 	applyMetadata : function(id, metadata){
  	 		this.metadataMap[id] = metadata;
  	 	},
  	 	metadata : function(id){
  	 		return this.metadataMap[id];
  	 	}
  	 });
});
