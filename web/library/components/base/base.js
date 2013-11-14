define(["base/listener"], function(){
	FwBase.Wtf.View.Controls.BaseControl = function(parentContainer, metadata, id, ctx){
		this.el = parentContainer;
		this.id = id;
		this.metadata = metadata;
		this.ctx = (ctx == null ? $app : ctx);
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
			this.visibleAttr = this.metadata.visible;
			this.enableAttr = this.metadata.enable;
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
			for(var attr in metadata){
				if(typeof this.metadata[attr] == 'undefined')
					this.metadata[attr] = metadata[attr];
			}
		},
		
		visible : function(visible){
			if(this.visbile && visible)
				return;
			if(visible)
				this.el.css("display", "visible");
			else
				this.el.css("display", "none");
			this.visible = visible;
		},
		
		reset : function(metadata){
			this.destroy();
			this.metadata = metadata;
			this.create();
		},
		destroy : function() {
			this.el.html("");
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
			return [{name : 'visible', params: {type: 'boolean'}, desc: 'Set the component visible or not'},
			        {name : 'visible', desc: "Get the component's visible state"}];
		}
	});
	return FwBase.Wtf.View.Controls.BaseControl;
});