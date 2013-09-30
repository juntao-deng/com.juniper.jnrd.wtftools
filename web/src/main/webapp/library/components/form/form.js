define(function(){
	FwBase.Wtf.View.Controls.Form = {
		template: _.template($('#sys_atom_controls_form').html()),
		create : function(parentContainer, headers){
			parentContainer.append(this.template(null));
			TableAdvanced.init();
		},
		fillData: function(datas){
			
		}
	};
	return FwBase.Wtf.View.Controls.Form;
});