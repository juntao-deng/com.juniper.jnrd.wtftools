wdefine(function(){
	/** Global scope begin*/
	
	DesignSupport.eventControllerWrapper();
	DesignSupport.modelControllerWrapper();
	/** Global scope end*/
	
	/** Events scope begin*/
	/**
	 * @Event
	 */
	$app.on('loaded', function(){
		var compId = FwBase.Wtf.Design.DesignSupport.currParent.attr('id');
		var comp = $app.parent.component(compId);
		var metadata = comp.metadata;
		$app.component('idattr').value(compId);
		$app.component('editableattr').checked(metadata.editable);
		$app.component('multiattr').checked(metadata.multiselect);
		$app.component('heightattr').value(metadata.height);
		$app.component('minheightattr').value(metadata.minHeight);
		$app.component('pageattr').value(metadata.pagination != null);
	});
	
	/**
	 * @Event('click') 
	 */
	$app.component('columnmenu').on("click", function(options){
		var id = options.trigger.id;
		if(id == "add"){
			var url = window.frameCtx + "/../designsupport/compattr/grid/columnedit";
			AppUtil.navigateToDialog(url, null, {title: 'Column Attributes'});
		}
	});
	
	/**
	 * @Event('click') 
	 */
	$comp("okbt").on('click', function(){
		var app = this.ctx;
		var idattr = app.component('idattr').value();
		var editableattr = app.component('editableattr').checked();
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
		
		FwBase.Wtf.Design.DesignSupport.syncMetadata(idattr, md);
		DesignSupport.closeForEvent();
		app.close();
	});
	/** Events scope end*/
	
});