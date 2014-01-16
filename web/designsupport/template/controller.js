wdefine(function(){
	var okbt = $app.component("okbt");
	okbt.on('click', function(){
		var templateinput = $app.component('templateinput');
		insertTemplate(templateinput.value());
		$app.close();
		FwBase.Wtf.Application.repaint(FwBase.Wtf.Design.DesignSupport.designable);
		FwBase.Wtf.Design.DesignSupport.syncHtml();
	});
	
	function insertTemplate(tplId){
		var str = '<div wtftype="template" wtfmetadata="' + tplId + '"></div>';
		var menu = FwBase.Wtf.Design.DesignSupport.currParent.children('.designmenu');
		menu.remove();
		FwBase.Wtf.Design.DesignSupport.currParent.append(str);
	}
});