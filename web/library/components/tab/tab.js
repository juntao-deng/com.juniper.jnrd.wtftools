define(["base/base"], function(base){
	FwBase.Wtf.View.Controls.Tab = function(){
		FwBase.Wtf.View.Controls.BaseControl.apply(this, arguments);
	};
	$.extend(FwBase.Wtf.View.Controls.Tab.prototype, FwBase.Wtf.View.Controls.BaseControl.prototype, {
		templateInit: function(){
			return _.template($('#sys_atom_controls_tab').html());
		},
		mockMetadata : function() {
			this.setDefault({activeitem: 'item1', items: [{id:'item1', text : 'TabItem1'}, {id:'item2', text : 'TabItem2'}]});
		},
		makeDefault : function() {
//			this.setDefault();
		},
		postInit : function(){
			this.activeIndex = 0;
			this.tab = this.el.children('#tab');
			this.tab.append(this.tab.siblings());
			this.fixtabs();
			this.tab.tabs();
			this.active(this.metadata.activeitem);
		},
		fixtabs : function() {
			for(var i = 0; i < this.metadata.items.length; i ++){
				var item = this.metadata.items[i];
				var itemDiv = this.tab.children("#" + item.id);
				if(itemDiv.length > 0)
					continue;
				this.tab.append('<div id="' + item.id + '"><div wtftype="container" class="design_minheight"></div></div>');
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
		active : function() {
			if(arguments.length == 0)
				return this.activeIndex;
			var item = arguments[0];
			if(typeof item == 'number'){
				if(item >= this.metadata.items.length)
					item = this.metadata.items.length - 1;
				else if(item < 0)
					item = 0;
				this.activeIndex = item;
				this.tab.tabs('option', 'active', item);
			}
			else{
				var index = null;
				if(item == null || item == "")
					index = 0;
				else
					index = this.itemIndex(item);
				if(index == -1)
					index = 0;
				this.activeIndex = index;
				this.tab.tabs('option', 'active', index);
			}
		},
		itemIndex : function(item){
			var items = this.metadata.items;
			for(var i = 0; i < items.length; i ++){
				if(item == items[i].id)
					return i;
			}
			return -1;
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