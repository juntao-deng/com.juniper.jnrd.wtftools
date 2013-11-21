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
         					{value: 'value1', name: 'Value 1'},
        					{value: 'value2', name: 'Value 2'},
        					{value: 'value3', name: 'Value 3'},
        					{value: 'value4', name: 'Value 4'}
        				]
				};
	return FwBase.Wtf.View.Controls.Input_checkboxgroup;
});