define(["input_base/input_base", "./jquery-ui-timepicker-addon", "css!./input_datetime"], function(inputbase){	
	FwBase.Wtf.View.Controls.Input_datetime = function(){
		FwBase.Wtf.View.Controls.InputBase.apply(this, arguments);
	};
	$.extend(FwBase.Wtf.View.Controls.Input_datetime.prototype, FwBase.Wtf.View.Controls.InputBase.prototype, 
		{
			inputMask : function(){
				this.input.datetimepicker();
			}
		}
	);
	return FwBase.Wtf.View.Controls.Input_datetime;
});