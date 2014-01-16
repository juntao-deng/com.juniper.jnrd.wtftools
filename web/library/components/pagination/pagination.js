FwBase.Wtf.View.Controls.Pagination = {
	template: _.template($('#sys_atom_controls_pagination').html()),
	create : function(parentContainer, headers){
		parentContainer.append(this.template(null));
	}
}