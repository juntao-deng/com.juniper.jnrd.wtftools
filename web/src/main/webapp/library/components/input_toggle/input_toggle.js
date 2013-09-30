define(["input/inputbase", "./toggle-buttons", "css!./toggle-buttons"], function(inputbase){	
	FwBase.Wtf.View.Controls.Input_toggle = function(){
		FwBase.Wtf.View.Controls.InputBase.apply(this, arguments);
	};
	$.extend(FwBase.Wtf.View.Controls.Input_toggle.prototype, FwBase.Wtf.View.Controls.InputBase.prototype, 
		{
			template: _.template($('#sys_atom_controls_input_toggle').html()),
			postInit : function(){
				this.el.find("#toggle-button").toggleButtons({
					label : {enabled:'ON', disabled:'OFF'}
				});
			},
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