define(["input_base/input_base"], function(inputbase){	
	FwBase.Wtf.View.Controls.Input_daterange = function(){
		FwBase.Wtf.View.Controls.InputBase.apply(this, arguments);
	};
	$.extend(FwBase.Wtf.View.Controls.Input_daterange.prototype, FwBase.Wtf.View.Controls.InputBase.prototype, 
		{
			inputMask : function(){
				this.input.datepicker();
			}
		}
	);
	return FwBase.Wtf.View.Controls.Input_daterange;
});


////Select a Date Range with datepicker
//$( "#rangeBa" ).datepicker({
//    defaultDate: "+1w",
//    changeMonth: true,
//    numberOfMonths: 3,
//    onClose: function( selectedDate ) {
//        $( "#rangeBb" ).datepicker( "option", "minDate", selectedDate );
//    }
//});
//$( "#rangeBb" ).datepicker({
//    defaultDate: "+1w",
//    changeMonth: true,
//    numberOfMonths: 3,
//    onClose: function( selectedDate ) {
//        $( "#rangeBa" ).datepicker( "option", "maxDate", selectedDate );
//    }
//});


//$('#file').customFileInput({
//    button_position : 'right'
//});