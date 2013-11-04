define(function(){
	var STATEMGR = FwBase.Wtf.View.ButtonStateManager = function(app){
		this.app = app;
		this.attrs = {};
	};
	FwBase.Wtf.View.ButtonStateManager.STATE_INVISIBLE = "2";
	FwBase.Wtf.View.ButtonStateManager.STATE_DISABLE = "1";
	FwBase.Wtf.View.ButtonStateManager.STATE_ENABLE = "0";
	$.extend(FwBase.Wtf.View.ButtonStateManager.prototype, {
		attrs : function(attrs) {
			for(var i in attrs){
				this.attrs[i] = attrs[i];
			}
		},
		updateButtons : function() {
			var components = this.app.components();
			if(components){
				for(var i = 0; i < components.length; i ++){
					var comp = components[i];
					if(comp.stateful && comp.statemgr){
						var state = comp.statemgr.state();
						if(state != null){
							this.stateCallBack(comp, state);
						}
					}
				}
			}
		},
		stateCallBack : function(comp, state) {
			if(state == STATEMGR.STATE_INVISIBLE)
				comp.visible(false);
			else if(state == STATEMGR.STATE_DISABLE)
				comp.disable(false);
			else{
				comp.visible(true);
				comp.disable(true);
			}
		}
	});
	
	FwBase.Wtf.View.ButtonState = function(comp, app, manager) {
		this.comp = comp;
		this.app = app;
		this.manager = manager;
	};
	$.extend(FwBase.Wtf.View.ButtonState.prototype, {
		state : function() {
			alert("The state method need to be implemented");
		},
		setState : function(state) {
			this.manager.stateCallBack.call(this.manager, this.comp, state);
		}
	});
};