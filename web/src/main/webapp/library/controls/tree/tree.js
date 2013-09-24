loadCss("library/controls/tree/treectrl.css");
define(["./treectrl"], function(treectrl){
	FwBase.Wtf.View.Controls.Tree = {
		template: _.template($('#sys_atom_controls_tree').html()),
		create : function(parentContainer, metadata){
			parentContainer.append(this.template(metadata));
		}
	};
	return FwBase.Wtf.View.Controls.Tree;
});