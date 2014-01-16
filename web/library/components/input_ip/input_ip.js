define(["input_base/input_base"], function(inputbase){	
	FwBase.Wtf.View.Controls.Input_ip = function(){
		FwBase.Wtf.View.Controls.InputBase.apply(this, arguments);
	};
	$.extend(FwBase.Wtf.View.Controls.Input_ip.prototype, FwBase.Wtf.View.Controls.InputBase.prototype, 
		{
			makeDefaultFurther : function() {
			},
			inputMask : function() {
				this.input.inputmask("ip");
			}
		}
	);
	return FwBase.Wtf.View.Controls.Input_ip;
});