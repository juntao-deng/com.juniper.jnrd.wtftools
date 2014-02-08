wdefine(["../attrcontroller"], function(attrcontroller){
	attrcontroller();
	/** Events scope begin*/
	/**
	 * @Event
	 */
	$app.on('loaded', function(){
		var compId = FwBase.Wtf.Design.DesignSupport.currParent.attr('id');
		var comp = $app.parent.component(compId);
		var metadata = comp.metadata;
		$app.component('idattr').value(compId);
		$app.component('editableattr').value(metadata.editable);
		$app.component('multiattr').value(metadata.multiselect);
		$app.component('heightattr').value(metadata.height);
		$app.component('minheightattr').value(metadata.minHeight);
		$app.component('pageattr').value(metadata.pagination != null);
	});
	
//	var modelBt = $app.component("modelbt");
//	modelBt.on('click', function(){
//		FwBase.Wtf.Design.DesignSupport.editModel();
//	});
//
//	var behaviorBt = $app.component("behaviorbt");
//	behaviorBt.on('click', function(){
//		FwBase.Wtf.Design.DesignSupport.editBehavior();
//	});
	
	/**
	 * @Event('click') 
	 */
	$comp("okbt").on('click', function(){
		var app = this.ctx;
		var idattr = app.component('idattr').value();
		var editableattr = app.component('editableattr').value();
		var model = app.model('columnsModel');
		var rows = model.page().rows();
		if(rows == null || rows.length == 0){
			alert("no columns defined");
			return;
		}
		var columns = [];
		for(var i = 0; i < rows.length; i ++){
			columns[i] = rows[i].toJSON();
		}
		
		var md = {model : this.ctx.component('modeldropdown').value(), 'columns': columns, editable : editableattr};
		
//		var controller = null;
//		var rest = {generateClass: app.attr('generateClass')};
		FwBase.Wtf.Design.DesignSupport.syncMetadata(idattr, md);
		DesignSupport.closeForEvent();
		app.close();
	});
	/** Events scope end*/
	
});