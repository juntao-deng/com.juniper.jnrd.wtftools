define(["input/inputbase"], function(inputbase){	
	FwBase.Wtf.View.Controls.Input_date = function(){
		FwBase.Wtf.View.Controls.InputBase.apply(this, arguments);
	};
	$.extend(FwBase.Wtf.View.Controls.Input_date.prototype, FwBase.Wtf.View.Controls.InputBase.prototype, 
		{
			template: _.template($('#sys_atom_controls_input_date').html()),
			postInit : function(){
				this.el.find("input").datepicker({
				      changeMonth: true,
				      changeYear: true
				});
			},
			makeDefault : function(metadata){
				if(metadata === window.globalEmptyObj){
					metadata = {linear: true, label : 'Date Input:', placeHolder : 'Please input here ...', 
								popOverTitle: 'Pop Over', popOverContent : 'Popover body goes here. This is a Date input',
								hint : 'This is hint for input'};
					this.metadata = metadata;
				}
			}
		}
	);
	return FwBase.Wtf.View.Controls.Input;
});