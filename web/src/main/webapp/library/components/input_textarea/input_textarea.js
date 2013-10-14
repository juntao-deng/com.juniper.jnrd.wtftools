define(["base/base"], function(base){
	FwBase.Wtf.View.Controls.Input_textarea = function(){
		FwBase.Wtf.View.Controls.BaseControl.apply(this, arguments);
	};
	
	$.extend(FwBase.Wtf.View.Controls.Input_textarea.prototype, FwBase.Wtf.View.Controls.BaseControl.prototype, 
		{
			template: _.template($('#sys_atom_controls_input_textarea').html()),
			postInit : function(){
				
			},
			makeDefault : function(){
				this.setDefault({rows : 3, width : 300});
			}
		}
	);
	return FwBase.Wtf.View.Controls.Input_textarea;
});