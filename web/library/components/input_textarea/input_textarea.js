define(["input_base/input_base"], function(base){
	FwBase.Wtf.View.Controls.Input_textarea = function(){
		FwBase.Wtf.View.Controls.InputBase.apply(this, arguments);
	};
	
	$.extend(FwBase.Wtf.View.Controls.Input_textarea.prototype, FwBase.Wtf.View.Controls.InputBase.prototype, 
		{
			template: _.template($('#sys_atom_controls_input_textarea').html()),
			makeDefaultFurther : function(){
				this.setDefault({rows : 3, width : 300});
			},
			inputMask : function() {
				
			},
			getInput : function() {
				return this.el.find('textarea');
			}
		}
	);
	return FwBase.Wtf.View.Controls.Input_textarea;
});