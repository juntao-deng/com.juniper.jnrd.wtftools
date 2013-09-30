define(["base/base", "./datatable", "./tablectrl", "css!./tablectrl"], function(tablebase, tablectrl){
	FwBase.Wtf.View.Controls.Table = function(){
		FwBase.Wtf.View.Controls.BaseControl.apply(this, arguments);
	};
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