define(["base/base"], function(base){
	FwBase.Wtf.View.Controls.Dialog = function(){
		FwBase.Wtf.View.Controls.BaseControl.apply(this, arguments);
	};
	
	FwBase.Wtf.View.Controls.Dialog.closeDialog = function() {
		if(FwBase.Wtf.View.Controls.Dialog.dialogs == null)
			FwBase.Wtf.View.Controls.Dialog.dialogs = [];
		var lastDialog = null;
		for(var i = 0; i < FwBase.Wtf.View.Controls.Dialog.dialogs.length; i ++){
			if(FwBase.Wtf.View.Controls.Dialog.dialogs[i].visible())
				lastDialog = FwBase.Wtf.View.Controls.Dialog.dialogs[i];
			else
				break;
		}
		if(lastDialog)
			lastDialog.visible(false);
	};
	FwBase.Wtf.View.Controls.Dialog.showDialog = function(modal, options) {
		var dialog = FwBase.Wtf.View.Controls.Dialog.getDialog(modal);
		if(options){
			if(options.width){
				dialog.width(options.width);
			}
			if(options.height){
				dialog.height(options.height);
			}
		}
		dialog.visible(true);
		return dialog;
	};
	FwBase.Wtf.View.Controls.Dialog.index = 0;
	FwBase.Wtf.View.Controls.Dialog.getDialog = function(modal) {
		if(!modal)
			modal = true;
		if(FwBase.Wtf.View.Controls.Dialog.dialogs == null)
			FwBase.Wtf.View.Controls.Dialog.dialogs = [];
		var dialog = null;
		for(var i = 0; i < FwBase.Wtf.View.Controls.Dialog.dialogs.length; i ++){
			if(!FwBase.Wtf.View.Controls.Dialog.dialogs[i].visible()){
				dialog = FwBase.Wtf.View.Controls.Dialog.dialogs[i];
				break;
			}
		}
		//all dialogs used,create new one
		if(dialog == null){
			dialog = new FwBase.Wtf.View.Controls.Dialog($(document.body), {modal : modal}, "" + (FwBase.Wtf.View.Controls.Dialog.index ++));
			FwBase.Wtf.View.Controls.Dialog.dialogs.push(dialog);
		}
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
			},
			makeDefault : function(){
				this.setDefault({visible : false});
			},
			visible : function() {
				if(arguments.length == 0)
					return this.dialog.css('display') != 'none';
				if(arguments[0]){
					var options = arguments[1];
					if(options){
						if(options.width){
							this.width(options.width);
						}
						if(options.height){
							this.height(options.height);
						}
					}
					this.dialog.modal();
				}
				else{
					this.dialog.modal('hide');
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
				this.dialog.css("width", width);
			},
			height : function(height){
				this.dialog.css("height", height);
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
			}
		}
	);
	return FwBase.Wtf.View.Controls.Dialog;
});