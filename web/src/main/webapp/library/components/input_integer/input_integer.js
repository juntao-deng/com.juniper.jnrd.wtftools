define(["input_base/input_base"], function(inputbase){	
	FwBase.Wtf.View.Controls.Input_integer = function(){
		FwBase.Wtf.View.Controls.InputBase.apply(this, arguments);
	};
	$.extend(FwBase.Wtf.View.Controls.Input_integer.prototype, FwBase.Wtf.View.Controls.InputBase.prototype, 
		{
			mockMetadata : function(){
				this.setDefault({linear: true, label : 'Integer Input:', placeHolder : 'Please input here ...', 
							popOverTitle: 'Pop Over', popOverContent : 'Popover body goes here. This is a Integer input',
							hint : 'This is hint for input'});
			},
			makeDefaultFurther : function() {
				this.setDefault({repeat:'10', greedy:false, clearIncomplete: false});
			},
			inputMask : function() {
				this.input.inputmask({mask: "9", repeat : this.metadata.repeat, "greedy": this.metadata.greedy, rightAlignNumerics: true});
			}
		}
	);
	return FwBase.Wtf.View.Controls.Input_integer;
});