define(["base/base", "css!./wizardctrl"], function(base){
	FwBase.Wtf.View.Controls.Wizard = function(){
		FwBase.Wtf.View.Controls.BaseControl.apply(this, arguments);
	};
	$.extend(FwBase.Wtf.View.Controls.Wizard.prototype, FwBase.Wtf.View.Controls.BaseControl.prototype, {
		template: _.template($('#sys_atom_controls_wizard').html()),
		makeDefault : function(metadata){
			if(metadata.pages == null){
				var pages = [{title:'Step 1', url:'apps/wizarddemo/step1'}, {title:'Step 2', url:'apps/wizarddemo/step2'}, {title:'Step 3', url:'apps/wizarddemo/step3'}];
				metadata.pages = pages;
				metadata.title = "Form Wizard";
				metadata.index = 0;
			}
		}
	});
	return FwBase.Wtf.View.Controls.Wizard;
});