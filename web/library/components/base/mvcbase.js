define(function(){
	 FwBase = {};
	 FwBase.Wtf = {};

	 FwBase.Wtf.View = {};
	 FwBase.Wtf.View.Controls = {};
	 
	 
	 FwBase.Wtf.Model = function(metadata){
		if(metadata == null)
			metadata = {};
	 	this.metadata = this.makeDefault(metadata);
	 	this.currentKey = FwBase.Wtf.Model.DEFAULT_KEY;
	 	this.stores = {};
	 	if(this.metadata.url){
			this.url(this.metadata.url);
		}
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
			if(metadata.autoload == null)
				metadata.autoload = true;
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
		
		fireAddRow : function(){
			var index = arguments[2].at;
//			var row = arguments[0].collection._byId[arguments[0].cid];
			this.trigger("add", {row : arguments[0], index : index});
	 	},
	 	fireDelRow : function(){
//	 		var row = arguments[0].collection._byId[arguments[0].cid];
	 		this.trigger("remove", {row : arguments[0]});
	 	},
	 	fireClear : function(argument){
	 		this.trigger("clear", {});
	 	},
	 	firePageChange : function(){
//	 		this.trigger("pagechange", {key: this.currentKey, pageIndex: this.store().currentPage});
	 	},
//	 	addRow : function(row, index) {
//	 		this.store().addRow(row, index);
//	 	},
	 	
	 	store : function(key) {
	 		var tmpKey = key ? key : this.currentKey;
	 		var store = this.stores[tmpKey];
	 		if(store == null)
	 			return null;
	 		return store;
	 	},
	 	page : function(key, index){
	 		return this.store(key).page(index);
	 	},
	 	
	 	pageSize : function(pageSize) {
	 		if(pageSize == this.metadata.pageSize)
	 			return;
	 		this.metadata.pageSize = pageSize;
	 		for(var i in this.stores){
	 			if(i == this.currentKey){
	 				this.stores[i].fireDirty();
	 			}
	 			else
	 				delete this.stores[i];
	 		}
	 	},
	 	url : function(url){
	 		if(!url.startWith('/')){
	 			var ctx = $app.webroot;
	 			url = "/" + ctx + "/" + FwBase.Wtf.Model.defaults.resturlbase + "/" + url;
	 			this.metadata.url = url;
	 		}
	 	},
	 	filters : function() {
	 		return null;
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
	 		rowList.setParent(this, this.model);
	 		this.pages[this.currentPage] = rowList;
	 		this.listenTo(rowList, 'add', this.fireAddRow);
	 		this.listenTo(rowList, 'remove', this.fireDelRow);
      		this.listenTo(rowList, 'reset', this.fireClear);
//      		rowList.on('all', rowList, this.render);
      		if(this.model.metadata.url != null && this.model.metadata.autoload){
      			rowList.fetch();
      		}
	 	},
	 	fireAddRow : function() {
	 		this.model.fireAddRow.apply(this.model, arguments);
	 	},
	 	fireDelRow : function() {
	 		this.model.fireDelRow.apply(this.model, arguments);
	 	},
	 	fireClear : function() {
	 		this.model.fireClear.apply(this.model, arguments);
	 	},
	 	fireDirty : function() {
	 		for(var i in this.pages){
	 			if(i == this.currentPage){
	 				this.pages[i].reset([]);
	 			}
	 			else{
	 				delete this.pages[i];
	 			}
	 		}
	 	},
	 	page : function(index){
	 		if(index)
	 			return this.pages[i];
	 		return this.pages[this.currentPage];;
	 	}
	 });
	 
	 FwBase.Wtf.Model.Row = Backbone.Model.extend({
		 sync : function() {
			 var dataset = this.collection.dataset;
			 var url = dataset.metadata.url;
		 	 var type = methodMap[arguments[0]];
		 	 this.url = url;
		 	 if(type == "DELETE")
		 		 this.url = this.url + "/" + this.id;
//		 		if(type == "GET"){
//		 			this.url = url + "/ctx/s_page=" + this.store.currentPage + "," + this.dataset.metadata.pageSize; 
//		 		}
//		 		var filters = null;//this.model.filters();
//		 		filters = [{key:'a', value:'d', joint:'='}, {key:'x', value:'y', joint:'like'}];
//		 		if(filters != null && filters.length > 0){
//		 			this.url += ";s_filter=" + $.toJSON(filters);
//		 		}
		 		
//		 		var success = arguments[2].success;
//		 		arguments[2].success = function() {
//		 			var respObj = arguments[0];
//		 			if(typeof respObj.totalRecords == 'undefined')
//		 				success.apply(this, arguments);
//		 			else{
//		 				arguments[0] = respObj.records;
//		 				success.apply(this, arguments);
//		 			}
//		 		}
		 		
		 	if(window.clientMode){
		 		this.syncFromClient.apply(this, arguments);
		 	}
		 	else{
		 		Backbone.sync.apply(this, arguments);
		 	}
		 }
	 });
	 
	 methodMap = {
	    'create': 'POST',
	    'update': 'PUT',
	    'patch':  'PATCH',
	    'delete': 'DELETE',
	    'read':   'GET'
	 };
  
	 FwBase.Wtf.Model.RowList = Backbone.Collection.extend({
	 	model : FwBase.Wtf.Model.Row,
	 	setParent: function(store, dataset) {
	 		this.store = store;
	 		this.dataset = dataset;
	 	},
	 	sync : function() {
	 		var url = this.dataset.metadata.url;
	 		var type = methodMap[arguments[0]];
	 		this.url = url;
	 		if(type == "GET"){
	 			this.url = url + "/ctx/s_page=" + this.store.currentPage + "," + this.dataset.metadata.pageSize; 
	 		}
	 		var filters = this.dataset.filters();
//	 		filters = [{key:'a', value:'d', joint:'='}, {key:'x', value:'y', joint:'like'}];
	 		if(filters != null && filters.length > 0){
	 			this.url += ";s_filter=" + $.toJSON(filters);
	 		}
	 		
	 		var success = arguments[2].success;
	 		arguments[2].success = function() {
	 			var respObj = arguments[0];
	 			if(typeof respObj.totalRecords == 'undefined')
	 				success.apply(this, arguments);
	 			else{
	 				arguments[0] = respObj.records;
	 				success.apply(this, arguments);
	 			}
	 		}
	 		
	 		if(window.clientMode){
	 			this.syncFromClient.apply(this, arguments);
	 		}
	 		else{
	 			Backbone.sync.apply(this, arguments);
	 		}
	 	},
	 	rows : function(){
	 		return this.models;
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
	 	var segs = url.substring(1).split("/");
	 	var requireUtil = requirejs;
		if(window.requirelibs){
			requireUtil = requirelibs[segs[0]];
		}
	 	var jsUrl = "../" + segs[1] + "/" + segs[2];
	 	var oThis = this;
	 	requireUtil([jsUrl], function(rest){
	 		var service = FwBase.Wtf.Client.restServices[segs[2]];
	 		if(service == null){
	 			alert("can't find rest service with url:" + url + ",name:" + segs[1]);
	 			return;
	 		}
	 		var method = null;
	 		var params = null;
	 		if(segs[3] == 'ctx'){
	 			method = "root";
	 		}
	 		else if(segs[3] == 'action'){
	 			method = "action_" + segs[4];
	 		}
	 		else if(segs[4] != null){
	 		}
	 		else{
	 			method = "root_id";
	 		}
	 		var methodName = options.type + "_" + method;
	 		var result = service[methodName].apply(oThis, params);
	 		options.success(result);
	 	});
	 };
	 
  	 FwBase.Wtf.Application = function(id){
  		 this.id = id;
  		 this.viewMap = {};
  	 	 this.modelMap = {};
  	 	 this.componentsMap = {};
  	 	 this.metadataMap = {};
  	 	 this.attrs = {};
  	 };
  	 
  	 _.extend(FwBase.Wtf.Application.prototype, Backbone.Events);
  	 
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
  	 	init : function(callback) {
  	  		 $("[wtftype='application']").each(function(){
  	  			var appid = $(this).attr("wtfmetadata");
  	  			if(appid == null || appid == ""){
  	  				alert("wtftype='application' must has an wtfmetadata for id");
  	  				return;
  	  			}
  	  			$(this).attr('wtfdone', 'done');
  	  			FwBase.Wtf.Application.navigateTo(appid, null, {callback : callback});
  	  		 });
  	 	},
  	 	navigateTo : function(url, reqData, options) {
  	 		var container = $('#sys_home_content_part');
			if(container.length == 0){
				container = $("#sys_design_home_content_part");
			}
			
			FwBase.Wtf.Application.doNavigateTo(url, reqData, options, container);
			var pathInfo = url.split("/");
			var title = FwBase.Wtf.Lang.Utils.capitalize(pathInfo[pathInfo.length - 1]);
			FwBase.Wtf.Application.updateTitle({title: title});
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
			
			requireUtil([prefix + rqhtml + pathInfo[pathInfo.length - 1] + ".html"], function(html){
				if(window.DesignMode && $.trim(html) == ''){
					html = '<div wtftype="container" style="height:99%;min-height:300px">';
				}
				if(container.length == 0){
					container = $(document.body);
					container.append(html);
				}
				else
					container.html(html);
				var app = new FwBase.Wtf.Application(url);
				app.webroot = pathInfo[0];
				if(options)
					app.parent = options.parent;
				FwBase.Wtf.Application.current(app);
				window.$app = app;
				var modelJs = rqhtml + "model";
				requireUtil([modelJs], function(){
					if(arguments[0] != null)
						arguments[0].exec();
 					app.tempCallback = function(){
	 					var controllerJs = rqhtml + "controller";
	 					requireUtil([controllerJs], function() {
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
 					FwBase.Wtf.Application.repaint(options ? options.callback : null);
				});
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
		 		dialog.update(options);
		 		var container = dialog.bodyContainer();
		 		FwBase.Wtf.Application.doNavigateTo(url, reqData, {callback : lazyDialog, parent : $app}, container);
	 		});
	 	},
	 	closeDialog : function() {
	 		var dialog = FwBase.Wtf.View.Controls.Dialog.closeDialog();
	 	},
  		 	
//	 	createControl : function(obj) {
//	 		if(obj == null){
//	 			alert("illegal null arguments");
//	 			return;
//	 		}
//	 		var type = obj.type;
//	 		if(type == null || type == ""){
//	 			alert("type attribute is needed");
//	 			return;
//	 		}
//	 		
//	 		requirejs(["text!" + type + "/" + type + ".html"], function(html){
//	 			var template = $('#sys_atom_controls_' + type);
//	 			if(template.length == 0){
//	 				$('body').append(html);
//	 			}
//	 			requirejs([type + "/" + type], function(module){
//	 				var id = obj.id;
//	 				var container = null;
//	 				if(id != null && id != ""){
//	 					container = $('#' + id);
//	 				}
//	 				else{
//	 					alert("object meta must contain id attribute");
//	 					return;
//	 				}
//	 				if(!container[0]){
//	 					alert("can not find element by id:" + id);
//	 					return;
//	 				}
//	 				var capStr = FwBase.Wtf.Lang.Utils.capitalize(type);
//	 				var ctrl = new FwBase.Wtf.View.Controls[capStr](container, obj.objMeta, obj.id);
//	 				FwBase.Wtf.Application._controlMap[ctrl.id, ctrl];
//	 			});
//	 		});
//	 	},
  		 	
	 	getControl : function(id){
	 		return FwBase.Wtf.Application._controlMap[id];
	 	},
	 	repaint : function(callback) {
	 		var func = null;
	 		if(callback){
	 			func = function(){
	 				FwBase.Wtf.Application.parseHtml(callback);
	 			};
	 		}
	 		else
	 			func = FwBase.Wtf.Application.parseHtml;
	 		
	 		var widgetCallback = function() {
	 			FwBase.Wtf.Application.parseTemplate(func);
	 		}
	 		
	 		FwBase.Wtf.Application.parseWidget(widgetCallback);
	 	},
	 	
	 	parseWidget : function(callback){
	 		var groups = [];
	 		var hasWidget = FwBase.Wtf.Application.detectWidget(groups);
	 		if(hasWidget)
	 			FwBase.Wtf.Application.doParseWidget(groups, callback);
	 		else
	 			callback();
	 	},
	 	
	 	doParseWidget : function(groups, callback){
			var prefix = window.ClientConfig ? ClientConfig.prefix("template", "text") : "text";
			prefix += "!";
			var rqhtml = "../widgets/";
			//TODO , neet wait all widgets displayed
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
					controllerArr.push(rqhtml + id + "/model");
				}
				requireUtil(htmlArr, function(){
					for(var j = 0; j < arguments.length; j ++){
						var html = arguments[j];
						var container = groups[i][j].container;
						container.html(html);
					}
					requireUtil(modelJsArr, function(){
						for(var m = 0; m < arguments.length; m ++){
							if(arguments[m] != null)
								arguments[m].exec();
						}
						
						requireUtil(controllerArr, function() {
							for(var m = 0; m < arguments.length; m ++){
								if(arguments[m] != null)
									arguments[m].exec();
							}
						});
					});
					if(callback)
						callback();
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
	 			if(groups[ids[0]] == null){
	 				groups[ids[0]] = [];
	 			}
	 			groups[ids[0]].push({id: ids[1], container: $(this)});
	 		});
	 		return has;
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
	 					var children = requireContainer[i].children();
	 					if(children.length > 0)
	 						children.remove();
	 					requireContainer[i].html(arguments[i]);
	 					var positions = requireContainer[i].find("[wtfpos]");
	 					//TODO
	 					if(positions.length > 0 && children.length > 0)
	 						positions.html(children);
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
	 	parseHtml : function(callback) {
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
	 			typeList.push(wtfType);
//	 			requireList.push(wtfType + "/" + wtfType);
//	 			requireHtmlList.push(prefix + "!" + wtfType + "/" + wtfType + ".html");
	 		});
 			requireComponent(typeList, function(){
				$("[wtftype]").each(function(){
					if($(this).attr('wtfdone') != null)
						return;
					$(this).attr('wtfdone', 'done');
					var wtfType = $(this).attr('wtftype');
					if(wtfType == 'container' || wtfType == 'widget')
 						return;
					var id = $(this).attr("id");
					if(id == null){
						alert("id can not be null for element with wtftype:" + wtfType);
						return;
					}
					var objMeta = FwBase.Wtf.Application.current().metadata(id);
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
				    	objMeta = FwBase.Wtf.Application.mergeMetadata(objMeta, attMeta);
				    }
				    if(objMeta == null)
 						objMeta = window.globalEmptyObj;
					var capStr = FwBase.Wtf.Lang.Utils.capitalize(wtfType);
					var ctrl = new FwBase.Wtf.View.Controls[capStr]($(this), objMeta, id);
					$app.component(ctrl);
				});
//				var app = FwBase.Wtf.Application.current();
//				app.component(ctrl);
				var tempCallback = $app.tempCallback;
				if(tempCallback != null){
					$app.tempCallback = null;
					tempCallback.apply($app);
				}
				if(callback)
					callback();
				
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
  	 		if(typeof model == "string" && arguments.length == 1)
  	 			return this.modelMap[model];
  	 		else if(typeof model == "string"){
  	 			arguments[1].app = this;
  	 			this.modelMap[model] = arguments[1];
  	 		}
  	 		else{
  	 			model.app = this;
  	 			this.modelMap[model.id] = model;
  	 		}
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
  	 		component.app = this;
  	 		this.componentsMap[component.id] = component;
  	 	},
  	 	metadata : function(metadata){
  	 		if(typeof metadata == "string" && arguments.length == 1)
  	 			return this.metadataMap[metadata];
  	 		else if(typeof metadata == "string"){
  	 			this.metadataMap[metadata] = arguments[1];
  	 		}
  	 		else
  	 			this.metadataMap[metadata.id] = metadata;
  	 	},
  	 	/**
  	 	 * will cause current dialog be closed
  	 	 */
  	 	close : function() {
  	 		FwBase.Wtf.Application.closeDialog();
  	 	}
  	 });
  	 
  	 function lazyDialog(){
  		var dialog = FwBase.Wtf.View.Controls.Dialog.getDialog();
 		dialog.visible(true);
  	 }
});
