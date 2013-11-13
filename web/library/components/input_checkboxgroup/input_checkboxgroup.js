define(["input_base/input_base"], function(inputbase){	
	FwBase.Wtf.View.Controls.Input_checkboxgroup = function(){
		FwBase.Wtf.View.Controls.InputBase.apply(this, arguments);
	};
	$.extend(FwBase.Wtf.View.Controls.Input_checkboxgroup.prototype, FwBase.Wtf.View.Controls.InputBase.prototype, 
		{
			template: _.template($('#sys_atom_controls_input_checkboxgroup').html()),
			mockMetadata : function(){
				this.setDefault({label : '', placeHolder : 'Select an option ...'});
				this.setDefault(datas);
			},
			makeDefaultFurther : function() {
				this.setDefault({options:null, line: false});
			},
			inputMask : function() {
				this.input = this.el.children('input_checkboxgroup');
			},
			value : function() {
				if(arguments.length == 0){
					return this.input.val();
				}
				else{
					
				}
			}
		}
	);
	
	var datas = {options:[
					{value: 'Dallas Cowboys', name: 'Dallas Cowboys'},
					{value: 'New York Giants', name: 'New York Giants'},
					{value: 'Philadelphia Eagles', name: 'Philadelphia Eagles'},
					{value: 'Washington Redskins', name: 'Washington Redskins'}
					]
				};
	return FwBase.Wtf.View.Controls.Input_checkboxgroup;
});