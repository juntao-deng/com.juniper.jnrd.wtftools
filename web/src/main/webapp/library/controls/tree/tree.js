loadCss("library/controls/tree/treectrl.css");
define(["base/base", "./treectrl"], function(base, treectrl){
	FwBase.Wtf.View.Controls.Tree = function(){};
	$.extend(FwBase.Wtf.View.Controls.Tree.prototype, FwBase.Wtf.View.Controls.BaseControl.prototype, {
		template: _.template($('#sys_atom_controls_tree').html()),
		create : function(parentContainer, metadata){
			parentContainer.append(this.template(metadata));
		}
	});
	return FwBase.Wtf.View.Controls.Tree;
});