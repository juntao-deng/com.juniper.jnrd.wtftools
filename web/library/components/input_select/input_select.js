define(["input_base/input_base"], function(inputbase){	
	FwBase.Wtf.View.Controls.Input_select = function(){
		FwBase.Wtf.View.Controls.InputBase.apply(this, arguments);
	};
	$.extend(FwBase.Wtf.View.Controls.Input_select.prototype, FwBase.Wtf.View.Controls.InputBase.prototype, 
		{
			template: _.template($('#sys_atom_controls_input_select').html()),
			mockMetadata : function(){
				this.setDefault({label : '', placeHolder : '', 
							hint : 'This is hint for input'});
				this.setDefault(datas);
			},
			makeDefaultFurther : function() {
				this.setDefault({filter : true, multiple : true, emptyRecord:false, options:null, groups:null, placeHolder : 'Select an option ...'});
			},
			inputMask : function() {
				this.input = this.el.find('select');
				if(this.metadata.value != null)
				this.value(this.metadata.value);
			},
			value : function() {
				if(arguments.length == 0){
					return this.input.val();
				}
				else{
					this.input.attr('value', arguments[0]);
				}
			}
		}
	);
	
	var datas = {
			options:[
				{value: 'Red', name: 'Red'},
				{value: 'yellow', name: 'Yellow'},
				{value: 'Green', name: 'Green'},
				{value: 'Blue', name: 'Blue'},
				{value: 'White', name: 'White'},
				{value: 'Dark', name: 'Dark'}
				,{value: 'Orange', name: 'Orange'}
			]
	};
	return FwBase.Wtf.View.Controls.Input_select;
});