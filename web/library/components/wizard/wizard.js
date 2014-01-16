define(["base/base", "./wizardctrl", "css!./wizardctrl"], function(base){
	FwBase.Wtf.View.Controls.Wizard = function(){
		FwBase.Wtf.View.Controls.BaseControl.apply(this, arguments);
	};
	$.extend(FwBase.Wtf.View.Controls.Wizard.prototype, FwBase.Wtf.View.Controls.BaseControl.prototype, {
		templateInit: function(){
			return _.template($('#sys_atom_controls_wizard').html());
		},
		mockMetadata : function() {
			this.setDefault({title:'Form Wizard' , activeItem: 'step1', items: [{id:'step1', text : 'Step 1, Do Something'}, {id:'step2', text : 'Do something again'}]});
		},
		makeDefault : function(){
			this.setDefault({close : false, finish : true});
		},
		
		postInit : function() {
			this.wizard = this.el.children('#wizard');
			this.wizardContent = this.wizard.find(".tab-content");
			this.wizardContent.append(this.wizard.siblings());
			this.fixpages();
			this.wizardObj = this.wizard.bootstrapWizard({}).data("bootstrapWizard");
			var oThis = this;
			var previousBtContainer = oThis.wizard.find("#btnprevious");
			oThis.previousBt = new FwBase.Wtf.View.Controls["Button"](previousBtContainer, {text : 'Previous'}, "btnprevious");
			oThis.previousBt.on('click', function(){
				oThis.previous();
			});
			
			var nextBtContainer = oThis.wizard.find("#btnnext");
			oThis.nextBt = new FwBase.Wtf.View.Controls["Button"](nextBtContainer, {text : 'Next', style : 'primary'}, "btnnext");
			oThis.nextBt.on('click', function(){
				oThis.next();
			});
			
			var finishBtContainer = oThis.wizard.find("#btnfinish");
			oThis.finishBt = new FwBase.Wtf.View.Controls["Button"](finishBtContainer, {text : 'Finish'}, "btnfinish");
			oThis.finishBt.on('click', function(){
				oThis.finish();
			});
			
			var closeBtContainer = oThis.wizard.find("#btnclose");
			oThis.closeBt = new FwBase.Wtf.View.Controls["Button"](closeBtContainer, {text : 'Close'}, "btnclose");
		},
		previous : function() {
			var eventCtx = {};
			this.trigger("previous", {source : this, eventCtx : eventCtx});
			if(eventCtx.stop){
				return;
			}
			this.wizardObj.previous();
		},
		next : function() {
			var eventCtx = {};
			this.trigger("next", {source : this, eventCtx : eventCtx});
			if(eventCtx.stop){
				return;
			}
			this.wizardObj.next();
		},
		finish : function() {
			var eventCtx = {};
			this.trigger("finish", {source : this, eventCtx : eventCtx});
			if(eventCtx.stop){
				return;
			}
		},
		fixpages : function() {
			for(var i = 0; i < this.metadata.items.length; i ++){
				var item = this.metadata.items[i];
				var itemDiv = this.wizard.find("#" + item.id);
				itemDiv.addClass("tab-pane");
				if(i == 0)
					itemDiv.addClass("active");
				if(itemDiv.length > 0)
					continue;
			}
		},
	});
	return FwBase.Wtf.View.Controls.Wizard;
});
