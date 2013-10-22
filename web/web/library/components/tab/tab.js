define(["base/base"], function(base){
	FwBase.Wtf.View.Controls.Tab = function(){
		FwBase.Wtf.View.Controls.BaseControl.apply(this, arguments);
	};
	$.extend(FwBase.Wtf.View.Controls.Tab.prototype, FwBase.Wtf.View.Controls.BaseControl.prototype, {
		templateInit: function(){
			return _.template($('#sys_atom_controls_tab').html());
		},
		mockMetadata : function() {
			this.setDefault({activeItem: 'item1', items: [{id:'item1', text : 'TabItem1'}, {id:'item2', text : 'TabItem2'}]});
		},
		makeDefault : function() {
//			this.setDefault();
		},
		postInit : function(){
			this.tab = this.el.children('#tab');
			this.tab.append(this.tab.siblings());
			this.fixtabs();
			this.tabs = this.tab.tabs();
		},
		fixtabs : function() {
			for(var i = 0; i < this.metadata.items.length; i ++){
				var item = this.metadata.items[i];
				var itemDiv = this.tab.children("#" + item.id);
				if(itemDiv.length > 0)
					continue;
			}
		},
		insertTab : function(itemMetadata, index){
			var trueIndex = index;
			if(typeof itemMetadata != 'number')
				this.metadata.items.splice(index, 0, itemMetadata);
			else
				trueIndex = itemMetadata;
			var md = this.metadata.items[trueIndex];
			this.tab.children()[trueIndex].after('<div id="' + md.id + '" wtftype="tabitem"></div>');
		},
		itemContent : function(id, content) {
			if(arguments.length == 1)
				return this.tab.children("#" + id).html();
			this.tab.children("#" + id).html(content);
		},
		active : function(item) {
			if(typeof item == 'integer'){
				this.tabs('option', 'active', item);
			}
			else{
				var index = this.itemIndex(item);
				this.tabs('option', 'active', index);
			}
		},
		itemIndex : function(item){
			
		},
		reInit : function(meta) {
			if(this.slider)
				this.slider.slider('destroy');
			for(var i in meta){
				this.metadata[i] = meta[i];
			}
			if(this.metadata.values.length == 1)
				this.metadata.range = false;
			this.postInit();
		},
		enable : function() {
			if(arguments.length == 0)
				return true;
		},
		values : function() {
			if(arguments.length == 0)
				return this.slider.slider("values");
			this.slider.slider("values", arguments[0]);
		}
	});
	return FwBase.Wtf.View.Controls.Tab;
});