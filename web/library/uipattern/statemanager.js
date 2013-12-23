define(function(){
	var STATEMGR = window.AppStateManager = FwBase.Wtf.View.AppStateManager = function(app){
		this.app = app;
		this.attrs = {};
	};
	FwBase.Wtf.View.AppStateManager.STATE_INVISIBLE = "2";
	FwBase.Wtf.View.AppStateManager.STATE_DISABLE = "1";
	FwBase.Wtf.View.AppStateManager.STATE_ENABLE = "0";
	$.extend(FwBase.Wtf.View.AppStateManager.prototype, {
		attrs : function(attrs) {
			for(var i in attrs){
				this.attrs[i] = attrs[i];
			}
		},
		initState : function() {
			var comps = this.app.components();
			for(var i = 0; i < comps.length; i ++){
				comps[i].updateState(this);
			}
		},
		connectState: function(triggerObjs, stateObjs){
  	 		if(triggerObjs == null || triggerObjs.length == 0 || stateObjs == null || stateObjs.length == 0)
  	 			return;
  	 		var oThis = this;
  	 		for(var i = 0; i < triggerObjs.length; i ++){
  	 			var trigger = triggerObjs[i];
  	 			if(trigger instanceof window.Model){
  	 				trigger.on("selection", function(){
  	 					for(var j = 0; j < stateObjs.length; j ++){
  	 						stateObjs[j].updateState(trigger);
  	 					}
  	 				});
  	 			}
  	 		}
  	 	}
	});
	
	FwBase.Wtf.View.StateMgr = function(comp, app, manager) {
		this.comp = comp;
		this.app = app;
		this.manager = manager;
		this.options = null;
	};
	$.extend(FwBase.Wtf.View.StateMgr.prototype, {
		setOptions : function(options) {
			this.options = options;
		},
		state : function(trigger) {
			alert("The state method need to be implemented");
		},
		setState : function(state) {
			if(state == STATEMGR.STATE_INVISIBLE)
				this.comp.visible(false);
			else if(state == STATEMGR.STATE_DISABLE)
				this.comp.enable(false);
			else{
				this.comp.visible(true);
				this.comp.enable(true);
			}
		},
		updateState : function(trigger) {
			var state = this.state(trigger);
			if(state != null){
				this.setState(state);
			}
		},
		detectModel : function(trigger){
			if(this.options){
				var model = this.options.model;
				if(model != null){
					if(typeof model == "string"){
						model = this.app.model(model);
						return model;
					}
					return model;
				}
			}
			else{
				if(trigger instanceof window.Model)
					return trigger;
				else{
					//try to find a model that is binding to a grid, if none, then form
					var comps = this.app.components();
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
			}
			return null;
		}
	});
	
	/**
	 * Single selection enable state manager
	 */
	FwBase.Wtf.View.S_StateMgr = function(comp, app, manager) {
		FwBase.Wtf.View.StateMgr.call(this, comp, app, manager);
	};
	
	$.extend(FwBase.Wtf.View.S_StateMgr.prototype, FwBase.Wtf.View.StateMgr.prototype, {
		state : function(trigger) {
			var model = this.detectModel(trigger);
			if(model == null){
				alert("can not find trigger model for S_StateMgr");
				return STATE_ENABLE.STATE_ENABLE;
			}
			var selections = model.selections();
			if(selections && selections.rows.length == 1)
				return STATEMGR.STATE_ENABLE;
			return STATEMGR.STATE_DISABLE;
		}
	});
	
	
	/**
	 * MultiSelections enable state manager
	 */
	FwBase.Wtf.View.M_StateMgr = function(comp, app, manager) {
		FwBase.Wtf.View.StateMgr.call(this, comp, app, manager);
	};
	
	$.extend(FwBase.Wtf.View.M_StateMgr.prototype, FwBase.Wtf.View.StateMgr.prototype, {
		state : function(trigger) {
			var model = this.detectModel(trigger);
			if(model == null){
				alert("can not find trigger model for S_StateMgr");
				return STATE_ENABLE.STATE_ENABLE;
			}
			var selections = model.selections();
			if(selections && selections.rows.length >= 1)
				return STATEMGR.STATE_ENABLE;
			return STATEMGR.STATE_DISABLE;
		}
	});
});