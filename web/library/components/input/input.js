define(["input_base/input_base"], function(inputbase){	
	FwBase.Wtf.View.Controls.Input = function(){
		FwBase.Wtf.View.Controls.InputBase.apply(this, arguments);
	};
	$.extend(FwBase.Wtf.View.Controls.Input.prototype, FwBase.Wtf.View.Controls.InputBase.prototype, 
		{
			mockMetadata : function(){
//				this.setDefault({linear: true, label : 'String Input:', placeHolder : 'Please input here ...', 
//							popOverTitle: 'Pop Over', popOverContent : 'Popover body goes here. This is a String input',
//							hint : 'This is hint for input'});
			},
			makeDefaultFurther : function() {
			}
		}
	);
	return FwBase.Wtf.View.Controls.Input;
});