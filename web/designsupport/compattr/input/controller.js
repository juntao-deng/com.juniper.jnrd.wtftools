wdefine(function(){
	DesignSupport.eventControllerWrapper();
	$app.on('loaded', function(){
		var compId = DesignSupport.currParent.attr('id');
		var comp = DesignSupport.getDesignApp().component(compId);
		var metadata = comp.metadata;
		this.component('idattr').value(compId);
		this.component('labelattr').value(metadata.label);
		this.component('editableattr').value(metadata.editable);
		this.component('labelwidthattr').value(metadata.labelWidth);
		this.component('helpattr').value(metadata.hint);
		this.component('requiredattr').value(metadata.required);
		this.component('placeholderattr').value(metadata.placeHolder);
	});
	$app.component('labelattr').on('valuechange', function(options){
		var value = options.value;
		if(value != 0 && value.length > 0){
			var oldValue = this.ctx.component('labelwidthattr').value();
			if(oldValue == null || oldValue == 0)
				this.ctx.component('labelwidthattr').value(100);
		}
		else{
			this.ctx.component('labelwidthattr').value(0);
		}
	});
	$app.component("okbt").on('click', function(){
		var id = this.ctx.component('idattr').value();
		var label = this.ctx.component('labelattr').value();
		var editable = this.ctx.component('editableattr').value();
		var labelWidth = this.ctx.component('labelwidthattr').value();
		var help = this.ctx.component('helpattr').value();
		var required = this.ctx.component('requiredattr').value();
		var placeholder = this.ctx.component('placeholderattr').value();
		var md = {label: label, editable: editable, labelWidth: labelWidth, hint:help, required: required, placeHolder : placeholder};
		updateComponent(id, md);
		DesignSupport.closeForEvent();
		this.ctx.close();
	});
	
	function updateComponent(id, md, controller, rest){
		FwBase.Wtf.Design.DesignSupport.syncMetadata(id, md);
	}
});
