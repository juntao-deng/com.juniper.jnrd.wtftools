define(["base/base", "./ztree.core", "css!./ztree"], function(base, treectrl){
	FwBase.Wtf.View.Controls.Tree = function(){
		FwBase.Wtf.View.Controls.BaseControl.apply(this, arguments);
	};
	var datas =[
		{ name:"Parent1 - Expand", open:true,
			children: [
				{ name:"Parent11 - Collapse",
					children: [
						{ name:"Leaf111"},
						{ name:"Leaf112"},
						{ name:"Leaf113"},
						{ name:"Leaf114"}
					]},
				{ name:"Parent12 - Expand", open:true,
					children: [
						{ name:"Leaf121"},
						{ name:"Leaf122"},
						{ name:"Leaf123"},
						{ name:"Leaf124"}
					]},
				{ name:"Parent13 - NoChildren", isParent:true}
			]},
		{ name:"Parent2 - Collapse",
			children: [
				{ name:"Parent21 - Expand", open:true,
					children: [
						{ name:"Leaf211"},
						{ name:"Leaf212"},
						{ name:"Leaf213"},
						{ name:"Leaf214"}
					]},
				{ name:"Parent22 - Collapse",
					children: [
						{ name:"Leaf221"},
						{ name:"Leaf222"},
						{ name:"Leaf223"},
						{ name:"Leaf224"}
					]},
				{ name:"Parent23 - Collapse",
					children: [
						{ name:"Leaf231"},
						{ name:"Leaf232"},
						{ name:"Leaf233"},
						{ name:"Leaf234"}
					]}
			]},
		{ name:"Parent3 - NoChildren", isParent:true}

	];

	$.extend(FwBase.Wtf.View.Controls.Tree.prototype, FwBase.Wtf.View.Controls.BaseControl.prototype, {
		template: _.template($('#sys_atom_controls_tree').html()),
		postInit: function() {
			var settings = buildSettings(this.metadata);
			var datas = this.metadata.data;
			$.fn.zTree.init(this.el.children("#tree"), settings, datas);
		},
		makeDefault : function() {
			this.setDefault({data : [{ name:"ROOT", open:false}]});
		},
		mockMetadata : function() {
			this.setDefault({data : datas});
		}
	});
	
	function buildSettings(metadata){
		return {};
	}
	return FwBase.Wtf.View.Controls.Tree;
});