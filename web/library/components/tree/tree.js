define(["base/base", "./ztree.core", "css!./ztree"], function(base, treectrl){
	FwBase.Wtf.View.Controls.Tree = function(){
		FwBase.Wtf.View.Controls.BaseControl.apply(this, arguments);
	};

	$.extend(FwBase.Wtf.View.Controls.Tree.prototype, FwBase.Wtf.View.Controls.BaseControl.prototype, {
		template: _.template($('#sys_atom_controls_tree').html()),
		postInit: function() {
			this.model = this.ctx.model(this.metadata.model);
			if(this.model){
				this.listenTo(this.model, "add", this.list_addRow);
				this.listenTo(this.model, "remove", this.deleteRow);
				this.listenTo(this.model, "change", this.changeRow);
			}
			var settings = buildSettings(this.metadata);
			var datas = this.metadata.data;
			this.tree = $.fn.zTree.init(this.el.children("#tree"), settings, datas);
		},
		makeDefault : function() {
			this.setDefault({editable : false});
		},
		mockMetadata : function() {
			this.setDefault({data : datas});
		},
		/*Listener begin*/
		list_addRow : function(options) {
			var row = options.row;
			var index = options.index;
			var itemId = row.id;
			var pidField = this.getPidFieldByBinding();
			var pNode = null;
			if(pidField != null){
				var pid = row.get(pidField);
				if(pid != null){
					pNode = this.tree.getNodeByTId(pid);
				}
			}
			var data = row.toJSON();
			this.updateDataByBinding(data);
			this.tree.addNodes(pNode, data, true);
		},
		/*Listeners end*/
		updateDataByBinding : function(data){
			var binding = this.metadata.binding;
			var nameField = binding.nameField;
			if(nameField != null)
				data.name = data[nameField];
			var folderField = binding.folderField;
			if(folderField == null)
				data.isParent = false;
			else
				data.isParent = (data[folderField] == 'y' || data[folderField]);
		},
		
		getPidFieldByBinding : function() {
			return this.metadata.binding == null ? 'pid' : this.metadata.binding.pidField;
		}
	});
	
	function buildSettings(metadata){
		return {};
	}
	
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
	return FwBase.Wtf.View.Controls.Tree;
});