define(["input/inputbase"], function(inputbase){	
	FwBase.Wtf.View.Controls.Input = function(){};
	$.extend(FwBase.Wtf.View.Controls.Input.prototype, FwBase.Wtf.View.Controls.InputBase.prototype, 
		{
			template: _.template($('#sys_atom_controls_input').html())
		}
	);
	return FwBase.Wtf.View.Controls.Input;
});