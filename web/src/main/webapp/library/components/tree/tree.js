define(["base/base", "./treectrl", "css!./treectrl"], function(base, treectrl){
	FwBase.Wtf.View.Controls.Tree = function(){
		FwBase.Wtf.View.Controls.BaseControl.apply(this, arguments);
	};
	$.extend(FwBase.Wtf.View.Controls.Tree.prototype, FwBase.Wtf.View.Controls.BaseControl.prototype, {
		template: _.template($('#sys_atom_controls_tree').html())
	});
	return FwBase.Wtf.View.Controls.Tree;
});