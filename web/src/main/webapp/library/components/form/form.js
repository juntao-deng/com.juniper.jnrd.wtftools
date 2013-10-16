define(["base/base"], function(base){
	FwBase.Wtf.View.Controls.Form = function(){
		FwBase.Wtf.View.Controls.BaseControl.apply(this, arguments);
	};
	$.extend(FwBase.Wtf.View.Controls.Form.prototype, FwBase.Wtf.View.Controls.BaseControl.prototype, {
		template: _.template($('#sys_atom_controls_form').html()),
		postInit : function(){
		},
		makeDefault : function() {
			
		}
	});
	return FwBase.Wtf.View.Controls.Form;
});