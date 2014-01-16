define(["input_base/input_base"], function(inputbase){	
	FwBase.Wtf.View.Controls.Input_search = function(){
		FwBase.Wtf.View.Controls.InputBase.apply(this, arguments);
	};
	$.extend(FwBase.Wtf.View.Controls.Input_search.prototype, FwBase.Wtf.View.Controls.InputBase.prototype, 
		{
			template: _.template($('#sys_atom_controls_input_search').html()),
			mockMetadata : function(){
			},
			makeDefaultFurther : function() {
				this.metadata.width = "100";
			}
		}
	);
	return FwBase.Wtf.View.Controls.Input_search;
});