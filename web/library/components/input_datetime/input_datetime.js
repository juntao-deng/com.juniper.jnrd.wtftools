define(["input_base/input_base"], function(inputbase){	
	FwBase.Wtf.View.Controls.Input_datetime = function(){
		FwBase.Wtf.View.Controls.InputBase.apply(this, arguments);
	};
	$.extend(FwBase.Wtf.View.Controls.Input_datetime.prototype, FwBase.Wtf.View.Controls.InputBase.prototype, 
		{
			inputMask : function(){
				this.input.datepicker();
			}
		}
	);
	return FwBase.Wtf.View.Controls.Input_datetime;
});