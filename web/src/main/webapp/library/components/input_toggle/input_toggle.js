define(["input_base/input_base", "./toggle_buttons", "css!./toggle_buttons"], function(inputbase){	
	FwBase.Wtf.View.Controls.Input_toggle = function(){
		FwBase.Wtf.View.Controls.InputBase.apply(this, arguments);
	};
	$.extend(FwBase.Wtf.View.Controls.Input_toggle.prototype, FwBase.Wtf.View.Controls.InputBase.prototype, 
		{
			template: _.template($('#sys_atom_controls_input_toggle').html()),
			postInit : function(){
				this.el.find("#toggle-button").toggleButtons({
					label : {enabled:'ON', disabled:'OFF'}
				});
			},
			makeDefaultFurther : function(){
			},
			mockMetadata : function() {
				this.setDefault({label : 'Toggle Input:', placeHolder : 'Please input here ...', 
						hint : 'This is hint for input'});
			}
		}
	);
	return FwBase.Wtf.View.Controls.Input_toggle;
});