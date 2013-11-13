wdefine(function(){
	var okbt = $app.component("okbt");
	okbt.on('click', function(){
		var widgetinput = this.ctx.component('widgetinput');
		insertWidget(widgetinput.value());
		this.ctx.close();
		AppUtil.repaint(FwBase.Wtf.Design.DesignSupport.designable);
		FwBase.Wtf.Design.DesignSupport.syncHtml();
	});
	
	function insertWidget(tplId){
		var str = '<div wtftype="widget" wtfmetadata="' + tplId + '"></div>';
		FwBase.Wtf.Design.DesignSupport.currParent.html(str);
	}
});