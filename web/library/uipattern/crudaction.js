define(function(){
	var UIPattern = FwBase.Wtf.UIPattern = {};
	FwBase.Wtf.UIPattern.defaults = {
		dialogWidth : 800,
		dialogHeight : 420,
		editform : "editform"
	};
	FwBase.Wtf.UIPattern.Action = {};
	
	FwBase.Wtf.UIPattern.BaseAction = function(params) {
		this.params = params || {};
	};
	FwBase.Wtf.UIPattern.BaseAction.prototype.detectModel = function(opt){
		var ctx = opt.source.ctx;
		if(this.options){
			var model = this.params.model;
			if(model != null){
				if(typeof model == "string"){
					model = ctx.model(model);
					return model;
				}
				return model;
			}
		}
		else{
			//try to find a model that is binding to a grid, if none, then form
			var comps = ctx.components();
			for(var i = 0; i < comps.length; i ++){
				if(FwBase.Wtf.View.Controls.Grid != null && comps[i] instanceof FwBase.Wtf.View.Controls.Grid){
					var model = comps[i].model;
					if(model)
						return model;
				}
			}
			for(var i = 0; i < comps.length; i ++){
				if(FwBase.Wtf.View.Controls.Form != null && comps[i] instanceof FwBase.Wtf.View.Controls.Form){
					var model = comps[i].model;
					if(model)
						return model;
				}
			}
		}
		return null;
	}
	
	
	FwBase.Wtf.UIPattern.Action.AddAction = function(params) {
		FwBase.Wtf.UIPattern.BaseAction.call(this, params);
		this.execute = function(options) {
			var width = options.eventCtx.width || this.params.width || UIPattern.defaults.dialogWidth;
			var height = options.eventCtx.height || this.params.height || UIPattern.defaults.dialogHeight;
			var url = options.eventCtx.url;
			var title = options.eventCtx.title || this.params.title || "Add";
			if(url == null || url == ""){
				var appid = options.source.ctx.id;
				url = appid + "/" + UIPattern.defaults.editform;
			}
			
			AppUtil.navigateToDialog(url, null, {width: width, height : height, title: title});
		}
	};
	$.extend(FwBase.Wtf.UIPattern.Action.AddAction.prototype, FwBase.Wtf.UIPattern.BaseAction.prototype);
	
	FwBase.Wtf.UIPattern.Action.EditAction = function(params) {
		FwBase.Wtf.UIPattern.BaseAction.call(this, params);
		this.execute = function(options) {
			var width = options.eventCtx.width || this.params.width || UIPattern.defaults.dialogWidth;
			var height = options.eventCtx.height || this.params.height || UIPattern.defaults.dialogHeight;
			var url = options.eventCtx.url;
			var title = options.eventCtx.title || this.params.title || "Add";
			if(url == null || url == ""){
				var appid = options.source.ctx.id;
				url = appid + "/" + UIPattern.defaults.editform;
			}
			var itemId = options.eventCtx.itemId;
			if(itemId == null){
				var centerModel = this.detectModel(options);
				if(centerModel != null){
					itemId = centerModel.selections().ids[0];
					if(itemId == null){
						alert("error while getting itemId in FwBase.Wtf.UIPattern.Action.EditAction");
						return;
					}
				}
			}
			AppUtil.navigateToDialog(url, {itemId: itemId}, {width: width, height : height, title: title});
		}
	};
	$.extend(FwBase.Wtf.UIPattern.Action.EditAction.prototype, FwBase.Wtf.UIPattern.BaseAction.prototype);
	
	
	FwBase.Wtf.UIPattern.Action.DelAction = function(params) {
		FwBase.Wtf.UIPattern.BaseAction.call(this, params);
		this.execute = function(options) {
			var model = this.detectModel(options);
			if(model){
				var selections = model.selections();
				if(selections.ids.length == 0){
					alert("At lease select one row to delete");
					return;
				}
				model.removeRow(selections.ids);
			}
		};
	};
	
	$.extend(FwBase.Wtf.UIPattern.Action.DelAction.prototype, FwBase.Wtf.UIPattern.BaseAction.prototype);

	FwBase.Wtf.UIPattern.Action.Export2CsvAction = function() {
		
	};
	
	FwBase.Wtf.UIPattern.Action.Export2PdfAction = function() {
		
	};
	
	FwBase.Wtf.UIPattern.Action.Export2CsvAllAction = function() {
		
	};
	
	FwBase.Wtf.UIPattern.Action.Export2PdfAllAction = function() {
		
	};
	
	FwBase.Wtf.UIPattern.Action.PrintAction = function() {
		
	};
	
	FwBase.Wtf.UIPattern.Action.PrintAllAction = function() {
		
	};
	
	FwBase.Wtf.UIPattern.Action.HelpAction = function() {
		alert("help");
	};
	return FwBase.Wtf.UIPattern;
});