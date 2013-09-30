define(["input/inputbase"], function(inputbase){	
	FwBase.Wtf.View.Controls.Input = function(){
		FwBase.Wtf.View.Controls.InputBase.apply(this, arguments);
	};
	$.extend(FwBase.Wtf.View.Controls.Input.prototype, FwBase.Wtf.View.Controls.InputBase.prototype, 
		{
			template: _.template($('#sys_atom_controls_input').html()),
			makeDefault : function(metadata){
				if(metadata === window.globalEmptyObj){
					metadata = {linear: true, label : 'String Input:', placeHolder : 'Please input here ...', 
								popOverTitle: 'Pop Over', popOverContent : 'Popover body goes here. This is a String input',
								hint : 'This is hint for input'};
					this.metadata = metadata;
				}
			}
		}
	);
	return FwBase.Wtf.View.Controls.Input;
});