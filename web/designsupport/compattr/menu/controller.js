wdefine(["../attrcontroller"], function(){
	$app.on('loaded', function(){
//		var compId = FwBase.Wtf.Design.DesignSupport.currParent.attr('id');
//		var comp = $app.parent.component(compId);
//		var metadata = comp.metadata;
//		$app.component('idattr').value(compId);
//		$app.component('textattr').value(metadata.text);
//		$app.component('enableattr').checked(metadata.enable);
//		$app.component('styleattr').value(metadata.style);
	});
	var okbt = $app.component("okbt");
	okbt.on('click', function(){
		var idattr = $app.component('idattr').value();
		var textattr = $app.component('textattr').value();
		var enableattr = $app.component('enableattr').checked();
		var styleattr = $app.component('styleattr').value();
		
		var md = {text : textattr, enable : (enableattr == 'on'), style: styleattr};
		
		var controller = null;
		var rest = null;
		updateComponent(idattr, md, controller, rest);
		$app.close();
	});
	
	function updateComponent(id, md, controller, rest){
		FwBase.Wtf.Design.DesignSupport.syncAppFiles(id, md, controller, rest);
	}
});
