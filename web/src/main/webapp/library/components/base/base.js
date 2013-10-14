define(["base/listener"], function(){
	FwBase.Wtf.View.Controls.BaseControl = function(parentContainer, metadata, id){
		this.el = parentContainer;
		this.id = id;
		this.metadata = metadata;
		this.create();
	};
	$.extend(FwBase.Wtf.View.Controls.BaseControl.prototype, FwBase.Wtf.View.Controls.Listener.prototype, {
		create : function(){
			if(this.metadata == null || this.metadata === window.globalEmptyObj){
				this.metadata = {};
				this.mockMetadata();
			}
			this.makeDefault();
			var childHtml = this.template(this.metadata);
			this.el.append(childHtml);
			this.postInit();
			this.visibleAttr = !(this.el.css("display").toLowerCase() == 'none');
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
				if(this.metadata[attr] == null)
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
		}
	});
	return FwBase.Wtf.View.Controls.BaseControl;
});