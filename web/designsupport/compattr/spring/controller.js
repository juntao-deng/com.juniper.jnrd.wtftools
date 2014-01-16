wdefine(function(){
	DesignSupport.eventControllerWrapper();
	$app.on('loaded', function(){
		var compId = FwBase.Wtf.Design.DesignSupport.currParent.attr('id');
		var comp = this.parent.component(compId);
		var metadata = comp.metadata;
		this.component('idattr').value(compId);
		this.component('widthattr').value(metadata.width);
	});
	$app.component("okbt").on('click', function(){
		var idattr = this.ctx.component('idattr').value();
		var widthattr = this.ctx.component('widthattr').value();
		var md = {width : widthattr};
		updateComponent(idattr, md);
		DesignSupport.closeForEvent();
		this.ctx.close();
	});
	
	function updateComponent(id, md){
		FwBase.Wtf.Design.DesignSupport.syncMetadata(id, md);
	}
});
