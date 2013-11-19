define(["input_base/input_base", "./toggle_buttons", "css!./toggle_buttons"], function(inputbase){	
	FwBase.Wtf.View.Controls.Input_toggle = function(){
		FwBase.Wtf.View.Controls.InputBase.apply(this, arguments);
	};
	$.extend(FwBase.Wtf.View.Controls.Input_toggle.prototype, FwBase.Wtf.View.Controls.InputBase.prototype, 
		{
			template: _.template($('#sys_atom_controls_input_toggle').html()),
			inputMask : function(){
				this.el.find("#toggle-button").toggleButtons({
					label : {enabled:'ON', disabled:'OFF'}
				});
			},
			makeDefaultFurther : function(){
				this.setDefault({defaultValue: true});
			},
			mockMetadata : function() {
//				this.setDefault({label : 'Toggle Input:', placeHolder : 'Please input here ...', 
//						hint : 'This is hint for input'});
			},
			checked : function(){
				if(arguments.length == 0)
					return this.input.is(':checked');
				else{
					this.el.find("#toggle-button").toggleButtons('setState', arguments[0], false);
				}
			}
		}
	);
	return FwBase.Wtf.View.Controls.Input_toggle;
});