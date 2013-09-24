FwBase.Wtf.View.Controls.Menu = {
	template: _.template($('#sys_atom_controls_menu').html()),
	create : function(parentContainer, metadata){
		parentContainer.append(this.template(metadata));
	}
};