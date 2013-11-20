define(function(){
	 window.Model = FwBase.Wtf.Model = function(id, metadata){
		this.id = id;
		if(metadata == null)
			metadata = {};
	 	this.metadata = this.makeDefault(metadata);
	 	this.currentKey = FwBase.Wtf.Model.DEFAULT_KEY;
	 	this.stores = {};
	 	this.tranurl = null;
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
				this.stores[this.currentKey] = new FwBase.Wtf.Model.Store(this, this.currentKey, 0);
			}
		},
		toInit : function(){
			if(!this.metadata.lazyInit)
				this.init();
		},
		
		fireAddRow : function(){
			var index = arguments[2].at;
			this.trigger("add", {row : arguments[0], index : index});
	 	},
	 	fireDelRow : function(){
	 		this.trigger("remove", {row : arguments[0]});
	 	},
	 	fireClear : function(argument){
	 		this.trigger("clear", {});
	 	},
	 	firePageChange : function(){
		 	this.trigger("pagechange", arguments[0]);
	 	},
	 	firePagination : function(){
		 	this.trigger("pagination", arguments[0]);
	 	},
	 	fireStoreReset : function() {
	 		if(this.currentKey == arguments[0].key){
	 			this.trigger("clear", {});
	 		}
	 	},
	 	store : function(key) {
	 		var tmpKey = key ? key : this.currentKey;
	 		var store = this.stores[tmpKey];
	 		if(store == null){
				store = new FwBase.Wtf.Model.Store(this, tmpKey, 0);
				this.stores[tmpKey] = store;
	 		}
	 		return store;
	 	},
	 	page : function(key, index){
	 		return this.store(key).page(index);
	 	},
	 	
	 	reset : function() {
	 		for(var i in this.stores){
	 			this.stores[i].reset();
	 			delete this.stores[i];
	 		}
	 	},
	 	requestPage : function(options){
	 		var pageSize = options.pageSize;
	 		if(pageSize != null && pageSize != this.metadata.pageSize){
	 			this.pageSize(pageSize);
	 		}
	 		var key = options.currentKey || this.currentKey;
	 		var store = this.store(key);
	 		store.requestPage(options);
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
	 			this.transurl = "/" + ctx + "/" + FwBase.Wtf.Model.defaults.resturlbase + "/" + url;
	 		}
	 	},
	 	filters : function() {
	 		return null;
	 	},
	 	eventDescs : function() {
			return [{value: 'beforeselect', text : 'Before Select'}, 
			        {value: 'select', text : 'Select'},
			        {value: 'unselect', text : 'Unselect'},
			        {value: 'request', text : 'Request'},
			        {value: 'requestend', text : 'Request End'},
			        {value: 'add', text : 'Add'}, 
			        {value: 'remove', text : 'Remove'},
			        {value: 'clear', text : 'Clear'}, 
			        {value: 'pagechange', text : 'PageChange'}
			       ];
		},
		
		methodDescs : function() {
			return [{name : 'enable', params: {type: 'boolean'}, desc: 'Set the component enable or not'},
			                      {name : 'enable', desc: "Get the component's enable state"}];
		}
	 });
	 
	 FwBase.Wtf.Model.Store = function(model, key, currentPage) {
	 	this.model = model;
	 	this.pages = {};
	 	this.key = key;
	 	this.currentPage = currentPage;
	 	this.reload();
	 };
	 _.extend(FwBase.Wtf.Model.Store.prototype, Backbone.Events);
	 
	 $.extend(FwBase.Wtf.Model.Store.prototype, {
	 	reload : function(index) {
	 		var cp = this.currentPage;
	 		if(index != null)
	 			cp = index;
	 		var rowList = new FwBase.Wtf.Model.RowList(cp);
	 		rowList.setParent(this, this.model);
	 		this.pages[cp] = rowList;
	 		this.listenTo(rowList, 'add', this.fireAddRow);
	 		this.listenTo(rowList, 'remove', this.fireDelRow);
      		this.listenTo(rowList, 'reset', this.fireClear);
      		this.listenTo(rowList, 'pagechange', this.firePageChange);
      		this.listenTo(rowList, 'pagination', this.firePagination);
//	      		rowList.on('all', rowList, this.render);
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
	 	firePageChange: function() {
	 		this.model.firePageChange.apply(this.model, arguments);
	 	},
	 	firePagination: function() {
	 		this.model.firePagination.apply(this.model, arguments);
	 	},
	 	reset : function() {
	 		for(var i in this.pages){
	 			if(i == this.currentPage){
	 				this.pages[i].reset([]);
	 			}
	 			delete this.pages[i];
	 		}
	 		this.model.fireStoreReset.apply(this.model, [this]);
	 	},
	 	requestPage: function(options) {
	 		var forceUpdate = options.forceUpdate || true;
	 		var cp = options.pageIndex || 0;
	 		if(forceUpdate){
	 			this.firePageChange(cp);
	 		}
	 		if(this.pages[cp] == null || forceUpdate){
	 			this.reload(cp);
	 		}
	 	},
	 	page : function(index){
	 		if(index)
	 			return this.pages[i];
	 		return this.pages[this.currentPage];
	 	}
	 });
	 
	 FwBase.Wtf.Model.Row = Backbone.Model.extend({
		 sync : function() {
			 var dataset = this.collection.dataset;
			 var url = dataset.transurl;
		 	 var type = methodMap[arguments[0]];
		 	 this.url = url;
		 	 if(type == "DELETE")
		 		 this.url = this.url + "/" + this.id;
//			 		if(type == "GET"){
//			 			this.url = url + "/ctx/s_page=" + this.store.currentPage + "," + this.dataset.metadata.pageSize; 
//			 		}
//			 		var filters = null;//this.model.filters();
//			 		filters = [{key:'a', value:'d', joint:'='}, {key:'x', value:'y', joint:'like'}];
//			 		if(filters != null && filters.length > 0){
//			 			this.url += ";s_filter=" + $.toJSON(filters);
//			 		}
		 		
//			 		var success = arguments[2].success;
//			 		arguments[2].success = function() {
//			 			var respObj = arguments[0];
//			 			if(typeof respObj.totalRecords == 'undefined')
//			 				success.apply(this, arguments);
//			 			else{
//			 				arguments[0] = respObj.records;
//			 				success.apply(this, arguments);
//			 			}
//			 		}
		 		
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
	 	constructor : function(pageIndex) {
	 		this.pageIndex = pageIndex;
	 		Backbone.Collection.apply(this, arguments);
	 	},
	 	setParent: function(store, dataset) {
	 		this.store = store;
	 		this.dataset = dataset;
	 	},
	 	action : function(actionName, options){
	 		if(options == null)
	 			options = {};
	 		var url = this.dataset.transurl;
	 		var type = "create";
	 		this.url = url + "/action/" + actionName;
	 		
//		 		var success = arguments[2].success;
//		 		arguments[2].success = function() {
//		 			var respObj = arguments[0];
//		 			if(respObj == null || typeof respObj.totalRecords == 'undefined')
//		 				success.apply(this, arguments);
//		 			else{
//		 				arguments[0] = respObj.records;
//		 				success.apply(this, arguments);
//		 			}
//		 		}
	 		
	 		if(window.clientMode){
	 			this.syncFromClient.call(this, type, this, options);
	 		}
	 		else{
	 			Backbone.sync.call(this, type, this, options);
	 		}
	 	},
	 	sync : function() {
	 		var url = this.dataset.transurl;
	 		var type = methodMap[arguments[0]];
	 		this.url = url;
	 		if(type == "GET"){
	 			this.url = url + "/ctx/s_page=" + this.pageIndex + "," + this.dataset.metadata.pageSize; 
	 		}
	 		var filters = this.dataset.filters();
//		 		filters = [{key:'a', value:'d', joint:'='}, {key:'x', value:'y', joint:'like'}];
	 		if(filters != null && filters.length > 0){
	 			this.url += ";s_filter=" + $.toJSON(filters);
	 		}
	 		var oThis = this;
	 		var success = arguments[2].success;
	 		arguments[2].success = function() {
	 			var respObj = arguments[0];
	 			if(respObj == null || typeof respObj.totalRecords == 'undefined')
	 				success.apply(this, arguments);
	 			else{
	 				var pageCount = (respObj.totalRecords % respObj.pageSize == 0) ? parseInt(respObj.totalRecords / respObj.pageSize) : parseInt(respObj.totalRecords / respObj.pageSize) + 1;
	 				var pagination = {pageIndex: oThis.pageIndex, pageSize: respObj.pageSize, totalRecords: respObj.totalRecords, pageCount: pageCount};
	 				arguments[0] = respObj.records;
	 				oThis.trigger("pagination", pagination);
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
});