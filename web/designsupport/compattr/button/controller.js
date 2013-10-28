wdefine(function(){
	$app.on('loaded', function(){
		var compId = FwBase.Wtf.Design.DesignSupport.currParent.attr('id');
		var comp = this.parent.component(compId);
		var metadata = comp.metadata;
		this.component('idattr').value(compId);
		this.component('textattr').value(metadata.text);
		this.component('enableattr').checked(metadata.enable);
		this.component('styleattr').value(metadata.style);
	});
	var okbt = $app.component("okbt");
	okbt.on('click', function(){
		var idattr = this.app.component('idattr').value();
		var textattr = this.app.component('textattr').value();
		var enableattr = this.app.component('enableattr').checked();
		var styleattr = this.app.component('styleattr').value();
		
		var md = {text : textattr, enable : enableattr, style: styleattr};
		
		var controller = null;
		var rest = null;
		updateComponent(idattr, md, controller, rest);
		this.app.close();
	});
	
	function updateComponent(id, md, controller, rest){
		FwBase.Wtf.Design.DesignSupport.syncAppFiles(id, md, controller, rest);
	}
});
