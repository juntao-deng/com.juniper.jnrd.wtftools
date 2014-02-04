define(["base/listener"], function(){
	FwBase.Wtf.View.Controls.BaseControl = function(parentContainer, metadata, id, ctx){
		this.el = parentContainer;
		this.id = id;
		this.metadata = metadata;
		this.ctx = (ctx == null ? $app : ctx);
		this.dataObj = null;
		this.create();
	};
	FwBase.Wtf.View.Controls.BaseControl.instances = {};
	$.extend(FwBase.Wtf.View.Controls.BaseControl.prototype, FwBase.Wtf.View.Controls.Listener.prototype, {
		create : function(){
			if(this.metadata == null || this.metadata === window.globalEmptyObj){
				this.metadata = {mock: true};
				this.mockMetadata();
			}
			else if(this.metadata.mock){
				this.mockMetadata();
			}
			else
				this.setDefault({mock: false});
			var ct = this.type();
			if(FwBase.Wtf.View.Controls.BaseControl.instances[ct] == null){
				FwBase.Wtf.View.Controls.BaseControl.instances[ct] = 0;
			}
			this.instance = (FwBase.Wtf.View.Controls.BaseControl.instances[ct] ++);
			this.makeDefault();
			this.setDefault({enable : true, visible : true});
			if(this.template == null)
				this.template = this.templateInit();
			var childHtml = this.paint();
			this.el.append(childHtml);
			this.postInit();
			this.visibleAttr = true;
			this.enableAttr = true;
			if(!this.metadata.visible)
				this.visible(false);
			if(!this.metadata.enable)
				this.enable(false);
			if(this.metadata.statemgr){
				if(typeof this.metadata.statemgr == 'string'){
					this.statemgr = new this.metadata.stagemgr(this, this.ctx, this.ctx.stateManager());
				}
				else{
//					if(typeof this.metadata.statemgr == 'function'){
//						this.statemgr = this.metadata.statemgr;
//					}
//					else{
					var mgrClass = this.metadata.statemgr.func;
					if(mgrClass != null){
						this.statemgr = new mgrClass(this, this.ctx, this.ctx.stateManager);
						this.statemgr.setOptions(this.metadata.statemgr.params);
					}
//					}
				}
			}
		},
		paint : function() {
			return this.template($.extend(null, this.metadata, {instanceId: this.instance, compId: this.id}));
		},
		type : function(){
			return "comp_";
		},
		postInit : function() {
		},
		/**
		 * each component set default attr itself
		 * @param metadata
		 */
		makeDefault : function(){
			
		},
		
		mockMetadata : function() {
			
		},
		
		setDefault : function(metadata){
			Util.setDefault(this.metadata, metadata);
		},
		
		visible : function(){
			if(arguments.length == 0)
				return this.visibleAttr;
			if(this.visbileAttr == arguments[0])
				return;
			if(this.doVisible)
				this.doVisible(arguments[0]);
			else{
				if(arguments[0])
					this.el.css("display", "visible");
				else
					this.el.css("display", "none");
			}
			this.visibleAttr = arguments[0];
		},
		enable : function(){
			if(arguments.length == 0)
				return this.enableAttr;
			if(this.enableAttr == arguments[0])
				return;
			if(this.doEnable)
				this.doEnable(arguments[0]);
			this.enableAttr = arguments[0];
		},
		reset : function(metadata){
			this.destroy();
			this.metadata = metadata;
			this.create();
		},
		destroy : function() {
			this.off();
			this.data = null;
			this.el.html("");
		},
		updateState : function(trigger) {
			if(this.statemgr)
				this.statemgr.updateState(trigger);
		},
		/**
		 * return all accepted events, for designer
		 */
		eventDescs : function() {
			return null;
		},
		/**
		 * return all public methods, for designer
		 */
		methodDescs : function() {
			return [{name : 'visible', params: [{name: 'visible', type: 'boolean'}], desc: 'Set the component visible or not'},
			        {name : 'visible', desc: "Get the component's visible state"}];
		},
		data : function() {
			if(arguments.length == 0)
				return this.dataObj;
			if(arguments.length == 1 && typeof arguments[0] == "string")
				return this.dataObj ? this.dataObj[arguments[0]] : null;
			else if(arguments.length == 1 && typeof arguments[0] == "object")
				this.dataObj = arguments[0];
			else{
				if(!this.dataObj)
					this.dataObj = {};
				this.dataObj[arguments[0]] = arguments[1];
			}
		},
		cloneMetadata : function(){
			return $.extend(true, {}, this.metadata);
		}
	});
	return FwBase.Wtf.View.Controls.BaseControl;
});