wdefine(function(){
	DesignSupport.eventControllerWrapper();
	$app.on('loaded', function(){
		var compId = FwBase.Wtf.Design.DesignSupport.currParent.attr('id');
		var comp = this.parent.component(compId);
		var metadata = comp.metadata;
		this.component('idattr').value(compId);
		this.component('textattr').value(metadata.text);
		this.component('enableattr').value(metadata.enable);
		this.component('styleattr').value(metadata.style);
	});
	$app.component("okbt").on('click', function(){
		var idattr = this.ctx.component('idattr').value();
		var textattr = this.ctx.component('textattr').value();
		var enableattr = this.ctx.component('enableattr').value();
		var styleattr = this.ctx.component('styleattr').value();
		
		var md = {text : textattr, enable : enableattr, style: styleattr};
		updateComponent(idattr, md);
		DesignSupport.closeForEvent();
		this.ctx.close();
	});
	
	function updateComponent(id, md, controller, rest){
		FwBase.Wtf.Design.DesignSupport.syncMetadata(id, md);
	}
});
