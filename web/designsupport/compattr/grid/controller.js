wdefine(function(){
	/** Global scope begin*/
	
	DesignSupport.eventControllerWrapper();
	DesignSupport.modelControllerWrapper();
	function updateComponent(id, md, controller, rest){
		FwBase.Wtf.Design.DesignSupport.syncAppFiles(id, md, controller, rest);
	}
	
	function callbackForEntity(infos) {
		if(infos.errormsg){
			alert(infos.errormsg);
			return;
		}
		var model = $app.model('columnsModel');
		var columnInfos = infos.columnInfos;
		if(columnInfos && columnInfos.length > 0){
			model.page().reset();
			model.page().add(columnInfos);
		}
		$app.attr("restservice", infos.restservice);
		if(infos.generateClass != null){
			$app.attr('generateClass', infos.generateClass);
			alert("The restful service for class '" + infos.generateClass + "' doesn't exist, it will be created after you click the 'Save Changes' Button");
		}
	}
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
		
		var columns = metadata.columns;
		var model = $app.model('columnsModel');
		model.page().add(columns);
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
	var okbt = $app.component("okbt");
	okbt.on('click', function(){
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
		
		var md = {model : $app.attr('restservice') + "_model", 'columns': columns, editable : editableattr};
		
		var controller = null;
		var rest = {generateClass: app.attr('generateClass')};
		updateComponent(idattr, md, controller, rest);
		app.close();
	});
	/** Events scope end*/
	
});