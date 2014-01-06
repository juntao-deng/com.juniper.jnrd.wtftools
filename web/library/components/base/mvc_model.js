define(["backbone"], function(){
	 window.Model = FwBase.Wtf.Model = function(id, metadata){
		this.id = id;
		if(metadata == null)
			metadata = {};
	 	this.metadata = this.makeDefault(metadata);
	 	this.currentKey = FwBase.Wtf.Model.DEFAULT_KEY;
	 	this.stores = {};
	 	this.tranurl = null;
	 	this.reqParamObj = {};
	 	if(this.metadata.url){
			this.url(this.metadata.url);
		}
	 	var idAttribute = metadata.idAttribute;
	 	if(idAttribute == null || idAttribute == "" || idAttribute == FwBase.Wtf.Model.defaults.idAttribute)
			 metadata.idAttribute = "";
	 };
	 FwBase.Wtf.Model.DEFAULT_KEY = "SYS_DEFAULT_KEY";
	 FwBase.Wtf.Model.defaults = {
	 	pageSize : 10,
	 	resturlbase: 'rest',
	 	idAttribute : 'id'
	 };
	 
	//use backbones extend method to add events function
	 _.extend(FwBase.Wtf.Model.prototype, Backbone.Events);
	 
	 $.extend(FwBase.Wtf.Model.prototype, {
		makeDefault : function(metadata){
			if(!metadata.pageSize)
				metadata.pageSize = FwBase.Wtf.Model.defaults.pageSize;
			if(metadata.autoSelect == null)
				metadata.autoSelect = false;
			return metadata;
		},
		init : function(key){
			if(key)
				this.currentKey = key;
			this.reload();
		},
		toInit : function(){
			if(!this.metadata.lazyInit)
				this.init();
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
	 	currentPage : function(key, index){
	 		if(arguments.length == 0)
	 			return this.store(key).currentPage();
//	 		var pageSize = options.pageSize;
//	 		if(pageSize != null && pageSize != this.metadata.pageSize){
//	 			this.pageSize(pageSize);
//	 		}
	 		key = key || this.currentKey;
	 		var store = this.store(key);
	 		store.currentPage(index);
	 		this.currentKey = key;
	 		
//	 		store.requestPage(options);
	 	},
	 	row : function() {
	 		if(arguments.length == 0){
	 			var row = this.createRow();
	 			var page = this.page();
	 			this.fireBeforeRowAdded(page, row);
	 			page.row(row);
	 			return row;
	 		}
	 		if(typeof arguments[0] == "object"){
	 			this.page().row(row);
	 		}
	 		else{
	 			return this.page().row(arguments[0]);
	 		}
	 	},
	 	rows : function(key, index){
	 		return this.store(key).page(index).rows();
	 	},
	 	removeRow : function(ids){
	 		this.page().removeRow(ids);
	 	},
	 	/**
	 	 * reload data for current store, current page. using current client parameters
	 	 */
	 	reload : function() {
	 		this.store().reload();
	 	},
	 	/**
	 	 * Select rows in current store
	 	 * @param 1.  ids, can be "rowid" or array of rowid :[rowid1, rowid2] or index of row index
	 	 * @param 2. clear , if clear before selections. default true.
	 	 */
	 	select : function() {
	 		var ids = arguments[0];
	 		var clear = arguments[1] == null? true : arguments[1];
	 		this.page().select(ids, clear);
	 	},
	 	selections : function() {
	 		return this.page().selections;
	 	},
	 	unselect : function() {
	 		var ids = arguments[0];
	 		this.page().unselect(ids);
	 	},
	 	save : function(options) {
	 		var selections = this.selections();
	 		if(selections != null && selections.ids.length > 0){
	 			var row = selections.rows[0];
	 			this.listenToOnce(row, 'error', this.fireModelError);
	 			row.save(null, options);
	 		}
	 	},
	 	selectCrossPage : function(ids){
	 		alert("not implemented");
	 	},
	 	reset : function() {
	 		for(var i in this.stores){
	 			this.stores[i].reset();
	 			delete this.stores[i];
	 		}
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
	 	filters : function() {
	 		return null;
	 	},
	 	/**
	 	 * add or get matrix parameter for restrul api
	 	 */
	 	reqParam : function() {
	 		if(arguments.length == 2){
	 			this.reqParamObj[arguments[0]] = arguments[1];
	 		}
	 		else{
	 			return this.reqParamObj[arguments[0]];
	 		}
	 	},
	 	reqParams : function() {
	 		return this.reqParamObj;
	 	},
	 	/**
	 	 * remvoe matrix parameter
	 	 */
	 	removeReqParam : function(key) {
	 		delete this.reqParamObj[key];
	 	},
	 	
	 	/*Fire events start */
	 	fireAddRow : function(){
	 		this.trigger("add", arguments[0]);
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
	 	fireSelection : function() {
	 		var options = {add: false, selection: arguments[0]};
	 		this.trigger("selection", options);
	 	},
	 	fireUnSelection : function() {
	 		var options = {add: false, selection: arguments[0]};
	 		this.trigger("unselection", options);
	 	},
	 	fireSyncOver : function() {
	 		var options = arguments[0];
//	 		var options = {key: collection.store.key, pageIndex: collection.pageIndex};
	 		this.trigger("syncover", options);
	 		if(this.metadata.autoSelect){
	 			if(this.page().rows().length > 0)
	 				this.select(0);
	 		}
	 	},
	 	fireSynching : function() {
	 		var options = arguments[0];
	 		this.trigger("synching", options);
	 	},
	 	fireModelError : function() {
	 		var errorMsg = arguments[1].responseText;
	 		//TODO JSON
	 		//if(errorMsg.startWith('{')
	 		var row = arguments[0];
	 		var options = {row: arguments[0], errorMsg: errorMsg};
	 		this.trigger('error', options);
	 	},
	 	fireCellChange : function() {
	 		var row = arguments[0];
	 		var changed = row.changed;
	 		var options = {row: arguments[0], changedAttr: changed};
	 		this.trigger('cellchange', options);
	 	},
	 	fireBeforeRowAdded : function(){
	 		var options = {page: arguments[0], row: arguments[1]};
	 		this.trigger("beforeadd", options);
	 	},
	 	/*Fire events end*/
	 	
	 	/*private methods start*/
	 	createRow : function() {
	 		return this.page().createRow();
	 	},
	 	url : function(url){
	 		if(!url.startWith('/')){
	 			var ctx = $app.webroot;
	 			this.transurl = "/" + ctx + "/" + FwBase.Wtf.Model.defaults.resturlbase + "/" + url;
	 		}
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
		/*private end */
	 });
	 
	 FwBase.Wtf.Model.Store = function(model, key, currentPage) {
	 	this.model = model;
	 	this.pages = {};
	 	this.key = key;
	 	this.currPage = currentPage;
//	 	this.reload();
	 };
	 _.extend(FwBase.Wtf.Model.Store.prototype, Backbone.Events);
	 
	 $.extend(FwBase.Wtf.Model.Store.prototype, {
	 	reload : function(index) {
//	 		var cp = index;
//	 		if(cp == null){
//	 			cp = this.currentPage;
////	 			this.fireClear();
//	 		}
	 		var p = this.page(index);
	 		p.reset();
	 		//if(this.model.metadata.url != null && this.model.metadata.autoload){
	 		this.fireSynching(p);
      		p.fetch();
      		//}
	 	},
	 	reset : function() {
	 		for(var i in this.pages){
	 			if(i == this.currPage){
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
	 		var cp = index;
	 		if(cp == null)
	 			cp = this.currPage;
	 		var p = this.pages[cp];
	 		if(p == null){
		 		var idAttribute = this.model.metadata.idAttribute;
		 		if(idAttribute != "")
		 			getRowListByType(idAttribute);
		 		var rowList = new FwBase.Wtf.Model["RowList" + idAttribute]();
		 		rowList.setParent(cp, this, this.model);
		 		this.pages[cp] = rowList;
		 		this.listenTo(rowList, 'add', this.fireAddRow);
		 		this.listenTo(rowList, 'remove', this.fireDelRow);
	      		this.listenTo(rowList, 'reset', this.fireClear);
	      		this.listenTo(rowList, 'pagechange', this.firePageChange);
	      		this.listenTo(rowList, 'pagination', this.firePagination);
	      		this.listenTo(rowList, 'sync', this.fireSyncOver);
	      		p = rowList;
	      		this.pages[cp] = p;
	 		}
	 		return p;
	 	},
	 	currentPage : function(index){
	 		if(arguments.length == 0)
	 			return this.currPage;
	 		var page = this.page(index);
	 		if(this.model.currentKey != this.key || this.currPage != index){
	 			this.firePageChange();
	 		}
	 		this.currPage = index;
	 		this.reload(this.currPage);
	 	},
	 	/*fire event start*/
	 	fireAddRow : function() {
	 		var attr = {row: arguments[0], page: arguments[1], current: (this.model.currentKey == this.key && this.currPage == arguments[1].pageIndex), index: arguments[2].at};
	 		this.model.fireAddRow.call(this.model, attr);
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
	 	fireSyncOver: function() {
	 		var page = arguments[0];
	 		page.synching = false;
	 		var attr = {key: page.store.key, pageIndex: page.pageIndex, current: (this.model.currentKey == this.key && this.currPage == page.pageIndex)};
	 		this.model.fireSyncOver.call(this.model, attr);
	 	},
	 	fireSynching: function(){
	 		var page = arguments[0];
	 		page.synching = true;
	 		var attr = {key: page.store.key, pageIndex: page.pageIndex, current: (this.model.currentKey == this.key && this.currPage == page.pageIndex)};
	 		this.model.fireSynching.call(this.model, attr);
	 	}
	 	/*fire event end*/
	 });
	 
	 
	 FwBase.Wtf.Model.Row = Backbone.Model.extend({
		 idAttribute : FwBase.Wtf.Model.defaults.idAttribute,
		 initialize : function() {
			 this.page = arguments[1].collection;
			 this.urlRoot = this.page.dataset.transurl;
			 this.page.listenTo(this, 'change', this.page.fireCellChange);
		 }
//		 sync : function() {
//			 var dataset = this.collection.dataset;
//			 var url = dataset.transurl;
//		 	 var type = methodMap[arguments[0]];
//		 	 this.url = url;
//		 	 if(type == "DELETE")
//		 		 this.url = this.url + "/" + this.id;
//		 	if(window.clientMode){
//		 		this.syncFromClient.apply(this, arguments);
//		 	}
//		 	else{
//		 		Backbone.sync.apply(this, arguments);
//		 	}
//		 }
	 });
	 
	 methodMap = {
	    'create': 'POST',
	    'update': 'PUT',
	    'patch':  'PATCH',
	    'delete': 'DELETE',
	    'read':   'GET'
	 };
	 /**
	  * Each rowlist stands for one page.It belongs to one store
	  */
	 FwBase.Wtf.Model.RowList = Backbone.Collection.extend({
	 	model : FwBase.Wtf.Model.Row,
	 	constructor : function() {
	 		this.pageIndex = 0;
	 		this.selections = {ids:[], indices:[], rows: []};
	 		Backbone.Collection.apply(this, arguments);
	 	},
	 	setParent: function(index, store, dataset) {
	 		this.pageIndex = index;
	 		this.store = store;
	 		this.dataset = dataset;
	 	},
	 	createRow : function() {
	 		//TODO emit set Default value event
	 		return new this.model({}, {collection: this});
	 	},
	 	removeRow : function(ids){
	 		
	 	},
	 	reset : function() {
	 		this.selections.ids = [];
	 		this.selections.indices = [];
	 		this.selections.rows = [];
	 	},
	 	row : function(row) {
	 		if(typeof row == "object")
	 			this.add(row);
	 		else if(typeof row == "string"){
	 			return this.get(row);
	 		}
	 		else
	 			return this.at(row);
	 	},
	 	rows : function(){
	 		return this.models;
	 	},
	 	select : function(){
	 		if(arguments.length == 0)
	 			return this.selections;
	 		var clear = true;
	 		if(arguments.length == 2){
	 			clear = arguments[1];
	 		}
	 		var newSelections = null;
	 		if(clear){
	 			newSelections = {ids: [], indices: [], rows: []};
	 		}	
	 		else{
	 			newSelections = this.selections;
	 		}
	 		var hasChanged = false;
//	 		var newSelections = this.selections;
	 		if(typeof arguments[0] == "string"){
	 			hasChanged = this.doSelectById(arguments[0], newSelections);
	 		}
	 		else if(typeof arguments[0] == "number"){
	 			hasChanged = this.doSelectByIndex(arguments[0], newSelections);
	 		}
	 		else if(arguments[0] instanceof Array){
	 			var arr = arguments[0];
	 			for(var i = 0; i < arr.length; i ++){
	 				if(typeof arr[i] == "string")
	 					hasChanged = this.doSelectById(arr[i], newSelections) || hasChanged;
	 				else
	 					hasChanged = this.doSelectByIndex(arr[i], newSelections) || hasChanged;
	 			}
	 		}
	 		if(clear)
	 			this.selections = newSelections;
	 		if(hasChanged)
	 			this.dataset.fireSelection.apply(this.dataset, [this.selections]);
	 	},
	 	doSelectById: function(id, newSelections) {
 			var model = this.get(id);
 			if(model != null){
 				var models = this.models;
 				var i = 0;
 				for(; i < models.length; i ++){
 					if(models[i] == model)
 						break;
 				}
 				var changed = _.indexOf(this.selections.ids, id) == -1;
 				var id = model.id;
 				newSelections.ids.push("" + id);
 				newSelections.indices.push(i);
 				newSelections.rows.push(model);
// 				return _.indexOf(this.selections.ids, id) == -1;
 				return changed;
 			}
 			else
 				return false;
	 	}, 
	 	doSelectByIndex: function(index, newSelections) {
 			var model = this.at(index);
 			if(model != null){
 				var changed = _.indexOf(this.selections.indices, index) == -1;
 				var id = model.id;
 				newSelections.ids.push("" + id);
 				newSelections.indices.push(index);
 				newSelections.rows.push(model);
// 				return _.indexOf(this.selections.indices, index) == -1;
 				return changed;
 			}
 			else
 				return false;
	 	},
	 	doUnSelectById: function(id) {
			var index = _.indexOf(this.selections.ids, id);
			if(index == -1)
				return false;
			this.selections.ids.splice(index, 1);
			this.selections.indices.splice(index, 1);
			this.selections.rows.splice(index, 1);
			return true;
	 	},
	 	doUnSelectByIndex: function(id) {
			var index = _.indexOf(this.selections.indices, id);
			if(index == -1)
				return false;
			this.selections.ids.splice(index, 1);
			this.selections.indices.splice(index, 1);
			this.selections.rows.splice(index, 1);
			return true;
	 	}, 
	 	unselect : function() {
	 		var hasChanged = false;
	 		if(typeof arguments[0] == "string"){
	 			hasChanged = this.doUnSelectById(arguments[0]);
	 		}
	 		else if(typeof arguments[0] == "number"){
	 			hasChanged = this.doUnSelectByIndex(arguments[0]);
	 		}
	 		else if(arguments[0] instanceof Array){
	 			var arr = arguments[0];
	 			for(var i = 0; i < arr.length; i ++){
	 				if(typeof arr[i] == "string")
	 					hasChanged = this.doUnSelectById(arr[i]) || hasChanged;
	 				else
	 					hasChanged = this.doUnSelectByIndex(arr[i]) || hasChanged;
	 			}
	 		}
	 		if(hasChanged)
	 			this.dataset.fireUnSelection.apply(this.dataset, [this.selections]);
	 	},
	 	
	 	/* fire event start*/
	 	fireCellChange: function() {
	 		this.dataset.fireCellChange.apply(this.dataset, arguments);
	 	},
	 	/* fire event end*/
	 	
	 	/* override start*/
	 	sync : function() {
	 		var url = this.dataset.transurl;
	 		var type = methodMap[arguments[0]];
	 		this.url = url;
	 		var qm = false;
	 		if(type == "GET"){
	 			qm = true;
	 			this.url = url + "?s_page=" + this.pageIndex + "," + this.dataset.metadata.pageSize; 
	 		}
	 		var filters = this.dataset.filters();
	 		if(filters != null && filters.length > 0){
	 			if(!qm){
	 				this.url += "?";
	 				qm = true;
	 			}
	 			this.url += "s_filter=" + $.toJSON(filters);
	 		}
	 		var reqPrams = this.dataset.reqParams();
	 		if(reqPrams != null && !_.isEmpty(reqPrams)){
	 			if(!qm){
	 				this.url += "?";
	 				qm = true;
	 			}
	 			for(var i in reqPrams){
	 				this.url += "&";
	 				this.url += (i + "=" + reqPrams[i]);
	 			}
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
	 	/*override end*/
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
	 
	 
	 function getRowListByType(idAttribute){
		 if(FwBase.Wtf.Model["RowList" + idAttribute] == null){
			 FwBase.Wtf.Model["Row" + idAttribute] = FwBase.Wtf.Model.Row.extend({
				 idAttribute : idAttribute
			 });
			 FwBase.Wtf.Model["RowList" + idAttribute] = FwBase.Wtf.Model.RowList.extend({
				 model : FwBase.Wtf.Model["Row" + idAttribute]
			 });
		 }
	 }
});