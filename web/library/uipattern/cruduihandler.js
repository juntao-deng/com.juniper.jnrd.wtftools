define(["./crudaction"], function(){
	FwBase.Wtf.UIPattern.Handler = {};
	FwBase.Wtf.UIPattern.Handler.CrudUIHandler = function(obj) {
		if(obj.eventCtx && obj.eventCtx.stop)
			return;
		var id = obj.trigger.id;
		var action = FwBase.Wtf.UIPattern.Action[window.Util.capitalize(id) + "Action"];
		if(action && typeof action == 'function'){
			action.call(this, obj);
		}
//		else{
//			alert(obj.source.id + "->" + id + " performed");
//		}
	};
});