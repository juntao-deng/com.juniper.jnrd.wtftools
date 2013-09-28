loadCss("library/controls/table/tablectrl.css");
define(["base/base", "./datatable", "./tablectrl"], function(tablebase, tablectrl){
	FwBase.Wtf.View.Controls.Table = function(){};
	$.extend(FwBase.Wtf.View.Controls.Table.prototype, FwBase.Wtf.View.Controls.BaseControl.prototype, 
		{
			template: _.template($('#sys_atom_controls_table').html()),
			postInit : function(){
				TableAdvanced.init();
			}
		}
	);
	return FwBase.Wtf.View.Controls.Table;
});