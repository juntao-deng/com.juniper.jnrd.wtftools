define(["base/base"], function(base){
	FwBase.Wtf.View.Controls.Spring = function(){
		FwBase.Wtf.View.Controls.BaseControl.apply(this, arguments);
	};
	
	$.extend(FwBase.Wtf.View.Controls.Spring.prototype, FwBase.Wtf.View.Controls.BaseControl.prototype, 
		{
			template: _.template($('#sys_atom_controls_spring').html()),
			postInit : function(){
				this.el.addClass("inline");
			},
			makeDefault : function(){
				this.setDefault({width:'10'});
			}
		}
	);
	return FwBase.Wtf.View.Controls.Spring;
});