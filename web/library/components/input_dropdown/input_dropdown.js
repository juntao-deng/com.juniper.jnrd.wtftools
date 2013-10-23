define(["input_base/input_base", "./chosen", "css!./chosen"], function(inputbase){	
	FwBase.Wtf.View.Controls.Input_dropdown = function(){
		FwBase.Wtf.View.Controls.InputBase.apply(this, arguments);
	};
	$.extend(FwBase.Wtf.View.Controls.Input_dropdown.prototype, FwBase.Wtf.View.Controls.InputBase.prototype, 
		{
			template: _.template($('#sys_atom_controls_input_dropdown').html()),
			mockMetadata : function(){
				this.setDefault({label : '', placeHolder : 'Select an option ...', 
							hint : 'This is hint for input'});
				this.setDefault(datas);
			},
			makeDefaultFurther : function() {
				this.setDefault({filter : true, multiple : false, emptyRecord:false, options:null, groups:null, placeHolder : 'Select an option ...'});
			},
			inputMask : function() {
				this.input = this.el.find('select');
				this.input.chosen();
//				if(this.metadata.filter){
//				}
				if(this.metadata.value != null)
					this.value(this.metadata.value);
			},
			value : function() {
				if(arguments.length == 0){
					return this.input.val();
				}
				else{
					this.input.attr('value', arguments[0]);
					this.input.trigger("liszt:updated");
				}
			}
		}
	);
	
	var datas = {
		groups:[
			{label:'NFC EAST', options:[
				{value: 'Dallas Cowboys', name: 'Dallas Cowboys'},
				{value: 'New York Giants', name: 'New York Giants'},
				{value: 'Philadelphia Eagles', name: 'Philadelphia Eagles'},
				{value: 'Washington Redskins', name: 'Washington Redskins'}
			]},
			{label:'NFC NORTH', options:[
				{value: 'Chicago Bears', name: 'Chicago Bears'},
				{value: 'Detroit Lions', name: 'Detroit Lions'},
				{value: 'Green Bay Packers', name: 'Green Bay Packers'},
				{value: 'Minnesota Vikings', name: 'Minnesota Vikings'}
			]},
			{label:'NFC SOUTH', options:[
				{value: 'Atlanta Falcons', name: 'Atlanta Falcons'},
				{value: 'Carolina Panthers', name: 'Carolina Panthers'},
				{value: 'New Orleans Saints', name: 'New Orleans Saints'},
				{value: 'Tampa Bay Buccaneers', name: 'Tampa Bay Buccaneers'}
			]},
			{label:'NFC WEST', options:[
				{value: 'Arizona Cardinals', name: 'Arizona Cardinals'},
				{value: 'St. Louis Rams', name: 'St. Louis Rams'},
				{value: 'San Francisco 49ers', name: 'San Francisco 49ers'},
				{value: 'Seattle Seahawks', name: 'Seattle Seahawks'}
			]},
		]
	};
	return FwBase.Wtf.View.Controls.Input_dropdown;
});