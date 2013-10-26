define(["base/base", "css!./wizardctrl"], function(base){
	FwBase.Wtf.View.Controls.Wizard = function(){
		FwBase.Wtf.View.Controls.BaseControl.apply(this, arguments);
	};
	$.extend(FwBase.Wtf.View.Controls.Wizard.prototype, FwBase.Wtf.View.Controls.BaseControl.prototype, {
		template: _.template($('#sys_atom_controls_wizard').html()),
		mockMetadata : function() {
			this.setDefault({title:'Form Wizard' , activeItem: 'step1', items: [{id:'step1', text : 'Step 1, Do Something'}, {id:'step2', text : 'Do something again'}, {id:'step3', text : 'The last thing to do'}]});
		},
		makeDefault : function(){
		},
		
		postInit : function() {
			this.wizard = this.el.children('#wizard');
			this.fixpages();
			this.wizardContent = this.wizard.find(".tab-content");
			this.wizardContent.append(this.wizard.siblings());
		},
		
		fixpages : function() {
			for(var i = 0; i < this.metadata.items.length; i ++){
				var item = this.metadata.items[i];
				var itemDiv = this.wizard.children("#" + item.id);
				if(itemDiv.length > 0)
					continue;
			}
		},
		insertPage : function(itemMetadata, index){
			var trueIndex = index;
			if(typeof itemMetadata != 'number')
				this.metadata.items.splice(index, 0, itemMetadata);
			else
				trueIndex = itemMetadata;
			var md = this.metadata.items[trueIndex];
			this.wizard.children()[trueIndex].after('<div id="' + md.id + '" wtftype="tabitem"></div>');
		},
		itemContent : function(id, content) {
			if(arguments.length == 1)
				return this.wizard.children("#" + id).html();
			this.wizard.children("#" + id).html(content);
		},
		active : function(item) {
			if(typeof item == 'integer'){
				this.wizards('option', 'active', item);
			}
			else{
				var index = this.itemIndex(item);
				this.wizards('option', 'active', index);
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
	return FwBase.Wtf.View.Controls.Wizard;
});