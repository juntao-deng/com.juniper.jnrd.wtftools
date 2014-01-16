wdefine(function(){
	DesignSupport.eventControllerWrapper();
	$app.on('loaded', function(){
		var compId = FwBase.Wtf.Design.DesignSupport.currParent.attr('id');
		var comp = this.parent.component(compId);
		var metadata = comp.metadata;
		this.component('idattr').value(compId);
		this.component('activeitemattr').value(metadata.currentItem);
	});
	$app.component("okbt").on('click', function(){
		var idattr = this.ctx.component('idattr').value();
		var textattr = this.ctx.component('activeitem').value();
		var md = {text : textattr, activeitem : activeitem};
		updateComponent(idattr, md);
		DesignSupport.closeForEvent();
		this.ctx.close();
	});
	
	function updateComponent(id, md){
		FwBase.Wtf.Design.DesignSupport.syncMetadata(id, md);
	}
});
