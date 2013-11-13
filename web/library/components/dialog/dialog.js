define(["base/base"], function(base){
	FwBase.Wtf.View.Controls.Dialog = function(){
		FwBase.Wtf.View.Controls.BaseControl.apply(this, arguments);
	};
	
//	FwBase.Wtf.View.Controls.Dialog.closeDialog = function() {
//		if(FwBase.Wtf.View.Controls.Dialog.dialogs == null)
//			FwBase.Wtf.View.Controls.Dialog.dialogs = [];
//		var lastDialog = null;
//		for(var i = 0; i < FwBase.Wtf.View.Controls.Dialog.dialogs.length; i ++){
//			if(FwBase.Wtf.View.Controls.Dialog.dialogs[i].visible())
//				lastDialog = FwBase.Wtf.View.Controls.Dialog.dialogs[i];
//			else
//				break;
//		}
//		if(lastDialog)
//			lastDialog.visible(false);
//	};
//	FwBase.Wtf.View.Controls.Dialog.showDialog = function(modal, options) {
//		var dialog = FwBase.Wtf.View.Controls.Dialog.getDialog(modal);
//		if(options){
//			if(options.width){
//				dialog.width(options.width);
//			}
//			if(options.height){
//				dialog.height(options.height);
//			}
//		}
//		if(options.title)
//			dialog.title(options.title);
//		else
//			dialog.title(title);
//		dialog.visible(true);
//		return dialog;
//	};
	FwBase.Wtf.View.Controls.Dialog.index = 0;
	FwBase.Wtf.View.Controls.Dialog.getDialog = function(options) {
		options = options || {};
		if(options.modal == null)
			options.modal = true;
		
		var dialog = new FwBase.Wtf.View.Controls.Dialog($(document.body), {modal : options.modal}, "" + (FwBase.Wtf.View.Controls.Dialog.index ++));
		dialog.update(options);
		return dialog;
	};
	$.extend(FwBase.Wtf.View.Controls.Dialog.prototype, FwBase.Wtf.View.Controls.BaseControl.prototype, 
		{
			template: _.template($('#sys_atom_controls_dialog').html()),
			postInit : function(){
				this.dialog = this.el.children("#sys_dialog" + this.id);
				this.dialog.notifyContentChange = function() {
					var head = this.find('.modal-header');
					var body = this.find('.modal-body');
					var footer = this.find('.modal-footer');
					body.outerHeight(this.height() - head.outerHeight() - footer.outerHeight());
				};
				this.dialog[0].oThis = this;
				this.dialog.dialog({autoOpen: false, modal: true, draggable: true, width: 600, height: 300, beforeClose: innerBeforeClose, close: innerClose, resizeStop : innerResize});
			},
			makeDefault : function(){
				this.setDefault({visible : false});
			},
			visible : function() {
				if(arguments.length == 0)
					return this.visibleAttr;
				if(arguments[0]){
					var options = arguments[1];
					if(options){
						this.update(options);
					}
					if(!this.visibleAttr){
						this.dialog.dialog("open");
						this.visibleAttr = true;
					}
				}
				else{
					if(this.visibleAttr){
						this.dialog.dialog('close');
						this.visibleAttr = false;
					}
				}
			},
			/**
			 * update the content of dialog
			 */
			content : function(content) {
				this.dialog.html(content);
			},
			
			bodyContainer : function() {
				return this.dialog;
			},
			
			width : function(width){
				this.dialog.dialog("option", "width", width);
			},
			height : function(height){
				this.dialog.dialog("option", "height", height);
			},
			title : function(title){
				this.dialog.dialog("option", "title", title);
			},
			update : function(options){
				if(!options)
					return;
				if(options.width){
					this.width(options.width);
				}
				if(options.height){
					this.height(options.height);
				}
				if(options.title){
					this.title(options.title);
				}
			},
			close : function() {
				var eventCtx = {};
				this.trigger("close", {source : this, eventCtx : eventCtx});
				this.dialog.dialog("destroy");
			}
		}
	);
	
	function innerResize() {
		this.oThis.dialog.notifyContentChange();
	}
	function innerClose() {
		this.oThis.close();
	}
	function innerBeforeClose() {
		var eventCtx = {};
		this.oThis.trigger("beforeclose", {source : this.oThis, eventCtx : eventCtx});
		if(eventCtx.stop)
			return false;
	}
	return FwBase.Wtf.View.Controls.Dialog;
});