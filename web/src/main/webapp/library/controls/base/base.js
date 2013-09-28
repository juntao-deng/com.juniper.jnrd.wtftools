define(["base/listener"], function(){
	FwBase.Wtf.View.Controls.BaseControl = function(){};
	$.extend(FwBase.Wtf.View.Controls.BaseControl.prototype, FwBase.Wtf.View.Controls.Listener.prototype, {
		create : function(parentContainer, metadata, id){
			var childHtml = this.template(metadata);
			parentContainer.append(childHtml);
			this.el = parentContainer;
			this.id = id;
			this.metadata = metadata;
			this.postInit();
		},
		postInit : function() {
		},
		registerCallBack : function(){
			//do nothing
		},
		putCallBack : function(key, func){
			
		}
	});
	return FwBase.Wtf.View.Controls.BaseControl;
});