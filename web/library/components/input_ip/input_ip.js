define(["input_base/input_base"], function(inputbase){	
	FwBase.Wtf.View.Controls.Input_ip = function(){
		FwBase.Wtf.View.Controls.InputBase.apply(this, arguments);
	};
	$.extend(FwBase.Wtf.View.Controls.Input_ip.prototype, FwBase.Wtf.View.Controls.InputBase.prototype, 
		{
			mockMetadata : function(){
				this.setDefault({linear: true, label : 'Ip Input:', placeHolder : 'Please input here ...', 
							popOverTitle: 'Pop Over', popOverContent : 'Popover body goes here. This is a IP v4 input',
							hint : 'This is hint for input'});
			},
			makeDefaultFurther : function() {
			},
			inputMask : function() {
				this.input.inputmask("ip");
			}
		}
	);
	return FwBase.Wtf.View.Controls.Input_ip;
});