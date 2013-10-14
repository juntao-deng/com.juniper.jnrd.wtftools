define(["base/base"], function(base){
	FwBase.Wtf.View.Controls.Dialog = function(){
		FwBase.Wtf.View.Controls.BaseControl.apply(this, arguments);
	};
	FwBase.Wtf.View.Controls.Dialog.showDialog = function(modal) {
		var dialog = FwBase.Wtf.View.Controls.Dialog.getDialog(modal);
		dialog.visible(true);
		return dialog;
	};
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
			dialog = new FwBase.Wtf.View.Controls.Dialog($(document.body), {modal : modal});
			FwBase.Wtf.View.Controls.Dialog.dialogs.push(dialog);
		}
		return dialog;
	};
	$.extend(FwBase.Wtf.View.Controls.Dialog.prototype, FwBase.Wtf.View.Controls.BaseControl.prototype, 
		{
			template: _.template($('#sys_atom_controls_dialog').html()),
			postInit : function(){
				this.dialog = this.el.children("#dialog");
			},
			makeDefault : function(){
			},
			visible : function() {
				if(arguments.length == 0)
					return this.visibleAttr;
				if(arguments[0])
					this.dialog.modal();
				else
					this.dialog.hide();
			},
			/**
			 * update the content of dialog
			 */
			content : function(content) {
				this.dialog.html(content);
			},
			
			bodyContainer : function() {
				return this.dialog;
			}
		}
	);
	return FwBase.Wtf.View.Controls.Dialog;
});