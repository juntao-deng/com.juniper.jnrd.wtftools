define(function(){
	FwBase.Wtf.UIPattern = {};
	FwBase.Wtf.UIPattern.Handler = {};
	FwBase.Wtf.UIPattern.Handler.CrudUIHandler = function() {
		
	};
	$.extend(FwBase.Wtf.UIPattern.Handler.CrudUIHandler.prototype, {
		run : function(obj){
			if(obj.eventCtx && obj.eventCtx.stop)
				return;
			var id = obj.trigger.id;
			var action = FwBase.Wtf.UIPattern.Action[window.capitalize(id) + "Action"];
			if(action && typeof action == 'function'){
				action.run.apply(obj);
			}
			else{
				alert(obj.id + "->" + id + " performed");
			}
		}
	});
});