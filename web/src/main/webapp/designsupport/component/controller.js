wdefine(function(){
	var okbt = $app.component("okbt");
	okbt.on('click', function(){
		var compInput = $app.component('componentinput');
		var compId = $app.component('componentid');
		insertComponent(compId.value(), compInput.value());
		$app.close();
		FwBase.Wtf.Application.repaint(FwBase.Wtf.Design.DesignSupport.designable);
	});
	
	function insertComponent(id, type){
		var str = '<div id="' + id + '" wtftype="' + type + '"></div>';
		FwBase.Wtf.Design.DesignSupport.currParent.html(str);
	}
});