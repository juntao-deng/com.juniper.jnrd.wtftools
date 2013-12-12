define(function(){
	var UIPattern = FwBase.Wtf.UIPattern = {};
	FwBase.Wtf.UIPattern.defaults = {
		dialogWidth : 800,
		dialogHeight : 420,
		editform : "editform"
	};
	FwBase.Wtf.UIPattern.Action = {};
	FwBase.Wtf.UIPattern.Action.AddAction = function(param) {
		var width = param.eventCtx.width ? param.eventCtx.width : UIPattern.defaults.dialogWidth;
		var height = param.eventCtx.height ? param.eventCtx.height : UIPattern.defaults.dialogHeight;
		var url = param.eventCtx.url;
		var title = param.eventCtx.title || "Add";
		if(url == null || url == ""){
			var appid = this.ctx.id;
			url = appid + "/" + UIPattern.defaults.editform;
		}
		
		AppUtil.navigateToDialog(url, null, {width: width, height : height, title: title});
	};
	
	FwBase.Wtf.UIPattern.Action.EditAction = function() {
		var width = param.eventCtx.width ? param.eventCtx.width : UIPattern.defaults.dialogWidth;
		var height = param.eventCtx.height ? param.eventCtx.height : UIPattern.defaults.dialogHeight;
		var url = param.eventCtx.url;
		var title = param.eventCtx.title || "Add";
		if(url == null || url == ""){
			var appid = this.ctx.id;
			url = appid + "/" + UIPattern.defaults.editform;
		}
		
		var itemId = param.eventCtx.itemId;
		if(itemId == null){
			var centerModel = $app.model('center_model');
			if(centerModel != null){
				var row = centerModel.select().rows[0];
				if(row == null){
					return;
				}
				itemId = row.id;
			}
		}
		AppUtil.navigateToDialog(url, {itemId: itemId}, {width: width, height : height, title: title});
	};
	
	FwBase.Wtf.UIPattern.Action.DelAction = function() {
		alert("delete");
	};

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