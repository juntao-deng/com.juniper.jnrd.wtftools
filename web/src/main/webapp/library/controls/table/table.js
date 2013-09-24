loadCss("library/controls/table/tablectrl.css");
define(["./datatable", "./tablectrl"], function(tablebase, tablectrl){
	FwBase.Wtf.View.Controls.Table = {
		template: _.template($('#sys_atom_controls_table').html()),
		create : function(parentContainer, headers){
			parentContainer.append(this.template(null));
			TableAdvanced.init();
		},
		fillData: function(datas){
			
		}
	};
	return FwBase.Wtf.View.Controls.Table;
});